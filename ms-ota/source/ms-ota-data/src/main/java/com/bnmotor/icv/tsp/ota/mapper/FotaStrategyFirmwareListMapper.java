package com.bnmotor.icv.tsp.ota.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyFirmwareListPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: FotaStrategyFirmwareListPo
* @Description: OTA升级策略-升级ecu固件列表 dao层
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaStrategyFirmwareListMapper extends BaseMapper<FotaStrategyFirmwareListPo> {
    /**
     * 物理删除所有策略与ECU关系
     * @param ids
     * @return
     */
    int delByIdsPhysical(List<Long> ids);

    /**
     * 物理删除所有策略与ECU关系
     * @param strategyid
     * @return
     */
    int delByStrategyIdPhysical(@Param("otaStrategyId") Long strategyid);
}
