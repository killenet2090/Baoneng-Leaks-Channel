package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class FotaFirmwareListServiceImpl implements IFotaFirmwareListService {
    @Autowired
    private IFotaFirmwareListDbService fotaFirmwareListDbService;

    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IFotaFirmwareService fotaFirmwareService;

    /*@Override
    public List<FotaFirmwareListPo> listAllByObjectId(long otaObjectId) {
        return fotaFirmwareListDbService.listAllByObjectId(otaObjectId);
    }*/

    @Override
    public void initWithObjectId(Long otaObjectId) {
        FotaObjectPo fotaObjectPo = fotaObjectDbService.getById(otaObjectId);
        MyAssertUtil.notNull(fotaObjectPo, OTARespCodeEnum.DATA_NOT_FOUND);
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(fotaObjectPo.getTreeNodeId());
        MyAssertUtil.notNull(deviceTreeNodePo, OTARespCodeEnum.DATA_NOT_FOUND);

        String projectId = deviceTreeNodePo.getProjectId();
        List<FotaFirmwarePo> fotaFirmwarePos = fotaFirmwareService.listFirmwarePos(fotaObjectPo.getTreeNodeId());
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
        fotaFirmwareListDbService.saveBatch(fotaFirmwareListPos);
    }

    /*@Override
    public List<FotaFirmwareListPo> getByOtaObjIdWithFirmwareId(Long otaPlanObjectId, List<Long> firmwareIds) {
        return fotaFirmwareListDbService.getByOtaObjIdWithFirmwareId(otaPlanObjectId, firmwareIds);
    }*/
}
