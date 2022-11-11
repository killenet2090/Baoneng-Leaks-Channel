package com.bnmotor.icv.tsp.ota.controller.test;


import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.SpringContextUtil;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownConfigCheckResponse;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.controller.inner.AbstractController;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.OtaMessageMapper;
import com.bnmotor.icv.tsp.ota.model.entity.DeviceTreeNodePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.req.FotaObjectReq;
import com.bnmotor.icv.tsp.ota.model.req.UpgradeTaskObjectReq;
import com.bnmotor.icv.tsp.ota.model.req.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.ota.model.resp.ExistValidPlanObjVo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuFirmwareConfigListVo;
import com.bnmotor.icv.tsp.ota.schedule.ScheduleTasks;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.service.feign.MsCommonDataFeignService;
import com.bnmotor.icv.tsp.ota.service.feign.PushFeignService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author xxc
 * @ClassName: FotaPlanPo
 * @Description: OTA升级计划表 controller层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-07
 */
@Api(value = "测试Controller", tags = {"测试Controller"})
@RestController
@Slf4j
public class TestController extends AbstractController {
	
	@Autowired
    private IFotaVersionCheckService fotaVersionCheckService;

	@Autowired
    private IFotaVersionCheckVerifyService fotaVersionCheckVerifyService;

	@Autowired
    private IFotaFirmwareListService fotaFirmwareListService;
	
	@Autowired
    private IFotaPlanObjListService fotaPlanObjListService;

	@Autowired
    private IFotaObjectService fotaObjectService;

	@Autowired
    private IFotaObjectDbService fotaObjectDbService;

	@Autowired
    private IDeviceTreeNodeDbService deviceTreeNodeDbService;

	@Autowired
    private IFotaFirmwareService fotaFirmwareService;

	@Autowired
    private ScheduleTasks scheduleTasks;
	
	@Autowired
	private PushFeignService pushFeignService;
	
	@Autowired
	private IFotaConfigService fotaConfigService;

    @Autowired
    private IFotaFileUploadRecordService fotaFileUploadRecordService;
    
	private final long transId = 10000L;
    private final long businessId = 10001L;
    private final String vin = "bqNo000000";

    @ApiOperation(value = "createFotaObject", response = Void.class)
    @PostMapping("/v1/createFotaObject")
    public ResponseEntity createFotaObject(@RequestBody @Valid FotaObjectReq req) {
        List<DeviceTreeNodePo> treeNodeDos1 = deviceTreeNodeDbService.listChildren(/*req.getProjectId(), *//*req.getRootNodeId(), */req.getTreeNodeId());
        boolean flag = false;
        if (!treeNodeDos1.isEmpty()) {
            flag = true;
        }

        if (!flag || req.getTreeNodeId().longValue() == 0) {
            return ResponseEntity.ok("输入参数有误");
        } else {
            log.info("id={}", req.getTreeNodeId());
            FotaObjectPo fotaObjectPo = new FotaObjectPo();
            fotaObjectPo.setProjectId(CommonConstant.PROJECT_ID_DEFAULT);
            fotaObjectPo.setId(IdWorker.getId());
            fotaObjectPo.setObjectId(req.getVin());
            fotaObjectPo.setProductionDate(new Date());
            fotaObjectPo.setLicenceNo("licenceNo1");

            fotaObjectPo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
            LocalDateTime date = LocalDateTime.now();
            fotaObjectPo.setCreateTime(date);

            fotaObjectPo.setTreeNodeId(req.getTreeNodeId());
            fotaObjectDbService.save(fotaObjectPo);
            fotaFirmwareListService.initWithObjectId(fotaObjectPo.getId());
            return ResponseEntity.ok(null);
        }
    }

