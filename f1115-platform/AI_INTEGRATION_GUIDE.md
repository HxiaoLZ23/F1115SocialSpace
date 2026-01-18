# 通义千问AI集成指南

## 📋 前置准备

### 1. 申请API Key

1. 访问：https://dashscope.aliyun.com/
2. 注册/登录阿里云账号
3. 开通DashScope服务
4. 创建API Key
5. 复制API Key

### 2. 配置API Key

修改 `f1115-main/src/main/resources/db.properties`：

```properties
# 通义千问API配置
dashscope.apiKey=sk-xxxxxxxxxxxxxxxxxxxxxxxx
dashscope.model=qwen-plus
```

---

## 🔧 AI功能说明

### 1. AI内容生成

#### 生成帖子
- **接口**: POST `/api/ai/generate/post`
- **参数**: `{"topic": "科技发展"}`
- **返回**: 生成的帖子内容

#### 生成评论
- **接口**: POST `/api/ai/generate/comment`
- **参数**: `{"postContent": "今天天气真好"}`
- **返回**: 生成的评论内容

### 2. 内容审核

- **接口**: POST `/api/ai/audit`
- **参数**: `{"content": "要审核的内容"}`
- **返回**: 审核结果（通过/拒绝/待审核）

### 3. 定时任务

#### AI定时发帖
- **时间**: 每天10:00和15:00
- **Cron**: `0 0 10,15 * * ?`
- **功能**: 自动生成并发布帖子

#### AI定时评论
- **时间**: 每小时执行
- **Cron**: `0 0 * * * ?`
- **功能**: 为热门帖子生成评论

---

## 🧪 测试AI功能

### 测试1: 生成帖子

使用Postman或浏览器测试：

**请求**:
```
POST http://localhost:8080/api/ai/generate/post
Content-Type: application/json

{
  "topic": "人工智能"
}
```

**期望响应**:
```json
{
  "code": 200,
  "msg": "生成成功",
  "data": "人工智能正在改变我们的生活..."
}
```

### 测试2: 生成评论

**请求**:
```
POST http://localhost:8080/api/ai/generate/comment
Content-Type: application/json

{
  "postContent": "今天学习了Vue 3，感觉很棒！"
}
```

**期望响应**:
```json
{
  "code": 200,
  "msg": "生成成功",
  "data": "Vue 3确实很强大！继续加油！"
}
```

### 测试3: 内容审核

**请求**:
```
POST http://localhost:8080/api/ai/audit
Content-Type: application/json

{
  "content": "这是一条测试内容"
}
```

**期望响应**:
```json
{
  "code": 200,
  "msg": "审核完成",
  "data": {
    "status": 0,
    "message": "审核通过"
  }
}
```

---

## ⚠️ 注意事项

### 1. API Key安全

**重要**: 不要将API Key提交到Git！

- ✅ API Key已配置在 `db.properties` 中
- ✅ `db.properties` 已添加到 `.gitignore`
- ✅ 使用 `db.properties.example` 作为模板

### 2. API调用限制

- 通义千问有调用频率限制
- 建议添加错误重试机制
- 记录API调用日志

### 3. 内容质量

- AI生成的内容需要人工审核
- 可以设置内容过滤规则
- 避免生成重复内容

### 4. 定时任务

- 定时任务默认启用
- 如果不需要，可以注释掉 `@Scheduled` 注解
- 建议在测试环境先测试

---

## 🔄 启用AI功能

### 步骤1: 配置API Key

在 `db.properties` 中填入真实的API Key

### 步骤2: 创建AI机器人用户

在数据库中已有AI机器人用户（id=2, username=ai_robot）

### 步骤3: 重启后端

在IDEA中重启Tomcat，AI功能会自动加载

### 步骤4: 测试AI接口

使用Postman测试上面的三个接口

### 步骤5: 观察定时任务

查看后端日志，定时任务会在指定时间执行

---

## 📝 降级策略

如果AI API调用失败，系统会：
- 返回默认内容
- 记录错误日志
- 不影响主要功能

---

## 🎯 下一步

AI功能集成完成后：
1. 测试AI生成内容质量
2. 调整prompt优化生成效果
3. 配置定时任务时间
4. 完善内容审核规则

---

**AI功能已就绪！配置API Key后即可使用！🚀**

---

**文档创建日期**: 2026-01-17  
**AI模型**: 通义千问 qwen-plus
