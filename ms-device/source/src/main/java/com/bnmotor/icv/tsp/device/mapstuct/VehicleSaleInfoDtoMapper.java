package com.bnmotor.icv.tsp.device.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import com.bnmotor.icv.tsp.device.model.entity.VehicleSaleInfoPo;
import com.bnmotor.icv.tsp.device.model.request.vehBind.VehicleSaleInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: VehicleSaleInfoDtoMapper
 * @Description: 车辆销售信息dto转换
 * @author: huangyun1
 * @date: 2020/5/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface VehicleSaleInfoDtoMapper extends AdamMybatisMapMapper<VehicleSaleInfoPo, VehicleSaleInfoDto> {
    VehicleSaleInfoDtoMapper INSTANCE = Mappers.getMapper(VehicleSaleInfoDtoMapper.class);
}
