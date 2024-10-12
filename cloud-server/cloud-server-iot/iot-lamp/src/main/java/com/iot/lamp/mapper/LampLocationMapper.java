package com.iot.lamp.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/10 20:51
 */
@Mapper
public interface LampLocationMapper {

    int insertLocation(Long lampId);

    int updateLocation(Long lampId, String latitude, String longitude);
}
