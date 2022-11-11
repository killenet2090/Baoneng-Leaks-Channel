package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.DeviceInfoSyncMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.bnmotor.icv.tsp.ota.model.resp.DeviceComponentVo;
import com.bnmotor.icv.tsp.ota.service.IDeviceComponentDbService;
import com.bnmotor.icv.tsp.ota.service.IDeviceComponentService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DeviceComponentPo
 * @Description: OTA升级硬件设备信息库 服务实现类
 * @author xuxiaochang1
 * @since 2020-11-05
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class DeviceComponentServiceImpl implements IDeviceComponentService {

    @Autowired
    private IDeviceComponentDbService deviceComponentDbService;

    @Override
    public List<DeviceComponentVo> getDeviceComponentVosByTreeNodeId(long treeNodeId) {
        List<DeviceComponentPo> deviceComponentPos = deviceComponentDbService.getDeviceComponentPosByTreeNodeId(treeNodeId);
        return MyCollectionUtil.newCollection(deviceComponentPos, item -> {
            DeviceComponentVo deviceComponentVo = DeviceInfoSyncMapper.INSTANCE.deviceComponentPo2DeviceComponentVo(item);
            deviceComponentVo.setDisplayName(item.getComponentName() + "("+ item.getComponentCode() +")");
            return deviceComponentVo;
        });
    }
}
