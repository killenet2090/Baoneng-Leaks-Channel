package com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceComponentInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceTreeNodeDto;
import com.bnmotor.icv.tsp.ota.model.resp.DeviceComponentVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName: DeviceInfoSyncMapper
 * @Description: 设备信息同步转换类
 * @author: xuxiaochang1
 * @date: 2020/7/27 17:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Mapper
public interface DeviceInfoSyncMapper {
    DeviceInfoSyncMapper INSTANCE = Mappers.getMapper(DeviceInfoSyncMapper.class);

    /**
     * 零件信息到树节点信息
     * @param fotaDeviceComponentInfoDto
     * @return
     */
    FotaDeviceTreeNodeDto fotaDeviceComponentInfoDto2FotaDeviceTreeNodeDto(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto);

    /**
     * 汽车信息到树节点信息
     * @param fotaCarInfoDto
     * @return
     */
    FotaDeviceTreeNodeDto fotaCarInfoDto2FotaDeviceTreeNodeDto(FotaCarInfoDto fotaCarInfoDto);

    /**
     * FotaDeviceComponentInfoDto --> DeviceComponentPo
     * @param fotaDeviceComponentInfoDto
     * @return
     */
    DeviceComponentPo fotaDeviceComponentInfoDto2DeviceComponentPo(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto);

    /**
     * DeviceComponentPo --> DeviceComponentVo
     * @param deviceComponentPo
     * @return
     */
    DeviceComponentVo deviceComponentPo2DeviceComponentVo(DeviceComponentPo deviceComponentPo);
}
