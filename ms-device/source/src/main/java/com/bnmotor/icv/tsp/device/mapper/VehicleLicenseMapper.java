package com.bnmotor.icv.tsp.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleLicensePo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: TspDrivingLicensePo
* @Description: 行驶证信息 dao层
 * @author huangyun1
 * @since 2020-09-24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Mapper
public interface VehicleLicenseMapper extends BaseMapper<VehicleLicensePo> {
    /**
     * 根据用户id删除行驶证
     */
    int deleteVehicleLicenseByUserId(VehicleLicensePo vehicleLicensePo);
}
