package com.cloud.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lamp {

    // 照明灯唯一标识id
    private Long lampId;
    // 关联deviceId
    private Long deviceId;
    // 卡号
    private String cardNo;

    // 添加照明灯
    public Lamp(Long deviceId, String cardNo) {
        this.deviceId = deviceId;
        this.cardNo = cardNo;
    }
}
