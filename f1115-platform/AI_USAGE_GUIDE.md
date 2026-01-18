# AI功能使用指南

## 🎯 功能概览

F1115平台已集成通义千问AI，支持：
1. ✅ 手动触发AI发帖
2. ✅ 自动定时发帖（每小时1次，24次/天）
3. ✅ 获取微博热搜话题
4. ✅ AI内容生成测试
5. ✅ 内容审核测试

---

## 🚀 功能1: 手动让AI发帖

### 方法1: 使用浏览器Console（最简单）

1. **打开浏览器** http://localhost:3000
2. **登录系统**
3. **按F12打开Console**
4. **复制以下代码并执行**：

```javascript
// 手动触发AI发帖（使用默认话题）
fetch('http://localhost:8080/api/ai/post/now', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  credentials: 'include',
  body: JSON.stringify({})
})
.then(res => res.json())
.then(data => {
  console.log('AI发帖结果:', data);
  alert('AI发帖成功！刷新页面查看');
})
```

**或者指定话题**：

```javascript
// 手动触发AI发帖（指定话题）
fetch('http://localhost:8080/api/ai/post/now', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  credentials: 'include',
  body: JSON.stringify({topic: '人工智能'})
})
.then(res => res.json())
.then(data => {
  console.log('AI发帖结果:', data);
  alert('AI发帖成功！刷新页面查看');
})
```

5. **刷新首页**，你会看到AI发布的帖子！

### 方法2: 使用Postman

**请求**:
```
POST http://localhost:8080/api/ai/post/now
Content-Type: application/json

{
  "topic": "科技发展"
}
```

**期望响应**:
```json
{
  "code": 200,
  "msg": "AI发帖成功",
  "data": {
    "id": 123,
    "userId": 2,
    "content": "AI生成的内容...",
    "createTime": "2026-01-17 16:00:00"
  }
}
```

---

## ⏰ 功能2: 自动定时发帖

### 当前配置

**发帖频率**: 每小时1次（24条/天）  
**Cron表达式**: `0 0 * * * ?`  
**话题来源**: 微博热搜前10（如果获取失败则使用备用话题）

### 查看定时任务日志

在IDEA的Tomcat控制台中，你会看到：

```
2026-01-17 16:00:00 INFO  AIScheduler - AI定时发帖任务开始执行
2026-01-17 16:00:00 INFO  AIScheduler - 选择话题：人工智能
2026-01-17 16:00:02 INFO  AIScheduler - AI定时发帖成功，话题：人工智能
```

### 修改发帖频率

如果想改变发帖频率，修改 `AIScheduler.java` 中的Cron表达式：

```java
// 每小时执行（24次/天）- 当前配置
@Scheduled(cron = "0 0 * * * ?")

// 每30分钟执行（48次/天）
@Scheduled(cron = "0 0/30 * * * ?")

// 每15分钟执行（96次/天）
@Scheduled(cron = "0 0/15 * * * ?")

// 每分钟执行（测试用）
@Scheduled(cron = "0 * * * * ?")

// 每天10点和15点执行（2次/天）
@Scheduled(cron = "0 0 10,15 * * ?")
```

### 临时禁用定时任务

如果暂时不需要定时发帖，注释掉 `@Scheduled` 注解：

```java
// @Scheduled(cron = "0 0 * * * ?")
public void generateAIPost() {
    ...
}
```

---

## 🔥 功能3: 获取微博热搜话题

### 自动获取

定时任务会自动尝试获取微博热搜前10：
1. 访问 https://weibo.com/hot/search
2. 解析热搜列表
3. 随机选择一个话题
4. 如果获取失败，使用备用话题

### 查看获取的话题

在后端日志中查看：

```
INFO  AIScheduler - 成功获取微博热搜10条
INFO  AIScheduler - 选择话题：某某热搜话题
```

### 备用话题

如果微博热搜获取失败，会使用以下备用话题：
- 科技发展
- 生活感悟
- 学习心得
- 美食分享
- 旅行见闻
- 读书笔记
- 健康生活
- 工作经验
- 音乐推荐
- 电影评论
- 运动健身
- 摄影技巧

---

## 🧪 快速测试AI发帖

### 一键测试脚本

复制到浏览器Console：

```javascript
// 连续发布5条AI帖子（用于快速测试）
async function testAIPosts() {
  const topics = ['科技', '生活', '学习', '美食', '旅行'];
  
  for (let topic of topics) {
    console.log(`正在生成关于"${topic}"的帖子...`);
    
    const res = await fetch('http://localhost:8080/api/ai/post/now', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      credentials: 'include',
      body: JSON.stringify({topic})
    }).then(r => r.json());
    
    console.log(`✅ 发帖成功！ID: ${res.data.id}`);
    
    // 等待2秒，避免API调用过快
    await new Promise(resolve => setTimeout(resolve, 2000));
  }
  
  console.log('🎉 全部完成！刷新页面查看');
  alert('AI已发布5条帖子！刷新页面查看');
}

// 运行测试
testAIPosts();
```

---

## 📊 查看AI发布的帖子

### 在首页查看

1. 刷新首页
2. 查找发帖人为"AI小助手"的帖子
3. 这些就是AI自动生成的内容

### 在数据库中查看

```sql
-- 查看AI发布的帖子
SELECT * FROM post WHERE user_id = 2 ORDER BY create_time DESC;

-- 统计AI发帖数量
SELECT COUNT(*) FROM post WHERE user_id = 2;
```

---

## ⚙️ 高级配置

### 1. 修改AI提示词

在 `AIScheduler.java` 的 `generatePostContent()` 方法中修改：

```java
Message systemMsg = Message.builder()
    .role(Role.SYSTEM.getValue())
    .content("你是一个专业的内容创作者，请生成...") // 修改这里
    .build();
```

### 2. 修改发帖频率

在 `AIScheduler.java` 中修改Cron表达式：

```java
@Scheduled(cron = "0 0 * * * ?")  // 每小时
```

### 3. 禁用微博热搜

如果不想使用微博热搜，注释掉相关代码：

```java
private String getHotTopic() {
    // 直接使用备用话题
    Random random = new Random();
    return BACKUP_TOPICS.get(random.nextInt(BACKUP_TOPICS.size()));
}
```

---

## 🎯 测试建议

### 快速测试（推荐）

1. **手动发帖测试**
   - 使用Console执行手动发帖
   - 立即看到效果

2. **修改为每分钟执行**（临时测试）
   ```java
   @Scheduled(cron = "0 * * * * ?")  // 每分钟执行
   ```
   - 重启Tomcat
   - 等待1分钟
   - 查看是否自动发帖

3. **恢复正常频率**
   ```java
   @Scheduled(cron = "0 0 * * * ?")  // 每小时执行
   ```

---

## 📝 API接口汇总

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 手动AI发帖 | POST | /api/ai/post/now | 立即发布一条AI帖子 |
| 生成帖子内容 | POST | /api/ai/generate/post | 仅生成内容，不发布 |
| 生成评论内容 | POST | /api/ai/generate/comment | 仅生成评论，不发布 |
| 内容审核 | POST | /api/ai/audit | 审核内容是否违规 |

---

## 🎉 开始使用

1. **重启Tomcat**（加载新代码）
2. **打开浏览器Console**
3. **执行手动发帖脚本**
4. **刷新首页查看AI帖子**

**祝测试顺利！🚀**

---

**文档创建日期**: 2026-01-17  
**AI模型**: 通义千问 qwen-flash
