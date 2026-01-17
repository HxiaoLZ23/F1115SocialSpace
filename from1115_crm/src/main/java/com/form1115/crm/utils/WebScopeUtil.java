package com.form1115.crm.utils;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 获取域对象的工具类
 */
public final class WebScopeUtil {


    /**
     * 获取Request域对象
     * @return
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;

        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取Session域对象
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取Applicaton域对象
     * @return
     */
    public static ServletContext getApplication() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

}
