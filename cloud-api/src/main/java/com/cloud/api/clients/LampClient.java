package com.cloud.api.clients;

import com.cloud.api.model.Lamp;
import com.cloud.api.factory.LampClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "iot-lamp-service",
        path = "/remote/lamp",
        qualifiers = {"lampClient"},
        fallbackFactory = LampClientFallbackFactory.class
)
public interface LampClient {

    @GetMapping("/get/cardNo/count")
    int getCardNoCount(@RequestParam("cardNo") String cardNo);

    @GetMapping("/get/total/num")
    int getTotalNum();

    @PostMapping("/add")
    int addLamp(@RequestBody Lamp lamp, @RequestParam("opsBy") String opsby);

    @PostMapping("/sub")
    boolean subscribe(@RequestBody String cardNo);
}
