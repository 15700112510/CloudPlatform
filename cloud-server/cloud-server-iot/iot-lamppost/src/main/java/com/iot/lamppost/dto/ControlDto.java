package com.iot.lamppost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/31 09:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlDto {
    // 控制类型：0为单灯，1为广播
    private Character type;
    // 广播群号
    private String groupNo;
    // 单灯卡号
    private String cardNo;
}
