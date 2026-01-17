package com.form1115.f1115.common.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论实体类
 */
@Data
public class Comment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 评论ID
     */
    private Long id;
    
    /**
     * 帖子ID
     */
    private Long postId;
    
    /**
     * 评论者ID
     */
    private Long userId;
    
    /**
     * 父评论ID（回复时使用）
     */
    private Long parentId;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 状态：0-删除 1-正常 2-审核中 3-违规
     */
    private Integer status;
    
    /**
     * 类型：0-普通 1-AI生成
     */
    private Integer type;
    
    /**
     * 评论时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    // 扩展字段（不在数据库中）
    /**
     * 评论者信息
     */
    private UserProfile user;
    
    /**
     * 父评论信息
     */
    private Comment parentComment;
    
    /**
     * 子评论列表
     */
    private List<Comment> replies;
    
    /**
     * 是否已点赞（当前用户是否点赞了该评论）
     */
    private Boolean isLiked;
}
