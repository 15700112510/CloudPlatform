package com.iot.lamppost.dto;

import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLamppostDto {
    /**
     * 灯杆名称
     */
    @StringValueCheck
    private String lamppostName;
    /**
     * 所属群号
     */
    @StringValueCheck
    private String groupNo;
    /**
     * 操作者
     */
    @StringValueCheck
    private String opsBy;
}
