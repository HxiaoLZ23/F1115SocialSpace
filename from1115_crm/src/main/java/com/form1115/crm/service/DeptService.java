package com.form1115.crm.service;

import com.form1115.crm.common.Result;
import com.form1115.crm.dto.DeptDto;

public interface DeptService {


    /**
     * 分页查询部门信息
     * @param pageNum 页码
     * @param pageSize 每页显示的条数
     * @return
     */
    Result findDeptsByPage(Integer pageNum, Integer pageSize);


    /**
     * 添加部门
     * @param deptDto
     * @return
     */
    Result saveDept(DeptDto deptDto);
}
