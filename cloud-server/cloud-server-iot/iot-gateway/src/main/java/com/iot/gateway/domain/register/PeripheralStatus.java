package com.iot.gateway.domain.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeripheralStatus {
    /**
     * 读取 Lan 口状态
     */
    private String lanStatus;
    /**
     * 读取挂接设备地址列表
     */
    private String[] deviceAddress;
    /**
     * 读取继电器 1 状态
     */
    private String relayStatus1;
    /**
     * 读取继电器 2 状态
     */
    private String relayStatus2;
    /**
     * 读取继电器 3 状态
     */
    private boolean relayStatus3;
}
