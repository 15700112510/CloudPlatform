package com.cloud.api.clients;

import com.cloud.api.model.GatewayInfo;
import com.cloud.api.factory.DeviceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/6/9 14:55
 */
@FeignClient(
        value = "iot-gateway-service",
        path = "/remote/gateway",
        qualifiers = {"iotGatewayClient"},
        fallbackFactory = DeviceClientFallbackFactory.class
)
public interface IotGatewayClient {

    @PostMapping("/sub")
    boolean subscribe(@RequestBody String serialNum);

    @PostMapping("/add")
    int addGateway(@RequestBody GatewayInfo info);

    @GetMapping("/num")
    int getSerialNum(@RequestParam("serial") String serial);
}
