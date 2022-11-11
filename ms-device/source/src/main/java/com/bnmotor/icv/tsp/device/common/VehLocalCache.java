package com.bnmotor.icv.tsp.device.common;

import com.bnmotor.icv.tsp.device.mapper.*;
import com.bnmotor.icv.tsp.device.model.entity.*;
import com.bnmotor.icv.tsp.device.model.response.vehicle.OrgLocalCacheVo;
import com.bnmotor.icv.tsp.device.service.mq.consumer.model.VehicleUpdate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: VehModelLocalCache
 * @Description: 车型配置本地缓存, 程序启动后加载数据库车型相关信息到本地缓存, 当车型相关信息修改时, 通过消费消息更改本地缓存
 * @author: zhangwei2
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class VehLocalCache {
    @Resource
    private VehicleOrgRelationMapper relationMapper;
    @Resource
    private DeviceTypeMapper typeMapper;
    @Resource
    private VehicleModelMapper modelMapper;

    /**
     * 每批最大查询条目
     */
    private static final Integer LIMIT = 1000;

    /**
     * 起始条目
     */
    private static final Long FROM = 0L;

    /**
     * 型号唯一标识->组织关系
     */
    private final Map<String, OrgLocalCacheVo> uniqKeyToOrgRelation = new HashMap<>();

    /**
     * 设备类型名称->设备类型
     */
    private final Map<String, DeviceTypePo> typeNameToDeviceType = new HashMap<>();
    /**
     * 设备类型id->设备类型
     */
    private final Map<Integer, DeviceTypePo> typeIdToDeviceType = new HashMap<>();

    /**
     * 组织id->组织对象
     */
    private final Map<Long, OrgLocalCacheVo> orgIdToOrg = new HashMap<>();

    /**
     * 车型Id->车型
     */
    private final Map<Long, VehicleModelPo> modelIdToModel = new HashMap<>();


    @PostConstruct

    public void init() {
        List<VehicleOrgRelationPo> relations = relationMapper.listByFromId(FROM, LIMIT);
        if (CollectionUtils.isNotEmpty(relations)) {
            while (CollectionUtils.isNotEmpty(relations)) {
                for (VehicleOrgRelationPo relationPo : relations) {
                    toCacheOrgRelation(relationPo);
                }
                relations = relationMapper.listByFromId(relations.get(relations.size() - 1).getId(), LIMIT);
            }
        }

        List<VehicleModelPo> models = modelMapper.listByFromId(FROM, LIMIT);
        if (CollectionUtils.isNotEmpty(models)) {
            while (CollectionUtils.isNotEmpty(models)) {
                for (VehicleModelPo modelPo : models) {
                    modelIdToModel.put(modelPo.getId(), modelPo);
                }
                models = modelMapper.listByFromId(models.get(models.size() - 1).getId(), LIMIT);
            }
        }

        List<DeviceTypePo> typePos = typeMapper.selectAll();
        if (CollectionUtils.isNotEmpty(typePos)) {
            typeNameToDeviceType.putAll(typePos.stream().collect(Collectors.toMap(DeviceTypePo::getTypeName, o -> o)));
            typeIdToDeviceType.putAll(typePos.stream().collect(Collectors.toMap(DeviceTypePo::getDeviceType, o -> o)));
        }
    }

    /**
     * 缓存车型配置
     */
    private void toCacheOrgRelation(VehicleOrgRelationPo relationPo) {
        if (relationPo == null) {
            return;
        }

        OrgLocalCacheVo cacheVo = new OrgLocalCacheVo();
        BeanUtils.copyProperties(relationPo, cacheVo);
        String sb = relationPo.getVehModelName() + relationPo.getYearStyleName() +
                relationPo.getConfigName();
        uniqKeyToOrgRelation.put(sb, cacheVo);
        orgIdToOrg.put(relationPo.getId(), cacheVo);
    }

    /**
     * 从缓存移除车型配置
     */
    private void removeCacheOrgRelation(VehicleOrgRelationPo relationPo) {
        if (relationPo == null) {
            return;
        }
        String sb = relationPo.getVehModelName() + relationPo.getYearStyleName() + relationPo.getConfigName();
        uniqKeyToOrgRelation.remove(sb);
        orgIdToOrg.remove(relationPo.getId());
    }

    /**
     * 从缓存中获取车型配置信息
     */
    public OrgLocalCacheVo getVehOrgFromCache(String modelName, String styleName, String configName) {
        StringBuilder sb = new StringBuilder().append(modelName).append(styleName).append(configName);
        OrgLocalCacheVo cacheVo = uniqKeyToOrgRelation.get(sb.toString());
        if (cacheVo != null) {
            return cacheVo;
        }

        VehicleOrgRelationPo relationPo = relationMapper.selectByModelName(modelName, styleName, configName);
        toCacheOrgRelation(relationPo);
        return uniqKeyToOrgRelation.get(sb.toString());
    }

    /**
     * 从缓存中获取设备类型
     */
    public Integer getDeviceTypeId(String typeName) {
        DeviceTypePo typePo = typeNameToDeviceType.get(typeName);
        if (typePo != null) {
            return typePo.getDeviceType();
        }

        typePo = typeMapper.selectByTypeName(typeName);
        if (typePo != null) {
            typeNameToDeviceType.put(typeName, typePo);
            return typePo.getDeviceType();
        }
        return null;
    }

    /**
     * 从缓存中获取设备类型名称
     */
    public String getDeviceTypeName(Integer deviceType) {
        DeviceTypePo typePo = typeIdToDeviceType.get(deviceType);
        if (typePo != null) {
            return typePo.getTypeName();
        }

        typePo = typeMapper.selectByDeviceType(deviceType);
        if (typePo != null) {
            typeIdToDeviceType.put(deviceType, typePo);
            return typePo.getTypeName();
        }
        return null;
    }

    /**
     * 根据orgId快速获取本地缓存中的车型配置关系
     *
     * @param id 车型配置orgId
     * @return 对象
     */
    public OrgLocalCacheVo getOrgById(Long id) {
        OrgLocalCacheVo cacheVo = orgIdToOrg.get(id);
        if (cacheVo != null) {
            return cacheVo;
        }

        VehicleOrgRelationPo relationPo = relationMapper.selectById(id);
        toCacheOrgRelation(relationPo);
        return orgIdToOrg.get(id);
    }

    /**
     * 根据车型id车型数据
     *
     * @param id 车型id
     * @return 车型数据
     */
    public VehicleModelPo getModel(Long id) {
        VehicleModelPo modelPo = modelIdToModel.get(id);
        if (modelPo != null) {
            return modelPo;
        }

        modelPo = modelMapper.selectById(id);
        if (modelPo != null) {
            modelIdToModel.put(id, modelPo);
        }
        return modelPo;
    }

    /**
     * 根据消息处理缓存数据
     *
     * @param vehicleUpdate 更新消息
     */
    public void process(VehicleUpdate vehicleUpdate) {
        Integer type = vehicleUpdate.getType();
        switch (type) {
            case 1:
                Integer action = vehicleUpdate.getAction();
                VehicleOrgRelationPo relationPo = relationMapper.selectById(vehicleUpdate.getOrgId());
                if (action == 1) {
                    toCacheOrgRelation(relationPo);
                } else {
                    removeCacheOrgRelation(relationPo);
                }
                break;
            case 2:
            default:
                break;
        }
    }
}
