package com.cloud.server.user.dto;

import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    //用户名
    @StringValueCheck
    private String username;
    //密码
    @StringValueCheck
    private String password;
    //角色
    private Character role;
    //昵称
    @StringValueCheck
    private String nickname;
    //手机号
    @StringValueCheck
    private String phoneNum;
    //验证码
    @StringValueCheck
    private String code;
}
