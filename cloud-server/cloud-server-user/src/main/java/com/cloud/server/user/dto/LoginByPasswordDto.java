package com.cloud.server.user.dto;

import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginByPasswordDto {
    // 用户名或手机号登录
    @StringValueCheck
    private String root;
    // 密码
    @StringValueCheck
    private String password;
}
