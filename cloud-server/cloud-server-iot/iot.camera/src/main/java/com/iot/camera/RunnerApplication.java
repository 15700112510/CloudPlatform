package com.iot.camera;

import com.cloud.common.redis.service.RedisService;
import com.iot.camera.domain.Camera;
import com.iot.camera.mapper.CameraMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public class RunnerApplication implements ApplicationRunner {
    @Resource
    private CameraMapper cameraMapper;
    @Resource
    private RedisService redisService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Camera> cameras = cameraMapper.queryCamera();
        if (cameras == null) {
            cameras = new ArrayList<>();
        }
        redisService.setCacheList("allCamera", cameras);
    }
}
