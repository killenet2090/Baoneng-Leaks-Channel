package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnmotor.icv.tsp.ota.mapper.UpgradeTaskConditionMapper;
import com.bnmotor.icv.tsp.ota.model.entity.UpgradeTaskConditionPo;
import com.bnmotor.icv.tsp.ota.service.IUpgradeTaskConditionDbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName: UpgradeTaskConditionPo
 * @Description: 终端升级条件枚举值 服务实现类
 * @author xuxiaochang1
 * @since 2020-09-09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class UpgradeTaskConditionDbServiceImpl extends ServiceImpl<UpgradeTaskConditionMapper, UpgradeTaskConditionPo> implements IUpgradeTaskConditionDbService {
    @Override
    public List<UpgradeTaskConditionPo> selectList(Long otaPlanId) {
        if(0 > otaPlanId){
            log.warn("获取任务前置条件列表参数不合法.otaPlanId={}", otaPlanId);
            return Collections.emptyList();
        }
        QueryWrapper<UpgradeTaskConditionPo> upgradeTaskConditionQueryWrapper = new QueryWrapper<>();
        upgradeTaskConditionQueryWrapper.eq("ota_plan_id", otaPlanId);
        List<UpgradeTaskConditionPo> upgradeTaskConditionPos = getBaseMapper().selectList(upgradeTaskConditionQueryWrapper);
        if(MyCollectionUtil.isEmpty(upgradeTaskConditionPos)){
            log.warn("获取任务前置条件列表为空.otaPlanId={}", otaPlanId);
            return Collections.emptyList();
        }
        log.info("获取任务前置条件列表长度.size={}", upgradeTaskConditionPos.size());
        return upgradeTaskConditionPos;
    }
}
