package com.form1115.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.form1115.crm.common.Constants;
import com.form1115.crm.common.Result;
import com.form1115.crm.domain.Account;
import com.form1115.crm.dto.AccountDto;
import com.form1115.crm.mapper.AccountMapper;
import com.form1115.crm.mapper.UserMapper;
import com.form1115.crm.service.AccountService;
import com.form1115.crm.utils.PwdUtil;
import com.form1115.crm.utils.WebScopeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Result findLogin(AccountDto accountDto) {
        // 先校验验证码是否正确
        HttpSession session = WebScopeUtil.getSession();
        String code = (String) session.getAttribute(Constants.SESSION_CAPTCHA);
        if (code == null || !code.equals(accountDto.getCaptcha())) {
            return new Result(-1,"验证码有误");
        }
        String newPwd = PwdUtil.encyPwd(accountDto.getPwd());
        accountDto.setPwd(newPwd);
        Account account = accountMapper.login(accountDto);

        if (account == null) {
            return new Result(-1,"用户名或密码错误");
        }

        // 登录成功后将用户名存到Session中
        session.setAttribute(Constants.SESSION_USER_NAME,account.getUsername());

        // 登录成功后，保存角色的标记
        session.setAttribute(Constants.SESSION_ROLE_FLAG,account.getRole());

        // 登录成功后，保存客户的ID
        session.setAttribute(Constants.SESSION_USER_ID,account.getId());

        Result result = new Result();
        result.setCode(200);
        result.setMsg("登录成功");
        result.setData(account);
        return result;
    }








    @Override
    public Result findAccountsByPage(Integer pageNum, Integer pageSize) {
        // 开启分页
        PageHelper.startPage(pageNum,pageSize);
        // 查询账户列表
        List<Account> accountList = accountMapper.selectAccountsList();
        // 将查询结果封装到分页对象中
        PageInfo<Account> pageInfo = new PageInfo<>(accountList);
        Result result = new Result();
        result.setTotal(pageInfo.getTotal());
        result.setData(pageInfo.getList());
        return result;
    }

    @Override
    public Result removeAccountById(Long id) {
        // 只有超级管理员才有权限删除账户
        Integer role = (Integer) WebScopeUtil.getSession().getAttribute(Constants.SESSION_ROLE_FLAG);
        if (role != 1) {
            return new Result(-1,"对不起，只有超级管理员才能删除账户");
        }

        // 超级管理员只能删除其他账户，不能删除自己
        Object userId = WebScopeUtil.getSession().getAttribute(Constants.SESSION_USER_ID);
        if (id == Long.parseLong(userId+"")) {
            return new Result(-1,"超级管理员不能删除自己");
        }

        if (id <= 0) {
            return Result.DATE_FORMAT_ERROR;
        }
        int row = accountMapper.deleteByPrimaryKey(id);
        if (row == 0) {
            return new Result(-1,"删除失败");
        }
        return new Result();
    }

    @Override
    public Result modifyResetAccountPwd(Long id) {
        // 只有超级管理员才有重置密码的权限
        Integer role = (Integer) WebScopeUtil.getSession().getAttribute(Constants.SESSION_ROLE_FLAG);

        if (role != 1) {
            return new Result(-1,"对不起，只有超级管理员才能重置密码");
        }


        if (id <= 0) {
            return Result.DATE_FORMAT_ERROR;
        }
        // 获取Session中登录账户的id
        Object userId = WebScopeUtil.getSession().getAttribute(Constants.SESSION_USER_ID);
        // 只能超级管管理员能重置密码
        if (id.equals(Long.parseLong(userId+""))) {
            // 退出操作，请求Sessoin中的数据
            // 获取Session
            HttpSession session = WebScopeUtil.getSession();
            // 清空Session的登录数据
            session.removeAttribute(Constants.SESSION_USER_ID);
            session.removeAttribute(Constants.SESSION_ROLE_FLAG);
            session.removeAttribute(Constants.SESSION_USER_NAME);
        }

        int row = accountMapper.updateResetAccountPwd(id);
        if (row == 0) {
            return new Result(-1,"重置密码失败");
        }

        return new Result();
    }

    @Override
    public Result saveAccount(String userName) {
        // 只有超级管理员才有权限添加用户
        Integer role = (Integer) WebScopeUtil.getSession().getAttribute(Constants.SESSION_ROLE_FLAG);
        if (role != 1) {
            return new Result(-1,"对不起，只有超级管理员才能添加账户");
        }

        if (userName == null || !userName.matches("\\w{4,20}")) {
            return Result.DATE_FORMAT_ERROR;
        }
        // 不能重复添加账户, 如果账户名已经存在，就不能添加
        Account account = accountMapper.selectAccountByUserName(userName);
        if (account != null) {
            return new Result(-1,"对不起，账户已经存在");
        }
        int row = accountMapper.insert(userName);
        if (row == 0) {
            return new Result(-1,"添加账户失败");
        }
        return new Result();
    }

    @Override
    public Result modifyAccountImgUrl(String imgUrl) {
        if(imgUrl != null) {
            String id = WebScopeUtil.getSession().getAttribute(Constants.SESSION_USER_ID) + "";
            int row = accountMapper.updateAccountImgUrl(imgUrl, Long.parseLong(id));
            if (row == 0) {
                return new Result(-1,"修改头像失败");
            }
        }
        return new Result();
    }

    @Override
    public Result modifyAccoutPwdById(String newPwd) {
        // 对新密码进行数据校验
        if (newPwd == null || !newPwd.matches("\\d{6}")) {
            return Result.DATE_FORMAT_ERROR;
        }

        // 校验新密码跟旧密码是否一致，如果一直，则不能修改
        HttpSession session = WebScopeUtil.getSession();
        String id = session.getAttribute(Constants.SESSION_USER_ID)+"";
        String oldPwd = accountMapper.selectAccountPwdById(id);
        newPwd = PwdUtil.encyPwd(newPwd);
        if (newPwd.equals(oldPwd)) {
            return new Result(-1,"对不起，新密码不能跟旧密码一样");
        }

        // 修改密码
        int i = accountMapper.updateAccountPwd(newPwd, id);
        if (i == 0) {
            return new Result(-1, "密码修改失败");
        }

        // 退出登录 将Session中的账户ID删除
        session.removeAttribute(Constants.SESSION_USER_ID);
        return new Result();
    }
}
