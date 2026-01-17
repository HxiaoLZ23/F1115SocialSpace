package com.form1115.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.form1115.crm.common.Result;
import com.form1115.crm.domain.Dept;
import com.form1115.crm.dto.DeptDto;
import com.form1115.crm.mapper.DeptMapper;
import com.form1115.crm.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public Result findDeptsByPage(Integer pageNum, Integer pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询数据库
        List<Dept> deptList = deptMapper.selectDeptsByPage();
        // 创建分页对象
        PageInfo<Dept> pageInfo = new PageInfo<Dept>(deptList);
        Result result = new Result();
        result.setData(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public Result saveDept(DeptDto deptDto) {
        // 查询部门是否存在
        int count = deptMapper.selectDeptNameIsExist(deptDto.getName());
        if (count >= 1) {
            return new Result(-1,"对不起，该部门已经存在");
        }
        int row = deptMapper.insert(deptDto);
        if (row == 0) {
            return new Result(-1,"添加部门失败");
        }

        return new Result();
    }
}
