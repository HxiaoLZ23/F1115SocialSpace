package com.form1115.crm.domain;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName account
 */
@Data
public class Account {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String username;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 头像地址
     */
    private String imgUrl;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 账户的角色
     */
    private Integer role;


}