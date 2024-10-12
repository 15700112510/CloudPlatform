package com.iot.lamp.service.impl;

import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.StringUtil;
import com.cloud.common.redis.service.RedisService;
import com.github.pagehelper.PageHelper;
import com.iot.lamp.dto.ControlDto;
import com.iot.lamp.dto.ElectricAutoSendDto;
import com.iot.lamp.dto.LampParamDto;
import com.iot.lamp.mapper.LampMapper;
import com.iot.lamp.mapper.LampParamMapper;
import com.iot.lamp.service.LampParamService;
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
 * 路灯参数服务实现类
 *
 * @author cloud
 */
@Service
public class LampParamServiceImpl implements LampParamService {

    private static final Logger logger = LoggerFactory.getLogger(LampParamServiceImpl.class);
    private static final String LAMP_PARAM_PREFIX = "cloud:param:lamp:";
    private static final String DEFAULT_GROUP_NO = "YZDX0001";
    private static final String BROADCAST = "BROADCAST";

    @Autowired
    private LampParamMapper lampParamMapper;
    @Autowired
    private LampMapper lampMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MqttClient mqttClient;

    /**
     * 根据卡号取出最近一条历史数据
     *
     * @param cardNo 单灯卡号
     * @return 统一结果类R
     */
    @Override
    public R<?> getLatestParamByCardNo(String cardNo) {
        // 卡号不存在
        if (lampMapper.getCardNoCount(cardNo) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_CARD_NO);
        }
        // 查询数据库取出并返回
        LampParamDto dto = lampParamMapper.getLatestParamByCardNo(cardNo);
        return R.ok(dto);
    }

    /**
     * 根据卡号分页取出历史数据
     *
     * @param cardNo   单灯卡号
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageParamByCardNo(String cardNo, Integer pageNum, Integer pageSize) {
        // 卡号不存在
        if (lampMapper.getCardNoCount(cardNo) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_CARD_NO);
        }
        // 查看是否有对应缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMP_PARAM_PREFIX + cardNo))) {
            // 若有，则查询缓存并返回
            List<LampParamDto> dtoList = redisService.getCacheList(LAMP_PARAM_PREFIX + cardNo, (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(dtoList);
        }
        // 没有缓存则开始从数据库查询
        PageHelper.startPage(pageNum, pageSize);
        // PageHelper拦截到查询所有并将其转为分页请求
        List<LampParamDto> dtoList = lampParamMapper.getAllParamByCardNo(cardNo);
        // 缓存进redis并设置ttl为三分钟
        // mapper层若查询不到集合会返回空集合而不是null，所以这里只需判断是否为空即可，不用担心空指针问题
        if (!dtoList.isEmpty()) {
            redisService.setCacheList(LAMP_PARAM_PREFIX + cardNo, dtoList);
            redisService.expire(LAMP_PARAM_PREFIX + cardNo, 3, TimeUnit.MINUTES);
        }
        return R.ok(dtoList);
    }

    /**
     * 根据卡号取出某个时间段的历史数据
     *
     * @param cardNo    单灯卡号
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 统一结果类R
     */
    @Override
    public R<?> getParamByTimeInterval(String cardNo, String startTime, String endTime) {
        // 卡号不存在
        if (lampMapper.getCardNoCount(cardNo) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_CARD_NO);
        }
        // 从数据库中查询
        List<LampParamDto> dtoList = lampParamMapper.getParamByTimeInterval(cardNo, startTime, endTime);
        return R.ok(dtoList);
    }

    /**
     * 分页获取电能信息
     *
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageParam(Integer pageNum, Integer pageSize) {
        // 查缓存，若存在缓存则直接返回
        if (Boolean.TRUE.equals(redisService.hasKey(LAMP_PARAM_PREFIX + "info"))) {
            List<LampParamDto> dtoList = redisService.getCacheList(LAMP_PARAM_PREFIX + "info", (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(dtoList);
        }
        // 分页查询数据库并返回
        PageHelper.startPage(pageNum, pageSize);
        List<LampParamDto> dtoList = lampParamMapper.getPageParam();
        // 缓存进redis并设置ttl为三分钟
        if (!dtoList.isEmpty()) {
            redisService.setCacheList(LAMP_PARAM_PREFIX + "info", dtoList);
            redisService.expire(LAMP_PARAM_PREFIX + "info", 3, TimeUnit.MINUTES);
        }
        return R.ok(dtoList);
    }

    /**
     * 改变灯群亮度
     *
     * @param brightness 亮度
     * @param groupNo    群号
     * @return 统一结果R
     */
    @Override
    public R<?> updateGroupBrightness(Integer brightness, String groupNo) {
        // 亮度不符合规则
        if (brightness < 0 || brightness > 100) {
            return R.fail(ErrorEnum.NOT_MATCH_RULES_BRIGHTNESS);
        }
        // 调用mapper
        lampParamMapper.updateGroupBrightness(brightness, groupNo);
        return R.ok();
    }

    @Override
    public R<?> callForElectricData(ControlDto dto) {
        // 取出参数
        Character type = dto.getType();    // 控制类型：0单播1组播
        String cardNo = dto.getCardNo();    // 单灯卡号
        String groupNo = dto.getGroupNo();    // 组播群号
        // 单灯时卡号为空
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 组播时群号为空
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("ElcData,%s%s", "0", "1");
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

    @Override
    public R<?> callForGnssInfo(ControlDto dto) {
        // 取出参数
        Character type = dto.getType();    // 控制类型：0单播1组播
        String cardNo = dto.getCardNo();    // 单灯卡号
        String groupNo = dto.getGroupNo();    // 组播群号
        // 单灯时卡号为空
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 组播时群号为空
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("GnssData,%s%s", "0", "2");
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

    @Override
    public R<?> autoSendElectricData(ElectricAutoSendDto dto) {
        // 取出参数
        Character type = dto.getType();    // 控制类型：0单播1组播
        String cardNo = dto.getCardNo();    // 单灯卡号
        String groupNo = dto.getGroupNo();    // 组播群号
        Character flag = dto.getFlag();    // 自动发送开关：0开1关
        Integer interval = dto.getInterval();    // 自动发送时间间隔
        // 单灯时卡号为空
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 组播时群号为空
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // 间隔时间格式不符合规则
        if (interval <= 0 || interval > 99) {
            return R.fail(ErrorEnum.NOT_MATCH_RULES_INTERVAL);
        }
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("AutoSend,%s%s", flag == '0' ? "01" : "00", interval < 10 ? "0" + interval : interval + "");
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

    @Override
    public R<?> electricityClear(ControlDto dto) {
        // 取出参数
        Character type = dto.getType();    // 控制类型：0单播1组播
        String cardNo = dto.getCardNo();    // 单灯卡号
        String groupNo = dto.getGroupNo();    // 组播群号
        // 单灯时卡号为空
        if (type == '0' && StringUtil.isNullOrEmpty(cardNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
        }
        // 组播时群号为空
        if (type == '1' && StringUtil.isNullOrEmpty(groupNo)) {
            return R.fail(ErrorEnum.IS_EMPTY_GROUP_NO);
        }
        // payload and topic
        String topic = String.format("%s/%s", type == '0' ? DEFAULT_GROUP_NO : groupNo, type == '0' ? cardNo : BROADCAST);
        String payload = String.format("ClearElc,%s%s", "0", "1");
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
