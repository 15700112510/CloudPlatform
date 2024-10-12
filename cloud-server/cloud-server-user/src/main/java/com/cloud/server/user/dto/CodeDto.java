package com.cloud.server.user.dto;

import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto {
    //业务类型：L为验证码登录，R为注册，U为修改密码
    @StringValueCheck
    private String type;
    //电话号码
    @StringValueCheck
    private String phoneNum;
}
