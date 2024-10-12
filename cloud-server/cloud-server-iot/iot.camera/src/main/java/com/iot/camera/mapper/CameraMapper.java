package com.iot.camera.mapper;

import com.iot.camera.domain.Camera;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CameraMapper {
    //添加摄像头
    int addCamera(Camera camera);
    //查找
    List<Camera> queryCamera();
}
