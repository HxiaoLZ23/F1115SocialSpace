package com.form1115.f1115.ai.controller;

import com.form1115.f1115.ai.service.AIService;
import com.form1115.f1115.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AI功能控制器（用于测试和手动触发）
 */
@RestController
@RequestMapping("/api/ai")
public class AIController {
    
    @Autowired
    private AIService aiService;
    
    /**
     * 测试生成帖子内容
     */
    @PostMapping("/generate/post")
    public Result generatePost(@RequestBody Map<String, String> params) {
        String topic = params.get("topic");
        if (topic == null || topic.trim().isEmpty()) {
            return Result.error("话题不能为空");
        }
        
        String content = aiService.generatePost(topic);
        
        return Result.success("生成成功", content);
    }
    
    /**
     * 测试生成评论内容
     */
    @PostMapping("/generate/comment")
    public Result generateComment(@RequestBody Map<String, String> params) {
        String postContent = params.get("postContent");
        if (postContent == null || postContent.trim().isEmpty()) {
            return Result.error("帖子内容不能为空");
        }
        
        String content = aiService.generateComment(postContent);
        
        return Result.success("生成成功", content);
    }
    
    /**
     * 测试内容审核
     */
    @PostMapping("/audit")
    public Result auditContent(@RequestBody Map<String, String> params) {
        String content = params.get("content");
        if (content == null || content.trim().isEmpty()) {
            return Result.error("内容不能为空");
        }
        
        AIService.AuditResult result = aiService.auditContent(content);
        
        return Result.success("审核完成", result);
    }
}
