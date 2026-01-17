package com.form1115.crm.controller;

import com.form1115.crm.common.Result;
import com.form1115.crm.dto.DeptDto;
import com.form1115.crm.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;


    @GetMapping("/getDeptsByPage.do")
    public Result getDeptsByPage(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        Result result = deptService.findDeptsByPage(pageNum, pageSize);

        return result;
    }

    @PostMapping("/addDept.do")
    public Result addDept(@Valid DeptDto deptDto, BindingResult br) {
        if (br.hasErrors()) {
            return new Result(-1,br.getFieldError().getDefaultMessage());
        }

        return deptService.saveDept(deptDto);

    }
}
