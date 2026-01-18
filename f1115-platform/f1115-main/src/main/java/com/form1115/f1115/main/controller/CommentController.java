package com.form1115.f1115.main.controller;

import com.form1115.f1115.common.domain.Comment;
import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.utils.Result;
import com.form1115.f1115.main.service.CommentService;
import com.form1115.f1115.main.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private LikeService likeService;
    
    private static final String SESSION_USER_KEY = "currentUser";
    
    /**
     * 发布评论
     */
    @PostMapping("/post/{postId}/comment")
    public Result createComment(@PathVariable Long postId,
                                @RequestBody Map<String, Object> params,
                                HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        String content = (String) params.get("content");
        Long parentId = params.get("parentId") != null ? 
                        Long.valueOf(params.get("parentId").toString()) : null;
        
        // 参数校验
        if (content == null || content.trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }
        
        // 发布评论
        Comment comment = commentService.createComment(postId, currentUser.getId(), content, parentId);
        
        return Result.success("评论成功", comment);
    }
    
    /**
     * 获取帖子的评论列表
     */
    @GetMapping("/post/{postId}/comments")
    public Result getComments(@PathVariable Long postId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        Long currentUserId = currentUser != null ? currentUser.getId() : null;
        
        List<Comment> comments = commentService.getCommentsByPostId(postId, currentUserId);
        
        return Result.success(comments);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/comment/{commentId}")
    public Result deleteComment(@PathVariable Long commentId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        commentService.deleteComment(commentId, currentUser.getId());
        
        return Result.success("删除成功");
    }
    
    /**
     * 点赞/取消点赞评论
     */
    @PostMapping("/comment/{commentId}/like")
    public Result toggleCommentLike(@PathVariable Long commentId, HttpSession session) {
        UserProfile currentUser = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        if (currentUser == null) {
            return Result.unauthorized();
        }
        
        boolean liked = likeService.toggleCommentLike(commentId, currentUser.getId());
        
        return Result.success(liked ? "点赞成功" : "取消点赞");
    }
}
