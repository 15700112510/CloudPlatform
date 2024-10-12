package com.iot.device.mapper;

import com.cloud.api.model.Device;
import com.iot.device.dto.LampDeviceDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceMapper {

    // 添加设备
    int addDevice(Device device);

    // 获取刚才插入的设备ID，线程独立
    long getLastInsert();

    // 设备登录上线
    int login(Long deviceId);

    int logout(Long deviceId);

    // 查看设备在线状态
    char getStatus(Long deviceId);

    //查询灯杆上挂载的照明灯
    LampDeviceDto getLamp(Character type, Long lamppostId);

    // 分页查询各个灯杆上的照明灯
    LampDeviceDto getPageLamps(String wrapper);

    //根据灯杆id查询是否存在此灯杆
    int lamppostExistCount(Long lamppostId);

    //查看该卡号对应的照明灯是否已挂载
    int lampExistCount(String cardNo);

    //查看灯杆上是否已挂载照明灯设备
    int lampTypeExistCount(Long lamppostId);

    //根据灯杆id及设备类型查询唯一设备id
    Long queryLatestDeviceId();
}
