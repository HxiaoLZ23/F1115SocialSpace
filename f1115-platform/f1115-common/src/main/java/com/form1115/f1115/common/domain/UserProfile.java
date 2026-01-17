package com.form1115.f1115.common.domain;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体类
 */
@Data
public class UserProfile implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名（唯一）
     */
    private String username;
    
    /**
     * 密码（BCrypt加密）
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 状态：0-禁用 1-正常
     */
    private Integer status;
    
    /**
     * 角色：0-普通用户 1-管理员 2-AI机器人
     */
    private Integer role;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    // 扩展字段（不在数据库中）
    /**
     * 关注数
     */
    private Integer followingCount;
    
    /**
     * 粉丝数
     */
    private Integer followerCount;
    
    /**
     * 发帖数
     */
    private Integer postCount;
    
    /**
     * 是否已关注（当前用户是否关注了该用户）
     */
    private Boolean isFollowed;
}
