package com.iot.lamp.dto;

import com.cloud.common.security.annotation.BasicValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricAutoSendDto extends ControlDto {
    // 开关：0开1关
    @BasicValueCheck
    private Character flag;
    // 控制自动发送的间隔秒数：01-99
    @BasicValueCheck
    private Integer interval;
    // 控制类型：0为单灯，1为广播
    private Character type;
    // 广播群号
    private String groupNo;
    // 单灯卡号
    private String cardNo;
}
