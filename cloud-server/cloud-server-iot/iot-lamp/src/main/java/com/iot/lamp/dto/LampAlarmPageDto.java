package com.iot.lamp.dto;

import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.StringValueCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LampAlarmPageDto {
    //卡号
    @StringValueCheck
    private String cardNo;
    //开始时间
    @StringValueCheck
    private String startTime;
    //结束时间
    @StringValueCheck
    private String endTime;
    //分页查询页码
    @BasicValueCheck
    private Integer pageNum;
    //分页查询数量
    @BasicValueCheck
    private Integer pageSize;
}
