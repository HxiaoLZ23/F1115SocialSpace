package com.form1115.f1115.main.controller;

import com.form1115.f1115.common.domain.Post;
import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.utils.Result;
import com.form1115.f1115.main.service.PostService;
import com.form1115.f1115.main.service.LikeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 帖子控制器
 */
@RestController
@RequestMapping("/api/post")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private LikeService likeService;
    
    private static final String SESSION_USER_KEY = "currentUser";
    
    /**
     * 发布帖子
     */
    @PostMapping
    public Result createPost(@RequestBody Map<String, Object> params, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        String content = (String) params.get("content");
        List<String> images = (List<String>) params.get("images");
        
        // 参数校验
        if (content == null || content.trim().isEmpty()) {
            return Result.error("帖子内容不能为空");
        }
        
        // 发布帖子
        Post post = postService.createPost(currentUser.getId(), content, images);
        
        return Result.success("发布成功", post);
    }
    
    /**
     * 获取帖子详情
     */
    @GetMapping("/{postId}")
    public Result getPostDetail(@PathVariable Long postId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        Long currentUserId = currentUser != null ? currentUser.getId() : null;
        
        Post post = postService.getPostDetail(postId, currentUserId);
        
        return Result.success(post);
    }
    
    /**
     * 获取帖子列表（分页）
     */
    @GetMapping("/list")
    public Result getPostList(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Post> pageInfo = postService.getPostList(pageNum, pageSize);
        
        return Result.success(pageInfo.getList(), pageInfo.getTotal());
    }
    
    /**
     * 获取首页时间线（分页）
     */
    @GetMapping("/timeline")
    public Result getTimeline(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        PageInfo<Post> pageInfo = postService.getTimeline(currentUser.getId(), pageNum, pageSize);
        
        return Result.success(pageInfo.getList(), pageInfo.getTotal());
    }
    
    /**
     * 获取用户发帖列表（分页）
     */
    @GetMapping("/user/{userId}")
    public Result getUserPosts(@PathVariable Long userId,
                               @RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Post> pageInfo = postService.getUserPosts(userId, pageNum, pageSize);
        
        return Result.success(pageInfo.getList(), pageInfo.getTotal());
    }
    
    /**
     * 删除帖子
     */
    @DeleteMapping("/{postId}")
    public Result deletePost(@PathVariable Long postId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        postService.deletePost(postId, currentUser.getId());
        
        return Result.success("删除成功");
    }
    
    /**
     * 点赞/取消点赞帖子
     */
    @PostMapping("/{postId}/like")
    public Result toggleLike(@PathVariable Long postId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        boolean liked = likeService.togglePostLike(postId, currentUser.getId());
        
        return Result.success(liked ? "点赞成功" : "取消点赞");
    }
}
