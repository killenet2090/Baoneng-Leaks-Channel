package com.bnmotor.icv.tsp.device.mapstuct;

import com.bnmotor.icv.adam.data.mysql.mapstruct.AdamMybatisMapMapper;
import com.bnmotor.icv.tsp.device.model.entity.DeviceReplacementPo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleDevicePo;
import com.bnmotor.icv.tsp.device.model.response.device.BindVehicleRecordVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: DeviceModelVoMapper
 * @Description:
 * @author: zhangwei2
 * @date: 2020/8/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Mapper
public interface ReplacementRecordVoMapper extends AdamMybatisMapMapper<DeviceReplacementPo, BindVehicleRecordVo> {
    ReplacementRecordVoMapper INSTANCE = Mappers.getMapper(ReplacementRecordVoMapper.class);
}
