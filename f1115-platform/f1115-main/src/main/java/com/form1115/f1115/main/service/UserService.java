package com.form1115.f1115.main.service;

import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.exception.BusinessException;
import com.form1115.f1115.common.utils.PasswordUtil;
import com.form1115.f1115.main.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务类
 */
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户注册
     */
    @Transactional
    public UserProfile register(String username, String password, String email, String nickname) {
        // 1. 校验用户名是否已存在
        if (userMapper.selectByUsername(username) != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 2. 校验邮箱是否已存在
        if (email != null && !email.isEmpty() && userMapper.selectByEmail(email) != null) {
            throw new BusinessException("邮箱已被注册");
        }
        
        // 3. 创建用户对象
        UserProfile user = new UserProfile();
        user.setUsername(username);
        user.setPassword(PasswordUtil.encode(password)); // BCrypt加密
        user.setEmail(email);
        user.setNickname(nickname != null ? nickname : username);
        user.setStatus(1); // 正常状态
        user.setRole(0);   // 普通用户
        
        // 4. 插入数据库
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException("注册失败");
        }
        
        // 5. 清除密码字段（不返回给前端）
        user.setPassword(null);
        
        return user;
    }
    
    /**
     * 用户登录
     */
    public UserProfile login(String username, String password) {
        // 1. 查询用户
        UserProfile user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 2. 校验密码
        if (!PasswordUtil.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 3. 校验用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 4. 清除密码字段
        user.setPassword(null);
        
        return user;
    }
    
    /**
     * 根据ID查询用户信息
     */
    public UserProfile getUserById(Long id) {
        UserProfile user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null); // 清除密码
        }
        return user;
    }
    
    /**
     * 查询用户详细信息（包含统计数据）
     */
    public UserProfile getUserWithStats(Long id, Long currentUserId) {
        UserProfile user = userMapper.selectUserWithStats(id, currentUserId);
        if (user != null) {
            user.setPassword(null); // 清除密码
        }
        return user;
    }
    
    /**
     * 更新用户信息
     */
    @Transactional
    public void updateUser(UserProfile user) {
        int result = userMapper.update(user);
        if (result <= 0) {
            throw new BusinessException("更新失败");
        }
    }
    
    /**
     * 修改密码
     */
    @Transactional
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        // 1. 查询用户
        UserProfile user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 2. 校验旧密码
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 3. 更新密码
        String encodedPassword = PasswordUtil.encode(newPassword);
        int result = userMapper.updatePassword(userId, encodedPassword);
        if (result <= 0) {
            throw new BusinessException("修改密码失败");
        }
    }
}
