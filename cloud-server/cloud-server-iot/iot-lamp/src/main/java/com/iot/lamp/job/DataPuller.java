package com.iot.lamp.job;

import com.iot.lamp.dto.ControlDto;
import com.iot.lamp.mapper.LampMapper;
import com.iot.lamp.service.LampParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/11 00:29
 */
@Component
public class DataPuller {

    @Autowired
    private LampMapper lampMapper;
    @Autowired
    private LampParamService lampParamService;

    @Scheduled(initialDelay = 3000L, fixedRate = 30000L)
    public void pullElcData() {
        // 从缓存中查询平台已注册的卡号
        List<String> allOnlineCardNo = lampMapper.getAllOnlineCardNo();
        if (allOnlineCardNo == null) {
            return;
        }

        for (String cardNo : allOnlineCardNo) {
            ControlDto dto = new ControlDto('0', null, cardNo);
            lampParamService.callForElectricData(dto);
        }
    }
}
