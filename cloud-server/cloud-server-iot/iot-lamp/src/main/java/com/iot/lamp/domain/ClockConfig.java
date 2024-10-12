package com.iot.lamp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 时钟配置实体类
 *
 * @author cloud
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockConfig {
    /**
     * 单灯卡号
     */
    private String cardNo;
    /**
     * 时钟种类号
     */
    private String clockNo;
    /**
     * 时钟时间
     */
    private String clockTime;
    /**
     * 时钟事件任务
     */
    private String clockTask;
    /**
     * 用户ID
     */
    private String userId;
}
