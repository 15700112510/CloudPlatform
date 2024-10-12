package com.iot.screen.service;

import com.hidisp.api.cloud.models.MaterialInfo;
import com.hidisp.api.cloud.models.MaterialResult;
import com.hidisp.api.cloud.models.MaterialStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvFileService {
    //获取当前可用的所有广告素材
    List<MaterialInfo> getMaterials();

    //检查所选MD5素材的当前状态
    List<MaterialStatus> check(MultipartFile multipartFile);

    //上传素材
    MaterialResult upload(MultipartFile file);
}
