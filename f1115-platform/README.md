# F1115 社交媒体平台

## 项目简介

F1115是一个基于SSM框架的社交媒体平台，支持用户发帖、评论、点赞、关注等核心功能，并集成通义千问AI实现内容生成和审核。

## 技术栈

### 后端
- **Java**: 1.8+
- **Spring Framework**: 5.3.4
- **Spring MVC**: 5.3.4
- **MyBatis**: 3.5.7
- **MySQL**: 8.0+
- **Redis**: 缓存、计数器、Session存储
- **Druid**: 数据库连接池
- **通义千问**: AI内容生成和审核

### 前端
- **Vue 3**: 前端框架
- **Element Plus**: UI组件库
- **Vite**: 构建工具
- **Pinia**: 状态管理
- **Axios**: HTTP客户端

## 项目结构

```
f1115-platform/
├── f1115-common/          # 公共模块（实体类、工具类、配置类）
├── f1115-main/            # 主平台模块（用户端功能）
├── f1115-admin/           # 管理平台模块（管理端功能）
├── f1115-ai/              # AI服务模块（内容生成和审核）
├── f1115-frontend/        # 前端项目（Vue 3）
├── sql/                   # 数据库脚本
└── README.md
```

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+

### 1. 数据库初始化

在IDEA中执行以下操作：

1. 打开IDEA的Database工具（View -> Tool Windows -> Database）
2. 连接到MySQL数据库（localhost:3306，用户名：root，密码：123456）
3. 执行SQL脚本：`sql/f1115_db.sql`

### 2. 配置修改

**重要**：首次使用需要创建配置文件

1. 复制配置模板：
   ```bash
   cd f1115-main/src/main/resources/
   cp db.properties.example db.properties
   ```

2. 修改 `db.properties`，填入你的真实配置：
   ```properties
   # 数据库配置（根据实际情况修改）
   jdbc.username=root
   jdbc.password=YOUR_MYSQL_PASSWORD
   
   # Redis配置（根据实际情况修改）
   redis.host=localhost
   redis.port=6379
   redis.password=YOUR_REDIS_PASSWORD
   
   # 通义千问API配置（需要申请API Key）
   dashscope.apiKey=YOUR_DASHSCOPE_API_KEY
   ```

**注意**：`db.properties` 已添加到 `.gitignore`，不会被提交到Git。详见 [CONFIG_GUIDE.md](CONFIG_GUIDE.md)

### 2.5 微博Cookie配置（可选，用于AI热搜发帖）

**重要**：微博Cookie用于AI定时任务获取热搜话题。如果不配置，AI会使用备用话题列表。

#### 方法1: 使用环境变量（推荐）

**Windows系统**：
1. 右键"此电脑" -> 属性 -> 高级系统设置 -> 环境变量
2. 新建系统变量：
   - 变量名：`WEIBO_SUB_COOKIE`
   - 变量值：你的SUB Cookie值
3. 新建系统变量：
   - 变量名：`WEIBO_SUBP_COOKIE`
   - 变量值：你的SUBP Cookie值
4. 重启IDEA和Tomcat

**Linux/Mac系统**：
在 `~/.bashrc` 或 `~/.zshrc` 中添加：
```bash
export WEIBO_SUB_COOKIE="your_sub_cookie_value"
export WEIBO_SUBP_COOKIE="your_subp_cookie_value"
```

#### 方法2: 修改代码（仅用于本地测试）

在 `AIScheduler.java` 的 `fetchWeiboHotSearch()` 方法中，替换占位符为实际Cookie值。

**注意**：修改后不要提交到Git！

#### 如何获取微博Cookie

1. 打开浏览器，访问 https://weibo.com
2. 登录你的微博账号
3. 按F12打开开发者工具
4. 切换到 `Application` 或 `存储` 标签
5. 左侧选择 `Cookies` -> `https://weibo.com`
6. 找到并复制：
   - `SUB` 的值
   - `SUBP` 的值

