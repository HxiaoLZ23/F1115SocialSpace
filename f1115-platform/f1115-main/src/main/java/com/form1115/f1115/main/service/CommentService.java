package com.form1115.f1115.main.service;

import com.form1115.f1115.common.domain.Comment;
import com.form1115.f1115.common.domain.Post;
import com.form1115.f1115.common.exception.BusinessException;
import com.form1115.f1115.main.mapper.CommentMapper;
import com.form1115.f1115.main.mapper.PostMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评论服务类
 */
@Service
public class CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    /**
     * 发布评论
     */
    @Transactional
    public Comment createComment(Long postId, Long userId, String content, Long parentId) {
        // 1. 参数校验
        if (content == null || content.trim().isEmpty()) {
            throw new BusinessException("评论内容不能为空");
        }
        if (content.length() > 500) {
            throw new BusinessException("评论内容不能超过500字");
        }
        
        // 2. 验证帖子存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getStatus() != 1) {
            throw new BusinessException("帖子不存在或已删除");
        }
        
        // 3. 如果是回复，验证父评论存在
        if (parentId != null) {
            Comment parentComment = commentMapper.selectById(parentId);
            if (parentComment == null || parentComment.getStatus() != 1) {
                throw new BusinessException("回复的评论不存在或已删除");
            }
        }
        
        // 4. 创建评论对象
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setStatus(1); // 正常状态
        comment.setType(0);   // 普通评论
        
        // 5. 插入数据库
        int result = commentMapper.insert(comment);
        if (result <= 0) {
            throw new BusinessException("评论失败");
        }
        
        // 6. 更新帖子评论数
        postMapper.incrementCommentCount(postId);
        
        return comment;
    }
    
    /**
     * 获取帖子的评论列表（包含回复）
     * 支持多层级回复结构（递归组织）
     */
    public List<Comment> getCommentsByPostId(Long postId, Long currentUserId) {
        // 查询所有评论
        List<Comment> allComments = commentMapper.selectCommentsByPostId(postId, currentUserId);
        
        // 构建评论ID到评论对象的映射
        Map<Long, Comment> commentMap = allComments.stream()
                .collect(Collectors.toMap(Comment::getId, c -> c));
        
        // 组织评论结构（递归方式）
        Map<Long, List<Comment>> repliesMap = allComments.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Comment::getParentId));
        
        // 为所有评论设置回复列表
        for (Comment comment : allComments) {
            List<Comment> replies = repliesMap.get(comment.getId());
            comment.setReplies(replies != null ? replies : new ArrayList<>());
        }
        
        // 获取一级评论（parentId为null的评论）
        List<Comment> topLevelComments = allComments.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());
        
        return topLevelComments;
    }
    
    /**
     * 根据ID获取评论
     */
    public Comment getCommentById(Long commentId) {
        return commentMapper.selectById(commentId);
    }
    
    /**
     * 删除评论
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        // 只能删除自己的评论
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除该评论");
        }
        
        int result = commentMapper.deleteById(commentId);
        if (result <= 0) {
            throw new BusinessException("删除失败");
        }
        
        // 更新帖子评论数（减1）
        // 注意：这里简化处理，实际应该递归删除所有回复并更新计数
        postMapper.decrementLikeCount(comment.getPostId());
    }
}
