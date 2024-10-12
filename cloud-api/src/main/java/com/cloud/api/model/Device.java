package com.cloud.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    //设备唯一标识id
    private Long deviceId;
    //该设备所挂载的灯杆id
    private Long lamppostId;
    //设备类型：'L'为照明，摄像头与显示屏为第三方服务，'G'为网关
    private Character type;
    //创建时间
    private Date createTime;
    //创建者
    private String createBy;
    //更新时间
    private Date updateTime;
    //更新者
    private String updateBy;
    //状态：0为启用，1为停用
    private Character status;

    //添加设备
    public Device(Long lamppostId, Character type, String createBy, String updateBy) {
        this.lamppostId = lamppostId;
        this.type = type;
        this.createBy = createBy;
        this.updateBy = updateBy;
    }
}
