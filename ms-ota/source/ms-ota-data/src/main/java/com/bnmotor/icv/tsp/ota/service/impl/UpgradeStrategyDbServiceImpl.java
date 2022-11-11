package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnmotor.icv.tsp.ota.model.entity.UpgradeStrategyPo;
import com.bnmotor.icv.tsp.ota.mapper.UpgradeStrategyMapper;
import com.bnmotor.icv.tsp.ota.service.IUpgradeStrategyDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: UpgradeStrategyDo
 * @Description: 升级策略 服务实现类
 * @author xxc
 * @since 2020-07-22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class UpgradeStrategyDbServiceImpl extends ServiceImpl<UpgradeStrategyMapper, UpgradeStrategyPo> implements IUpgradeStrategyDbService {

    @Override
    public List<UpgradeStrategyPo> listByOtaPlanId(Long otaPlanId) {
        QueryWrapper<UpgradeStrategyPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_plan_id", otaPlanId);
        List<UpgradeStrategyPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public UpgradeStrategyPo findOne(Long planId, Long firmwareId, Long firmwareVersionId) {
        QueryWrapper<UpgradeStrategyPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_plan_id", planId);
        queryWrapper.eq("firmware_id", firmwareId);
        queryWrapper.eq("firmware_version_id", firmwareVersionId);
        queryWrapper.orderByDesc("create_time");
        List<UpgradeStrategyPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return MyCollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }
}
