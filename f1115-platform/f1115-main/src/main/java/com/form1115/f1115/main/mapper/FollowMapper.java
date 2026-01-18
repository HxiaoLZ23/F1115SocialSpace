package com.form1115.f1115.main.mapper;

import com.form1115.f1115.common.domain.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注Mapper接口
 */
public interface FollowMapper {
    
    /**
     * 关注用户
     */
    int insert(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    /**
     * 取消关注
     */
    int delete(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    /**
     * 检查是否已关注
     */
    int checkFollow(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    /**
     * 获取关注列表
     */
    List<UserProfile> selectFollowing(@Param("userId") Long userId);
    
    /**
     * 获取粉丝列表
     */
    List<UserProfile> selectFollowers(@Param("userId") Long userId);
}
