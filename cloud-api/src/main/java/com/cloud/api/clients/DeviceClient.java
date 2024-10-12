package com.cloud.api.clients;

import com.cloud.api.model.Device;
import com.cloud.api.factory.DeviceClientFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "iot-device-service",
        path = "/remote/device",
        qualifiers = {"deviceClient"},
        fallbackFactory = DeviceClientFallbackFactory.class
)
public interface DeviceClient {

    @GetMapping("/login")
    int deviceLogin(@RequestParam("deviceId") Long deviceId);

    @GetMapping("/logout")
    int deviceLogout(@RequestParam("deviceId") Long deviceId);

    @GetMapping("/status")
    char deviceStatus(@RequestParam("deviceId") Long deviceId);

    @PostMapping("/add")
    long addDevice(@RequestBody Device device);
}
