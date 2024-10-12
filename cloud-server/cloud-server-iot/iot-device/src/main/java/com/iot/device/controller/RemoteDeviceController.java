package com.iot.device.controller;

import com.cloud.api.model.Device;
import com.iot.device.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/remote/device")
public class RemoteDeviceController {

    @Autowired
    private DeviceMapper deviceMapper;

    @GetMapping("/login")
    public int deviceLogin(@RequestParam("deviceId") Long deviceId) {
        return deviceMapper.login(deviceId);
    }

    @GetMapping("/logout")
    public int deviceLogout(@RequestParam("deviceId") Long deviceId) {
        return deviceMapper.logout(deviceId);
    }

    @GetMapping("/status")
    public char deviceStatus(@RequestParam("deviceId") Long deviceId) {
        return deviceMapper.getStatus(deviceId);
    }

    @PostMapping("/add")
    public long addDevice(@RequestBody Device device) {
        deviceMapper.addDevice(device);
        return deviceMapper.getLastInsert();
    }
}
