package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.tsp.device.model.entity.VehicleSeriesPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleSeriesMapper
 * @Description: 车系 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleSeriesMapper extends BaseMapper<VehicleSeriesPo> {
    /**
     * 查询所有车系
     *
     * @return 品牌集合
     */
    List<VehicleSeriesPo> selectAll();
}
