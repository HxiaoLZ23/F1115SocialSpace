<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>F1115 社交平台</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        
        .container {
            text-align: center;
            background: white;
            padding: 60px 80px;
            border-radius: 10px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
        }
        
        h1 {
            font-size: 48px;
            color: #333;
            margin-bottom: 20px;
        }
        
        p {
            font-size: 18px;
            color: #666;
            margin-bottom: 30px;
        }
        
        .status {
            background: #f0f9ff;
            border: 1px solid #bae6fd;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
        }
        
        .status-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #e0f2fe;
        }
        
        .status-item:last-child {
            border-bottom: none;
        }
        
        .status-label {
            font-weight: bold;
            color: #0369a1;
        }
        
        .status-value {
            color: #22c55e;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: #667eea;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            transition: background 0.3s;
            margin: 0 10px;
        }
        
        .btn:hover {
            background: #5568d3;
        }
        
        .btn-secondary {
            background: #10b981;
        }
        
        .btn-secondary:hover {
            background: #059669;
        }
        
        .footer {
            margin-top: 30px;
            font-size: 14px;
            color: #999;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎉 F1115 社交平台</h1>
        <p>后端服务已成功启动！</p>
        
        <div class="status">
            <div class="status-item">
                <span class="status-label">服务状态</span>
                <span class="status-value">✅ 运行中</span>
            </div>
            <div class="status-item">
                <span class="status-label">API地址</span>
                <span class="status-value">http://localhost:8080/api</span>
            </div>
            <div class="status-item">
                <span class="status-label">前端地址</span>
                <span class="status-value">http://localhost:3000</span>
            </div>
        </div>
        
        <div style="margin-top: 30px;">
            <a href="http://localhost:3000" class="btn">访问前端页面</a>
            <a href="/api/user/profile" class="btn btn-secondary">测试API接口</a>
        </div>
        
        <div class="footer">
            <p>技术栈：Spring MVC + MyBatis + Redis + Vue 3 + Element Plus</p>
            <p>查看 README.md 了解更多信息</p>
        </div>
    </div>
</body>
</html>
