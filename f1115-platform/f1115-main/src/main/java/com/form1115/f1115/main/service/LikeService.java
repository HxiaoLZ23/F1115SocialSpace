package com.form1115.f1115.main.service;

import com.form1115.f1115.common.domain.Comment;
import com.form1115.f1115.common.domain.Post;
import com.form1115.f1115.common.exception.BusinessException;
import com.form1115.f1115.main.mapper.CommentMapper;
import com.form1115.f1115.main.mapper.LikeMapper;
import com.form1115.f1115.main.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 点赞服务类
 */
@Service
public class LikeService {
    
    @Autowired
    private LikeMapper likeMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    /**
     * 点赞/取消点赞帖子
     */
    @Transactional
    public boolean togglePostLike(Long postId, Long userId) {
        // 1. 验证帖子存在
        Post post = postMapper.selectById(postId);
        if (post == null || post.getStatus() != 1) {
            throw new BusinessException("帖子不存在或已删除");
        }
        
        // 2. 检查是否已点赞
        int count = likeMapper.checkPostLike(postId, userId);
        
        if (count > 0) {
            // 已点赞，取消点赞
            likeMapper.deletePostLike(postId, userId);
            postMapper.decrementLikeCount(postId);
            return false; // 返回false表示取消点赞
        } else {
            // 未点赞，添加点赞
            likeMapper.insertPostLike(postId, userId);
            postMapper.incrementLikeCount(postId);
            return true; // 返回true表示点赞成功
        }
    }
    
    /**
     * 点赞/取消点赞评论
     */
    @Transactional
    public boolean toggleCommentLike(Long commentId, Long userId) {
        // 1. 验证评论存在
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null || comment.getStatus() != 1) {
            throw new BusinessException("评论不存在或已删除");
        }
        
        // 2. 检查是否已点赞
        int count = likeMapper.checkCommentLike(commentId, userId);
        
        if (count > 0) {
            // 已点赞，取消点赞
            likeMapper.deleteCommentLike(commentId, userId);
            commentMapper.decrementLikeCount(commentId);
            return false;
        } else {
            // 未点赞，添加点赞
            likeMapper.insertCommentLike(commentId, userId);
            commentMapper.incrementLikeCount(commentId);
            return true;
        }
    }
}
