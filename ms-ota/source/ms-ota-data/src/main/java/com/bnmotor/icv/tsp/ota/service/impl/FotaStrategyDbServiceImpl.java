package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaStrategyMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyFirmwareListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPo;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyFirmwareListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaStrategyDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: FotaStrategyPo
 * @Description: OTA升级策略-新表 服务实现类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaStrategyDbServiceImpl extends ServiceImpl<FotaStrategyMapper, FotaStrategyPo> implements IFotaStrategyDbService {
    @Autowired
    IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delFotaStrategy(Long id) {
        getBaseMapper().deleteById(id);
        QueryWrapper<FotaStrategyFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_strategy_id", id);
        fotaStrategyFirmwareListService.getBaseMapper().delete(queryWrapper);
    }

    @Override
    public List<FotaStrategyPo> listByColumnValue(String columnName, Object columnValue) {
        QueryWrapper<FotaStrategyPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(columnName, columnValue);
        List<FotaStrategyPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return MyCollectionUtil.safeList(list);
    }
}