**详细说明**：详见 [WEIBO_COOKIE_CONFIG.md](WEIBO_COOKIE_CONFIG.md)

### 3. 后端启动（在IDEA中操作）

1. 在IDEA中打开项目：`File -> Open -> 选择f1115-platform文件夹`
2. 等待Maven依赖下载完成
3. 配置Tomcat服务器：
   - Run -> Edit Configurations -> + -> Tomcat Server -> Local
   - 配置Tomcat路径
   - Deployment -> + -> Artifact -> f1115-main:war exploded
   - Application context: /
4. 启动Tomcat（点击Run按钮）
5. 访问：http://localhost:8080

### 4. 前端启动（在VSCode中操作）

```bash
# 进入前端目录
cd f1115-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 访问：http://localhost:3000
```

## 功能模块

### 已完成功能

#### 阶段一：基础框架 ✅ (100%)
- [x] Maven多模块项目结构
- [x] Spring + MyBatis + Redis配置
- [x] 数据库表结构设计（8个表）
- [x] 统一响应格式和异常处理
- [x] 日志系统配置
- [x] 配置文件安全保护

#### 阶段二：用户系统 ✅ (100%)
- [x] 用户注册功能（BCrypt密码加密）
- [x] 用户登录功能（Session管理）
- [x] 用户信息管理
- [x] 个人主页展示
- [x] 前端登录/注册页面
- [x] 用户状态持久化（localStorage）

#### 阶段三：内容系统 ✅ (100%)
- [x] 发帖功能（文本+图片）
- [x] 图片上传功能（单张/批量）
- [x] 帖子列表（首页时间线）
- [x] 帖子详情页
- [x] 分页功能（PageHelper）
- [x] 帖子删除功能

#### 阶段3.5：个人主页完善 ✅ (100%)
- [x] 用户统计数据展示（发帖数、关注数、粉丝数）
- [x] 用户帖子列表（分页）
- [x] 编辑个人资料（昵称、邮箱、简介）
- [x] 头像上传功能
- [x] 查看他人主页
- [x] 点击头像/昵称跳转到用户主页
- [x] 标签页切换（帖子/关注/粉丝）

#### 阶段四：社交功能 ✅ (100%)
- [x] 评论功能（一级、二级评论）
- [x] 点赞功能（帖子、评论）
- [x] 关注/取消关注功能
- [x] 粉丝/关注列表
- [x] 评论删除
- [x] 实时状态更新

#### 阶段五：管理后台 ✅ (100%)
- [x] 用户管理（列表/搜索/启用禁用/删除）
- [x] 帖子管理（列表/搜索/置顶/删除）
- [x] 评论管理（列表/删除）
- [x] 数据统计（用户数/帖子数/活跃度/热门帖子）
- [x] 权限控制（管理员验证）
- [x] 管理后台前端（布局/数据概览/管理页面）

#### 阶段六：AI功能 ✅ (100%)
- [x] 通义千问API集成
- [x] AI内容生成（帖子、评论）
- [x] 定时任务（AI定时发帖，24条/天）
- [x] 内容自动审核（发帖时自动审核）
- [x] 微博热搜集成（自动获取前10条）
- [x] 手动触发AI发帖

#### AI增强功能 ✅ (100%)
- [x] AI自动评论用户帖子（发帖后3秒）
- [x] AI智能回复用户（保持对话连贯性）
- [x] 对话上下文传递（帖子+评论+回复）
- [x] 视觉模型集成准备（生产环境可用）

### 待开发功能

#### 阶段七：测试与优化 ⏳ (0%)
- [ ] 功能测试
- [ ] 性能测试
- [ ] 安全测试
- [ ] Bug修复

#### 阶段八：部署上线 ⏳ (0%)
- [ ] 服务器配置
- [ ] 应用部署
- [ ] 域名配置
- [ ] 监控配置

