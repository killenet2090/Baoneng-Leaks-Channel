package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.UpgradeStrategyPo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: IUpgradeStrategyService
 * @Description: 升级策略 服务类
 * @author xxc
 * @since 2020-07-22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IUpgradeStrategyDbService extends IService<UpgradeStrategyPo> {

    /**
     *
     * @param otaPlanId
     * @return
     */
    List<UpgradeStrategyPo> listByOtaPlanId(Long otaPlanId);

    /**
     *
     * @param planId
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    UpgradeStrategyPo findOne(Long planId, Long firmwareId, Long firmwareVersionId);
}
