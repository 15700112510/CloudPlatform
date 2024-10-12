package com.iot.lamp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单灯默认配置实体类
 *
 * @author cloud
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultConfig {
    /**
     * 单灯卡号
     */
    private String cardNo;
    /**
     * 自动发送消息配置
     */
    private String autoSend;
    /**
     * 心跳配置
     */
    private String alive;
    /**
     * 灯控器自我检查配置
     */
    private String selfCheck;
    /**
     * 定位功能配置
     */
    private String locate;
    /**
     * rtc配置
     */
    private String rtc;
    /**
     * 员工ID
     */
    private String userId;
}
