package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.mapper.FotaFirmwareListMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareListDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xxc
 * @ClassName: FotaFirmwareListPo
 * @Description: OTA升级固件清单
 * 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-06
 */

@Service
@Slf4j
public class FotaFirmwareListDbServiceImpl extends ServiceImpl<FotaFirmwareListMapper, FotaFirmwareListPo> implements IFotaFirmwareListDbService {
    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeService;

    @Autowired
    private IFotaObjectDbService fotaObjectService;

    @Autowired
    private IFotaFirmwareDbService fotaFirmwareService;

    @Override
    public List<FotaFirmwareListPo> listAllByObjectId(long otaObjectId) {
        MyAssertUtil.isTrue(otaObjectId > 0, OTARespCodeEnum.DATA_NOT_FOUND);
        QueryWrapper<FotaFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_object_id", otaObjectId);
        queryWrapper.orderByDesc("create_time");
        List<FotaFirmwareListPo> fotaFirmwareListPos = list(queryWrapper);
        return fotaFirmwareListPos;
    }

    @Override
    public void initWithObjectId(Long otaObjectId) {
        FotaObjectPo fotaObjectPo = fotaObjectService.getById(otaObjectId);
        MyAssertUtil.notNull(fotaObjectPo, OTARespCodeEnum.DATA_NOT_FOUND);
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeService.getById(fotaObjectPo.getTreeNodeId());
        MyAssertUtil.notNull(deviceTreeNodePo, OTARespCodeEnum.DATA_NOT_FOUND);

        String projectId = deviceTreeNodePo.getProjectId();
        List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareService.listFirmwareDos(fotaObjectPo.getTreeNodeId());
        if (MyCollectionUtil.isEmpty(fotaFirmwarePos)) {
            log.warn("no fotaFirmwarePos find.deviceTreeNodePo={}", deviceTreeNodePo.toString());
            return;
        }

        List<FotaFirmwareListPo> fotaFirmwareListPos = Lists.newArrayList();
        for (FotaFirmwarePo fotaFirmwarePo : fotaFirmwarePos) {
            FotaFirmwareListPo fotaFirmwareListPo = new FotaFirmwareListPo();
            fotaFirmwareListPo.setId(IdWorker.getId());
            fotaFirmwareListPo.setComponentId(fotaFirmwarePo.getComponentCode());
            fotaFirmwareListPo.setFirmwareId(fotaFirmwarePo.getId());
            fotaFirmwareListPo.setOtaObjectId(otaObjectId);
            fotaFirmwareListPo.setProjectId(projectId);
            fotaFirmwareListPo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
            fotaFirmwareListPos.add(fotaFirmwareListPo);
        }
        saveBatch(fotaFirmwareListPos);
    }

    @Override
    public List<FotaFirmwareListPo> getByOtaObjIdWithFirmwareId(Long otaObjectId, Collection<Long> firmwareIds) {
        if(Objects.isNull(otaObjectId) || MyCollectionUtil.isEmpty(firmwareIds)){
            log.warn("参数异常.otaPlanObjectId={}, firmwareIds={}", otaObjectId, firmwareIds);
            return Collections.emptyList();
        }
        QueryWrapper<FotaFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_object_id", otaObjectId);
        queryWrapper.in("firmware_id",firmwareIds);
        List<FotaFirmwareListPo> fotaFirmwareListPos = list(queryWrapper);
        return fotaFirmwareListPos;
    }

    @Override
    public boolean remove(Long otaObjectId, Long firmwareId) {
        //如果是删除固件
        QueryWrapper<FotaFirmwareListPo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("ota_object_id", otaObjectId);
        queryWrapper.eq("firmware_id", firmwareId);
        return remove(queryWrapper);
    }

    @Override
    public void delByOtaObjIdWithFirmwareId(Long otaObjectId, Collection<Long> firmwareListIds) {
        if(Objects.isNull(otaObjectId) || MyCollectionUtil.isEmpty(firmwareListIds)){
            log.warn("参数异常.otaObjectId={}, firmwareListIds={}", otaObjectId, firmwareListIds);
            return;
        }
        QueryWrapper<FotaFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_object_id", otaObjectId);
        queryWrapper.in("firmware_id",firmwareListIds);
        remove(queryWrapper);
        return;
    }

    @Override
    public FotaFirmwareListPo findOne(Long otaObjectId, Long firmwareId) {
        if(Objects.isNull(otaObjectId) || Objects.isNull(firmwareId)){
            log.warn("参数异常.otaObjectId={}, firmwareId={}", otaObjectId, firmwareId);
            return null;
        }
        QueryWrapper<FotaFirmwareListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ota_object_id", otaObjectId);
        queryWrapper.eq("firmware_id",firmwareId);
        return getOne(queryWrapper);
    }
}
