# 微博Cookie配置说明

## ⚠️ 重要：Cookie安全

微博Cookie包含敏感信息，**不应该硬编码在代码中**！

---

## 🔧 配置方法

### 方法1: 使用环境变量（推荐）

#### Windows系统
1. 右键"此电脑" -> 属性 -> 高级系统设置 -> 环境变量
2. 新建系统变量：
   - 变量名：`WEIBO_SUB_COOKIE`
   - 变量值：你的SUB Cookie值
3. 新建系统变量：
   - 变量名：`WEIBO_SUBP_COOKIE`
   - 变量值：你的SUBP Cookie值
4. 重启IDEA和Tomcat

#### Linux/Mac系统
在 `~/.bashrc` 或 `~/.zshrc` 中添加：
```bash
export WEIBO_SUB_COOKIE="your_sub_cookie_value"
export WEIBO_SUBP_COOKIE="your_subp_cookie_value"
```

### 方法2: 修改代码（仅用于本地测试）

在 `AIScheduler.java` 中：
```java
// 替换为你的实际Cookie值
if (SUB == null || SUB.isEmpty()) {
    SUB = "_2AkMfz4MRf8NxqwFRmvoWyGPnaYVyygHEieKpk3LKJRMxHRl-yT9xqlJbtRB6NE-t_sZGI14RVGs2WgKndW1IezGbJFlz";
}
if (SUBP == null || SUBP.isEmpty()) {
    SUBP = "0033WrSXqPxfM72-Ws9jqgMF55529P9D9WhE8eoZL2Sdflmn-OVsyxqZ";
}
```

**注意**：修改后不要提交到Git！

---

## 🔍 如何获取微博Cookie

1. 打开浏览器，访问 https://weibo.com
2. 登录你的微博账号
3. 按F12打开开发者工具
4. 切换到 `Application` 或 `存储` 标签
5. 左侧选择 `Cookies` -> `https://weibo.com`
6. 找到并复制：
   - `SUB` 的值
   - `SUBP` 的值

---

## 📝 当前状态

### Cookie已保护 ✅
- Cookie值已从代码中移除
- 改为从环境变量读取
- 提供占位符 `YOUR_WEIBO_XXX_COOKIE`

### 降级策略 ✅
- 如果Cookie未配置或获取失败
- 自动使用备用话题列表
- 不影响AI发帖功能

---

## 🧪 测试微博热搜功能

### 步骤1: 配置Cookie

选择上面的方法1或方法2配置Cookie

### 步骤2: 重启Tomcat

### 步骤3: 查看日志

在IDEA控制台查看：
```
INFO  AIScheduler - 正在尝试抓取微博热搜
INFO  AIScheduler - 成功获取微博热搜10条
INFO  AIScheduler - 选择话题：某某热搜话题
```

### 步骤4: 手动触发AI发帖

在浏览器Console执行：
```javascript
fetch('http://localhost:8080/api/ai/post/now', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  credentials: 'include',
  body: JSON.stringify({})
}).then(r => r.json()).then(d => console.log(d))
```

查看生成的帖子是否使用了微博热搜话题。

---

## ⚠️ 注意事项

### 1. Cookie有效期

- 微博Cookie会过期
- 需要定期更新
- 建议使用长期有效的Cookie

### 2. 反爬虫

- 微博有反爬虫机制
- 不要频繁请求
- 当前设置：每小时1次

### 3. 备用方案

如果微博热搜获取失败：
- 自动使用12个备用话题
- 不影响AI发帖功能
- 查看日志确认使用的话题

---

**Cookie已保护！代码已提交！🚀**

---

**文档创建日期**: 2026-01-17
