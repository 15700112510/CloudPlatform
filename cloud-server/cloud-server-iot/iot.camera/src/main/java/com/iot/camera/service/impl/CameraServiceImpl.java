package com.iot.camera.service.impl;

import com.cloud.common.core.R;
import com.cloud.common.datasource.DataSourceUtil;
import com.cloud.common.datasource.mbtspls.DataSourceService;
import com.cloud.common.redis.service.RedisService;

import com.github.pagehelper.PageHelper;
import com.iot.camera.domain.Camera;
import com.iot.camera.mapper.CameraMapper;
import com.iot.camera.service.CameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("CameraService")
public class CameraServiceImpl implements CameraService {
    @Resource
    private CameraMapper cameraMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate redisTemplate;
    private static final String  CAMERA_NUM = "allCamera:";
    //添加设备
    @Override
    public R<?> addCamera(Camera camera) {
        //根据创建名称更换数据库
        DataSourceUtil.setDB(camera.getCreateBy());
            //新增数据到数据库
            int i = cameraMapper.addCamera(camera);
            redisService.deleteObject(CAMERA_NUM+camera.getCreateBy());
            return R.ok(i);
    }
    //查找设备
    @Override
    public R<?> queryCamera(String createBy,Integer pageNum, Integer pageSize) {
        //根据创建名称更换数据库
        DataSourceUtil.setDB(createBy);
        //数据库分页
        PageHelper.startPage(pageNum,pageSize);
        if (redisService.hasKey(CAMERA_NUM+createBy)==false){
            List<Camera> cameras = cameraMapper.queryCamera();
            redisService.setCacheList(CAMERA_NUM + createBy,cameras);
            return R.ok(cameras);
        }
        List<Object> listAll = redisService.getCacheListAll(CAMERA_NUM + createBy);
        return R.ok(listAll);
    }
}
