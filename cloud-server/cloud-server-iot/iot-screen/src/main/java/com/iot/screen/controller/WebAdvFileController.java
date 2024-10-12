package com.iot.screen.controller;

import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.RequiredRoles;
import com.hidisp.api.cloud.models.MaterialInfo;
import com.hidisp.api.cloud.models.MaterialResult;
import com.hidisp.api.cloud.models.MaterialStatus;
import com.iot.screen.service.AdvFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/led")
@RequiredOrigin("gateway")
public class WebAdvFileController {
    @Resource
    private AdvFileService advFileService;

    // 获取当前可用的所有广告素材
    @GetMapping("/file/get")
    public List<MaterialInfo> getMaterials() {
        return advFileService.getMaterials();
    }

    // 检查所选MD5素材的当前状态
    @PostMapping("/file/check")
    public List<MaterialStatus> check(@RequestBody MultipartFile multipartFile) {
        return advFileService.check(multipartFile);
    }

    // 上传素材
    @PostMapping("/file/upload")
    @RequiredRoles("1")
    public MaterialResult upload(@RequestBody MultipartFile multipartFile) {
        return advFileService.upload(multipartFile);
    }
}
