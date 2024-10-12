package com.iot.lamp.service;

import com.cloud.common.core.R;
import com.iot.lamp.dto.ControlDto;
import com.iot.lamp.dto.ElectricAutoSendDto;

public interface LampParamService {

    // 根据卡号取出最近一条历史数据
    R<?> getLatestParamByCardNo(String cardNo);

    // 根据卡号分页取出历史数据
    R<?> getPageParamByCardNo(String cardNo, Integer pageNum, Integer pageSize);

    // 根据卡号取出某个时间段的所有历史数据
    R<?> getParamByTimeInterval(String cardNo, String startTime, String endTime);

    // 分页获取电能信息
    R<?> getPageParam(Integer pageNum, Integer pageSize);

    // 改变灯群亮度
    R<?> updateGroupBrightness(Integer brightness, String groupNo);

    // 通知发送一次电能信息
    R<?> callForElectricData(ControlDto dto);

    // 通知发送一次定位消息
    R<?> callForGnssInfo(ControlDto dto);

    // 通知自动发送电能信息
    R<?> autoSendElectricData(ElectricAutoSendDto dto);

    // 累计电量清零
    R<?> electricityClear(ControlDto dto);
}
