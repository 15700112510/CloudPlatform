package com.iot.lamppost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariousNumDto {
    // 灯杆总数
    private Integer lamppostNum;
    // 灯总数
    private Integer lampNum;
    // 亮灯总数
    private Integer lampOnNum;
    // 报警灯总数
    private Integer lampAlarmNum;
}
