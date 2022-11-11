package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaStrategyFirmwareListMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyFirmwareListPo;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyFirmwareListDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: FotaStrategyFirmwareListPo
 * @Description: OTA升级策略-升级ecu固件列表 服务实现类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaStrategyFirmwareListDbServiceImpl extends ServiceImpl<FotaStrategyFirmwareListMapper, FotaStrategyFirmwareListPo> implements IFotaStrategyFirmwareListDbService {

    @Override
    public int delByStrategyIdsPhysical(List<Long> ids) {
        AtomicInteger result = new AtomicInteger(0);
        Optional.ofNullable(ids).ifPresentOrElse(item -> result.set(getBaseMapper().delByIdsPhysical(item)), ()-> log.warn("参数id集合未空"));
        return result.get();
    }

    @Override
    public int delByStrategyIdPhysical(Long strategyId) {
        AtomicInteger result = new AtomicInteger(0);
        Optional.ofNullable(strategyId).ifPresentOrElse(item -> result.set(getBaseMapper().delByStrategyIdPhysical(item)), ()-> log.warn("策略id参数未空"));
        return result.get();
    }

    @Override
    public int deleteByOtaStrategyId(Long otaStrategyId) {
        QueryWrapper<FotaStrategyFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_strategy_id", otaStrategyId);
        return getBaseMapper().delete(queryWrapper);
    }

    @Override
    public List<FotaStrategyFirmwareListPo> listByOtaStrategyId(Long otaStrategyId) {
        QueryWrapper<FotaStrategyFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_strategy_id", otaStrategyId);
        List<FotaStrategyFirmwareListPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return MyCollectionUtil.safeList(list);
    }

    @Override
    public List<FotaStrategyFirmwareListPo> list(Long firmwareId, Long firmwareVersionId) {
        QueryWrapper<FotaStrategyFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("firmware_id", firmwareId);
        queryWrapper.eq("target_version_id", firmwareVersionId);
        List<FotaStrategyFirmwareListPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return MyCollectionUtil.safeList(list);
    }
}