    @ApiOperation(value = "validate4AddFotaPlan", response = ExistValidPlanObjVo.class)
    @PostMapping("/v1/validate4AddFotaPlan")
    public ResponseEntity validate4AddFotaPlan(@RequestBody List<UpgradeTaskObjectReq> upgradeTaskObjectReqList) {
        return ResponseEntity.ok(fotaPlanObjListService.validate4AddFotaPlan(upgradeTaskObjectReqList));
    }

//    @ApiOperation("")
//    @PostMapping("/v1/test/insertDownloadProcess")
//    public void insertDownloadProcess(@RequestBody List<com.bnmotor.icv.adam.sdk.ota.domain.DownloadProcessDetail> downloadProcessDetails) {
//        //mongoService.insertDownloadProcess(downloadProcessDetails);
//    }

//    @ApiOperation("")
//    @GetMapping("/v1/test/listDownloadProcessByTransId")
//    public ResponseEntity<List<com.bnmotor.icv.adam.sdk.ota.domain.DownloadProcessDetail>> listDownloadProcessByTransId(Long transId) {
//        return ResponseEntity.ok(fotaVersionCheckVerifyService.listDownloadProcessByTransId(transId));
//    }

//    @ApiOperation("")
//    @PostMapping("/v1/test/insertUpgradeProcess")
//    public void insertUpgradeProcess(@RequestBody List<com.bnmotor.icv.adam.sdk.ota.domain.InstallProcessDetail> upgradeProcessDetails) {
//        //mongoService.insertUpgradeProcess(upgradeProcessDetails);
//    }

//    @ApiOperation("")
//    @PostMapping("/v1/test/insertUpgradeResult")
//    public void insertUpgradeResult(@RequestBody List<com.bnmotor.icv.adam.sdk.ota.domain.UpgradeResultDetail> upgradeResultDetails) {
//        //mongoService.insertUpgradeResult(upgradeResultDetails);
//    }

//    @ApiOperation("")
//    @GetMapping("/v1/test/listUpgradeProcessByTransId")
//    public ResponseEntity<List<com.bnmotor.icv.adam.sdk.ota.domain.InstallProcessDetail>> listUpgradeProcessByTransId(Long transId) {
//        return ResponseEntity.ok(fotaVersionCheckVerifyService.listUpgradeProcessByTransId(transId));
//    }

    @ApiOperation(value = "testPushFeignService", response = Void.class)
    @GetMapping("/v1/testPushFeignService")
    public ResponseEntity testPushFeignService() {
        String content = "你用".concat("132661812911").concat("的手机，手机号码：").concat("SN112233").concat("为你的").concat("0123");
        List<String> idList= Lists.newArrayList();
        idList.add("1114a897921a6a33b16");
        JpushMsgDto build = JpushMsgDto.builder()
                .businessId("123456")
                .fromType(3)
                .sendContent("内容")
                .sendTitle("OTA推送消息测试")
                .sendAllFlag(false)
                //.extraData("")
                .sendRegistrationIdList(idList)
                //.sendTagsList("")
                .sendType(1)
                .build();

        ResponseEntity responseEntity = pushFeignService.sendMessage(build);
        if (responseEntity.getStatusCodeValue()== HttpStatus.OK.value()) {
            return RestResponse.ok(null);
        } else {
            return RestResponse.error(RespCode.SERVER_EXECUTE_ERROR);
        }
    }


    @ApiOperation(value = "testPushFeignService1", response = Void.class)
    @GetMapping("/v1/testPushFeignService1")
    public ResponseEntity testPushFeignService1() {
        String content = "你用".concat("132661812911").concat("的手机，手机号码：").concat("SN112233").concat("为你的").concat("0123");
        List<String> idList= Lists.newArrayList();
        idList.add("1114a897921a6a33b16");
        JpushMsgDto build = JpushMsgDto.builder()
                .businessId("123456")
                .fromType(3)
                .sendContent("{\"body\":\"eeew\",\"businessId\":1,\"businessType\":3}")
                .sendTitle("OTA推送消息测试")
                .sendAllFlag(false)
                //.extraData("")
                .sendRegistrationIdList(idList)
                //.sendTagsList("")
                .sendType(1)
                .build();

        ResponseEntity responseEntity = pushFeignService.sendMessage(build);
        if (responseEntity.getStatusCodeValue()== HttpStatus.OK.value()) {
            return RestResponse.ok(null);
        } else {
            return RestResponse.error(RespCode.SERVER_EXECUTE_ERROR);
        }
    }

