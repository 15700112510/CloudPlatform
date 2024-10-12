package com.iot.lamppost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LamppostInfoDto {
    // 灯杆名
    private String lamppostName;
    // 所属群组
    private String groupNo;
    // 纬度
    private Double longitude;
    // 经度
    private Double latitude;
    // 位置
    private String location;
}
