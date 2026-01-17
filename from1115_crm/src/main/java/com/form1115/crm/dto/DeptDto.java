package com.form1115.crm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptDto {

    /**
     * 主键
     */
    private Long id;

    /**
     * 部门名称
     */
    @NotNull(message = "部门名称不能为空")
    @Pattern(regexp = ".{2,10}",message = "部门的名称长度必须是2-10之间")
    private String name;

    /**
     * 部门地址
     */
    @NotNull(message = "部门地址不能为空")
    @Pattern(regexp = ".{4,50}",message = "部门地址的长度必须是4-50之间")
    private String loc;

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

}
