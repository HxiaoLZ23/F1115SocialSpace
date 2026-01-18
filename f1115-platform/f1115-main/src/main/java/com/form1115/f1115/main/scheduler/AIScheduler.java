package com.form1115.f1115.main.scheduler;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.form1115.f1115.main.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * AI定时任务
 * 每小时发一次帖（24条/天）
 */
@Component
public class AIScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(AIScheduler.class);
    
    @Autowired
    private PostService postService;
    
    @Value("${dashscope.apiKey}")
    private String apiKey;
    
    @Value("${dashscope.model}")
    private String model;
    
    // AI机器人用户ID
    private static final Long AI_USER_ID = 2L;
    
    // 备用话题列表
    private static final List<String> BACKUP_TOPICS = Arrays.asList(
        "科技发展", "生活感悟", "学习心得", "美食分享",
        "旅行见闻", "读书笔记", "健康生活", "工作经验",
        "音乐推荐", "电影评论", "运动健身", "摄影技巧"
    );
    
    /**
     * AI定时发帖 - 每小时执行一次（24条/天）
     * Cron表达式：0 0 * * * ? - 每小时整点执行
     * 
     * 测试用：每分钟执行一次
     * Cron表达式：0 * * * * ? - 每分钟执行
     */
    @Scheduled(cron = "0 0 * * * ?")  // 每小时执行
    // @Scheduled(cron = "0 * * * * ?")  // 每分钟执行（测试用）
    public void generateAIPost() {
        try {
            logger.info("AI定时发帖任务开始执行");
            
            // 1. 获取热点话题
            String topic = getHotTopic();
            logger.info("选择话题：{}", topic);
            
            // 2. 生成内容
            String content = generatePostContent(topic);
            
            // 3. 发布帖子
            postService.createPost(AI_USER_ID, content, null);
            
            logger.info("AI定时发帖成功，话题：{}", topic);
            
        } catch (Exception e) {
            logger.error("AI定时发帖失败", e);
        }
    }
    
    /**
     * 获取热点话题（从微博热搜）
     */
    private String getHotTopic() {
        try {
            // 尝试获取微博热搜
            List<String> weiboTopics = fetchWeiboHotSearch();
            if (!weiboTopics.isEmpty()) {
                Random random = new Random();
                return weiboTopics.get(random.nextInt(Math.min(10, weiboTopics.size())));
            }
        } catch (Exception e) {
            logger.warn("获取微博热搜失败，使用备用话题", e);
        }
        
        // 如果获取失败，使用备用话题
        Random random = new Random();
        return BACKUP_TOPICS.get(random.nextInt(BACKUP_TOPICS.size()));
    }

    /**
     * 获取微博热搜前10
     * 注意：Cookie信息已从环境变量读取，不在代码中硬编码
     */
    private List<String> fetchWeiboHotSearch() {
        List<String> topics = new ArrayList<>();
        try {
            logger.info("正在尝试抓取微博热搜");

            // 从环境变量或配置文件读取Cookie（生产环境应该这样做）
            // 开发环境可以硬编码，但不提交到GYOUR_WEIBO_SUB_COOKIE
            String SUB = System.getenv("YOUR_WEIBO_SUB_COOKIE");
            String SUBP = System.getenv("YOUR_WEIBO_SUBP_COOKIE");
            
            // 如果环境变量没有配置，使用默认值（仅用于开发测试）
            if (SUB == null || SUB.isEmpty()) {
                SUB = "YOUR_WEIBO_SUB_COOKIE";  // 需要配置
            }
            if (SUBP == null || SUBP.isEmpty()) {
                SUBP = "YOUR_WEIBO_SUBP_COOKIE";  // 需要配置
            }

            Document doc = Jsoup.connect("https://s.weibo.com/top/summary")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .header("Sec-Fetch-Dest", "document")
                    .header("Sec-Fetch-Mode", "navigate")
                    .header("Sec-Fetch-Site", "same-origin")
                    .header("Upgrade-Insecure-Requests", "1")
                    .cookie("SUB", SUB)
                    .cookie("SUBP", SUBP)
                    .timeout(10000)
                    .get();

            // 调试：打印 HTML 预览
            String html = doc.html();
            logger.debug("HTML长度: {}, 前300字符:\n{}", html.length(),
                    html.length() > 300 ? html.substring(0, 300).replaceAll("\\s+", " ") : html);

            // 解析热搜
            Elements rows = doc.select("table tbody tr");
            logger.debug("解析到表格行数: {}", rows.size());

            for (Element row : rows) {
                Element link = row.select("td.td-02 a").first();
                if (link != null) {
                    String topic = link.text().trim();
                    if (!topic.isEmpty() && !topic.equals("搜索") && !topic.equals("反馈")) {
                        topics.add(topic);
                        if (topics.size() >= 10) break;
                    }
                }
            }

            logger.info("成功获取微博热搜{}条", topics.size());
            if (!topics.isEmpty()) {
                logger.debug("热搜示例: {}", topics.subList(0, Math.min(3, topics.size())));
            } else {
                logger.warn("解析结果为空，body文本: '{}'",
                        doc.body().text().length() > 200 ?
                                doc.body().text().substring(0, 200) :
                                doc.body().text());
            }

        } catch (Exception e) {
            logger.error("抓取微博热搜异常", e);
        }
        return topics;
    }
    /**
     * 生成帖子内容
     */
    private String generatePostContent(String topic) throws Exception {
        Generation gen = new Generation();
        
        Message systemMsg = Message.builder()
            .role(Role.SYSTEM.getValue())
            .content("你是一个社交媒体内容创作者，请生成有趣、积极、正能量的帖子内容。内容要简洁明了，不超过200字。可以使用适当的emoji表情。")
            .build();
            
        Message userMsg = Message.builder()
            .role(Role.USER.getValue())
            .content("请生成一个关于'" + topic + "'的帖子内容")
            .build();
        
        GenerationParam param = GenerationParam.builder()
            .model(model)
            .messages(Arrays.asList(systemMsg, userMsg))
            .resultFormat(GenerationParam.ResultFormat.MESSAGE)
            .apiKey(apiKey)
            .build();
        
        GenerationResult result = gen.call(param);
        return result.getOutput().getChoices().get(0).getMessage().getContent();
    }
    
    /**
     * Cron表达式说明：
     * 秒 分 时 日 月 周
     * 
     * 0 0 * * * ?     - 每小时执行（24次/天）
     * 0 0 0-23 * * ?  - 每小时执行（24次/天）
     * 0 0/30 * * * ?  - 每30分钟执行（48次/天）
     * 0 * * * * ?     - 每分钟执行（测试用）
     * 0 0 10,15 * * ? - 每天10点和15点执行（2次/天）
     */
}