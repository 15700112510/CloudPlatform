package com.cloud.common.datasource.mbtspls;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class DataSourceConfig1 {

    public static final String DATASOURCE_MASTER = "cloud";
    public static final String DATASOURCE_SYSTEM = "system";
}
