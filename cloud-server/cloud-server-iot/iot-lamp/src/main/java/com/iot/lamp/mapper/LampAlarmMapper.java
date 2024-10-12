package com.iot.lamp.mapper;

import com.iot.lamp.dto.AddLampAlarmDto;
import com.iot.lamp.dto.LampAlarmDto;
import com.iot.lamp.dto.LampAlarmPageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LampAlarmMapper {

    // 分页查询报警信息
    List<LampAlarmDto> getPage();

    // 根据卡号分页查询报警信息
    List<LampAlarmDto> getPageByCardNo(String cardNo);

    // 根据某个时间段分页查询某个卡号的报警信息
    List<LampAlarmDto> getPageByCardNoAndTime(LampAlarmPageDto dto);

    // 报警信息新增
    int addAlarm(AddLampAlarmDto dto);

    // 报警清除
    int alarmClear(String cardNo, String userId);

    // 分页查看修复信息
    List<LampAlarmDto> getPageRepair();

    // 查看alarmId是否存在
    int getAlarmCount(Long alarmId);

    // 根据alarmId查找对应的卡号(用于删除对应缓存)
    String getCardNoByAlarmId(Long alarmId);

    // 是否已修复
    int isRepaired(Long alarmId);

    // 查询报警的总灯数
    int getAlarmNum();
}
