package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleConfigCommonPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleConfigCommonMapper
 * @Description: 配置颜色 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleConfigCommonMapper extends BaseMapper<VehicleConfigCommonPo> {
    /**
     * 根据配置id查询车型配置通用参数
     */
    List<VehicleConfigCommonPo> listByConfigIds(@Param("configIds") List<Long> configIds);

    /**
     * 获取车型通用配置
     */
    VehicleConfigCommonPo getByConfigIdAndColorName(@Param("configId") Long configId, @Param("colorName") String colorName);
}