**当前完成度：90%**

### AI功能增强（2026-01-17）

#### AI智能交互
- AI自动评论用户帖子（发帖后3秒）
- AI智能回复用户（保持对话连贯性）
- 支持纯文本和带图片帖子
- 传入完整对话上下文

#### 视觉模型说明
**重要**：视觉模型（qwen-vl-plus）需要图片URL是公网可访问的地址。

**开发环境**（localhost）：
- 当前使用文本模型降级方案
- 在提示词中说明帖子包含图片
- AI仍能生成相关评论

**生产环境**（公网部署）：
- 图片部署到云存储（阿里云OSS/腾讯云COS）
- 图片URL改为公网地址（如：https://your-domain.com/upload/xxx.jpg）
- 启用视觉模型，AI可以真正理解图片内容

**如何启用视觉模型**：
1. 将图片上传到云存储
2. 修改 `FileUploadService.java` 返回云存储URL
3. 在 `AICommentService.java` 中恢复视觉模型代码
4. 重新部署

## API接口文档

### 用户相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | /api/user/register | 用户名、密码、邮箱、昵称 |
| 用户登录 | POST | /api/user/login | 用户名、密码 |
| 用户登出 | POST | /api/user/logout | - |
| 获取当前用户信息 | GET | /api/user/profile | 需要登录 |
| 更新用户信息 | PUT | /api/user/profile | 需要登录 |
| 修改密码 | PUT | /api/user/password | 需要登录 |
| 获取指定用户信息 | GET | /api/user/{userId} | 含统计数据 |
| 上传头像 | POST | /api/user/avatar | 需要登录 |

### 帖子相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 发布帖子 | POST | /api/post | 需要登录，带AI审核 |
| 获取帖子详情 | GET | /api/post/{postId} | 公开访问 |
| 获取帖子列表 | GET | /api/post/list | 公开访问，分页 |
| 获取首页时间线 | GET | /api/post/timeline | 需要登录，分页 |
| 获取用户帖子 | GET | /api/post/user/{userId} | 公开访问，分页 |
| 删除帖子 | DELETE | /api/post/{postId} | 需要登录 |
| 点赞帖子 | POST | /api/post/{postId}/like | 需要登录 |

### 评论相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 发布评论 | POST | /api/post/{postId}/comment | 需要登录 |
| 获取评论列表 | GET | /api/post/{postId}/comments | 公开访问 |
| 删除评论 | DELETE | /api/comment/{commentId} | 需要登录 |
| 点赞评论 | POST | /api/comment/{commentId}/like | 需要登录 |

### 关注相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 关注用户 | POST | /api/user/{userId}/follow | 需要登录 |
| 获取关注列表 | GET | /api/user/{userId}/following | 公开访问，分页 |
| 获取粉丝列表 | GET | /api/user/{userId}/followers | 公开访问，分页 |

### 上传相关接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 上传图片 | POST | /api/upload/image | 需要登录，单张 |
| 批量上传图片 | POST | /api/upload/images | 需要登录，最多9张 |

### AI功能接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 手动AI发帖 | POST | /api/ai/post/now | 需要登录，立即发布 |
| 生成帖子内容 | POST | /api/ai/generate/post | 测试接口 |
| 生成评论内容 | POST | /api/ai/generate/comment | 测试接口 |
| 内容审核 | POST | /api/ai/audit | 测试接口 |

### 管理后台接口

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户列表 | GET | /api/admin/user/list | 需要管理员权限 |
| 更新用户状态 | PUT | /api/admin/user/{userId}/status | 需要管理员权限 |
| 删除用户 | DELETE | /api/admin/user/{userId} | 需要管理员权限 |
| 修改用户角色 | PUT | /api/admin/user/{userId}/role | 需要管理员权限 |
| 用户统计 | GET | /api/admin/user/stats | 需要管理员权限 |

**总计：33个API接口**

