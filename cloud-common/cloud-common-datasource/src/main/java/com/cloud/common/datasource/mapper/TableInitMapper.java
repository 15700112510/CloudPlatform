package com.cloud.common.datasource.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TableInitMapper {

    int createDemo(@Param("tableName") String tableName);
}
