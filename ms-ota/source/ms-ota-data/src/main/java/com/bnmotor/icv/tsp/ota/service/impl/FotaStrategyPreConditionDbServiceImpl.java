package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaStrategyPreConditionMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPreConditionPo;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyPreConditionDbService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FotaStrategyPreConditionPo
 * @Description: 升级策略前置条件表 服务实现类
 * @author xuxiaochang1
 * @since 2020-12-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
public class FotaStrategyPreConditionDbServiceImpl extends ServiceImpl<FotaStrategyPreConditionMapper, FotaStrategyPreConditionPo> implements IFotaStrategyPreConditionDbService {

    @Override
    public void delByStrategyIdPhysical(Long otaStrategyId) {
        getBaseMapper().delByStrategyIdPhysical(otaStrategyId);
    }

    @Override
    public List<FotaStrategyPreConditionPo> listByOtaStrategyId(Long otaStrategyId) {
        QueryWrapper<FotaStrategyPreConditionPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_strategy_id", otaStrategyId);
        return list(queryWrapper);
    }
}
