package com.form1115.f1115.main.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.form1115.f1115.common.domain.UserProfile;
import com.form1115.f1115.common.utils.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录认证拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {
    
    private static final String SESSION_USER_KEY = "currentUser";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 获取Session中的用户信息
        HttpSession session = request.getSession();
        UserProfile user = (UserProfile) session.getAttribute(SESSION_USER_KEY);
        
        if (user == null) {
            // 未登录，返回401
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            
            Result result = Result.unauthorized();
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(result));
            
            return false;
        }
        
        return true;
    }
}
