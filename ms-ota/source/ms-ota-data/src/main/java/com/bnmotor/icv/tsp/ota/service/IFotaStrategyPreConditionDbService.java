package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPreConditionPo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: FotaStrategyPreConditionPo
 * @Description: 升级策略前置条件表 服务类
 * @author xuxiaochang1
 * @since 2020-12-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaStrategyPreConditionDbService extends IService<FotaStrategyPreConditionPo> {
    /**
     * 根据策略Id物理删除
     * @param otaStrategyId
     */
    void delByStrategyIdPhysical(Long otaStrategyId);

    /**
     *
     * @param otaStrategyId
     * @return
     */
    List<FotaStrategyPreConditionPo> listByOtaStrategyId(Long otaStrategyId);
}
