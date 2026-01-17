package com.form1115.crm.service;

import com.form1115.crm.common.Result;
import com.form1115.crm.dto.AccountDto;

public interface AccountService {

    /**
     * 登录操作
     * @param accountDto
     * @return
     */
    Result findLogin(AccountDto accountDto);


    /**
     * 分页查询账户列表
     * @param pageNum 页码
     * @param pageSize 每页显示的条数
     * @return
     */
    Result findAccountsByPage(Integer pageNum, Integer pageSize);

    /**
     * 根据Id 删除账户信息
     * @param id
     * @return
     */
    Result removeAccountById(Long id);


    /**
     * 重置账户的密码
     * @param id
     * @return
     */
    Result modifyResetAccountPwd(Long id);

    /**
     * 添加账户 只有超级管理员才有权限添加
     * @param userName
     * @return
     */
    Result saveAccount(String userName);

    /**
     * 修改头像
     * @param imgUrl
     * @return
     */
    Result modifyAccountImgUrl(String imgUrl);

    /**
     * 修改账户的密码
     * @param newPwd
     * @return
     */
    Result modifyAccoutPwdById(String newPwd);
}
