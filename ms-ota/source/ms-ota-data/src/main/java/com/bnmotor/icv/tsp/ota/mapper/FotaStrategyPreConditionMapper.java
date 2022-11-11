package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.tsp.ota.model.entity.FotaStrategyPreConditionPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: FotaStrategyPreConditionPo
* @Description: 升级策略前置条件表 dao层
 * @author xuxiaochang1
 * @since 2020-12-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface FotaStrategyPreConditionMapper extends BaseMapper<FotaStrategyPreConditionPo> {
    /**
     * 根据策略Id物理删除
     * @param otaStrategyId
     */
    void delByStrategyIdPhysical(@Param("otaStrategyId") Long otaStrategyId);
}
