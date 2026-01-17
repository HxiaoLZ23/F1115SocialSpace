package com.form1115.crm.filters;

import com.form1115.crm.common.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 放行 已经登录 /login.html /getLogin.do  /getCaptcha.do  /resources/
        HttpSession session = request.getSession();
        Object userName = session.getAttribute(Constants.SESSION_USER_NAME);
        // 获取请求路径
        String uri = request.getRequestURI();
        if (userName != null || uri.endsWith("/login.html") || uri.endsWith("/getLogin.do")
        || uri.endsWith("/getCaptcha.do") || uri.contains("/resources/")) {
          // 放行
            filterChain.doFilter(request, response);
        } else {
            // 拦截并跳转到登录页面
            response.sendRedirect("/views/login.html");
        }
    }

    @Override
    public void destroy() {

    }
}
