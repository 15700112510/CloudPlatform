package com.iot.lamp.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.RequiredRoles;
import com.iot.lamp.domain.ClockConfig;
import com.iot.lamp.domain.DefaultConfig;
import com.iot.lamp.dto.BrightnessControlDto;
import com.iot.lamp.dto.ControlDto;
import com.iot.lamp.service.LampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/31 09:37
 */
@RestController
@RequestMapping("/api/lamp")
@RequiredOrigin("gateway")
@RequiredRoles("1")
public class WebLampController {

    @Autowired
    private LampService lampService;

    // 亮度控制
    @PostMapping("/bri/ctrl")
    public R<?> brightnessControl(@RequestBody BrightnessControlDto dto) {
        return lampService.brightnessControl(dto);
    }

    // 重启控制
    @PostMapping("/restart")
    public R<?> restartControl(@RequestBody ControlDto dto) {
        return lampService.restartControl(dto);
    }

    // 默认配置发送
    @PostMapping("/default/config")
    public R<?> defaultConfig(@RequestBody DefaultConfig config) {
        return lampService.defaultConfig(config);
    }

    // 时钟配置发送
    @PostMapping("/clock/config")
    public R<?> clockConfig(@RequestBody ClockConfig config) {
        return lampService.clockConfig(config);
    }
}
