package com.form1115.crm.mapper;

import com.form1115.crm.domain.Account;
import com.form1115.crm.dto.AccountDto;

import java.util.List;

/**
* @author Administrator
* @description 针对表【account】的数据库操作Mapper
* @createDate 2025-01-11 19:19:33
* @Entity com.form1115.crm.domain.Account
*/
public interface AccountMapper {

    int deleteByPrimaryKey(Long id);

    /**
     * 添加账户 - 只有超级管理员才有权限
     * @param userName
     * @return
     */
    int insert(String userName);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    /**
     * 登录操作
     * @param accountDto
     * @return
     */
    Account login(AccountDto accountDto);

    /**
     * 查询账户列表
     * @return
     */
    List<Account> selectAccountsList();


    /**
     * 重置密码 只有超级管理员才有权限
     * @param id
     * @return
     */
    int updateResetAccountPwd(Long id);

    /**
     * 根据用户名查询账户信息
     * @param userName
     * @return
     */
    Account selectAccountByUserName(String userName);


    /**
     * 修改头像
     * @param imgUrl
     * @param id
     * @return
     */
    int updateAccountImgUrl(String imgUrl, Long id);


    /**
     * 修改账户的密码
     * @param pwd
     * @param id
     * @return
     */
    int updateAccountPwd(String pwd, String id);


    /**
     * 根据账户ID查询它的密码（旧密码）
     * @param id
     * @return
     */
    String selectAccountPwdById(String id);



}
