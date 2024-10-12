package com.iot.device.service;

import com.cloud.common.core.R;
import com.iot.device.dto.AddDeviceDto;

public interface DeviceService {

    // 添加设备
    R<?> addDevice(AddDeviceDto dto) throws Exception;

    // 查询设备
    R<?> getDevice(Character type, Long lamppostId);

    // 获取分页灯控器信息
    R<?> getPageLamps(int page, int limit, String wrapper);

    // 获取某个类型设备的总数
    R<?> getTotalNumByType(Character type);
}
