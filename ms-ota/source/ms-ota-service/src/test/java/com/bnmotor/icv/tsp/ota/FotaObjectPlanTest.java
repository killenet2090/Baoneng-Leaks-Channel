package com.bnmotor.icv.tsp.ota;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.req.FotaPlanReq;
import com.bnmotor.icv.tsp.ota.model.req.TaskUpgradeConditionReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeFirmwareReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeTaskObjectReq;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

/**
 * @ClassName: FotaObjectPlanTest
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/7/15 11:38
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@SpringBootTest
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class FotaObjectPlanTest {
    @Autowired
    private IFotaPlanService fotaPlanService;
    @Autowired
    private IFotaObjectService fotaObjectService;
    @Autowired
    private IFotaObjectDbService fotaObjectDbService;
    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;
    @Autowired
    private IFotaFirmwareService fotaFirmwareService;
    @Autowired
    private IFotaFirmwareListService fotaFirmwareListService;

    private final Long treeNodeId = 1278883235621863426L;

    @Test
    public void taskTest() {
        FotaPlanReq fotaPlanReq = new FotaPlanReq();
        fotaPlanReq.setProjectId(CommonConstant.PROJECT_ID_DEFAULT);
        fotaPlanReq.setDisclaimer("test-aaaaaaaaaaaa");
        fotaPlanReq.setDownloadTips("test-bbbbbbbbbbbbbbbb");
        fotaPlanReq.setId(IdWorker.getId());
        fotaPlanReq.setMaxDownloadFailed(3);
        fotaPlanReq.setMaxInstallFailed(3);
        fotaPlanReq.setMaxVerifyFailed(3);
        Date now = new Date();
        fotaPlanReq.setStartTime(DateUtils.addDays(now, -3));
        fotaPlanReq.setFinishTime(DateUtils.addDays(now, 3));
        fotaPlanReq.setName("我们的任务包");
        fotaPlanReq.setNewVersionTips("test-ccccccccccccc");
        fotaPlanReq.setTaskTips("test-ddddddddddddddddd");
        fotaPlanReq.setUpgradeMode(1);

        fotaPlanReq.setCreateBy(CommonConstant.USER_ID_SYSTEM);
        fotaPlanReq.setObjectParentId(treeNodeId);



        List<UpgradeTaskObjectReq> upgradeTaskObjectList = Lists.newArrayList();

        List<FotaObjectPo> fotaObjectPos = fotaObjectDbService.listAllByTreeNodeId(treeNodeId);
        if(MyCollectionUtil.isEmpty(fotaObjectPos)){
            log.warn("no fotaObjectDos find");
            return;
        }
        //List<FotaPlanObjListPo>  fotaPlanObjListPos = Lists.newArrayList();
        for (FotaObjectPo fotaObjectPo : fotaObjectPos) {
            UpgradeTaskObjectReq upgradeTaskObjectReq = new UpgradeTaskObjectReq();
            //upgradeTaskObjectReq.setStatus(Enums.TaskObjStatusTypeEnum.CREATED.getType());
            upgradeTaskObjectReq.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
            upgradeTaskObjectReq.setOtaPlanId(fotaPlanReq.getObjectParentId());
            upgradeTaskObjectReq.setOtaObjectId(fotaObjectPo.getId());
            upgradeTaskObjectList.add(upgradeTaskObjectReq);
        }
        fotaPlanReq.setUpgradeTaskObjectList(upgradeTaskObjectList);

        List<FotaFirmwarePo> fotaFirmwarePos = Lists.newArrayList();
        DeviceTreeNodePo deviceTreeNodePo = deviceTreeNodeDbService.getById(treeNodeId);
        List<DeviceTreeNodePo> treeNodeDos1 = deviceTreeNodeDbService.listChildren(/*deviceTreeNodePo.getProjectId(), *//*deviceTreeNodePo.getRootNodeId(), */deviceTreeNodePo.getId());
        for (DeviceTreeNodePo item : treeNodeDos1) {
            log.info("treeNodeDos={}", treeNodeDos1.toString());
            /*FotaFirmwarePo fotaFirmwarePo = fotaFirmwareService.getFotaFirmwareByTreeNodeId(item.getProjectId(), item.getId());
            if (fotaFirmwarePo != null) {
                fotaFirmwarePos.add(fotaFirmwarePo);
            }*/
        }

        if(MyCollectionUtil.isEmpty(fotaFirmwarePos)){
            log.warn("no fotaFirmwarePos find");
            return;
        }

        List<UpgradeFirmwareReq> upgradeFirmwareList = Lists.newArrayList();
        AtomicInteger upgradeSeq = new AtomicInteger(0);
        fotaFirmwarePos.forEach(
                item -> {
                    UpgradeFirmwareReq upgradeFirmwareReq = new UpgradeFirmwareReq();
                    upgradeFirmwareReq.setFirmwareId(item.getId());
                    //planFirmwareListDo.setFirmwareVersionId(item.getFirmwareVersionId());
                    upgradeFirmwareReq.setUpgradeSeq(upgradeSeq.incrementAndGet());
                    //planFirmwareListDo.setVersion(item.getVersion());
                    //planFirmwareListDo.setDelFlag(Enums.DelFlgEnum.RESERVED.getFlg());
                    upgradeFirmwareList.add(upgradeFirmwareReq);
                }
        );
        fotaPlanReq.setUpgradeFirmwareList(upgradeFirmwareList);

        List<TaskUpgradeConditionReq> upgradeConditionList = Lists.newArrayList();
        LongStream.rangeClosed(1, 3).forEach(item ->{
            TaskUpgradeConditionReq taskUpgradeConditionReq = new TaskUpgradeConditionReq();
            taskUpgradeConditionReq.setConditionId(item);
            upgradeConditionList.add(taskUpgradeConditionReq);
        });
        fotaPlanReq.setUpgradeConditionList(upgradeConditionList);

        fotaPlanService.addFotaPlan(fotaPlanReq);


        /*List<DeviceTreeNodePo> treeNodeDos = deviceTreeNodeService.listByTreeLevel(CommonConstant.PROJECT_ID_DEFAULT, DeviceTreeNodeLevelEnum.CONF.getLevel());

        final AtomicLong treeNodeId = new AtomicLong(0L);
        for (DeviceTreeNodePo treeNodeDo : treeNodeDos) {
            List<DeviceTreeNodePo> treeNodeDos1 = deviceTreeNodeService.listChildren(treeNodeDo.getProjectId(), treeNodeDo.getRootNodeId(), treeNodeDo.getId());
            for (DeviceTreeNodePo deviceTreeNodePo : treeNodeDos1) {
                log.info("treeNodeDos={}", treeNodeDos1.toString());
                FotaFirmwarePo fotaFirmwarePo = fotaFirmwareService.getFotaFirmwareByTreeNodeId(treeNodeDo.getProjectId(), deviceTreeNodePo.getId());
                if (fotaFirmwarePo != null) {
                    treeNodeId.set(treeNodeDo.getId());
                    break;
                }
            }
        }
        log.info("id={}", treeNodeId.get());
        IntStream.rangeClosed(0, 10).forEach(item -> {
            FotaObjectDo fotaObjectDo = new FotaObjectDo();
            fotaObjectDo.setProjectId(CommonConstant.PROJECT_ID_DEFAULT);
            fotaObjectDo.setId(IdWorker.getId());
            fotaObjectDo.setObjectId("bqNo00000" + item);
            fotaObjectDo.setProductionDate(new Date());
            fotaObjectDo.setLicenceNo("licenceNo1");
            *//*Date now = new Date();*//*
            *//*fotaObjectDo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
            fotaObjectDo.setUpdateTime(now);*//*
            fotaObjectDo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
            LocalDateTime date = LocalDateTime.now();
            fotaObjectDo.setCreateTime(date);

            fotaObjectDo.setTreeNodeId(treeNodeId.get());
            fotaObjectService.save(fotaObjectDo);
            fotaFirmwareListService.initWithObjectId(fotaObjectDo.getId());
        });*/

    }
}
