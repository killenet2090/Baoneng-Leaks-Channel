package com.bnmotor.icv.tsp.ota.service;

/**
 * @ClassName: FotaStrategyFirmwareListPo
 * @Description: OTA升级策略-升级ecu固件列表 服务类
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Deprecated
public interface IFotaStrategyFirmwareListService{
    /**
     * 物理删除所有策略与ECU关系
     * @param ids
     * @return
     */
    //int delByStrategyIdsPhysical(List<Long> ids);

    /**
     * 物理删除所有策略与ECU关系
     * @param strategyId
     * @return
     */
    //int delByStrategyIdPhysical(Long strategyId);
}
