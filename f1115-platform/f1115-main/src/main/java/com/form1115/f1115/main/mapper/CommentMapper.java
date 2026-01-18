package com.form1115.f1115.main.mapper;

import com.form1115.f1115.common.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 */
public interface CommentMapper {
    
    /**
     * 插入评论
     */
    int insert(Comment comment);
    
    /**
     * 根据ID查询评论
     */
    Comment selectById(@Param("id") Long id);
    
    /**
     * 查询帖子的评论列表（一级评论）
     */
    List<Comment> selectByPostId(@Param("postId") Long postId);
    
    /**
     * 查询评论的回复列表（二级评论）
     */
    List<Comment> selectRepliesByParentId(@Param("parentId") Long parentId);
    
    /**
     * 删除评论（逻辑删除）
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 增加点赞数
     */
    int incrementLikeCount(@Param("id") Long id);
    
    /**
     * 减少点赞数
     */
    int decrementLikeCount(@Param("id") Long id);
    
    /**
     * 查询评论详情（包含用户信息和点赞状态）
     */
    Comment selectDetailById(@Param("id") Long id, @Param("currentUserId") Long currentUserId);
    
    /**
     * 查询帖子的评论列表（包含用户信息和回复）
     */
    List<Comment> selectCommentsByPostId(@Param("postId") Long postId, @Param("currentUserId") Long currentUserId);
}
