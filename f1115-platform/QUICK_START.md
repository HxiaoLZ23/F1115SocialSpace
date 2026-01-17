# F1115 快速启动指南

## 📋 前置准备

### 必需软件
- ✅ JDK 1.8+ 
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ Redis 6.0+
- ✅ Node.js 16+
- ✅ IDEA (推荐)
- ✅ VSCode (可选)

---

## 🚀 启动步骤

### 第一步：数据库初始化（在IDEA中操作）

1. **打开IDEA**
2. **打开项目**：`File -> Open -> 选择 f1115-platform 文件夹`
3. **等待Maven依赖下载**（第一次会比较慢）
4. **连接数据库**：
   - 打开 `View -> Tool Windows -> Database`
   - 点击 `+` -> `Data Source` -> `MySQL`
   - 配置连接：
     - Host: `localhost`
     - Port: `3306`
     - User: `root`
     - Password: `123456`
   - 点击 `Test Connection`，确保连接成功
5. **执行SQL脚本**：
   - 在Database工具中，右键点击数据库连接
   - 选择 `Run SQL Script`
   - 选择文件：`sql/f1115_db.sql`
   - 点击 `Execute`
6. **验证数据库**：
   - 刷新数据库连接，应该能看到 `f1115_db` 数据库
   - 展开数据库，应该能看到8个表

---

### 第二步：配置修改

修改配置文件：`f1115-main/src/main/resources/db.properties`

```properties
# 数据库配置（根据你的实际情况修改）
jdbc.username=root
jdbc.password=123456

# Redis配置（根据你的实际情况修改）
redis.host=localhost
redis.port=6379
redis.password=

# 通义千问API配置（暂时不用修改，后续开发AI功能时再配置）
dashscope.apiKey=YOUR_DASHSCOPE_API_KEY
```

---

### 第三步：启动Redis

**Windows**:
```bash
# 下载Redis for Windows后，进入Redis目录
redis-server.exe
```

**macOS**:
```bash
brew services start redis
# 或
redis-server
```

**Linux**:
```bash
sudo service redis-server start
# 或
redis-server
```

验证Redis是否启动：
```bash
redis-cli ping
# 应该返回：PONG
```

---

### 第四步：启动后端（在IDEA中操作）

1. **配置Tomcat**：
   - 点击 `Run -> Edit Configurations`
   - 点击 `+` -> `Tomcat Server` -> `Local`
   - 配置Tomcat：
     - Name: `f1115-main`
     - Tomcat Home: 选择你的Tomcat安装目录
   - 切换到 `Deployment` 标签：
     - 点击 `+` -> `Artifact`
     - 选择 `f1115-main:war exploded`
     - Application context: `/` (根路径)
   - 点击 `Apply` -> `OK`

2. **启动Tomcat**：
   - 点击右上角的绿色运行按钮
   - 等待启动完成（看到日志输出）
   - 浏览器会自动打开 `http://localhost:8080`

3. **验证后端启动**：
   - 访问：`http://localhost:8080/api/user/profile`
   - 应该返回：`{"code":401,"msg":"未登录或登录已过期"}`
   - 说明后端启动成功！

---

### 第五步：启动前端（在VSCode中操作）

1. **打开VSCode**
2. **打开前端项目**：
   - `File -> Open Folder`
   - 选择 `f1115-platform/f1115-frontend` 文件夹

3. **安装依赖**：
   ```bash
   # 打开终端（Ctrl + `）
   npm install
   ```

4. **启动开发服务器**：
   ```bash
   npm run dev
   ```

5. **访问前端**：
   - 浏览器访问：`http://localhost:3000`
   - 应该能看到登录页面

---

## 🎉 测试功能

### 1. 测试注册
1. 访问 `http://localhost:3000`
2. 点击"还没有账号？立即注册"
3. 填写注册信息：
   - 用户名：`testuser`
   - 密码：`123456`
   - 邮箱：`test@test.com`（可选）
   - 昵称：`测试用户`（可选）
4. 点击"注册"按钮
5. 注册成功后会自动跳转到登录页

### 2. 测试登录
1. 使用刚才注册的账号登录
   - 用户名：`testuser`
   - 密码：`123456`
2. 点击"登录"按钮
3. 登录成功后会跳转到首页

### 3. 测试个人中心
1. 登录后，点击"个人中心"按钮
2. 应该能看到用户信息

### 4. 使用测试账号
数据库中已经预置了测试账号：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| testuser1 | 123456 | 普通用户 |
| testuser2 | 123456 | 普通用户 |

---

## ❓ 常见问题

### 1. Maven依赖下载失败
**解决方案**：
- 检查网络连接
- 配置Maven镜像（阿里云镜像）
- 在IDEA中：`File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven`
- 修改 `settings.xml` 添加阿里云镜像

### 2. 数据库连接失败
**解决方案**：
- 检查MySQL是否启动：`mysql -u root -p`
- 检查用户名密码是否正确
- 检查端口是否被占用

### 3. Redis连接失败
**解决方案**：
- 检查Redis是否启动：`redis-cli ping`
- 检查配置文件中的Redis地址和端口
- Windows用户需要下载Redis for Windows

### 4. Tomcat启动失败
**解决方案**：
- 检查端口8080是否被占用
- 查看IDEA控制台的错误日志
- 检查配置文件是否正确

### 5. 前端启动失败
**解决方案**：
- 删除 `node_modules` 文件夹，重新 `npm install`
- 检查Node.js版本是否符合要求
- 检查端口3000是否被占用

### 6. 跨域问题
**解决方案**：
- 已在 `springmvc.xml` 中配置CORS
- 确保前端请求的地址是 `/api/...`
- 检查Vite配置的代理是否正确

---

## 📞 获取帮助

如果遇到问题：
1. 查看 `README.md` 详细文档
2. 查看 `F1115开发文档.md` 技术文档
3. 检查IDEA和VSCode的控制台日志
4. 检查浏览器的开发者工具（F12）

---

## 🎯 下一步开发

当前已完成：
- ✅ 项目框架搭建
- ✅ 用户注册登录功能
- ✅ 前端基础页面

待开发功能：
- ⏳ 发帖功能
- ⏳ 评论功能
- ⏳ 点赞功能
- ⏳ 关注功能
- ⏳ AI功能集成

继续开发请参考 `F1115开发文档.md` 的开发阶段规划！

---

**祝你开发顺利！🚀**
