package com.iot.lamppost.service.impl;

import com.alibaba.fastjson2.JSON;
import com.cloud.common.core.R;
import com.cloud.common.core.enums.ErrorEnum;
import com.cloud.common.core.utils.StringUtil;
import com.cloud.common.redis.service.RedisService;
import com.github.pagehelper.PageHelper;
import com.iot.lamppost.domain.Lamppost;
import com.iot.lamppost.dto.AddLamppostDto;
import com.iot.lamppost.dto.LamppostInfoDto;
import com.iot.lamppost.dto.LamppostNameDto;
import com.iot.lamppost.mapper.LamppostMapper;
import com.iot.lamppost.service.LamppostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 灯杆服务实现类
 *
 * @author cloud
 */
@Service
public class LamppostServiceImpl implements LamppostService {

    private final static int LAMPPOST_NAME_MAX_LENGTH = 8;
    private final static String LAMPPOST_PREFIX = "cloud:lamppost";

    @Autowired
    private LamppostMapper lamppostMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 添加灯杆
     *
     * @param dto 添加灯杆所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> addLamppost(AddLamppostDto dto) {
        // 取出灯杆基本参数
        String lamppostName = dto.getLamppostName();
        String groupNo = dto.getGroupNo();
        String opsBy = dto.getOpsBy();
        // 灯杆名称过长
        if (lamppostName.length() > LAMPPOST_NAME_MAX_LENGTH) {
            return R.fail(ErrorEnum.BEYOND_MAX_LENGTH_LAMPPOST_NAME);
        }
        // 同一个灯群不能有同名灯杆
        if (lamppostMapper.queryLamppostByNameAndGroup(lamppostName, groupNo) != 0) {
            return R.fail(ErrorEnum.ALREADY_EXIST_NAME);
        }
        // 灯杆信息装载进实体
        Lamppost lamppost = new Lamppost(lamppostName, groupNo, opsBy, opsBy);
        // 插入数据库
        lamppostMapper.addLamppost(lamppost);
        // 更新灯杆列表缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX))) {
            Lamppost latestLamppost = lamppostMapper.getByNameAndGroup(lamppostName, groupNo);
            // 不能往redis中插入null对象或空集合
            if (latestLamppost != null) {
                redisService.setCacheObject(LAMPPOST_PREFIX, latestLamppost);
                redisService.expire(LAMPPOST_PREFIX, 3, TimeUnit.MINUTES);
            }
        }
        // 删除总数量缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX + ":num"))) {
            redisService.deleteObject(LAMPPOST_PREFIX + ":num");
        }
        return R.ok();
    }

    /**
     * 查询所有灯杆
     *
     * @return 统一结果类R
     */
    @Override
    public R<?> getAllLamppost() {
        // 查看redis是否有缓存数据
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX))) {
            // 若有则返回
            List<Lamppost> allLamppost = redisService.getCacheListAll(LAMPPOST_PREFIX);
            return R.ok(allLamppost);
        }
        // 若没有，从数据库分页查询
        List<Lamppost> lamppostList = lamppostMapper.getAllLamppost();
        // 存入redis
        if (!lamppostList.isEmpty()) {
            redisService.setCacheList(LAMPPOST_PREFIX, lamppostList);
            redisService.expire(LAMPPOST_PREFIX, 3, TimeUnit.MINUTES);
        }
        return R.ok(lamppostList);
    }

    /**
     * 分页模糊查询灯杆
     *
     * @param pageNum  分页页码
     * @param pageSize 页大小
     * @param wrapper  模糊查询条件
     * @return 统一结果类R
     */
    @Override
    public R<?> getPageLamppost(Integer pageNum, Integer pageSize, String wrapper) {
        // 查询条件为空才缓存，否则不缓存
        if (wrapper.length() > 0) {
            wrapper = "%" + wrapper + "%";
            // 开始分页
            PageHelper.startPage(pageNum, pageSize);
            // PageHelper拦截到查询所有并将其转为分页请求
            List<Lamppost> lamppostList = lamppostMapper.getPageLamppost(wrapper);
            return R.ok(lamppostList);
        }
        // 查看redis是否有缓存数据
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX))) {
            // 若有则返回
            List<Lamppost> pageLamppost = redisService.getCacheList(LAMPPOST_PREFIX, (long) (pageNum - 1) * pageSize, (long) pageNum * pageSize - 1);
            return R.ok(pageLamppost);
        }
        // 若没有，从数据库分页查询
        PageHelper.startPage(pageNum, pageSize);
        // PageHelper拦截到查询所有并将其转为分页请求
        List<Lamppost> lamppostList = lamppostMapper.getPageLamppost(wrapper);
        // 将所有灯杆数据写入缓存
        if (lamppostList.size() > 0) {
            redisService.setCacheList(LAMPPOST_PREFIX, lamppostList);
            redisService.expire(LAMPPOST_PREFIX, 3, TimeUnit.MINUTES);
        }
        return R.ok(lamppostList);
    }

    /**
     * 修改灯杆名称
     *
     * @param dto 修改灯杆名所需参数
     * @return 统一结果类R
     */
    @Override
    public R<?> editLamppostName(LamppostNameDto dto) {
        // 取出参数
        String lamppostName = dto.getLamppostName();

        // 灯杆名称过长
        if (lamppostName.length() > LAMPPOST_NAME_MAX_LENGTH) {
            return R.fail(ErrorEnum.BEYOND_MAX_LENGTH_LAMPPOST_NAME);
        }
        lamppostMapper.editLamppostName(dto);
        // 删除缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX))) {
            redisService.deleteObject(LAMPPOST_PREFIX);
        }
        return R.ok();
    }

    /**
     * 查询灯杆总数量
     *
     * @return 统一结果类R
     */
    @Override
    public R<?> queryLamppostNum() {
        // 是否有缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX + ":num"))) {
            // 查到缓存则返回
            return R.ok(redisService.getCacheObject(LAMPPOST_PREFIX + ":num"));
        }
        // 未命中缓存
        int num = lamppostMapper.queryLamppostNum();
        // 缓存进redis且设置三分钟ttl
        redisService.setCacheObject(LAMPPOST_PREFIX + ":num", Integer.toString(num), 3, TimeUnit.MINUTES);
        // 返回总数量
        return R.ok(num);
    }

    /**
     * 根据灯杆id获取挂载灯
     *
     * @param lamppostId 灯杆id
     * @return 统一结果类R
     */
    @Override
    public R<?> queryCardNoByLamppostId(Long lamppostId) {
        // 是否有缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX + ":card:" + lamppostId))) {
            // 查到缓存则返回
            return R.ok(redisService.getCacheObject(LAMPPOST_PREFIX + ":card:" + lamppostId));
        }
        // 未命中缓存
        String cardNo = lamppostMapper.queryCardNoByLamppostId(lamppostId);
        // 缓存进redis且设置三分钟ttl
        if (!StringUtil.isNullOrEmpty(cardNo)) {
            redisService.setCacheObject(LAMPPOST_PREFIX + ":card:" + lamppostId, cardNo, 3, TimeUnit.MINUTES);
        }
        // 返回总数量
        return R.ok(cardNo);
    }

    /**
     * 根据卡号查询所挂载的灯杆信息
     *
     * @param cardNo 单灯卡号
     * @return 统一结果类R
     */
    @Override
    public R<?> queryLamppostInfoByCardId(String cardNo) {
        // 是否有缓存
        if (Boolean.TRUE.equals(redisService.hasKey(LAMPPOST_PREFIX + ":of:" + cardNo))) {
            // 查到缓存则返回
            return R.ok(redisService.getCacheObject(LAMPPOST_PREFIX + ":of:" + cardNo));
        }
        // 未命中缓存
        LamppostInfoDto dto = lamppostMapper.queryLamppostInfoByCardNo(cardNo);
        // 缓存进redis且设置三分钟ttl
        if (Objects.nonNull(dto)) {
            redisService.setCacheObject(LAMPPOST_PREFIX + ":of:" + cardNo, JSON.toJSONString(dto), 3, TimeUnit.MINUTES);
        }
        // 返回灯杆信息
        return R.ok(dto);
    }
}