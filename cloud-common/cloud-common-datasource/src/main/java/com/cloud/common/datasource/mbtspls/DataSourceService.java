package com.cloud.common.datasource.mbtspls;


import com.alibaba.druid.pool.DruidDataSource;
import com.cloud.common.datasource.DataSourceUtil;
import com.cloud.common.datasource.DynamicDataSource;
import com.cloud.common.datasource.mapper.TableInitMapper;
import com.cloud.common.datasource.mapper.dbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceService {
    @Autowired
    private DataSourceConfig1 dataSourceConfig;

    @Autowired
    private dbMapper dbMapper;
    @Autowired
    private TableInitMapper tableInitMapper;
    @Autowired
    private DynamicDataSource dynamicDataSource;
    public void init(String dbName,String tableName){
        //判断数据库是否存在，不存在创建
        try {
            dbMapper.createDataBase(dbName);
        } catch (Exception e) {

        }
       //创建库之后
        addDataSource(dbName);
        DataSourceUtil.setDB("system");
        try {
            tableInitMapper.createDemo(tableName);
        } catch (Exception e) {

        }
        System.out.println("初始化数据完成");
    }
    //添加配置数据库
    private void addDataSource(String dbName){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(MessageFormat.format("jdbc:mysql://{0}:{1}/{2}?useUnicode=true&characterEncoding=utf-8",
                "119.45.119.119","13306",dbName));

        _addDataSource(dataSource, DataSourceConfig1.DATASOURCE_SYSTEM, dynamicDataSource);
    }


    public void _addDataSource(DataSource dataSource, String key, AbstractRoutingDataSource ard) {
        // 将当前数据源全部取出
        Map<Object, DataSource> originMap = ard.getResolvedDataSources();

        // 克隆新的集合，并将原始数据源插入
        Map<Object, Object> newMap = new HashMap<>(originMap);
        // 插入新数据源
        newMap.put(key, dataSource);

        ard.setTargetDataSources(newMap);
        // 将新的targetDataSources拷贝到resolvedDataSources中供determineTargetDataSource方法使用
        ard.afterPropertiesSet();
    }
}


