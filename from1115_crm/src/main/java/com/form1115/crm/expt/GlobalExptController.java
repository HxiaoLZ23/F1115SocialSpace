package com.form1115.crm.expt;

import com.form1115.crm.common.Result;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器类
 */
@ControllerAdvice
public class GlobalExptController {

    private Logger log = Logger.getLogger(GlobalExptController.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handExpt(Exception expt) {
        // 开发阶段将错误结果打印到控制台
        expt.printStackTrace();
        // 项目上线后，将日志打印到日志文件中
        //log.debug(expt);
        return new Result(-1,"请求后台服务器失败");
    }

}
