package com.form1115.f1115.main.mapper;

import com.form1115.f1115.common.domain.UserProfile;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    UserProfile selectByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询用户
     */
    UserProfile selectByEmail(@Param("email") String email);
    
    /**
     * 根据ID查询用户
     */
    UserProfile selectById(@Param("id") Long id);
    
    /**
     * 插入用户
     */
    int insert(UserProfile user);
    
    /**
     * 更新用户信息
     */
    int update(UserProfile user);
    
    /**
     * 更新密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    
    /**
     * 更新头像
     */
    int updateAvatar(@Param("id") Long id, @Param("avatar") String avatar);
    
    /**
     * 查询用户统计信息（关注数、粉丝数、发帖数）
     */
    UserProfile selectUserWithStats(@Param("id") Long id, @Param("currentUserId") Long currentUserId);
}
