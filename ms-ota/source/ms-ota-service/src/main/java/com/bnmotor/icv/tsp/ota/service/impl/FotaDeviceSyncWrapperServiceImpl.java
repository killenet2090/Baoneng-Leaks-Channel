package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaCarInfoDto;
import com.bnmotor.icv.tsp.ota.model.req.device.FotaDeviceComponentInfoDto;
import com.bnmotor.icv.tsp.ota.service.IDeviceComponentDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncService;
import com.bnmotor.icv.tsp.ota.service.IFotaDeviceSyncWrapperService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaDeviceSyncServiceImpl
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/10/27 11:40
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class FotaDeviceSyncWrapperServiceImpl implements IFotaDeviceSyncWrapperService {

    @Autowired
    @Qualifier("fotaDeviceSyncServiceV2")
    private IFotaDeviceSyncService fotaDeviceSyncService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IDeviceComponentDbService deviceComponentDbService;

    @Autowired
    private FotaDeviceSyncInner fotaDeviceSyncInner;

    @Override
    public void syncFotaCar(FotaCarInfoDto fotaCarInfoDto, DeviceTreeNodePo deviceTreeNodePo) {
        FotaObjectPo existFotaObjectDo = fotaObjectDbService.findByVin(fotaCarInfoDto.getVin());
        Runnable r = () -> {
            fotaDeviceSyncService.syncFotaCar(fotaCarInfoDto, deviceTreeNodePo, existFotaObjectDo);
        };
        String lockName = "OTA_DEVICE_SYNC_OBJECT_" + fotaCarInfoDto.getVin();
        log.info("lockName={}", lockName);
        log.info("--- 开始锁定操作 start ------");
        //此处加锁操作
        fotaDeviceSyncInner.handle(lockName, r);
        log.info("--- 开始锁定操作 end ------");
    }

    /**
     * 获取零件元数据
     * @param componentCode
     * @param componentModel
     * @return
     */
    private DeviceComponentPo getDeviceComponentPo(String componentCode, String componentModel){
        DeviceComponentPo deviceCompnentPo = deviceComponentDbService.findOne(componentCode, componentModel);
        return deviceCompnentPo;
    }

    @Override
    public void syncFotaDeviceComponentInfo(FotaDeviceComponentInfoDto fotaDeviceComponentInfoDto, DeviceTreeNodePo deviceTreeNodePo) {
        DeviceComponentPo deviceCompnentPo = getDeviceComponentPo(fotaDeviceComponentInfoDto.getComponentCode(), fotaDeviceComponentInfoDto.getComponentModel());
        Runnable r = () -> {
            fotaDeviceSyncService.syncFotaDeviceComponentInfo(fotaDeviceComponentInfoDto, deviceTreeNodePo, deviceCompnentPo);
        };
        String lockName = "OTA_DEVICE_SYNC_COMPONENT_" + fotaDeviceComponentInfoDto.getComponentType() + "_" + fotaDeviceComponentInfoDto.getComponentModel();
        log.info("lockName={}", lockName);
        log.info("--- 开始锁定操作 start ------");
        //此处加锁操作
        fotaDeviceSyncInner.handle(lockName, r);
        log.info("--- 开始锁定操作 end ------");
    }
}
