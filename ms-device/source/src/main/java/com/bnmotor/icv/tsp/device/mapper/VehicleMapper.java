package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnmotor.icv.adam.data.mysql.mapper.AdamMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehiclePo;
import com.bnmotor.icv.tsp.device.model.request.vehicle.QueryVehicleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author zhangwei2
 * @ClassName: VehicleMapper
 * @Description: 车辆信息 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleMapper extends AdamMapper<VehiclePo> {
    /**
     * 通过vin码查询车辆信息
     *
     * @param vin 车辆唯一标识
     * @return 车辆信息或返回null
     */
    VehiclePo selectByVin(@Param("vin") String vin);

    /**
     * 通过vin码查询车辆信息
     *
     * @param vins 车辆集合
     * @return 车辆信息或返回null
     */
    List<VehiclePo> selectAllByVins(@Param("vins") Collection<String> vins);

    /**
     * 分页查询车辆信息
     *
     * @param page 分页对象
     * @param dto  查询条件
     * @return 车辆信息对象
     */
//    @DataAccess(joinColumn = {"vin"},table = "tb_user_car")
    IPage<VehiclePo> selectAllByCondition(IPage<?> page, @Param("dto") QueryVehicleDto dto);

    /**
     * 根据orgId查询该org下所有车辆
     */
    List<String> selectAllVinsByOrgId(@Param("orgId") Long orgId);

    /**
     * 判断车辆是否有效(售出并且未报废登记)
     */
    VehiclePo checkValid(@Param("vin") String vin, @Param("vehStatus") Integer vehStatus);

    /**
     * 比较和更新
     */
    int compareBindStatusAndUpdate(VehiclePo vehiclePo);

    /**
     * 根据配置id查询车辆集合
     *
     * @param configId 配置id
     * @param limit    查询条件
     * @return 车辆集合对象
     */
    List<VehiclePo> listByConfigId(@Param("from") Long from, @Param("configId") Long configId, @Param("limit") Integer limit);

    /**
     * 更新车牌号码
     *
     * @return 影响条目
     */
    int updateLicPlate(@Param("vin") String vin, @Param("drivingLicPlate") String drivingLicPlate);

    /**
     * 判断车牌号是否存在
     */
    int existByLicPlate(@Param("drivingLicPlate") String drivingLicPlate);

    /**
     * 根据品牌，车系，车型组合id分页查询车辆信息
     *
     * @param modelIds 车系ids
     * @param fromId   开始条目
     * @param size     每页大小
     * @return 车辆信息
     */
    List<VehiclePo> listByFromId(@Param("createTime") LocalDateTime createTime,
                                 @Param("modelIds") List<Long> modelIds,
                                 @Param("fromId") Long fromId,
                                 @Param("size") Integer size);

    /**
     * 更新认证状态
     * @param vehiclePo
     */
    void compareCertificationStatusAndUpdate(VehiclePo vehiclePo);
}
