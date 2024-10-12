package com.iot.camera.service;


import com.cloud.common.core.R;
import com.iot.camera.domain.Camera;

public interface CameraService {
    R<?> addCamera(Camera camera);

    R<?> queryCamera(String createBy,Integer pageNum, Integer pageSize);
}
