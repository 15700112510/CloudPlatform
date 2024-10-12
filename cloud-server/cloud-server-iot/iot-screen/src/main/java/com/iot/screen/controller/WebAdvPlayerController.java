package com.iot.screen.controller;

import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.RequiredRoles;
import com.hidisp.api.cloud.models.CommandResult;
import com.hidisp.api.cloud.models.ScreenInfo;
import com.hidisp.api.cloud.models.SendResult;
import com.iot.screen.service.AdvPlayerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/led")
@RequiredOrigin("gateway")
public class WebAdvPlayerController {
    @Resource
    private AdvPlayerService playerService;

    // 获取全部显示屏
    @GetMapping("/screen/get")
    public List<ScreenInfo> getScreens() {
        return playerService.getScreens();
    }

    // 设置屏幕开关机
    @GetMapping("/screen/power/{status}")
    @RequiredRoles("1")
    public CommandResult setPower(@PathVariable String status) {
        return playerService.setPower(status);
    }

    // 设置屏幕亮度
    @GetMapping("/screen/light/{light}")
    @RequiredRoles("1")
    public CommandResult setLight(@PathVariable Integer light) {
        return playerService.setLight(light);
    }

    // 设置屏幕当前时间
    @PostMapping("/screen/time")
    @RequiredRoles("1")
    public CommandResult setTime() {
        return playerService.setTime();
    }

    // 投放广告
    @PostMapping("/send")
    @RequiredRoles("1")
    public SendResult sendProgram(@RequestBody MultipartFile multipartFile) throws Exception {
        return playerService.sendProgram(multipartFile);
    }
}
