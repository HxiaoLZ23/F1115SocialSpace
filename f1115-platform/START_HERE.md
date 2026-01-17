# 🚀 F1115 项目 - 从这里开始！

## 👋 欢迎

恭喜!你已经成功获得了F1115社交媒体平台的完整项目代码。这是一个基于**SSM框架 + Vue 3**开发的现代化社交平台。

---

## 📋 项目概览

- **项目名称**: F1115 社交媒体平台
- **技术栈**: Spring + Spring MVC + MyBatis + Vue 3 + Redis
- **当前状态**: ✅ 基础框架 + 用户系统已完成
- **完成度**: 30%
- **开发日期**: 2026-01-17

---

## 🎯 快速导航

### 📖 必读文档（按顺序阅读）

1. **START_HERE.md** ⭐ - 你正在阅读的文件，从这里开始！
2. **QUICK_START.md** ⭐ - 快速启动指南，手把手教你启动项目
3. **README.md** - 项目详细说明
4. **PROJECT_SUMMARY.md** - 项目总结和进度
5. **PROJECT_STRUCTURE.md** - 项目结构说明
6. **DELIVERY_CHECKLIST.md** - 项目交付清单
7. **F1115开发文档.md** - 详细技术文档（在上级目录）

### 🔗 快速链接

- [快速启动指南](QUICK_START.md) - 5分钟启动项目
- [项目说明](README.md) - 了解项目详情
- [API文档](README.md#api接口文档) - 查看接口说明
- [数据库脚本](sql/f1115_db.sql) - 数据库初始化

---

## ⚡ 三步启动项目

### 第一步：准备环境 ✅

确保你已安装以下软件：
- ✅ JDK 1.8+
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Redis 6.0+
- ✅ Node.js 16+
- ✅ IDEA（推荐）
- ✅ VSCode（可选）

### 第二步：初始化数据库 ✅

1. 打开IDEA
2. 打开项目：`File -> Open -> f1115-platform`
3. 打开Database工具：`View -> Tool Windows -> Database`
4. 连接MySQL（localhost:3306, root/123456）
5. 执行SQL脚本：`sql/f1115_db.sql`

### 第三步：启动项目 ✅

#### 启动Redis
```bash
redis-server
```

#### 启动后端（IDEA）
1. 配置Tomcat服务器
2. 部署f1115-main.war
3. 启动Tomcat
4. 访问：http://localhost:8080

#### 启动前端（VSCode）
```bash
cd f1115-frontend
npm install
npm run dev
# 访问：http://localhost:3000
```

**详细步骤请查看**: [QUICK_START.md](QUICK_START.md)

---

## 🎮 测试功能

### 1. 访问前端
打开浏览器访问：http://localhost:3000

### 2. 测试注册
- 点击"还没有账号？立即注册"
- 填写信息：用户名、密码、邮箱、昵称
- 点击"注册"按钮

### 3. 测试登录
使用测试账号登录：
- 用户名：`testuser1`
- 密码：`123456`

或者使用管理员账号：
- 用户名：`admin`
- 密码：`admin123`

### 4. 查看个人中心
登录后点击"个人中心"按钮，查看用户信息

---

## 📊 当前功能

### ✅ 已完成功能

#### 用户系统
- ✅ 用户注册（BCrypt密码加密）
- ✅ 用户登录（Session管理）
- ✅ 用户登出
- ✅ 个人信息查看
- ✅ 个人信息修改
- ✅ 密码修改

#### 前端页面
- ✅ 登录页面
- ✅ 注册页面
- ✅ 首页
- ✅ 个人中心

### ⏳ 待开发功能

#### 内容系统（下一步）
- ⏳ 发帖功能
- ⏳ 帖子列表
- ⏳ 帖子详情
- ⏳ 图片上传

#### 社交功能
- ⏳ 评论功能
- ⏳ 点赞功能
- ⏳ 关注功能

#### 管理后台
- ⏳ 用户管理
- ⏳ 内容管理
- ⏳ 审核管理

#### AI功能
- ⏳ 通义千问集成
- ⏳ AI内容生成
- ⏳ 内容审核

---

## 🗂️ 项目结构

```
f1115-platform/
├── 📁 f1115-common/          # 公共模块（实体类、工具类）
├── 📁 f1115-main/            # 主平台（用户端功能）✅
├── 📁 f1115-admin/           # 管理平台（管理端功能）⏳
├── 📁 f1115-ai/              # AI服务（内容生成和审核）⏳
├── 📁 f1115-frontend/        # 前端项目（Vue 3）✅
├── 📁 sql/                   # 数据库脚本
└── 📄 各种文档.md            # 项目文档
```

---

## 🔑 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| testuser1 | 123456 | 普通用户 |
| testuser2 | 123456 | 普通用户 |

---

## 💡 开发建议

### 在IDEA中操作
- ✅ Maven项目管理
- ✅ 数据库操作
- ✅ Tomcat配置和启动
- ✅ 代码调试

### 在VSCode中操作
- ✅ Java代码编写（AI辅助）
- ✅ 前端Vue 3开发
- ✅ 终端命令执行

---

## 📝 配置说明

### 数据库配置
修改文件：`f1115-main/src/main/resources/db.properties`

```properties
jdbc.username=root
jdbc.password=123456
```

### Redis配置
```properties
redis.host=localhost
redis.port=6379
```

### 通义千问配置（AI功能时需要）
```properties
dashscope.apiKey=YOUR_DASHSCOPE_API_KEY
```

---

## ❓ 遇到问题？

### 常见问题

1. **数据库连接失败**
   - 检查MySQL是否启动
   - 检查用户名密码是否正确

2. **Redis连接失败**
   - 检查Redis是否启动：`redis-cli ping`

3. **端口占用**
   - 后端：修改Tomcat端口（默认8080）
   - 前端：修改Vite端口（默认3000）

4. **Maven依赖下载失败**
   - 配置阿里云镜像
   - 检查网络连接

### 查看日志
- **后端日志**：IDEA控制台
- **前端日志**：浏览器开发者工具（F12）

---

## 🎯 下一步开发

### 推荐开发顺序

1. **内容系统**（优先级：高）
   - 发帖功能
   - 帖子列表
   - 帖子详情

2. **社交功能**（优先级：高）
   - 评论功能
   - 点赞功能
   - 关注功能

3. **管理后台**（优先级：中）
   - 用户管理
   - 内容管理

4. **AI功能**（优先级：中）
   - 通义千问集成
   - AI内容生成

---

## 📚 学习资源

### 技术文档
- [Spring官方文档](https://spring.io/projects/spring-framework)
- [MyBatis官方文档](https://mybatis.org/mybatis-3/)
- [Vue 3官方文档](https://cn.vuejs.org/)
- [Element Plus官方文档](https://element-plus.org/)
- [通义千问API文档](https://help.aliyun.com/zh/dashscope/)

### 项目文档
- 📖 [README.md](README.md) - 项目说明
- 🚀 [QUICK_START.md](QUICK_START.md) - 快速启动
- 📊 [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - 项目总结
- 📁 [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - 项目结构
- ✅ [DELIVERY_CHECKLIST.md](DELIVERY_CHECKLIST.md) - 交付清单

---

## 🎉 开始你的开发之旅！

现在你已经了解了项目的基本情况，接下来：

1. **阅读** [QUICK_START.md](QUICK_START.md) 快速启动项目
2. **测试** 用户注册登录功能
3. **查看** 项目代码结构
4. **开始** 开发新功能！

---

## 📞 获取帮助

如果遇到问题：
1. 查看 [QUICK_START.md](QUICK_START.md) 的常见问题部分
2. 查看 [F1115开发文档.md](../F1115开发文档.md) 详细技术文档
3. 检查IDEA和浏览器的控制台日志

---

**祝你开发顺利！加油！🚀**

---

**最后更新**: 2026-01-17  
**项目版本**: v1.0.0  
**完成度**: 30%
