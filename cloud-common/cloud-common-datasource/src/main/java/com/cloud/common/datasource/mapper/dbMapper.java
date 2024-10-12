package com.cloud.common.datasource.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface dbMapper {
    int dataBaseIsExit(@Param("dbName") String dbName);
    int createDataBase(@Param("dbName") String dbName);
    int tableIsExit(@Param("dbName") String dbName,@Param("tableName") String tableName);
}
