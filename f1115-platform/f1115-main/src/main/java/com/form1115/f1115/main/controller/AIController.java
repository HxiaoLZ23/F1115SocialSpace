package com.form1115.f1115.main.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.form1115.f1115.common.domain.Post;
import com.form1115.f1115.common.utils.Result;
import com.form1115.f1115.main.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * AI功能控制器（用于测试和手动触发）
 */
@RestController
@RequestMapping("/api/ai")
public class AIController {
    
    private static final Logger logger = LoggerFactory.getLogger(AIController.class);
    
    @Autowired
    private PostService postService;
    
    @Value("${dashscope.apiKey}")
    private String apiKey;
    
    @Value("${dashscope.model}")
    private String model;
    
    // AI机器人用户ID
    private static final Long AI_USER_ID = 2L;
    
    /**
     * 测试生成帖子内容
     */
    @PostMapping("/generate/post")
    public Result generatePost(@RequestBody Map<String, String> params) {
        String topic = params.get("topic");
        if (topic == null || topic.trim().isEmpty()) {
            return Result.error("话题不能为空");
        }
        
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个社交媒体内容创作者，请生成有趣、积极、正能量的帖子内容。内容要简洁明了，不超过200字。")
                .build();
                
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("请生成一个关于" + topic + "的帖子内容")
                .build();
            
            GenerationParam param = GenerationParam.builder()
                .model(model)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .apiKey(apiKey)
                .build();
            
            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            logger.info("AI生成帖子成功，话题：{}，内容：{}", topic, content);
            return Result.success("生成成功", content);
            
        } catch (Exception e) {
            logger.error("AI生成帖子失败", e);
            return Result.error("生成失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试生成评论内容
     */
    @PostMapping("/generate/comment")
    public Result generateComment(@RequestBody Map<String, String> params) {
        String postContent = params.get("postContent");
        if (postContent == null || postContent.trim().isEmpty()) {
            return Result.error("帖子内容不能为空");
        }
        
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个友好的社交媒体用户，请针对帖子内容生成简短、友好的评论。评论要自然，不超过50字。")
                .build();
                
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("请针对以下帖子生成一条评论：" + postContent)
                .build();
            
            GenerationParam param = GenerationParam.builder()
                .model(model)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .apiKey(apiKey)
                .build();
            
            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            logger.info("AI生成评论成功，评论：{}", content);
            return Result.success("生成成功", content);
            
        } catch (Exception e) {
            logger.error("AI生成评论失败", e);
            return Result.error("生成失败：" + e.getMessage());
        }
    }
    
    /**
     * 测试内容审核
     */
    @PostMapping("/audit")
    public Result auditContent(@RequestBody Map<String, String> params) {
        String content = params.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error("内容不能为空");
        }
        
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个内容审核员，请判断内容是否违规。只需回答：通过、拒绝、待审核。如果拒绝，请简要说明原因。")
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
            String auditResult = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            logger.info("AI审核完成，结果：{}", auditResult);
            return Result.success("审核完成", auditResult);
            
        } catch (Exception e) {
            logger.error("AI审核失败", e);
            return Result.error("审核失败：" + e.getMessage());
        }
    }
    
    /**
     * 手动触发AI发帖（用于测试）
     */
    @PostMapping("/post/now")
    public Result postNow(@RequestBody(required = false) Map<String, String> params) {
        String topic = params != null ? params.get("topic") : null;
        
        try {
            // 如果没有指定话题，使用默认话题
            if (topic == null || topic.trim().isEmpty()) {
                topic = "生活感悟";
            }
            
            // 生成内容
            String content = generatePostContent(topic);
            
            // 发布帖子
            Post post = postService.createPost(AI_USER_ID, content, null);
            
            logger.info("AI手动发帖成功，ID：{}", post.getId());
            return Result.success("AI发帖成功", post);
            
        } catch (Exception e) {
            logger.error("AI发帖失败", e);
            return Result.error("发帖失败：" + e.getMessage());
        }
    }
    
    /**
     * 生成帖子内容（内部方法）
     */
    private String generatePostContent(String topic) throws Exception {
        Generation gen = new Generation();
        
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("你是一个社交媒体内容创作者，请生成有趣、积极、正能量的帖子内容。内容要简洁明了，不超过200字。")
            .build();
            
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content("请生成一个关于" + topic + "的帖子内容")
            .build();
        
        GenerationParam param = GenerationParam.builder()
            .model(model)
            .messages(Arrays.asList(systemMsg, userMsg))
            .resultFormat(GenerationParam.ResultFormat.MESSAGE)
            .apiKey(apiKey)
            .build();
        
        GenerationResult result = gen.call(param);
        return result.getOutput().getChoices().get(0).getMessage().getContent();
    }
}
