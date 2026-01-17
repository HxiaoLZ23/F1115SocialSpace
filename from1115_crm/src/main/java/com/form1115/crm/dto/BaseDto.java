package com.form1115.crm.dto;

import lombok.Data;

@Data
public class BaseDto {

    /**
     * 页码
     */
    private Integer pageNum  = 1;

    /**
     * 每页显示的条数
     */
    private Integer pageSize = 10;
}
