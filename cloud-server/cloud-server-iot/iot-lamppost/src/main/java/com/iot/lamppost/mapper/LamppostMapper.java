package com.iot.lamppost.mapper;

import com.iot.lamppost.domain.Lamppost;
import com.iot.lamppost.dto.LamppostInfoDto;
import com.iot.lamppost.dto.LamppostNameDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LamppostMapper {

    // 添加灯杆
    int addLamppost(Lamppost lamppost);

    // 查询所有灯杆
    List<Lamppost> getAllLamppost();

    // 分页查询灯杆
    List<Lamppost> getPageLamppost(String wrapper);

    // 根据灯杆名称和组号查询唯一灯杆(供新增灯杆缓存进redis用，不提供接口)
    Lamppost getByNameAndGroup(String lamppostName, String groupNo);

    // 查询某群中是否有该灯杆名的灯杆
    int queryLamppostByNameAndGroup(String lamppostName, String groupNo);

    // 修改灯杆名称
    int editLamppostName(LamppostNameDto dto);

    // 查询灯杆总数量
    int queryLamppostNum();

    // 根据灯杆id查询挂载的单灯卡号
    String queryCardNoByLamppostId(Long lamppostId);

    // 根据卡号查询挂载的灯杆信息
    LamppostInfoDto queryLamppostInfoByCardNo(String cardNo);
}
