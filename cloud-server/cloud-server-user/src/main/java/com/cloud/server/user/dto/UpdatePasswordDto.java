package com.cloud.server.user.dto;

import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {
    //密码
    @StringValueCheck
    private String password;
    //手机号码
    @StringValueCheck
    private String phoneNum;
    //验证码
    @StringValueCheck
    private String code;
    //更新者
    @StringValueCheck
    private String updateBy;
}
