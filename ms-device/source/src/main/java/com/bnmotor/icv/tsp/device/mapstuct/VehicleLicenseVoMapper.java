package com.bnmotor.icv.tsp.device.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleLicensePo;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleLicenseVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: DrivingLicenseVoMapper
 * @Description: 行驶证信息vo转换
 * @author: huangyun1
 * @date: 2020/5/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface VehicleLicenseVoMapper extends AdamMybatisMapMapper<VehicleLicensePo, VehicleLicenseVo> {
    VehicleLicenseVoMapper INSTANCE = Mappers.getMapper(VehicleLicenseVoMapper.class);
}
