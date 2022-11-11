package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceComponentInfoDto;

/**
 * @ClassName: IFotaDeviceSyncWrapperService
 * @Description:    升级同步服务
 * @author: xuxiaochang1
 * @date: 2020/10/27 11:26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IFotaDeviceSyncWrapperService {
    /**
     * 车辆信息同步
     * @param fotaCarInfoDto
     */
    void syncFotaCar(FotaCarInfoDto fotaCarInfoDto, DeviceTreeNodePo deviceTreeNodePo);

    /**
     * 车型零件信息同步
     * @param fotaDeviceComponentInfoDto
     */
    void syncFotaDeviceComponentInfo(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto, DeviceTreeNodePo deviceTreeNodePo);
}
