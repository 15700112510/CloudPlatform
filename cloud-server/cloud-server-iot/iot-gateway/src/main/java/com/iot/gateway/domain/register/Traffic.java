package com.iot.gateway.domain.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/4 14:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Traffic {
    /**
     * 总上行流量
     */
    private String totalUp;
    /**
     * 总下行流量
     */
    private String totalDown;
    /**
     * 日上行流量
     */
    private String dayUp;
    /**
     * 日下行流量
     */
    private String dayDown;
    /**
     * 最近更新时间
     */
    private Timestamp updateTime;
}
