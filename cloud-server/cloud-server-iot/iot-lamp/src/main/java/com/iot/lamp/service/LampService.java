package com.iot.lamp.service;

import com.cloud.common.core.R;
import com.iot.lamp.domain.ClockConfig;
import com.iot.lamp.domain.DefaultConfig;
import com.iot.lamp.dto.BrightnessControlDto;
import com.iot.lamp.dto.ControlDto;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/31 09:41
 */
public interface LampService {

    //亮度控制
    R<?> brightnessControl(BrightnessControlDto dto);

    //控制灯控器重启一次
    R<?> restartControl(ControlDto dto);

    // 默认参数配置
    R<?> defaultConfig(DefaultConfig config);

    // 闹钟配置
    R<?> clockConfig(ClockConfig config);
}
