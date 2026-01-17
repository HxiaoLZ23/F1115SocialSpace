package com.form1115.f1115.common.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 统一响应结果类
 */
@Data
@Accessors(chain = true)
public class Result implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String msg;
    
    /**
     * 响应数据
     */
    private Object data;
    
    /**
     * 总记录数（分页时使用）
     */
    private Long total;
    
    // 响应码常量
    public static final int SUCCESS = 200;
    public static final int ERROR = -1;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int SERVER_ERROR = 500;
    
    /**
     * 成功响应（无数据）
     */
    public static Result success() {
        return new Result()
                .setCode(SUCCESS)
                .setMsg("操作成功");
    }
    
    /**
     * 成功响应（带数据）
     */
    public static Result success(Object data) {
        return new Result()
                .setCode(SUCCESS)
                .setMsg("操作成功")
                .setData(data);
    }
    
    /**
     * 成功响应（带消息和数据）
     */
    public static Result success(String msg, Object data) {
        return new Result()
                .setCode(SUCCESS)
                .setMsg(msg)
                .setData(data);
    }
    
    /**
     * 成功响应（分页数据）
     */
    public static Result success(Object data, Long total) {
        return new Result()
                .setCode(SUCCESS)
                .setMsg("操作成功")
                .setData(data)
                .setTotal(total);
    }
    
    /**
     * 失败响应
     */
    public static Result error() {
        return new Result()
                .setCode(ERROR)
                .setMsg("操作失败");
    }
    
    /**
     * 失败响应（带消息）
     */
    public static Result error(String msg) {
        return new Result()
                .setCode(ERROR)
                .setMsg(msg);
    }
    
    /**
     * 失败响应（带响应码和消息）
     */
    public static Result error(Integer code, String msg) {
        return new Result()
                .setCode(code)
                .setMsg(msg);
    }
    
    /**
     * 未登录响应
     */
    public static Result unauthorized() {
        return new Result()
                .setCode(UNAUTHORIZED)
                .setMsg("未登录或登录已过期");
    }
    
    /**
     * 无权限响应
     */
    public static Result forbidden() {
        return new Result()
                .setCode(FORBIDDEN)
                .setMsg("无权限访问");
    }
}
