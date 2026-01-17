package com.form1115.crm.utils;

import cn.hutool.crypto.SecureUtil;

public final class PwdUtil {

    private static String init(String pwd) {
        if (pwd == null) {
            return "";
        }

        String newPwd = SecureUtil.md5(pwd);
        return newPwd;
    }

    public static String encyPwd(String pwd) {
        return init(pwd)+"pwd";
    }

    public static void main(String[] args) {
        System.out.println(encyPwd("123456"));
    }


}