### 响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {},
  "total": 0
}
```

**响应码说明**：
- `200`: 成功
- `-1`: 业务失败
- `401`: 未登录
- `403`: 无权限
- `500`: 服务器错误

## 通义千问API配置

### 1. 申请API Key

访问：https://dashscope.aliyun.com/

1. 注册/登录阿里云账号
2. 开通DashScope服务
3. 创建API Key
4. 复制API Key

### 2. 配置API Key

修改 `f1115-main/src/main/resources/db.properties`：

```properties
dashscope.apiKey=sk-xxxxxxxxxxxxxxxxxxxxxxxx
dashscope.model=qwen-plus
```

## 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| testuser1 | 123456 | 普通用户 |
| testuser2 | 123456 | 普通用户 |

## 注意事项

### IDEA中需要操作的内容

1. **Maven项目导入**：在IDEA中打开项目
2. **数据库连接**：使用IDEA的Database工具连接MySQL
3. **执行SQL脚本**：创建数据库表结构
4. **Tomcat配置**：配置Tomcat服务器
5. **项目启动**：启动Tomcat运行项目
6. **调试**：使用IDEA的调试功能

### VSCode中可以操作的内容

1. **代码编写**：Java代码编写（AI辅助）
2. **前端开发**：Vue 3前端项目开发
3. **Git操作**：版本控制
4. **终端操作**：Maven命令、npm命令

### Redis配置

如果没有安装Redis，可以：

1. **Windows**：下载Redis for Windows
2. **macOS**：`brew install redis`
3. **Linux**：`sudo apt-get install redis-server`

启动Redis：
```bash
redis-server
```

### 常见问题

1. **数据库连接失败**：检查MySQL是否启动，用户名密码是否正确
2. **端口占用**：修改Tomcat端口或前端端口
3. **跨域问题**：已在springmvc.xml中配置CORS
4. **Session丢失**：检查Cookie设置

## 开发进度

- [x] 阶段一：基础框架搭建（已完成 100%）
- [x] 阶段二：用户系统开发（已完成 100%）
- [x] 阶段三：内容系统开发（已完成 100%）
- [x] 阶段3.5：个人主页完善（已完成 100%）
- [x] 阶段四：社交功能开发（已完成 100%）
- [x] 阶段五：管理后台开发（已完成 100%）
- [x] 阶段六：AI功能集成（已完成 100%）
- [x] AI增强：智能评论和回复（已完成 100%）
- [ ] 阶段七：测试与优化（待开发 0%）
- [ ] 阶段八：部署上线（待开发 0%）

**当前总体完成度：90%**

### 最新更新（2026-01-17）

#### 阶段三：内容系统 ✅
- 发帖功能（文本+图片，最多9张）
- 图片上传（文件验证、大小限制）
- 帖子列表（分页、图片展示）
- 帖子详情（浏览量统计）
- 首页时间线（基于关注关系）

#### 阶段3.5：个人主页完善 ✅
- 用户统计数据（发帖数、关注数、粉丝数）
- 用户帖子列表（分页显示）
- 编辑个人资料（昵称、邮箱、简介）
- 头像上传和更换
- 查看他人主页
- 点击头像/昵称跳转功能
- 用户状态持久化（刷新保持登录）

#### 阶段四：社交功能 ✅
- 评论系统（一级评论+二级回复）
- 点赞系统（帖子点赞+评论点赞）
- 关注系统（关注/取消关注）
- 关注列表和粉丝列表
- 实时状态更新

#### 阶段六：AI功能 ✅
- 通义千问API集成（qwen-flash模型）
- AI内容生成（帖子和评论）
- 自动定时发帖（每小时1次，24条/天）
- 微博热搜集成（自动获取前10条热搜）
- 内容自动审核（发帖时AI审核）
- 手动触发AI发帖接口

## 联系方式

如有问题，请参考开发文档：`F1115开发文档.md`

## 许可证

MIT License
