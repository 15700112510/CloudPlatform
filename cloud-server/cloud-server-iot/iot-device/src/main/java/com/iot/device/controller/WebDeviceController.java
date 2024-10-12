package com.iot.device.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.RequiredRoles;
import com.iot.device.dto.AddDeviceDto;
import com.iot.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/device")
@RequiredOrigin("gateway")
public class WebDeviceController {

    @Autowired
    private DeviceService deviceService;

    // 往某灯杆上添加设备
    @PostMapping("/add")
    @RequiredRoles("1")
    public R<?> addDevice(@RequestBody AddDeviceDto dto) throws Exception {
        return deviceService.addDevice(dto);
    }

    // 获取某灯杆上挂载的某类型设备
    @GetMapping("/get")
    public R<?> getDevice(@RequestParam("type") Character type, @RequestParam("lamppostId") Long lamppostId) {
        return deviceService.getDevice(type, lamppostId);
    }

    // 分页获取路灯类型的设备
    @GetMapping("/lamps")
    public R<?> getPageLamps(@RequestParam Integer page, @RequestParam Integer limit, @RequestParam String wrapper) {
        return deviceService.getPageLamps(page, limit, wrapper);
    }

    // 获取某类型设备的总数量
    @GetMapping("/total/{type}")
    public R<?> getTotalNumByType(@PathVariable Character type) {
        return deviceService.getTotalNumByType(type);
    }
}
