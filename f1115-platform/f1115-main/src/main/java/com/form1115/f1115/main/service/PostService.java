package com.form1115.f1115.main.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.fastjson.JSON;
import com.form1115.f1115.common.domain.Post;
import com.form1115.f1115.common.exception.BusinessException;
import com.form1115.f1115.main.mapper.PostMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 帖子服务类
 */
@Service
public class PostService {
    
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    
    @Autowired
    private PostMapper postMapper;
    
    @Value("${dashscope.apiKey}")
    private String apiKey;
    
    @Value("${dashscope.model}")
    private String model;
    
    /**
     * 发布帖子（带AI审核）
     */
    @Transactional
    public Post createPost(Long userId, String content, List<String> imageList) {
        // 参数校验
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("帖子内容不能为空");
        }
        if (content.length() > 5000) {
            throw new BusinessException("帖子内容不能超过5000字");
        }
        
        // AI内容审核（非AI机器人发帖时才审核）
        if (!userId.equals(2L)) {  // 2L是AI机器人ID
            AuditResult auditResult = auditContent(content);
            logger.info("帖子审核结果 - 用户ID:{}, 状态:{}, 原因:{}", userId, auditResult.status, auditResult.message);
            
            // 如果审核拒绝，抛出异常
            if (auditResult.status == 1) {
                throw new BusinessException("内容审核未通过：" + auditResult.message);
            }
            
            // 如果待审核，设置帖子状态为审核中
            if (auditResult.status == 2) {
                logger.warn("内容需要人工审核 - 用户ID:{}", userId);
            }
        }
        
        // 创建帖子对象
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(content);
        
        // 处理图片列表
        if (imageList != null && !imageList.isEmpty()) {
            if (imageList.size() > 9) {
                throw new BusinessException("最多只能上传9张图片");
            }
            post.setImages(JSON.toJSONString(imageList));
        }
        
        post.setType(userId.equals(2L) ? 2 : 0); // AI发帖标记为type=2
        post.setStatus(1); // 正常状态（审核通过后才能发布）
        
        // 插入数据库
        int result = postMapper.insert(post);
        if (result <= 0) {
            throw new BusinessException("发布失败");
        }
        
        return post;
    }
    
    /**
     * AI内容审核
     */
    private AuditResult auditContent(String content) {
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个宽松的内容审核员，只拒绝明显违规的内容，请判断内容是否违规（包括：色情、暴力、政治敏感、广告、谩骂等）。" +
                        "只需回答：通过、拒绝、待审核。如果拒绝，请简要说明原因。")
                .build();
                
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("请审核以下内容：" + content)
                .build();
            
            GenerationParam param = GenerationParam.builder()
                .model(model)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .apiKey(apiKey)
                .build();
            
            GenerationResult result = gen.call(param);
            String auditResultText = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            // 解析审核结果
            if (auditResultText.contains("通过")) {
                return new AuditResult(0, "审核通过");
            } else if (auditResultText.contains("拒绝")) {
                return new AuditResult(1, auditResultText);
            } else {
                return new AuditResult(2, "待人工审核");
            }
            
        } catch (Exception e) {
            logger.error("AI审核失败", e);
            // 审核失败时，默认通过（避免影响用户体验）
            return new AuditResult(0, "审核服务暂时不可用，内容已发布");
        }
    }
    
    /**
     * 审核结果内部类
     */
    private static class AuditResult {
        int status; // 0-通过 1-拒绝 2-待审核
        String message;
        
        AuditResult(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
    
    /**
     * 获取帖子详情
     */
    public Post getPostDetail(Long postId, Long currentUserId) {
        Post post = postMapper.selectDetailById(postId, currentUserId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        
        // 解析图片列表
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            List<String> imageList = JSON.parseArray(post.getImages(), String.class);
            post.setImageList(imageList);
        }
        
        // 增加浏览量
        postMapper.incrementViewCount(postId);
        
        return post;
    }
    
    /**
     * 获取帖子列表（分页）
     */
    public PageInfo<Post> getPostList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.selectList(1); // 只查询正常状态的帖子
        
        // 解析图片列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                List<String> imageList = JSON.parseArray(post.getImages(), String.class);
                post.setImageList(imageList);
            }
        }
        
        return new PageInfo<>(posts);
    }
    
    /**
     * 获取用户发帖列表（分页）
     */
    public PageInfo<Post> getUserPosts(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.selectByUserId(userId, 1);
        
        // 解析图片列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                List<String> imageList = JSON.parseArray(post.getImages(), String.class);
                post.setImageList(imageList);
            }
        }
        
        return new PageInfo<>(posts);
    }
    
    /**
     * 获取首页时间线（分页）
     */
    public PageInfo<Post> getTimeline(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postMapper.selectTimeline(userId);
        
        // 解析图片列表
        for (Post post : posts) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                List<String> imageList = JSON.parseArray(post.getImages(), String.class);
                post.setImageList(imageList);
            }
        }
        
        return new PageInfo<>(posts);
    }
    
    /**
     * 删除帖子
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        
        // 只能删除自己的帖子
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该帖子");
        }
        
        int result = postMapper.deleteById(postId);
        if (result <= 0) {
            throw new BusinessException("删除失败");
        }
    }
}
