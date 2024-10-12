package com.iot.gateway.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.NotNullCheck;
import com.cloud.common.security.exception.IllegalParamException;
import com.iot.gateway.domain.register.CommonConf;
import com.iot.gateway.domain.register.MqttConf;
import com.iot.gateway.domain.register.WifiConf;
import com.iot.gateway.service.getter.GatewayGetterService;
import com.iot.gateway.service.setter.GatewaySetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: 获取数据库数据
 *
 * @author likain
 * @since 2023/5/29 10:53
 */
// @RequiredOrigin("gateway")
@RestController
@RequestMapping("/api/gateway")
public class WebGatewayController {

    @Autowired
    private GatewayGetterService impl;
    @Autowired
    private GatewaySetterService setter;

    @GetMapping("/get/all")
    public R<?> getAllGateway() {
        return R.ok(impl.getAllGateway());
    }

    @GetMapping("/get/mqtt")
    public R<?> getMqttConf(@RequestParam("gatewayId") Long gatewayId) {
        return R.ok(impl.getMqttConf(gatewayId));
    }

    @GetMapping("/get/common")
    public R<?> getCommonConf(@RequestParam("gatewayId") Long gatewayId) {
        return R.ok(impl.getCommonConf(gatewayId));
    }

    @GetMapping("/get/wifi")
    public R<?> getWifiConf(@RequestParam("gatewayId") Long gatewayId) {
        return R.ok(impl.getWifiConf(gatewayId));
    }

    @GetMapping("/get/traffic")
    public R<?> getTraffic(@RequestParam("gatewayId") Long gatewayId) {
        return R.ok(impl.getTraffic(gatewayId));
    }

    @PostMapping("/set/mqtt/{id}")
    public R<?> setMqttConf(@PathVariable("id") Long gatewayId, @RequestBody MqttConf conf) {
        setter.setMqttConf(gatewayId, conf);
        return R.ok();
    }

    @PostMapping("/set/common/{id}")
    public R<?> setCommonConf(@PathVariable("id") Long gatewayId, @RequestBody CommonConf conf) {
        setter.setCommonConf(gatewayId, conf);
        return R.ok();
    }

    @PostMapping("/set/sub/{id}")
    public R<?> setSubscribe(@PathVariable("id") Long gatewayId, @RequestBody String sub) {
        setter.setSubscribe(gatewayId, sub);
        return R.ok();
    }

    @PostMapping("/set/reboot/{id}")
    public R<?> setReboot(@PathVariable("id") Long gatewayId) {
        setter.setReboot(gatewayId);
        return R.ok();
    }

    @PostMapping("/set/wifi/{id}")
    public R<?> setWifiConf(@PathVariable("id") Long gatewayId, @RequestBody WifiConf conf) {
        setter.setWifiConf(gatewayId, conf);
        return R.ok();
    }
}
