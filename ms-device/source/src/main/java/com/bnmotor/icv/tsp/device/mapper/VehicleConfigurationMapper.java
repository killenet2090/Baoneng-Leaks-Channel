package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.tsp.device.model.entity.VehicleConfigurationPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleConfigurationMapper
 * @Description: 配置 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleConfigurationMapper extends BaseMapper<VehicleConfigurationPo> {
    /**
     * 查询所有的车型配置数据
     *
     * @return 所有的配置
     */
    List<VehicleConfigurationPo> selectAll();

    /**
     * 根据年款id 配置集合
     *
     * @param yearIds 年款id
     * @return 配置集合
     */
    List<VehicleConfigurationPo> listByYearIds(@Param("yearIds") List<Long> yearIds);
}
