package com.form1115.f1115.main.controller;

import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.utils.Result;
import com.form1115.f1115.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    private static final String SESSION_USER_KEY = "currentUser";
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String email = params.get("email");
        String nickname = params.get("nickname");
        
        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        if (password.length() < 6) {
            return Result.error("密码长度不能少于6位");
        }
        
        // 注册用户
        UserProfile user = userService.register(username, password, email, nickname);
        
        return Result.success("注册成功", user);
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params, HttpSession session) {
        String username = params.get("username");
        String password = params.get("password");
        
        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return Result.error("密码不能为空");
        }
        
        // 登录验证
        UserProfile user = userService.login(username, password);
        
        // 保存到Session
        session.setAttribute(SESSION_USER_KEY, user);
        
        return Result.success("登录成功", user);
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.removeAttribute(SESSION_USER_KEY);
        session.invalidate();
        return Result.success("登出成功");
    }
    
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/profile")
    public Result getProfile(HttpSession session) {
        UserProfile user = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (user == null) {
            return Result.unauthorized();
        }
        
        // 查询最新的用户信息
        UserProfile currentUser = userService.getUserWithStats(user.getId(), user.getId());
        
        return Result.success(currentUser);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public Result updateProfile(@RequestBody UserProfile user, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        // 只能更新自己的信息
        user.setId(currentUser.getId());
        userService.updateUser(user);
        
        // 更新Session中的用户信息
        UserProfile updatedUser = userService.getUserById(currentUser.getId());
        session.setAttribute(SESSION_USER_KEY, updatedUser);
        
        return Result.success("更新成功", updatedUser);
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result updatePassword(@RequestBody Map<String, String> params, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        
        // 参数校验
        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return Result.error("原密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error("新密码不能为空");
        }
        if (newPassword.length() < 6) {
            return Result.error("新密码长度不能少于6位");
        }
        
        // 修改密码
        userService.updatePassword(currentUser.getId(), oldPassword, newPassword);
        
        return Result.success("密码修改成功");
    }
    
    /**
     * 获取指定用户信息
     */
    @GetMapping("/{userId}")
    public Result getUserInfo(@PathVariable Long userId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        Long currentUserId = currentUser != null ? currentUser.getId() : null;
        
        // 查询用户信息（包含统计数据）
        UserProfile user = userService.getUserWithStats(userId, currentUserId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        return Result.success(user);
    }
}
