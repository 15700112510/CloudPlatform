package com.iot.lamp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmClearDto extends ControlDto {
    /**
     * 报警清除人员ID
     */
    private String userId;
}