    @ApiOperation(value = "testScheduleTask", response = Void.class)
    @GetMapping("/v1/testScheduleTask")
    public ResponseEntity testScheduleTask(@RequestParam String taskName) {
        scheduleTasks.scheduleTask(taskName);
        return RestResponse.ok("success");
    }

    @ApiOperation(value = "testDelRecordFromDb", response = Void.class)
    @GetMapping("/v1/testDelRecordFromDb")
    public ResponseEntity testDelRecordFromDb(@RequestParam Long id) {
        (SpringContextUtil.getBean(IFotaPlanTaskDetailDbService.class)).removeById(id);
        return RestResponse.ok("success");
    }

    @ApiOperation(value = "findCurFotaVersionCheckVerifyPo", response = Void.class)
    @GetMapping("/v1/findCurFotaVersionCheckVerifyPo")
    public ResponseEntity findCurFotaVersionCheckVerifyPo(@RequestParam Long planId, @RequestParam Long planObjectId) {
        (SpringContextUtil.getBean(IFotaVersionCheckVerifyDbService.class)).findCurFotaVersionCheckVerifyPo(planId, planObjectId);
        return RestResponse.ok("success");
    }

    @ApiOperation(value = "testPush2Async", response = Void.class)
    @GetMapping("/v1/testPush2Async")
    public ResponseEntity testPush2Async(@RequestParam Long planId, @RequestParam Long planObjectId) {
        (SpringContextUtil.getBean(IPush4AppService.class)).pushInstalledVerifyReq2App("10086", planId, planObjectId);
        return RestResponse.ok("success");
    }


    @ApiOperation(value = "testGetChinaRegions", response = Void.class)
    @GetMapping("/v1/testGetChinaRegions")
    public ResponseEntity testGetChinaRegions(@RequestParam String code) {
        (SpringContextUtil.getBean(MsCommonDataFeignService.class)).list(code);
        return RestResponse.ok("success");
    }

    @ApiOperation(value = "testGetEcuConfigs", response = OtaDownConfigCheckResponse.class)
    @GetMapping("/v1/testGetEcuConfigs")
    public ResponseEntity testGetEcuConfigs(@RequestParam String vin) {
        EcuFirmwareConfigListVo ecuFirmwareConfigListVo = fotaConfigService.queryEcuConfigFirmwareDos(vin);
        OtaDownConfigCheckResponse downConfigCheckResponse = OtaMessageMapper.INSTANCE.eEcuFirmwareConfigListVo2OtaDownConfigCheckResponse(ecuFirmwareConfigListVo);
        downConfigCheckResponse.setConfVersion(ecuFirmwareConfigListVo.getConfVersion());
        return RestResponse.ok(downConfigCheckResponse);
    }

    @ApiOperation(value = "deleteByOtaPlanObjId", response = Void.class)
    @GetMapping("/v1/deleteByOtaPlanObjId")
    public ResponseEntity deleteByOtaPlanObjId() {
        (SpringContextUtil.getBean(IFotaPlanTaskDetailDbService.class)).deleteByOtaPlanObjId(0L, 1L);
        return RestResponse.ok(null);

        //ObjId(otaPlanId, otaPlanObjId);
    }

    @ApiOperation(value = "testGetFileInfoDetails", response = Void.class)
    @PostMapping("/v1/testGetFileInfoDetails")
    public ResponseEntity testGetFileInfoDetails(@RequestBody List<Long> fileIds) {
        fotaFileUploadRecordService.delByFileIdsTest(fileIds);
        return RestResponse.ok(null);
    }
}
