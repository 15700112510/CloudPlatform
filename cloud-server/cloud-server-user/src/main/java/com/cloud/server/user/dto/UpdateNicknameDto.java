package com.cloud.server.user.dto;

import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNicknameDto {
    //用户id
    @BasicValueCheck
    private Long userId;
    //昵称
    @StringValueCheck
    private String nickname;
    //更新者
    @StringValueCheck
    private String updateBy;
}
