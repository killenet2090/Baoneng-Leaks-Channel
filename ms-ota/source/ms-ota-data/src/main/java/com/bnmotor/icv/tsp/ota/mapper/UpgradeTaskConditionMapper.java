/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.mapper;

import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.ota.model.entity.UpgradeTaskConditionPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: UpgradeTaskConditionMapper
*  @Description: 终端升级条件枚举值 dao层
 * @author xuxiaochang1
 * @since 2020-09-09
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface UpgradeTaskConditionMapper extends AdamMapper<UpgradeTaskConditionPo> {
    /**
     * 查询所有任务相关的条件
     * @param otaPlanId
     * @return
     */
    List<UpgradeTaskConditionPo> queryByOtaPlanId(@Param("otaPlanId") Long otaPlanId);

    /**
     * 根据任务Id删除(历史原因，重新定义)
     * @param planId
     * @return
     */
    int deleteByOtaPlanIdPhysical(Long planId);
}
