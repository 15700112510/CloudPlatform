package com.iot.camera.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Camera {
    /**
     * id
     */
    private int id;
//    /**
//     * 经度
//     */
//    private double longitude;
//    /**
//     * 纬度
//     */
//    private double latitude;
    /**
     * 所属灯杆号
     */
    private String lampId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 状态
     */
    private int status;
}
