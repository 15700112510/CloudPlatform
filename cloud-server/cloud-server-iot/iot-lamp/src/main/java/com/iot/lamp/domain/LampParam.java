package com.iot.lamp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampParam {
    //电能参数id
    private Long paramId;
    //关联电灯id
    private Long lampId;
    //亮度
    private Integer brightness;
    //电流
    private Double current;
    //电压
    private Double voltage;
    //功率
    private Double power;
    //功率因数
    private Double factor;
    //累计电量
    private Double electricity;
    //创建时间
    private Date createTime;
}
