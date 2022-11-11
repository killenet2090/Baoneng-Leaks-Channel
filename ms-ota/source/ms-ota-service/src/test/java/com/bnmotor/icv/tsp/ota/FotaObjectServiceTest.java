package com.bnmotor.icv.tsp.ota;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.DeviceTreeNodeLevelEnum;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.service.IDeviceTreeNodeDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareListService;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectDbService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * @ClassName: FotaObjectServiceTest
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/7/15 11:38
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@SpringBootTest
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public class FotaObjectServiceTest {
    @Autowired
    private IFotaObjectDbService fotaObjectDbService;
    @Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;
    @Autowired
    private IFotaFirmwareListService fotaFirmwareListService;


    @Test
    public void insertBatch(){
        List<DeviceTreeNodePo> treeNodeDos =  deviceTreeNodeDbService.listByTreeLevel(/*CommonConstant.PROJECT_ID_DEFAULT, */DeviceTreeNodeLevelEnum.CONF.getLevel());

        final AtomicLong treeNodeId = new AtomicLong(0L);
        for (DeviceTreeNodePo treeNodeDo : treeNodeDos) {
            List<DeviceTreeNodePo> treeNodeDos1 = deviceTreeNodeDbService.listChildren(/*treeNodeDo.getProjectId(), *//*treeNodeDo.getRootNodeId(), */treeNodeDo.getId());
            for (DeviceTreeNodePo deviceTreeNodePo : treeNodeDos1) {
                log.info("treeNodeDos={}", treeNodeDos1.toString());
                /*FotaFirmwarePo fotaFirmwarePo = fotaFirmwareService.getFotaFirmwareByTreeNodeId(treeNodeDo.getProjectId(), deviceTreeNodePo.getId());
                if(fotaFirmwarePo != null){
                    treeNodeId.set(treeNodeDo.getId());
                    break;
                }*/
            }
        }
        log.info("id={}", treeNodeId.get());
        IntStream.rangeClosed(0, 10).forEach(item -> {
            FotaObjectPo fotaObjectPo = new FotaObjectPo();
            fotaObjectPo.setProjectId(CommonConstant.PROJECT_ID_DEFAULT);
            fotaObjectPo.setId(IdWorker.getId());
            fotaObjectPo.setObjectId("bqNo00000"+item);
            fotaObjectPo.setProductionDate(new Date());
            fotaObjectPo.setLicenceNo("licenceNo1");
            /*Date now = new Date();*/
            /*fotaObjectDo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
            fotaObjectDo.setUpdateTime(now);*/
            fotaObjectPo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
            LocalDateTime date = LocalDateTime.now();
            fotaObjectPo.setCreateTime(date);

            fotaObjectPo.setTreeNodeId(treeNodeId.get());
            fotaObjectDbService.save(fotaObjectPo);
            fotaFirmwareListService.initWithObjectId(fotaObjectPo.getId());
        });

    }
}
