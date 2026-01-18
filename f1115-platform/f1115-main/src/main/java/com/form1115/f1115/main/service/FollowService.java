package com.form1115.f1115.main.service;

import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.exception.BusinessException;
import com.form1115.f1115.main.mapper.FollowMapper;
import com.form1115.f1115.main.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 关注服务类
 */
@Service
public class FollowService {
    
    @Autowired
    private FollowMapper followMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 关注/取消关注用户
     */
    @Transactional
    public boolean toggleFollow(Long followerId, Long followingId) {
        // 1. 验证用户存在
        UserProfile followingUser = userMapper.selectById(followingId);
        if (followingUser == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 不能关注自己
        if (followerId.equals(followingId)) {
            throw new BusinessException("不能关注自己");
        }
        
        // 3. 检查是否已关注
        int count = followMapper.checkFollow(followerId, followingId);
        
        if (count > 0) {
            // 已关注，取消关注
            followMapper.delete(followerId, followingId);
            return false;
        } else {
            // 未关注，添加关注
            followMapper.insert(followerId, followingId);
            return true;
        }
    }
    
    /**
     * 获取关注列表（分页）
     */
    public PageInfo<UserProfile> getFollowing(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserProfile> users = followMapper.selectFollowing(userId);
        return new PageInfo<>(users);
    }
    
    /**
     * 获取粉丝列表（分页）
     */
    public PageInfo<UserProfile> getFollowers(Long userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserProfile> users = followMapper.selectFollowers(userId);
        return new PageInfo<>(users);
    }
}
