package com.bnmotor.icv.tsp.device.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import com.bnmotor.icv.tsp.device.model.request.vehBind.VehicleUnbindDto;
import com.bnmotor.icv.tsp.device.model.request.feign.UserVehicleUnbindDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: VehicleUserUnbindDtoMapper
 * @Description: 用户车辆解绑dto转换
 * @author: huangyun1
 * @date: 2020/5/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface VehicleUserUnbindDtoMapper extends AdamMybatisMapMapper<VehicleUnbindDto, UserVehicleUnbindDto> {
    VehicleUserUnbindDtoMapper INSTANCE = Mappers.getMapper(VehicleUserUnbindDtoMapper.class);
}
