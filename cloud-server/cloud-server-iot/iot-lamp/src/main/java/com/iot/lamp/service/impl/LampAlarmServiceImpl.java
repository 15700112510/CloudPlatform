package com.iot.lamp.service.impl;

import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.StringUtil;
import com.cloud.common.redis.service.RedisService;
import com.github.pagehelper.PageHelper;
import com.iot.lamp.dto.AlarmClearDto;
import com.iot.lamp.dto.LampAlarmDto;
import com.iot.lamp.dto.LampAlarmPageDto;
import com.iot.lamp.mapper.LampAlarmMapper;
import com.iot.lamp.mapper.LampMapper;
import com.iot.lamp.service.LampAlarmService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 路灯报警服务实现类
 *
 * @author cloud
 */
@Service
public class LampAlarmServiceImpl implements LampAlarmService {

    private static final String LAMP_ALARM_PREFIX = "cloud:alarm:lamp:";
    private static final Logger logger = LoggerFactory.getLogger("MqttServiceImpl");
    private static final String DEFAULT_GROUP_NO = "YZDX0001";
    private static final String BROADCAST = "BROADCAST";

    @Autowired
    private LampAlarmMapper lampAlarmMapper;
    @Autowired
    private LampMapper lampMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MqttClient mqttClient;

    /**
     * 分页查询报警信息
     *
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @return 统一结果类R
     */
    @Override
    public R<?> getPage(Integer pageNum, Integer pageSize) {
        // 是否有缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMP_ALARM_PREFIX + "page"))) {
            // 若命中则返回
            List<LampAlarmDto> dtoList = redisService.getCacheList(LAMP_ALARM_PREFIX + "page", (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(dtoList);
        }
        // 分页查询数据库并返回
        PageHelper.startPage(pageNum, pageSize);
        List<LampAlarmDto> dtoList = lampAlarmMapper.getPage();
        // 写入缓存并设置ttl为三分钟
        if (!dtoList.isEmpty()) {
            redisService.setCacheList(LAMP_ALARM_PREFIX + "page", dtoList);
            redisService.expire(LAMP_ALARM_PREFIX + "page", 3, TimeUnit.MINUTES);
        }
        return R.ok(dtoList);
    }

    /**
     * 根据卡号分页查询报警信息
     *
     * @param cardNo   单灯卡号
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageByCardNo(String cardNo, Integer pageNum, Integer pageSize) {
        // 卡号不存在
        if (lampMapper.getCardNoCount(cardNo) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_CARD_NO);
        }
        // 是否有缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMP_ALARM_PREFIX + "page:" + cardNo))) {
            // 若命中则返回
            List<LampAlarmDto> dtoList = redisService.getCacheList(LAMP_ALARM_PREFIX + "page:" + cardNo, (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(dtoList);
        }
        // 分页查询并返回
        PageHelper.startPage(pageNum, pageSize);
        List<LampAlarmDto> dtoList = lampAlarmMapper.getPageByCardNo(cardNo);
        // 写入缓存并设置ttl为三分钟
        if (!dtoList.isEmpty()) {
            redisService.setCacheList(LAMP_ALARM_PREFIX + "page:" + cardNo, dtoList);
            redisService.expire(LAMP_ALARM_PREFIX + "page:" + cardNo, 3, TimeUnit.MINUTES);
        }
        return R.ok(dtoList);
    }

    /**
     * 根据某个时间段分页查询某个卡号的报警信息
     *
     * @param dto 时间段查询所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageByCardNoAndTime(LampAlarmPageDto dto) {
        // 取出参数
        String cardNo = dto.getCardNo();
        Integer pageNum = dto.getPageNum();
        Integer pageSize = dto.getPageSize();
        // 卡号不存在
        if (lampMapper.getCardNoCount(cardNo) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_CARD_NO);
        }
        // 开始分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<LampAlarmDto> dtoList = lampAlarmMapper.getPageByCardNoAndTime(dto);
        return R.ok(dtoList);
    }

    /**
     * 分页获取修复信息
     *
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageRepair(Integer pageNum, Integer pageSize) {
        // 查询缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMP_ALARM_PREFIX + "page:repair"))) {
            List<LampAlarmDto> dtoList = redisService.getCacheList(LAMP_ALARM_PREFIX + "page:repair", (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(dtoList);
        }
        // 开始从数据库分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<LampAlarmDto> dtoList = lampAlarmMapper.getPageRepair();
        // 缓存进redis并设置ttl为三分钟
        if (!dtoList.isEmpty()) {
            redisService.setCacheList(LAMP_ALARM_PREFIX + "page:repair", dtoList);
            redisService.expire(LAMP_ALARM_PREFIX + "page:repair", 3, TimeUnit.MINUTES);
        }
        return R.ok(dtoList);
    }

    /**
     * 获取总报警的数量
     * 同类型报警不计数
     *
     * @return 统一结果类R
     */
    @Override
    public R<?> getAlarmNum() {
        return R.ok(lampAlarmMapper.getAlarmNum());
    }

    @Override
    public R<?> alarmClear(AlarmClearDto dto) {
        // get parameters
        Character type = dto.getType();
        String cardNo = dto.getCardNo();
        String groupNo = dto.getGroupNo();
        String userId = dto.getUserId();
        // cardNo is null when type is single control
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // groupNo is null when type is group control
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("ClearWarning,%s%s,%s", "0", "1", userId);
        // 发布控制消息
        try {
            mqttClient.publish(topic, new MqttMessage(payload.getBytes(StandardCharsets.UTF_8)));
            // 发布成功，返回ok
            return R.ok();
        } catch (Exception e) {
            // 出现异常发布失败，返回发布失败消息
            logger.error("消息发往MQTT服务器失败");
            return R.fail(ErrorEnum.FAIL_PUBLISH);
        }
    }
}
