package com.form1115.crm.controller;

import com.form1115.crm.common.Constants;
import com.form1115.crm.common.Result;
import com.form1115.crm.dto.AccountDto;
import com.form1115.crm.service.AccountService;
import com.form1115.crm.utils.WebScopeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getLogin.do")
    public Result getLogin(@Valid AccountDto accountDto, BindingResult br) {
        if (br.hasErrors()) {
            return Result.DATE_FORMAT_ERROR;
        }
        return accountService.findLogin(accountDto);
    }

    @GetMapping("/getAccountsByPage.do")
    public Result getAccountsByPage(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return accountService.findAccountsByPage(pageNum, pageSize);
    }

    @DeleteMapping("/cutOneAccount.do")
    public Result cutOneAccount(Long id) {
        return accountService.removeAccountById(id);
    }

    @PostMapping("/editResetAccountPwd.do")
    public Result editResetAccountPwd(Long id) {
        return accountService.modifyResetAccountPwd(id);
    }

    @PutMapping("/addAccount.do")
    public Result addAccount(String username) {
        return accountService.saveAccount(username);
    }

    @RequestMapping("/editAccountPwd.do")
    public Result editAccountPwd(String pwd) {
        return accountService.modifyAccoutPwdById(pwd);
    }

    @RequestMapping("/editLoginToOut.do")
    public Result logOut() {
        HttpSession session = WebScopeUtil.getSession();
        session.removeAttribute(Constants.SESSION_CAPTCHA);
        session.removeAttribute(Constants.SESSION_USER_ID);
        session.removeAttribute(Constants.SESSION_ROLE_FLAG);
        session.removeAttribute(Constants.SESSION_USER_NAME);

        // 领完一直退出登录的方式
        //session.invalidate();
        return new Result();
    }


}
