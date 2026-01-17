package com.form1115.crm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {


    /* 响应码 200 表示成功  -1 表示失败 */
    private Integer code = 200;

    /* 响应消息，只有响应码为-1时才给该属性赋值 */
    private String msg;

    /* 返回的数据，只有查询时才给该属性赋值 */
    private Object data;

    private Long total = 0L;

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static final Result DATE_FORMAT_ERROR = new Result(-1,"参数有误");
}
