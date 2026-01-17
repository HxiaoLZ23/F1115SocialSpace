package com.form1115.crm.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AccountDto {

    @NotNull
    @Pattern(regexp = "\\w{4,20}")
    private String username;

    @NotNull
    @Pattern(regexp = "\\d{6}")
    private String pwd;

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z]{4}")
    private String captcha;
}
