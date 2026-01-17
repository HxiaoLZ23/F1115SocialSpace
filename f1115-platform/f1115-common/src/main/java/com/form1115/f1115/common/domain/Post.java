package com.form1115.f1115.common.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子实体类
 */
@Data
public class Post implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 帖子ID
     */
    private Long id;
    
    /**
     * 发布者ID
     */
    private Long userId;
    
    /**
     * 帖子内容
     */
    private String content;
    
    /**
     * 图片列表（JSON数组字符串）
     */
    private String images;
    
    /**
     * 类型：0-普通 1-置顶 2-AI生成
     */
    private Integer type;
    
    /**
     * 状态：0-删除 1-正常 2-审核中 3-违规
     */
    private Integer status;
    
    /**
     * 点赞数
     */
    private Integer likeCount;
    
    /**
     * 评论数
     */
    private Integer commentCount;
    
    /**
     * 浏览量
     */
    private Integer viewCount;
    
    /**
     * 发布时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    // 扩展字段（不在数据库中）
    /**
     * 发布者信息
     */
    private UserProfile user;
    
    /**
     * 图片列表（解析后的）
     */
    private List<String> imageList;
    
    /**
     * 是否已点赞（当前用户是否点赞了该帖子）
     */
    private Boolean isLiked;
}
