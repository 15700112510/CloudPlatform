package com.iot.device.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampDeviceDto {
    //卡号
    private String cardNo;
    //创建时间
    private String createTime;
    //创建者
    private String createBy;
    //更新时间
    private String updateTime;
    //更新者
    private String updateBy;
    //状态
    private Character status;
}
