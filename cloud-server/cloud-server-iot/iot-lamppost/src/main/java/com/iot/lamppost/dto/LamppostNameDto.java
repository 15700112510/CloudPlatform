package com.iot.lamppost.dto;

import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LamppostNameDto {
    // 灯杆id
    @BasicValueCheck
    private Long lamppostId;
    // 操作者
    @StringValueCheck
    private String opsBy;
    // 新名称
    @StringValueCheck
    private String lamppostName;
}
