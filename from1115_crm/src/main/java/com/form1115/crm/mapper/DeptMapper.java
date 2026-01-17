package com.form1115.crm.mapper;

import com.form1115.crm.domain.Dept;
import com.form1115.crm.dto.DeptDto;

import java.util.List;

/**
* @author Administrator
* @description 针对表【dept】的数据库操作Mapper
* @createDate 2025-02-15 18:51:02
* @Entity com.form1115.crm.domain.Dept
*/
public interface DeptMapper {

    int deleteByPrimaryKey(Long id);



    int insertSelective(Dept record);

    Dept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);

    /**
     * 添加部门信息
     * @param record
     * @return
     */
    int insert(DeptDto record);

    /**
     * 根据部门名称查询部门是否存在
     * @param deptName
     * @return
     */
    int selectDeptNameIsExist(String deptName);

    /**
     * 分页查询部门信息
     * @return
     */
    List<Dept> selectDeptsByPage();





}
