package com.iot.device.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cloud.api.clients.IotGatewayClient;
import com.cloud.api.clients.LampClient;
import com.cloud.api.model.Device;
import com.cloud.api.model.GatewayInfo;
import com.cloud.api.model.Lamp;
import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.StringUtil;
import com.cloud.common.redis.service.RedisService;
import com.github.pagehelper.PageHelper;
import com.iot.device.dto.AddDeviceDto;
import com.iot.device.dto.LampDeviceDto;
import com.iot.device.mapper.DeviceMapper;
import com.iot.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 设备服务实现类
 *
 * @author cloud
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private static final String DEVICE_PREFIX = "cloud:device:";
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private LampClient lampClient;
    @Autowired
    private IotGatewayClient iotGatewayClient;
    @Autowired
    private RedisService redisService;

    /**
     * 添加设备
     *
     * @param dto 泛型设备对象
     * @return 统一结果类R
     */
    @Override
    public R<?> addDevice(AddDeviceDto dto) {
        // 取出参数
        Long lamppostId = dto.getLamppostId();
        Character type = dto.getType();
        String opsBy = dto.getOpsBy();
        String details = dto.getDetails();
        // 所挂载的灯杆是否存在
        if (lamppostId != null && deviceMapper.lamppostExistCount(lamppostId) == 0) {
            return R.fail(ErrorEnum.NOT_EXIST_LAMPPOST);
        }
        // 判断添加设备的类型
        switch (type) {
            case 'L':
                Lamp newLamp = JSON.parseObject(details, Lamp.class);
                String cardNo = newLamp.getCardNo();
                // 卡号为空
                if (StringUtil.isNullOrEmpty(cardNo)) {
                    return R.fail(ErrorEnum.IS_EMPTY_CARD_NO);
                }
                // 卡号已存在
                int count = lampClient.getCardNoCount(cardNo);
                if (count == -1) {
                    return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
                } else if (count > 0) {
                    return R.fail(ErrorEnum.ALREADY_EXIST_CARD_NO);
                } else if (count == 0) {
                    // 不存在，则继续添加该设备
                    Device device = new Device(lamppostId, 'L', opsBy, opsBy);
                    Long deviceId = invokeAddDevice(device);
                    newLamp.setDeviceId(deviceId);
                    int result = lampClient.addLamp(newLamp, opsBy);
                    if (result == -1) {
                        return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
                    }

                    // 订阅相关主题
                    boolean subscribed = lampClient.subscribe(cardNo);
                    if (!subscribed) {
                        return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
                    }
                    return R.ok();
                }
            case 'C':
                return R.ok();
            case 'G':
                GatewayInfo newGatewayInfo = JSON.parseObject(details, GatewayInfo.class);
                String serialNum = newGatewayInfo.getSerialNum();
                // 序列号为空
                if (StringUtil.isNullOrEmpty(serialNum)) {
                    return R.fail(ErrorEnum.IS_EMPTY_SERIAL_NUM);
                }
                int num = iotGatewayClient.getSerialNum(serialNum);
                if (num == -1) {
                    return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
                }
                if (num == 1) {
                    return R.fail(ErrorEnum.ALREADY_EXIST_SERIAL_NUM);
                }

                // 开始添加网关设备
                Device device = new Device(lamppostId, 'G', opsBy, opsBy);
                Long deviceId = invokeAddDevice(device);
                newGatewayInfo.setDeviceId(deviceId);
                int result = iotGatewayClient.addGateway(newGatewayInfo);
                if (result == -1) {
                    return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
                }

                // 订阅相关主题
                boolean subscribed = iotGatewayClient.subscribe(serialNum);
                if (!subscribed) {
                    return R.fail(ErrorEnum.REMOTE_CALL_EXCEPTION);
                }
                return R.ok();
            // 其他待扩展
            default:
                // 传入的类型参数不匹配
                return R.fail(ErrorEnum.TYPE_NOT_MATCH);
        }
    }

    private Long invokeAddDevice(Device device) {
        Long deviceId;
        synchronized (this) {
            deviceMapper.addDevice(device);
            deviceId = deviceMapper.queryLatestDeviceId();
        }
        return deviceId;
    }

    /**
     * 获取灯杆上某类型设备
     *
     * @param type       类型
     * @param lamppostId 灯杆id
     * @return 统一结果R
     */
    @Override
    public R<?> getDevice(Character type, Long lamppostId) {
        // 若有缓存数据
        if (Boolean.TRUE.equals(redisService.hasKey(DEVICE_PREFIX + lamppostId + ":" + type))) {
            // 查缓存并返回
            String device = redisService.getCacheObject(DEVICE_PREFIX + lamppostId + ":" + type);
            return R.ok(JSON.parse(device));
        }
        // 根据所需类型执行
        switch (type) {
            // 照明灯设备类型
            case 'L':
                // 数据库查询
                LampDeviceDto dto = deviceMapper.getLamp(type, lamppostId);
                // 写入redis并设ttl为三分钟
                if (Objects.nonNull(dto)) {
                    redisService.setCacheObject(DEVICE_PREFIX + lamppostId + ":" + type, JSON.toJSONString(dto), 3, TimeUnit.MINUTES);
                }
                return R.ok(dto);
            // 其他待扩展
            default:
                // 传入的类型参数不匹配
                return R.fail(ErrorEnum.TYPE_NOT_MATCH);
        }
    }

    @Override
    public R<?> getPageLamps(int page, int limit, String wrapper) {
        // 查询条件为空才缓存，否则不缓存
        if (wrapper.length() > 0) {
            // 模糊搜索条件构造
            wrapper = "%" + wrapper + "%";
            // 分页查询并返回
            PageHelper.startPage(page, limit);
            LampDeviceDto dto = deviceMapper.getPageLamps(wrapper);
            return R.ok(dto);
        }
        // 若有缓存数据
        if (Boolean.TRUE.equals(redisService.hasKey(DEVICE_PREFIX + ":Lamps"))) {
            // 查缓存并返回
            String lamps = redisService.getCacheObject(DEVICE_PREFIX + ":Lamps");
            return R.ok(JSON.parse(lamps));
        }
        // 分页查询并返回
        PageHelper.startPage(page, limit);
        LampDeviceDto dto = deviceMapper.getPageLamps(wrapper);
        // 缓存数据3分钟
        redisService.setCacheObject(DEVICE_PREFIX + ":Lamps", dto, 3, TimeUnit.MINUTES);
        return R.ok(dto);
    }

    /**
     * 获取某类型设备的总数
     *
     * @param type 类型
     * @return 统一结果R
     */
    @Override
    public R<?> getTotalNumByType(Character type) {
        // 若有缓存数据
        if (Boolean.TRUE.equals(redisService.hasKey(DEVICE_PREFIX + type + ":num"))) {
            // 查缓存并返回
            String device = redisService.getCacheObject(DEVICE_PREFIX + type + ":num");
            System.out.println(device);
            return R.ok(JSON.parse(device));
        }
        // 根据类型执行
        switch (type) {
            case 'L':
                // 数据库查询
                int num = lampClient.getTotalNum();
                if (num == -1) {
                    return R.fail(ErrorEnum.SERVICE_FALLBACK);
                }
                // 写入redis并设ttl为三分钟
                redisService.setCacheObject(DEVICE_PREFIX + type + ":num", String.valueOf(num));
                return R.ok(num);
            // 其他待扩展
            default:
                // 传入的类型参数不匹配
                return R.fail(ErrorEnum.TYPE_NOT_MATCH);
        }
    }
}
