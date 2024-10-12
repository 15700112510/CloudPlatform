package com.iot.lamp.dto;

import com.cloud.common.security.annotation.BasicValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrightnessControlDto {
    // 亮度值
    @BasicValueCheck
    private Integer brightness;
    // 开关：0为开，1为关
    @BasicValueCheck
    private Character flag;
    // 控制类型：0为单灯，1为广播
    @BasicValueCheck
    private Character type;
    // 广播群号
    private String groupNo;
    // 单灯卡号
    private String cardNo;
}
