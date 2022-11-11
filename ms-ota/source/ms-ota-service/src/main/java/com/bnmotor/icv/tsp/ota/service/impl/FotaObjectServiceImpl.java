package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.resp.app.TboxUpgradStatusVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.CommonUtil;
import com.bnmotor.icv.tsp.ota.util.MyBusinessUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.UUIDShort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName: FotaObjectServiceImpl
 * @Description: OTA升级对象指需要升级的一个完整对象，在车联网中指一辆车通常拿车的vin作为升级的ID服务实现类
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaObjectServiceImpl implements IFotaObjectService {
    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IFotaFirmwareListDbService fotaFirmwareListDbService;

    @Override
    public TboxUpgradStatusVo getFotaUpgradeStatus(String vin) {
        Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = getTaskObjStatusTypeEnum(vin);
        return TboxUpgradStatusVo.builder().status(taskObjStatusTypeEnum.getType()).desc(taskObjStatusTypeEnum.getDesc()).build();
    }

    @Override
    public Enums.TaskObjStatusTypeEnum getTaskObjStatusTypeEnum(String vin) {
        //TempPlanObjListWrapper tempPlanObjWrapper = getTempPlanObjWrapper(vin);
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);

        if(Objects.isNull(fotaVinCacheInfo)){
            log.warn("[获取升级车辆信息]异常.vin={}", vin);
            return Enums.TaskObjStatusTypeEnum.NO_VERSION;
        }

        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaVinCacheInfo.getOtaPlanId());
        /*
            新版本状态为=无新版本
            1、不存在升级任务
            2、升级任务过期/没启用
         */
        if(Objects.isNull(fotaPlanPo)){
            log.warn("[获取升级任务信息]异常.vin={}", vin);
            return Enums.TaskObjStatusTypeEnum.NO_VERSION;
        }
        boolean validPlanPo = MyBusinessUtil.validPlanPo(fotaPlanPo);
        if(!validPlanPo){
            log.warn("[升级任务无效].vin={}, fotaPlanPo={}", vin, fotaPlanPo);
            return Enums.TaskObjStatusTypeEnum.NO_VERSION;
        }

        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVinCacheInfo.getOtaPlanObjectId());
        if(Objects.isNull(fotaPlanObjListPo)){
            log.warn("[获取升级车辆信息]异常.fotaPlanObjListPo={}", fotaPlanObjListPo.toString());
            return Enums.TaskObjStatusTypeEnum.NO_VERSION;
        }

        Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = Enums.TaskObjStatusTypeEnum.getByType(fotaPlanObjListPo.getStatus());
        //如果状态异常，默认没有新版本
        if(Objects.isNull(taskObjStatusTypeEnum)){
            log.warn("[获取车辆升级任务状态]异常.fotaPlanObjListPo={}", fotaPlanObjListPo.toString());
            taskObjStatusTypeEnum = Enums.TaskObjStatusTypeEnum.NO_VERSION;
        }
        return taskObjStatusTypeEnum;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process4AddFotaFirmware(FotaObjectPo fotaObjectPo, FotaFirmwarePo fotaFirmwarePo) {
        //获取可能已经存在的车辆升级固件列表
        final List<FotaFirmwareListPo> existFotaFirmwareListDos = fotaFirmwareListDbService.listAllByObjectId(fotaObjectPo.getId());
        if (MyCollectionUtil.isNotEmpty(existFotaFirmwareListDos)) {
            FotaFirmwareListPo exist = existFotaFirmwareListDos.stream().filter(item1 -> item1.getFirmwareId().equals(fotaFirmwarePo.getId())).findFirst().orElse(null);
            if (Objects.nonNull(exist)) {
                log.info("不需要添加.firmwareId={}", fotaFirmwarePo.getId());
                return;
            }
        }

        //TODO 该部分逻辑需要做成事务处理
        FotaFirmwareListPo fotaFirmwareListDo = new FotaFirmwareListPo();
        fotaFirmwareListDo.setId(IdWorker.getId());
        fotaFirmwareListDo.setOtaObjectId(fotaObjectPo.getId());
        fotaFirmwareListDo.setProjectId(fotaObjectPo.getProjectId());
        fotaFirmwareListDo.setFirmwareId(fotaFirmwarePo.getId());
        fotaFirmwareListDo.setComponentId(fotaFirmwarePo.getComponentCode());
        CommonUtil.wrapBasePo(fotaFirmwareListDo, true);
        log.info("添加车辆该固件列表信息。vin={}, fotaObject={}", fotaObjectPo.getObjectId(), fotaObjectPo);
        boolean save = fotaFirmwareListDbService.save(fotaFirmwareListDo);
        log.info("保存车辆固件信息结果.vin={}, save={}", fotaObjectPo.getObjectId(), save);

        updateCofVersion(fotaObjectPo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void process4DelFotaFirmware(Long otaObjectId, Long firmwareId) {
        //如果是删除固件
        fotaFirmwareListDbService.remove(otaObjectId, firmwareId);
        updateCofVersion(otaObjectId);
    }

    @Override
    public void process4UpdateFotaFirmware(FotaObjectPo fotaObjectPo, FotaFirmwarePo fotaFirmwarePo) {
        FotaFirmwareListPo fotaFirmwareListPo = new FotaFirmwareListPo();
        fotaFirmwareListPo.setId(IdWorker.getId());
        fotaFirmwareListPo.setOtaObjectId(fotaObjectPo.getId());
        fotaFirmwareListPo.setProjectId(fotaObjectPo.getProjectId());
        fotaFirmwareListPo.setFirmwareId(fotaFirmwarePo.getId());
        fotaFirmwareListPo.setComponentId(fotaFirmwarePo.getComponentCode());
        CommonUtil.wrapBasePo(fotaFirmwareListPo, false);
        log.info("更新车辆该固件列表信息。vin={}, fotaObject={}", fotaObjectPo.getObjectId(), fotaObjectPo);
        boolean update = fotaFirmwareListDbService.updateById(fotaFirmwareListPo);
        log.info("更新车辆固件信息结果.vin={}, save={}", fotaObjectPo.getObjectId(), update);

        updateCofVersion(fotaObjectPo.getId());
    }

    //更新版本配置信息
    private void updateCofVersion(Long id){
        FotaObjectPo newFotaObjectPo = new FotaObjectPo();
        newFotaObjectPo.setId(id);
        newFotaObjectPo.setConfVersion(UUIDShort.generate());
        log.info("设置升级对象新版本号：newFotaObjectPo={}", newFotaObjectPo);
        CommonUtil.wrapBasePo4Update(newFotaObjectPo, CommonConstant.USER_ID_SYSTEM, null);
        fotaObjectDbService.updateById(newFotaObjectPo);

        fotaObjectCacheInfoService.setFotaObjectCacheInfo(fotaObjectDbService.getById(id));
    }
}
