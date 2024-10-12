package com.iot.lamp.mapper;

import com.iot.lamp.dto.LampParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LampParamMapper {

    //根据卡号取出最近一条历史数据
    LampParamDto getLatestParamByCardNo(String cardNo);

    //根据卡号取出所有历史数据
    List<LampParamDto> getAllParamByCardNo(String cardNo);

    //根据卡号取出某个时间段的所有历史数据
    List<LampParamDto> getParamByTimeInterval(String cardNo, String startTime, String endTime);

    //分页取出电能数据
    List<LampParamDto> getPageParam();

    //改变灯群亮度
    int updateGroupBrightness(Integer brightness, String groupNo);

    // 插入一条电能数据
    int addElcData(LampParamDto dto);
}
