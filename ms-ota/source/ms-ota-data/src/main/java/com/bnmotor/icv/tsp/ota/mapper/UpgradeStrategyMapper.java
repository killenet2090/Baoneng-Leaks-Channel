package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.ota.model.entity.UpgradeStrategyPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UpgradeStrategyDo
* @Description: 升级策略 dao层
 * @author xxc
 * @since 2020-07-22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface UpgradeStrategyMapper extends AdamMapper<UpgradeStrategyPo> {
    /**
     *
     * @param upgradeFirmwareList
     * @return
     */
    //int insertFirmwareStrategyList(List<UpgradeFirmwareReq> upgradeFirmwareList);

    /**
     * 根据任务Id删除(历史原因，重新定义)
     * @param otaPlanId
     * @return
     */
    int deleteByOtaPlanIdPhysical(Long otaPlanId);
}
