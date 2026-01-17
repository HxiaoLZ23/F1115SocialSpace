package com.form1115.crm.controller;

import com.form1115.crm.common.Result;
import com.form1115.crm.domain.User;
import com.form1115.crm.dto.UserDto;
import com.form1115.crm.dto.UserSearchDto;
import com.form1115.crm.mapper.UserMapper;
import com.form1115.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/addUser.do")
    public Result addUser(@Valid UserDto userDto, BindingResult br) {
        Result result = new Result();
        if (br.hasErrors()) {
            result.setCode(-1);
            result.setMsg(br.getFieldError().getDefaultMessage());
            return result;
        }

        return userService.saveUser(userDto);
    }

    @GetMapping("/getUsersByPage.do")
    public Result getUsersByPage(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        return userService.findUsersByPage(pageNum, pageSize);
    }

    @DeleteMapping("/cutOneUser.do")
    public Result cutOneUser(Long id) {
        return userService.removeUserById(id);
    }

    @DeleteMapping("/cutManyUser.do")
    public Result cutManyUser(String id) {
        return userService.removeManyUser(id);
    }

    @GetMapping("/getUsersBySearch.do")
    public Result getUsersBySearch(@Valid UserSearchDto userSearchDto, BindingResult br) {
        if (br.hasErrors()) {
            return new Result(-1, br.getFieldError().getDefaultMessage());
        }
        return userService.findUserBySearch(userSearchDto);
    }

    @PutMapping("/editUser.do")
    public Result editUserById(@Valid UserDto userDto, BindingResult br) {
        if (br.hasErrors()) {
            return new Result(-1, br.getFieldError().getDefaultMessage());
        }

        return userService.modifyUserById(userDto);
    }

}
