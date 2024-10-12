package com.iot.lamppost.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lamppost {
    // 灯杆唯一标识id
    private Long lamppostId;
    // 灯杆名称
    private String lamppostName;
    // 所属群号
    private String groupNo;
    // 经度
    private Double longitude;
    // 纬度
    private Double latitude;
    // 地址
    private String location;
    // 创建时间
    private Date createTime;
    // 创建者
    private String createBy;
    // 更新时间
    private Date updateTime;
    // 更新者
    private String updateBy;
    // 状态：0启用，1停用
    private Character status;

    // 添加灯杆
    public Lamppost(String lamppostName, String groupNo, String createBy, String updateBy) {
        this.lamppostName = lamppostName;
        this.groupNo = groupNo;
        this.createBy = createBy;
        this.updateBy = updateBy;
    }
}
