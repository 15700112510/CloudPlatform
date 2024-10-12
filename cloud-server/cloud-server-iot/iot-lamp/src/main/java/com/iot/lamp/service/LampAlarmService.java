package com.iot.lamp.service;

import com.cloud.common.core.R;
import com.iot.lamp.dto.AlarmClearDto;
import com.iot.lamp.dto.LampAlarmPageDto;

public interface LampAlarmService {

    // 分页查询报警信息
    R<?> getPage(Integer pageNum, Integer pageSize);

    // 根据卡号分页查询报警信息
    R<?> getPageByCardNo(String cardNo, Integer pageNum, Integer pageSize);

    // 根据某个时间段分页查询某个卡号的报警信息
    R<?> getPageByCardNoAndTime(LampAlarmPageDto dto);

    // 分页获取修复信息
    R<?> getPageRepair(Integer pageNum, Integer pageSize);

    // 获取报警数量
    R<?> getAlarmNum();

    // 报警清除
    R<?> alarmClear(AlarmClearDto dto);
}
