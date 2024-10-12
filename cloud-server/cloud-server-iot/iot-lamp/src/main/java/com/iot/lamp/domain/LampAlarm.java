package com.iot.lamp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampAlarm {
    //报警信息id
    private Long alarmId;
    //关联的lampId
    private Long lampId;
    //报警的类型
    private String type;
    //报警的数值
    private String value;
    //报警次数
    private Integer number;
    //产生时间
    private Date createTime;
    //状态：0代表存在，1代表已修复
    private Character status;
    //更新时间：包括次数累积以及状态变更
    private Date updateTime;
    //维修人员
    private String opsBy;

    //用于mqtt报警消息的新增
    public LampAlarm(Long lampId, String type, String value, Date createTime, Date updateTime, String opsBy) {
        this.lampId = lampId;
        this.type = type;
        this.value = value;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.opsBy = opsBy;
    }
}
