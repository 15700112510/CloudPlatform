package com.iot.lamp.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.StringValueCheck;
import com.iot.lamp.dto.AlarmClearDto;
import com.iot.lamp.dto.LampAlarmPageDto;
import com.iot.lamp.service.LampAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lamp/alarm")
@RequiredOrigin("gateway")
public class WebLampAlarmController {

    @Autowired
    private LampAlarmService lampAlarmService;

    // 分页查询报警信息
    @GetMapping("/page")
    public R<?> getPage(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return lampAlarmService.getPage(pageNum, pageSize);
    }

    // 根据卡号分页查询报警信息
    @GetMapping("/page/card")
    public R<?> getPageByCardNo(
            @RequestParam("cardNo") @StringValueCheck String cardNo,
            @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return lampAlarmService.getPageByCardNo(cardNo, pageNum, pageSize);
    }

    // 根据某个时间段分页查询某个卡号的报警信息
    @PostMapping("/page/time")
    public R<?> getPageByCardNoAndTime(@RequestBody LampAlarmPageDto dto) {
        return lampAlarmService.getPageByCardNoAndTime(dto);
    }

    // 分页获取修复信息
    @GetMapping("/page/repair")
    public R<?> getPageRepair(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        return lampAlarmService.getPageRepair(pageNum, pageSize);
    }

    // 获取总报警灯数
    @GetMapping("/num")
    public R<?> getAlarmNum() {
        return lampAlarmService.getAlarmNum();
    }

    // 报警清除控制
    @PostMapping("/alarm/clear")
    public R<?> alarmClear(@RequestBody AlarmClearDto dto) {
        return lampAlarmService.alarmClear(dto);
    }
}
