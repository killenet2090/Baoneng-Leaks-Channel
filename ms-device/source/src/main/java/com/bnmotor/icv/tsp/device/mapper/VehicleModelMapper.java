package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleModelPo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehConfigDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleModelMapper
 * @Description: 车型 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleModelMapper extends BaseMapper<VehicleModelPo> {
    /**
     * 查询全量模型数据或者根据seriesId查询该车系下全部数据
     *
     * @param seriesId 车系
     * @return 型号集合
     */
    List<VehicleModelPo> listBySeriesId(Long seriesId);

    /**
     * 查询所有车型
     *
     * @return 品牌集合
     */
    List<VehicleModelPo> selectAll();

    /**
     * 根据配置ID获取型号详情
     */
    VehConfigDetailVo getVehicleModelDetailByConfigId(@Param("configId") Long configId);

    /**
     * 分页查询车型信息
     */
    List<VehicleModelPo> listByFromId(@Param("fromId") Long id, @Param("limit") Integer limit);
}
