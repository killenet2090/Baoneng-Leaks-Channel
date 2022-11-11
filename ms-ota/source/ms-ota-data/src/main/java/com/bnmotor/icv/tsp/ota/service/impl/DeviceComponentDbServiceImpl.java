package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.DeviceComponentMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceComponentPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaComponentListPo;
import com.bnmotor.icv.tsp.ota.service.IDeviceComponentDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaComponentListDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
public class DeviceComponentDbServiceImpl extends ServiceImpl<DeviceComponentMapper, DeviceComponentPo> implements IDeviceComponentDbService {

    @Autowired
    private IFotaComponentListDbService fotaComponentListDbService;

    @Override
    public List<DeviceComponentPo> getDeviceComponentPosByTreeNodeId(long treeNodeId) {
        List<FotaComponentListPo> fotaComponentListPos = fotaComponentListDbService.listByTreeNodeIds(treeNodeId);
        if(MyCollectionUtil.isNotEmpty(fotaComponentListPos)){
            List<DeviceComponentPo> deviceComponentPos = listByIds(MyCollectionUtil.newCollection(fotaComponentListPos, item -> item.getDeviceComponentId()));
            log.info("deviceComponentPos.size={}", MyCollectionUtil.size(deviceComponentPos));
            return deviceComponentPos;
        }else{
            log.warn("获取零件关系列表异常，请检查是否已经同步添加。treeNodeId={}", treeNodeId);
        }
        return Collections.emptyList();
    }

    @Override
    public DeviceComponentPo findOne(String componentCode, String componentModel) {
        QueryWrapper<DeviceComponentPo> deviceComponentPoQueryWrapper = new QueryWrapper<>();
        deviceComponentPoQueryWrapper.eq("component_code", componentCode);
        deviceComponentPoQueryWrapper.eq("component_model", componentModel);
        return getOne(deviceComponentPoQueryWrapper);
    }
}
