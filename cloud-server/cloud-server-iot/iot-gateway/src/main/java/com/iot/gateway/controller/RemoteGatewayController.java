package com.iot.gateway.controller;

import com.cloud.api.model.GatewayInfo;
import com.iot.gateway.core.AddGatewayEvent;
import com.iot.gateway.mapper.GatewayMapper;
import com.iot.gateway.mqtt.subscribe.GatewaySubscriber;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/9 14:51
 */
@RestController
@RequestMapping("/remote/gateway")
public class RemoteGatewayController {

    @Autowired
    private GatewayMapper gatewayMapper;
    @Autowired
    private GatewaySubscriber subscriber;
    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/sub")
    public boolean subscribe(@RequestBody String serialNum) throws MqttException {
        subscriber.invokeSubscribe(serialNum);
        return true;
    }

    @PostMapping("/add")
    public int addGateway(@RequestBody GatewayInfo info) {
        publisher.publishEvent(new AddGatewayEvent(this, info));
        return 0;
    }

    @GetMapping("/num")
    public int getSerialNum(@RequestParam String serial) {
        boolean contains = gatewayMapper.getAllSerialNum().contains(serial);
        if (contains) {
            return 1;
        }
        return 0;
    }
}
