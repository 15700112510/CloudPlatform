package com.iot.camera.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.iot.camera.domain.Camera;
import com.iot.camera.service.CameraService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/camera")
@RequiredOrigin("gateway")
public class CameraController {
    @Resource
    private CameraService cameraService;
    @PostMapping("/add")
    public R<?> addCamera(@RequestBody Camera camera){
        return cameraService.addCamera(camera);
    }
    @GetMapping("/query")
    public R<?> queryCamera(@RequestParam String createBy,@RequestParam Integer page, @RequestParam Integer limit){
        return cameraService.queryCamera(createBy,page,limit);
    }


}
