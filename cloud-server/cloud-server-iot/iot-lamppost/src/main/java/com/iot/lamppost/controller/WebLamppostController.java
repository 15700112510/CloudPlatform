package com.iot.lamppost.controller;

import com.cloud.common.core.R;
import com.cloud.common.security.annotation.RequiredOrigin;
import com.cloud.common.security.annotation.RequiredRoles;
import com.iot.lamppost.dto.AddLamppostDto;
import com.iot.lamppost.dto.LamppostNameDto;
import com.iot.lamppost.service.LamppostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lamppost")
@RequiredOrigin("gateway")
public class WebLamppostController {

    @Autowired
    private LamppostService lamppostService;

    // 添加灯杆
    @PostMapping("/add")
    @RequiredRoles("1")
    public R<?> addLamppost(@RequestBody AddLamppostDto dto) {
        return lamppostService.addLamppost(dto);
    }

    // 查询所有灯杆
    @GetMapping("/get/all")
    public R<?> getAllLamppost() {
        return lamppostService.getAllLamppost();
    }

    // 分页查询灯杆
    @GetMapping("/get/page")
    public R<?> getPageLamppost(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String wrapper) {
        return lamppostService.getPageLamppost(pageNum, pageSize, wrapper);
    }

    // 修改灯杆名称
    @PutMapping("/update/name")
    @RequiredRoles("1")
    public R<?> editLamppostName(@RequestBody LamppostNameDto dto) {
        return lamppostService.editLamppostName(dto);
    }

    // 查询灯杆总数量
    @GetMapping("/get/num")
    public R<?> queryLamppostNum() {
        return lamppostService.queryLamppostNum();
    }

    // 根据灯杆id查询卡号
    @GetMapping("/get/card/{lamppostId}")
    public R<?> queryCardNoByLamppostId(@PathVariable Long lamppostId) {
        return lamppostService.queryCardNoByLamppostId(lamppostId);
    }

    // 根据卡号查询所挂载的灯杆信息
    @GetMapping("/get/lamppost/{cardNo}")
    public R<?> queryLamppostInfoByCardNo(@PathVariable String cardNo) {
        return lamppostService.queryLamppostInfoByCardId(cardNo);
    }
}
