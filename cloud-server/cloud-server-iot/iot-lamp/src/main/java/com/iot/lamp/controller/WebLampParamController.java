package com.iot.lamp.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.BasicValueCheck;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.StringValueCheck;
import com.iot.lamp.dto.ControlDto;
import com.iot.lamp.dto.ElectricAutoSendDto;
import com.iot.lamp.service.LampParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lamp/param")
@RequiredOrigin("gateway")
public class WebLampParamController {

    @Autowired
    private LampParamService lampParamService;

    // 根据卡号取出最近一条数据
    @GetMapping("/get/latest/{cardNo}")
    public R<?> getLatestParamByCardNo(@PathVariable String cardNo) {
        return lampParamService.getLatestParamByCardNo(cardNo);
    }

    // 根据卡号分页取出数据
    @GetMapping("/get/page/card")
    public R<?> getPageParamByCardNo(
            @RequestParam @StringValueCheck String cardNo,
            @RequestParam @BasicValueCheck Integer pageNum, @RequestParam @BasicValueCheck Integer pageSize) {
        return lampParamService.getPageParamByCardNo(cardNo, pageNum, pageSize);
    }

    // 根据卡号取出某个时间段的历史数据，时间格式为"2022-01-01/15"
    @GetMapping("/get/time")
    public R<?> getByTimeInterval(
            @RequestParam @StringValueCheck String cardNo,
            @RequestParam @StringValueCheck String startTime, @RequestParam @StringValueCheck String endTime) {
        return lampParamService.getParamByTimeInterval(cardNo, startTime, endTime);
    }

    // 获取分页电能信息
    @GetMapping("/get/page")
    public R<?> getPageParam(
            @RequestParam @BasicValueCheck Integer pageNum, @RequestParam @BasicValueCheck Integer pageSize) {
        return lampParamService.getPageParam(pageNum, pageSize);
    }

    // 获取一次电能信息
    @PostMapping("/elc/get")
    public R<?> callForElectricData(@RequestBody ControlDto dto) {
        return lampParamService.callForElectricData(dto);
    }

    // 获取一次定位信息
    @PostMapping("/gnss/get")
    public R<?> callForGnssInfo(@RequestBody ControlDto dto) {
        return lampParamService.callForGnssInfo(dto);
    }

    // 自动发送电能信息控制
    @PostMapping("/elc/auto/send")
    public R<?> autoSendElectricData(@RequestBody ElectricAutoSendDto dto) {
        return lampParamService.autoSendElectricData(dto);
    }

    // 电量清零控制
    @PostMapping("/elc/clear")
    public R<?> electricityClear(@RequestBody ControlDto dto) {
        return lampParamService.electricityClear(dto);
    }
}
