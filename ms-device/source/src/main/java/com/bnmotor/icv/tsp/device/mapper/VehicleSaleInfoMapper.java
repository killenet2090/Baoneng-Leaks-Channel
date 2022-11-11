package com.bnmotor.icv.tsp.device.mapper;

import com.bnmotor.icv.tsp.device.model.entity.VehicleSaleInfoPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangwei2
 * @ClassName: VehicleSaleInfoMapper
 * @Description: 车辆销售信息 dao层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Mapper
public interface VehicleSaleInfoMapper extends BaseMapper<VehicleSaleInfoPo> {
    /**
     * 根据vin查询车辆销售信息
     *
     * @param vin 车辆vin码
     * @return 车辆销售信息
     */
    VehicleSaleInfoPo selectByVin(@Param("vin") String vin);
}
