package com.form1115.f1115.main.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 点赞Mapper接口
 */
public interface LikeMapper {
    
    /**
     * 帖子点赞
     */
    int insertPostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 取消帖子点赞
     */
    int deletePostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 检查是否已点赞帖子
     */
    int checkPostLike(@Param("postId") Long postId, @Param("userId") Long userId);
    
    /**
     * 评论点赞
     */
    int insertCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
    
    /**
     * 取消评论点赞
     */
    int deleteCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
    
    /**
     * 检查是否已点赞评论
     */
    int checkCommentLike(@Param("commentId") Long commentId, @Param("userId") Long userId);
}
