package com.iot.gateway.controller.v2;

import com.cloud.common.core.R;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.Traffic;
import com.iot.gateway.domain.register.WifiConf;
import com.iot.gateway.service.getter.GatewayGetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: 获取强实时性数据
 *
 * @author likain
 * @since 2023/6/4 21:42
 */
@RestController
@RequestMapping("/api/gateway/v2")
// @RequiredOrigin("gateway")
public class WebGatewayControllerV2 {

    @Autowired
    private GatewayGetterService implV2;

    @GetMapping("/mqtt")
    public R<?> getMqttConf(@RequestParam("gatewayId") Long gatewayId) {
        MqttConf conf = implV2.getMqttConf(gatewayId);
        return R.ok(conf);
    }

    @GetMapping("/common")
    public R<?> getCommonConf(@RequestParam("gatewayId") Long gatewayId) {
        CommonConf conf = implV2.getCommonConf(gatewayId);
        return R.ok(conf);
    }

    @GetMapping("/wifi")
    public R<?> getWifiConf(@RequestParam("gatewayId") Long gatewayId) {
        WifiConf conf = implV2.getWifiConf(gatewayId);
        return R.ok(conf);
    }

    @GetMapping("/traffic")
    public R<?> getTraffic(@RequestParam("gatewayId") Long gatewayId) {
        Traffic traffic = implV2.getTraffic(gatewayId);
        return R.ok(traffic);
    }
}
