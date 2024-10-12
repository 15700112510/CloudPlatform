package com.iot.screen.service.impl;

import com.hidisp.api.cloud.CommandManager;
import com.hidisp.api.cloud.ProgramManager;
import com.hidisp.api.cloud.ScreenManager;
import com.hidisp.api.cloud.models.CommandResult;
import com.hidisp.api.cloud.models.ScreenInfo;
import com.hidisp.api.cloud.models.SendResult;
import com.hidisp.api.cloud.models.SendTask;
import com.iot.screen.properties.AdvScreenProperties;
import com.iot.screen.service.AdvPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告显示屏实现类
 *
 * @author cloud
 */
@Service
public class AdvPlayerServiceImpl implements AdvPlayerService {

    @Autowired
    private AdvScreenProperties properties;

    /**
     * 获取全部显示屏
     *
     * @return 显示屏信息集合
     */
    @Override
    public List<ScreenInfo> getScreens() {
        List<ScreenInfo> screenInfos;
        screenInfos = ScreenManager.getInstance().getScreens(properties.getApiUrl(), properties.getAppKey());
        return screenInfos;
    }

    /**
     * 设置屏幕开关
     *
     * @param status 开关状态
     * @return 指令执行结果
     */
    @Override
    public CommandResult setPower(String status) {
        return CommandManager.getInstance().setPower(properties.getApiUrl(), properties.getAppKey(), properties.getScreens(), status, null);
    }

    /**
     * 设置屏幕亮度
     *
     * @param light 亮度值
     * @return 指令执行结果
     */
    @Override
    public CommandResult setLight(Integer light) {
        return CommandManager.getInstance().setLight(properties.getApiUrl(), properties.getAppKey(), properties.getScreens(), light, null);
    }

    /**
     * 设置屏幕当前时间
     *
     * @return 指令执行结果
     */
    @Override
    public CommandResult setTime() {
        return CommandManager.getInstance().setTime(properties.getApiUrl(), properties.getAppKey(), properties.getScreens(), null, null);
    }

    /**
     * 投放广告
     *
     * @param multipartFile 广告文件对象
     * @return 投放广告结果
     */
    @Override
    public SendResult sendProgram(MultipartFile multipartFile) {
        SendResult result = new SendResult();
        try {
            File file = AdvFileServiceImpl.copyMultipartToFile(multipartFile);
            Map<String, File[]> data = new HashMap<>();
            // 单文件投放，也可投放多个广告文件
            data.put("0", new File[]{file});
            // 开始发送节目
            result = ProgramManager.getInstance().sendProgram(properties.getApiUrl(), properties.getAppKey(), properties.getScreens(), data, new SendTask());
        } catch (Exception e) {
            // 发送失败
            result.setSuccess(false);
            result.setResult(1);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
