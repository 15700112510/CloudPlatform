package com.iot.screen.service;

import com.hidisp.api.cloud.models.CommandResult;
import com.hidisp.api.cloud.models.ScreenInfo;
import com.hidisp.api.cloud.models.SendResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvPlayerService {
    List<ScreenInfo> getScreens();

    CommandResult setPower(String status);

    CommandResult setLight(Integer light);

    CommandResult setTime();

    SendResult sendProgram(MultipartFile multipartFile) throws Exception;
}
