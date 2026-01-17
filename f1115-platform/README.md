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

修改配置文件 `f1115-main/src/main/resources/db.properties`：

```properties
# 数据库配置（根据实际情况修改）
jdbc.url=jdbc:mysql://localhost:3306/f1115_db?...
jdbc.username=root
jdbc.password=123456

# Redis配置（根据实际情况修改）
redis.host=localhost
redis.port=6379
redis.password=

# 通义千问API配置（需要申请API Key）
dashscope.apiKey=YOUR_DASHSCOPE_API_KEY
```

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

#### 阶段一：基础框架 ✅
- [x] Maven多模块项目结构
- [x] Spring + MyBatis + Redis配置
- [x] 数据库表结构设计（8个表）
- [x] 统一响应格式和异常处理
- [x] 日志系统配置

#### 阶段二：用户系统 ✅
- [x] 用户注册功能（BCrypt密码加密）
- [x] 用户登录功能（Session管理）
- [x] 用户信息管理
- [x] 个人主页展示
- [x] 前端登录/注册页面

### 待开发功能

#### 阶段三：内容系统
- [ ] 发帖功能（文本+图片）
- [ ] 图片上传功能
- [ ] 帖子列表（首页时间线）
- [ ] 帖子详情页
- [ ] 分页功能

#### 阶段四：社交功能
- [ ] 评论功能（一级、二级评论）
- [ ] 点赞功能（帖子、评论）
- [ ] 关注/取消关注功能
- [ ] 粉丝/关注列表

#### 阶段五：管理后台
- [ ] 用户管理（列表、状态管理）
- [ ] 帖子管理（列表、删除、置顶）
- [ ] 评论管理
- [ ] 内容审核

#### 阶段六：AI功能
- [ ] 通义千问API集成
- [ ] AI内容生成（帖子、评论）
- [ ] 定时任务（AI定时发帖）
- [ ] 内容自动审核

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
| 获取指定用户信息 | GET | /api/user/{userId} | - |

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

- [x] 阶段一：基础框架搭建（已完成）
- [x] 阶段二：用户系统开发（已完成）
- [ ] 阶段三：内容系统开发（待开发）
- [ ] 阶段四：社交功能开发（待开发）
- [ ] 阶段五：管理后台开发（待开发）
- [ ] 阶段六：AI功能集成（待开发）
- [ ] 阶段七：测试与优化（待开发）
- [ ] 阶段八：部署上线（待开发）

## 联系方式

如有问题，请参考开发文档：`F1115开发文档.md`

## 许可证

MIT License
