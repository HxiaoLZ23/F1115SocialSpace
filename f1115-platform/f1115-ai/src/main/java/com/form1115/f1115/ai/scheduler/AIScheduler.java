package com.form1115.f1115.ai.scheduler;

import com.form1115.f1115.ai.service.AIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * AI定时任务
 */
@Component
public class AIScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(AIScheduler.class);
    
    @Autowired
    private AIService aiService;
    
    // AI机器人用户ID（需要在数据库中创建）
    private static final Long AI_USER_ID = 2L;
    
    // 热点话题列表
    private static final List<String> HOT_TOPICS = Arrays.asList(
        "科技发展",
        "生活感悟",
        "学习心得",
        "美食分享",
        "旅行见闻",
        "读书笔记",
        "健康生活",
        "工作经验"
    );
    
    /**
     * AI定时发帖 - 每天上午10点和下午3点执行
     * Cron表达式：秒 分 时 日 月 周
     * 0 0 10,15 * * ? - 每天10点和15点执行
     */
    @Scheduled(cron = "0 0 10,15 * * ?")
    public void generateAIPost() {
        try {
            logger.info("AI定时发帖任务开始执行");
            
            // 随机选择话题
            String topic = getRandomTopic();
            
            // 调用AI生成内容
            String content = aiService.generatePost(topic);
            
            // 发布帖子（需要注入PostService）
            // postService.createPost(AI_USER_ID, content, null);
            
            logger.info("AI定时发帖成功，话题：{}，内容：{}", topic, content);
            
        } catch (Exception e) {
            logger.error("AI定时发帖失败", e);
        }
    }
    
    /**
     * AI定时评论 - 每小时执行一次
     * 0 0 * * * ? - 每小时执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void generateAIComment() {
        try {
            logger.info("AI定时评论任务开始执行");
            
            // 获取热门帖子（需要注入PostService）
            // List<Post> hotPosts = postService.getHotPosts(5);
            
            // 为每个热门帖子生成评论
            // for (Post post : hotPosts) {
            //     String comment = aiService.generateComment(post.getContent());
            //     commentService.createComment(post.getId(), AI_USER_ID, comment, null);
            // }
            
            logger.info("AI定时评论任务执行完成");
            
        } catch (Exception e) {
            logger.error("AI定时评论失败", e);
        }
    }
    
    /**
     * 获取随机话题
     */
    private String getRandomTopic() {
        Random random = new Random();
        int index = random.nextInt(HOT_TOPICS.size());
        return HOT_TOPICS.get(index);
    }
    
    /**
     * Cron表达式说明：
     * 秒 分 时 日 月 周
     * 
     * 示例：
     * 0 0 10,15 * * ?  - 每天10点和15点执行
     * 0 0 * * * ?      - 每小时执行
     * 0 0/30 * * * ?   - 每30分钟执行
     * 0 0 0 * * ?      - 每天凌晨执行
     * 0 0 12 * * ?     - 每天中午12点执行
     */
}
