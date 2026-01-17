package com.form1115.crm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {


    /**
     * 客户ID
     */
    private Long id;

    /**
     * 客户姓名
     */
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "[\\u4e00-\\u9fa5]{2,10}",message = "请输入中文名称")
    private String username;

    /**
     * 生日
     */
    @NotNull(message = "请输入生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 1男 0女
     */
    @NotNull(message = "请选择性别")
    @Pattern(regexp = "[10]",message = "性别格式有误")
    private String gender;

    /**
     * 手机
     */
    @NotNull(message = "请输入手机号")
    @Pattern(regexp = "[1][35678]\\d{9}",message = "手机号格式有误")
    private String tel;

    /**
     * 工资
     */
    @Min(value = 0L,message = "薪资格式有误")
    private Double sal;

    /**
     * 客户职业
     */
    @NotNull(message = "请选择职业")
    @Pattern(regexp = "[123]",message = "职业的格式有误")
    private String profession;

    /**
     * 客户住址
     */
    @NotNull(message = "请输入客户的住址")
    @Pattern(regexp = ".{5,200}",message = "地址格式有误")
    private String address;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门编号
     */
    @NotNull(message = "请输入部门ID")
    private Long deptId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
