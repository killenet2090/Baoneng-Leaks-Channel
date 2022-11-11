package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.event.FotaDeviceSyncMessage;
import com.bnmotor.icv.tsp.ota.event.FotaDeviceSyncResult;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceComponentInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceTreeNodeDto;
import com.bnmotor.icv.tsp.ota.model.req.device.VehTagDto;

import java.util.function.Supplier;

/**
 * @ClassName: IFotaDeviceSyncService
 * @Description:    升级同步服务
 * @author: xuxiaochang1
 * @date: 2020/10/27 11:26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IFotaDeviceSyncService {
    /**
     *
     */
    void buildTreeNodeFromTspDevice();

    /**
     * 车辆信息同步
     * @param fotaCarInfoDto
     * @return
     */
    long syncFotaCar(FotaCarInfoDto fotaCarInfoDto, DeviceTreeNodePo deviceTreeNodePo, FotaObjectPo fotaObjectDo);

    /**
     * 固件更新维护车辆固件列表
     * @param fotaDeviceSyncMessage
     * @return
     */
    FotaDeviceSyncResult process4Firmware(FotaDeviceSyncMessage fotaDeviceSyncMessage);

    /**
     * 车型零件信息同步
     * @param fotaDeviceComponentInfoDtoList
     */
    /*void syncFotaDeviceComponentInfo(List<FotaDeviceComponentInfoDto> fotaDeviceComponentInfoDtoList);*/

    /**
     *
     * @param obj
     * @param clz
     * @param tobeSaveSupplier
     * @param <T>
     * @return
     */
    <T> DeviceTreeNodePo getConfLevelDeviceTreeNode(Object obj, Class<T> clz, Supplier<FotaDeviceTreeNodeDto> tobeSaveSupplier);

    /**
     * 获取树节点
     * @param obj
     * @param clz
     * @param <T>
     * @return
     */
    <T> DeviceTreeNodePo getConfLevelDeviceTreeNode(Object obj, Class<T> clz);

    /**
     * 车型零件信息同步
     * @param fotaDeviceComponentInfoDto
     * @param deviceTreeNodePo
     */
    void syncFotaDeviceComponentInfo(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto, DeviceTreeNodePo deviceTreeNodePo, DeviceComponentPo deviceCompnentPo);

    /**
     * 车辆标签信息同步
     * @param vehTagDto
     */
    void syncVehTagInfo(VehTagDto vehTagDto);
}
