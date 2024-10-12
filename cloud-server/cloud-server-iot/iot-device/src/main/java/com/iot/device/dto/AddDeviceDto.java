package com.iot.device.dto;

import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDeviceDto {
    /**
     * 所挂载的灯杆id
     */
    private Long lamppostId;
    /**
     * 添加的设备类型
     */
    @StringValueCheck
    private Character type;
    /**
     * 操作者
     */
    @StringValueCheck
    private String opsBy;
    /**
     * 不同类型设备的详细参数
     */
    @StringValueCheck
    private String details;
}
