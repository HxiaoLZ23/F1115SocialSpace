package com.form1115.f1115.main.controller;

import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.utils.Result;
import com.form1115.f1115.main.service.FollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 关注控制器
 */
@RestController
@RequestMapping("/api/user")
public class FollowController {
    
    @Autowired
    private FollowService followService;
    
    private static final String SESSION_USER_KEY = "currentUser";
    
    /**
     * 关注/取消关注用户
     */
    @PostMapping("/{userId}/follow")
    public Result toggleFollow(@PathVariable Long userId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        boolean followed = followService.toggleFollow(currentUser.getId(), userId);
        
        return Result.success(followed ? "关注成功" : "取消关注");
    }
    
    /**
     * 获取关注列表
     */
    @GetMapping("/{userId}/following")
    public Result getFollowing(@PathVariable Long userId,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "20") Integer pageSize) {
        PageInfo<UserProfile> pageInfo = followService.getFollowing(userId, pageNum, pageSize);
        
        return Result.success(pageInfo.getList(), pageInfo.getTotal());
    }
    
    /**
     * 获取粉丝列表
     */
    @GetMapping("/{userId}/followers")
    public Result getFollowers(@PathVariable Long userId,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "20") Integer pageSize) {
        PageInfo<UserProfile> pageInfo = followService.getFollowers(userId, pageNum, pageSize);
        
        return Result.success(pageInfo.getList(), pageInfo.getTotal());
    }
}
