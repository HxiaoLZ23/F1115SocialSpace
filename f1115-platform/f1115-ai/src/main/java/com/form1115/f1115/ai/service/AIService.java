package com.form1115.f1115.ai.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.JsonUtils;
import com.form1115.f1115.ai.config.AIConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * AI服务类
 */
@Service
public class AIService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIService.class);
    
    @Autowired
    private AIConfig aiConfig;
    
    /**
     * 生成帖子内容
     */
    public String generatePost(String topic) {
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个社交媒体内容创作者，请生成有趣、积极、正能量的帖子内容。内容要简洁明了，不超过200字。")
                .build();
                
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("请生成一个关于" + topic + "的帖子内容")
                .build();
            
            GenerationParam param = GenerationParam.builder()
                .model(aiConfig.getModel())
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .apiKey(aiConfig.getApiKey())
                .build();
            
            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            logger.info("AI生成帖子成功，话题：{}，内容：{}", topic, content);
            return content;
            
        } catch (Exception e) {
            logger.error("AI生成帖子失败", e);
            return "今天天气真好！分享一下美好的心情。"; // 降级返回默认内容
        }
    }
    
    /**
     * 生成评论内容
     */
    public String generateComment(String postContent) {
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个友好的社交媒体用户，请针对帖子内容生成简短、友好的评论。评论要自然，不超过50字。")
                .build();
                
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("请针对以下帖子生成一条评论：" + postContent)
                .build();
            
            GenerationParam param = GenerationParam.builder()
                .model(aiConfig.getModel())
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .apiKey(aiConfig.getApiKey())
                .build();
            
            GenerationResult result = gen.call(param);
            String content = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            logger.info("AI生成评论成功，评论：{}", content);
            return content;
            
        } catch (Exception e) {
            logger.error("AI生成评论失败", e);
            return "很有意思的内容！"; // 降级返回默认内容
        }
    }
    
    /**
     * 内容审核
     */
    public AuditResult auditContent(String content) {
        try {
            Generation gen = new Generation();
            
            Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("你是一个内容审核员，请判断内容是否违规。只需回答：通过、拒绝、待审核。如果拒绝，请简要说明原因。")
                .build();
                
            Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("请审核以下内容：" + content)
                .build();
            
            GenerationParam param = GenerationParam.builder()
                .model(aiConfig.getModel())
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .apiKey(aiConfig.getApiKey())
                .build();
            
            GenerationResult result = gen.call(param);
            String auditResult = result.getOutput().getChoices().get(0).getMessage().getContent();
            
            logger.info("AI审核完成，结果：{}", auditResult);
            
            // 解析审核结果
            if (auditResult.contains("通过")) {
                return new AuditResult(0, "审核通过");
            } else if (auditResult.contains("拒绝")) {
                return new AuditResult(1, auditResult);
            } else {
                return new AuditResult(2, "待人工审核");
            }
            
        } catch (Exception e) {
            logger.error("AI审核失败", e);
            return new AuditResult(2, "审核失败，待人工审核");
        }
    }
    
    /**
     * 审核结果类
     */
    public static class AuditResult {
        private int status; // 0-通过 1-拒绝 2-待审核
        private String message;
        
        public AuditResult(int status, String message) {
            this.status = status;
            this.message = message;
        }
        
        public int getStatus() {
            return status;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
