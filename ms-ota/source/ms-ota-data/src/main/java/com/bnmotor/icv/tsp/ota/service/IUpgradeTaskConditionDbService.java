package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.UpgradeTaskConditionPo;

import java.util.List;

/**
 * @ClassName: UpgradeTaskConditionPo
 * @Description: 终端升级条件枚举值 服务类
 * @author xuxiaochang1
 * @since 2020-09-09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IUpgradeTaskConditionDbService extends IService<UpgradeTaskConditionPo> {
    /**
     *
     * @param otaPlanId
     * @return
     */
    List<UpgradeTaskConditionPo> selectList(Long otaPlanId);
}
