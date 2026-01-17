package com.form1115.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.form1115.crm.common.Result;
import com.form1115.crm.domain.User;
import com.form1115.crm.dto.UserDto;
import com.form1115.crm.dto.UserSearchDto;
import com.form1115.crm.mapper.UserMapper;
import com.form1115.crm.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result saveUser(UserDto userDto) {
        // 查询部门是否存在
        int i = userMapper.selectDeptIsExist(userDto);
        if (i == 0) {
            return new Result(-1,"对不起，部门不存在");
        }
        // 查询客户是否存在
        int j = userMapper.selectUserIsExist(userDto);
        if (j >= 1) {
            return new Result(-1,"对不起，客户已经存在");
        }

        // 添加新的客户信息
        userDto.setUpdateTime(new Date());
        userDto.setCreateTime(new Date());
        int x = userMapper.insertUser(userDto);
        if (x == 0) {
            return new Result(-1, "添加客户失败");
        }
        return new Result();
    }

    @Override
    public Result findUsersByPage(Integer pageNum, Integer pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectUsersByPage();
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        Result result = new Result();
        result.setTotal(pageInfo.getTotal());
        result.setData(pageInfo.getList());

        return result;
    }

    @Override
    public Result removeUserById(Long id) {
        if (id == null || id <= 0) {
            return Result.DATE_FORMAT_ERROR;
        }
        int i = userMapper.deleteUserById(id);
        if (i == 0) {
            return new Result(-1, "删除失败");
        }
        return new Result();
    }

    @Override
    public Result removeManyUser(String id) {
        if (id == null || !(id+",").matches("([1-9][0-9]*,)+")) {
            return Result.DATE_FORMAT_ERROR;
        }
        int i = userMapper.deleteManyUser(id);
        if (i == 0) {
            return new Result(-1, "删除失败");
        }
        return new Result();
    }

    @Override
    public Result findUserBySearch(UserSearchDto userSearchDto) {
        PageHelper.startPage(userSearchDto.getPageNum(), userSearchDto.getPageSize());
        List<User> userList = userMapper.selectUserBySearch(userSearchDto);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        Result result = new Result();
        result.setTotal(pageInfo.getTotal());
        result.setData(pageInfo.getList());
        return result;
    }

    @Override
    public Result modifyUserById(UserDto userDto) {
        // 查询部门是否存在
        int i = userMapper.selectDeptIsExist(userDto);
        if (i == 0) {
            return new Result(-1,"对不起，部门不存在");
        }
        // 查询客户是否存在（排除当前修改的用户）
        // 先查询当前用户信息
        User currentUser = userMapper.selectByPrimaryKey(userDto.getId());
        if (currentUser == null) {
            return new Result(-1, "用户不存在");
        }
        // 如果关键字段（用户名、生日、地址、性别）有变化，检查是否与其他用户重复
        boolean keyFieldsChanged = !currentUser.getUsername().equals(userDto.getUsername()) 
            || !currentUser.getBirthday().equals(userDto.getBirthday())
            || !currentUser.getAddress().equals(userDto.getAddress())
            || !currentUser.getGender().equals(userDto.getGender());
        
        if (keyFieldsChanged) {
            int j = userMapper.selectUserIsExist(userDto);
            if (j >= 1) {
                return new Result(-1,"对不起，客户已经存在");
            }
        }

        int row = userMapper.updateUserById(userDto);
        if (row == 0) {
            return new Result(-1, "修改客户失败");
        }
        return new Result();
    }

}
