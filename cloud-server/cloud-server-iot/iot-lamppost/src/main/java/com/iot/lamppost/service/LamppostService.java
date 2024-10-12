package com.iot.lamppost.service;

import com.cloud.common.core.R;
import com.iot.lamppost.dto.AddLamppostDto;
import com.iot.lamppost.dto.LamppostNameDto;

public interface LamppostService {

    // 添加灯杆
    R<?> addLamppost(AddLamppostDto dto);

    // 查询全部灯杆
    R<?> getAllLamppost();

    // 根据关键词分页查询灯杆
    R<?> getPageLamppost(Integer pageNum, Integer pageSize, String wrapper);

    // 修改灯杆名称
    R<?> editLamppostName(LamppostNameDto dto);

    // 查询灯杆总数量
    R<?> queryLamppostNum();

    // 根据灯杆id获取挂载卡号
    R<?> queryCardNoByLamppostId(Long lamppostId);

    // 根据卡号查询所挂载的灯杆信息
    R<?> queryLamppostInfoByCardId(String cardNo);
}
