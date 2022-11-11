package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.tsp.device.model.entity.VehicleYearStylePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleYearStyleMapper
 * @Description: 年款 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleYearStyleMapper extends BaseMapper<VehicleYearStylePo> {
    /**
     * 查询所有品牌
     *
     * @return 品牌集合
     */
    List<VehicleYearStylePo> selectAll();

    /**
     * 根据车型id查询所有年款
     *
     * @param modelId 车型id
     * @return 车型年款
     */
    List<VehicleYearStylePo> listByModelId(@Param("modelId") Long modelId);
}
