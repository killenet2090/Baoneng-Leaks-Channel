package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.tsp.device.model.entity.VehicleOrgRelationPo;
import com.bnmotor.icv.tsp.device.model.request.vehModel.VehModelListQueryDto;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehModelConfigVo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehModelStatisticsVo;
import com.bnmotor.icv.tsp.device.model.response.vehModel.VehicleModelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleOrgRelationMapper
 * @Description: dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-23
 */
@Mapper
public interface VehicleOrgRelationMapper extends BaseMapper<VehicleOrgRelationPo> {

    /**
     * 通过modelId查询车辆信息
     *
     * @param modelCode 车型编码
     * @return 车辆信息列表或空集合
     */
    List<VehModelConfigVo> selectByModelCode(@Param("modelCode") String modelCode, @Param("vehType") Integer vehType);

    /**
     * 分页查询车辆信息
     *
     * @param page    分页对象
     * @param vehType 车辆动力类型
     * @return 车辆信息对象
     */
    IPage<VehModelStatisticsVo> selectAllStatistics(IPage<?> page, @Param("vehType") Integer vehType, @Param("searchKey") String searchKey);
    
    List<VehicleOrgRelationPo> selectAll();

    /**
     * 根据id批量获取组织层级
     */
    List<VehicleOrgRelationPo> listByFromId(@Param("fromId") Long fromId, @Param("limit") Integer limit);

    /**
     * 分页查询某个车型下面配置
     *
     * @param modelId 车型id
     * @return 列表
     */
    List<VehicleOrgRelationPo> listConfigByModelId(@Param("modelId") Long modelId,
                                                   @Param("yearId") Long yearId,
                                                   @Param("configId") Long configId,
                                                   @Param("fromConfigId") Long fromConfigId,
                                                   @Param("limit") Integer limit);
    /**
     * 统计车型下配置个数
     *
     * @param modelId 车型id
     * @return 配置个数
     */
    Integer countConfigByModelId(@Param("modelId") Long modelId, @Param("yearId") Long yearId);

    /**
     * 根据车型名称，年款名称，配置名称查询车辆信息
     *
     * @param modelName  车型名称
     * @param yearName   年款名称
     * @param configName 配置名称
     * @return 配置
     */
    VehicleOrgRelationPo selectByModelName(@Param("modelName") String modelName,
                                           @Param("yearName") String yearName,
                                           @Param("configName") String configName);

    List<VehicleModelVo> getVehModelList(@Param("dto")VehModelListQueryDto dto);
}
