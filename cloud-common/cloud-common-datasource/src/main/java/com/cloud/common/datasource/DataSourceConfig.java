package com.cloud.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
/**
 * 数据库配置
 * @date 2022/5/19
 */
@Configuration
public class DataSourceConfig {

    /**
     * 数据源1
     * spring.datasource.db1：application.properteis中对应属性的前缀
     * @return
     */
    @Bean(name = "cloud")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceOne() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }


    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Primary
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSourceOne());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("cloud", dataSourceOne());
        dynamicDataSource.setTargetDataSources(dsMap);
        return dynamicDataSource;
    }

    /**
     * 配置多数据源后IOC中存在多个数据源了，事务管理器需要重新配置，不然器不知道选择哪个数据源
     * 事务管理器此时管理的数据源将是动态数据源dynamicDataSource
     * 配置@Transactional注解
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
