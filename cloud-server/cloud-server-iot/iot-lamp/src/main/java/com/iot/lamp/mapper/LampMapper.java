package com.iot.lamp.mapper;

import com.cloud.api.model.Lamp;
import com.iot.lamp.domain.ClockConfig;
import com.iot.lamp.domain.DefaultConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LampMapper {

    // 根据卡号获取设备id
    Long getDeviceId(String cardNo);

    // 根据卡号获取灯控器id
    Long getLampId(String cardNo);

    // 根据设备id获取灯控器id
    Long getLampIdByDeviceId(Long deviceId);

    // 添加照明灯设备
    int addLamp(Lamp lamp);

    // 卡号是否存在
    int getCardNoCount(String cardNo);

    // 照明灯总数量
    int getTotalNum();

    // 获取所有单灯控制器设备id
    List<Long> getAllDeviceId();

    // 获取所有单灯控制器卡号
    List<String> getAllCardNo();

    // 获取所有在线的单灯控制器卡号
    List<String> getAllOnlineCardNo();

    // 新增默认配置
    int addDefaultConfig(DefaultConfig config);

    // 新增时钟配置
    int addClockConfig(ClockConfig config);

    // 更新默认配置
    int updateDefaultConfig(DefaultConfig config);

    // 更新时钟配置
    int updateClockConfig(ClockConfig config);

    // 获取某类型的时钟时间信息
    String getClockTime(String clockNo);

    // 获取某类型的时钟任务信息
    String getClockTask(String clockNo);
}
