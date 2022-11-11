package com.bnmotor.icv.tsp.ota.controller.test;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.sdk.ota.domain.DownloadProcessDetail;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaCommonPayload;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.domain.UpgradeResultDetail;
import com.bnmotor.icv.adam.sdk.ota.up.*;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.controller.FotaUpgradeController;
import com.bnmotor.icv.tsp.ota.handler.tbox.TestSendMsgFromTboxHandler;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.TBoxUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * @author xxc
 * @ClassName: TestSendMsg2TboxController
 * @Description: OTA升级计划表 controller层
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-07
 */
@Api(value = "测试来自TBOX的请求消息", tags = {"测试来自TBOX的请求消息"})
@RestController
@Slf4j
public class TestSendMsg2TboxController {
	
	@Autowired
    private IFotaVersionCheckService fotaVersionCheckService;

	@Autowired
    private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;

	@Autowired
    private TestSendMsgFromTboxHandler testSendMsgFromTboxHandler;

	@Autowired
    private FotaUpgradeController fotaUpgradeController;

//    private final long transId = 10000L;
//    private final long businessId = 10001L;
//    private final String vin = "bqNo000000";
//    private final PushFeignService pushFeignService;

    @ApiOperation(value = "checkVersion", response = OtaUpVersionCheck.class)
    @GetMapping("/v1/2Tbox/checkVersion")
    public ResponseEntity checkVersion(@RequestParam String vin) {
        OtaProtocol otaProtocol = fotaUpgradeController.buildCheckVersionOtaProtocol(vin);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "downloadVerifyReq", response = OtaUpUpgradeVerify.class)
    @GetMapping("/v1/2Tbox/downloadVerifyReq")
    public ResponseEntity downloadVerifyReq(@RequestParam String vin) {
        OtaProtocol otaProtocol = fotaUpgradeController.buildDownloadVerifyReqOtaProtocal(vin);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "downloadVerifyResult", response = OtaUpUpgradeVerifyResult.class)
    @GetMapping("/v1/2Tbox/downloadVerifyResult")
    public ResponseEntity downloadVerifyResult(@RequestParam String vin) {
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_UP_DOWNLOAD_VERIFY_RESULT, vin, null, IdWorker.getId());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        
        OtaUpUpgradeVerifyResult otaUpUpgradeVerifyResult = new OtaUpUpgradeVerifyResult();
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);
        
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, fotaVersionCheckVerifyPo.getOtaPlanId(), fotaVersionCheckVerifyPo.getId());
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, Enums.DownloadVerifySourceEnum.FROM_APP.getType());
//        otaUpUpgradeVerifyResult.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
//        otaUpUpgradeVerifyResult.setTransId(fotaVersionCheckVerifyPo.getId());
        otaUpUpgradeVerifyResult.setImmediateDownload(1);
//        otaUpUpgradeVerifyResult.setVerifySource(Enums.DownloadVerifySourceEnum.FROM_APP.getType());
        otaUpUpgradeVerifyResult.setVerifyResult(1);
        otaProtocol.getBody().setOtaUpUpgradeVerifyResult(otaUpUpgradeVerifyResult);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "downloadProcessReq", response = OtaUpDownloadProcess.class)
    @GetMapping("/v1/2Tbox/downloadProcessReq")
    public ResponseEntity downloadProcessReq(@RequestParam String vin) {
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_UP_DOWNLOAD_VERIFY_RESULT, vin, null, IdWorker.getId());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        
        OtaUpUpgradeVerifyResult otaUpUpgradeVerifyResult = new OtaUpUpgradeVerifyResult();
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);
//        otaUpUpgradeVerifyResult.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
//        otaUpUpgradeVerifyResult.setTransId(fotaVersionCheckVerifyPo.getId());
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, fotaVersionCheckVerifyPo.getOtaPlanId(), fotaVersionCheckVerifyPo.getId());
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, Enums.DownloadVerifySourceEnum.FROM_APP.getType());
        
        otaUpUpgradeVerifyResult.setImmediateDownload(1);
//        otaUpUpgradeVerifyResult.setVerifySource(Enums.DownloadVerifySourceEnum.FROM_APP.getType());
        
        otaUpUpgradeVerifyResult.setVerifyResult(1);
        otaProtocol.getBody().setOtaUpUpgradeVerifyResult(otaUpUpgradeVerifyResult);

        OtaUpDownloadProcess otaUpDownloadProcess = new OtaUpDownloadProcess();
        otaProtocol.getBody().setBusinessId(1L);
//        otaUpDownloadProcess.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaUpDownloadProcess.setTransId(fotaVersionCheckVerifyPo.getId());
        
        
        otaUpDownloadProcess.setDownloadProcessType(5);
        otaUpDownloadProcess.setDownloadProcessDetails(Lists.newArrayList());
        DownloadProcessDetail detail1 = new DownloadProcessDetail();
        detail1.setDownloadSize(1000L);
        detail1.setDownloadStatus(1);
        detail1.setPkgId(200L);
        detail1.setAccumulateNum(1);
        Lists.newArrayList().add(detail1);

        DownloadProcessDetail detail2 = new DownloadProcessDetail();
        detail2.setDownloadSize(1000L);
        detail2.setDownloadStatus(1);
        detail2.setPkgId(200L);
        detail2.setAccumulateNum(1);
        Lists.newArrayList().add(detail2);

        otaCommonPayload.setErrorCode(100);
        otaCommonPayload.setErrorMsg("测试的锅");
//        otaUpDownloadProcess.setErrorCode(100);
//        otaUpDownloadProcess.setErrorMsg("测试的锅");
        
        otaProtocol.getBody().setOtaUpDownloadProcess(otaUpDownloadProcess);

        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "upgradeVerifyReq", response = OtaUpInstallVerify.class)
    @GetMapping("/v1/2Tbox/upgradeVerifyReq")
    public ResponseEntity upgradeVerifyReq(@RequestParam String vin) {
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_UP_INSTALLED_VERIFY_REQ, vin, null, IdWorker.getId());

        OtaUpInstallVerify otaUpInstallVerify = new OtaUpInstallVerify();
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);
        otaUpInstallVerify.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaUpInstallVerify.setTransId(fotaVersionCheckVerifyPo.getId());

        otaProtocol.getBody().setOtaUpInstallVerify(otaUpInstallVerify);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "upgradeVerifyResult", response = OtaUpInstallVerifyResult.class)
    @GetMapping("/v1/2Tbox/upgradeVerifyResult")
    public ResponseEntity upgradeVerifyResult(@RequestParam String vin) {
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_UP_INSTALLED_VERIFY_RESULT, vin, null, IdWorker.getId());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        
        OtaUpInstallVerifyResult otaUpInstallVerifyResult = new OtaUpInstallVerifyResult();
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);
        
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, fotaVersionCheckVerifyPo.getOtaPlanId(), fotaVersionCheckVerifyPo.getId());
//        otaUpInstallVerifyResult.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
//        otaUpInstallVerifyResult.setTransId(fotaVersionCheckVerifyPo.getId());
        otaUpInstallVerifyResult.setInstalledType(1);
        otaUpInstallVerifyResult.setInstalledTime(Instant.now().toEpochMilli());
        otaUpInstallVerifyResult.setVerifyResult(1);
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, Enums.InstalledVerifySourceEnum.FROM_APP.getType());
//        otaUpInstallVerifyResult.setVerifySource(Enums.InstalledVerifySourceEnum.FROM_APP.getType());

        otaProtocol.getBody().setOtaUpInstallVerifyResult(otaUpInstallVerifyResult);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "upgradeResult", response = OtaUpUpgradeResult.class)
    @GetMapping("/v1/2Tbox/upgradeResult")
    public ResponseEntity upgradeResult(@RequestParam String vin) {
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_UP_UPGRADE_RESULT, vin, null, IdWorker.getId());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        
        OtaUpUpgradeResult otaUpUpgradeResult = new OtaUpUpgradeResult();
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, fotaVersionCheckVerifyPo.getOtaPlanId(), fotaVersionCheckVerifyPo.getId());
//        otaUpUpgradeResult.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
//        otaUpUpgradeResult.setTransId(fotaVersionCheckVerifyPo.getId());

        otaUpUpgradeResult.setStartTime(Instant.now().toEpochMilli() - 86400L);
        otaUpUpgradeResult.setEndTime(Instant.now().toEpochMilli() + 86400L);
        otaUpUpgradeResult.setInstalledResult(1);
        List<UpgradeResultDetail> upgradeResultDetails = Lists.newArrayList();


        List<FotaFirmwarePo> fotaFirmwarePos = fotaVersionCheckService.queryFotaFirmwarePos(vin);
        if(MyCollectionUtil.isNotEmpty(fotaFirmwarePos)){
            for(FotaFirmwarePo fotaFirmwarePo : fotaFirmwarePos){
                UpgradeResultDetail upgradeResultDetail = new UpgradeResultDetail();
                upgradeResultDetail.setFirmwareId(fotaFirmwarePo.getId());
                upgradeResultDetail.setEcuId(fotaFirmwarePo.getComponentCode());
                upgradeResultDetail.setDiagnose(fotaFirmwarePo.getDiagnose());
                upgradeResultDetail.setFirmwareCode(fotaFirmwarePo.getFirmwareCode());
                upgradeResultDetail.setStatus(1);

                upgradeResultDetails.add(upgradeResultDetail);
            }
        }
        otaUpUpgradeResult.setUpgradeResultDetails(upgradeResultDetails);

        otaProtocol.getBody().setOtaUpUpgradeResult(otaUpUpgradeResult);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }

    @ApiOperation(value = "upgradeProcessReq", response = OtaUpInstallProcess.class)
    @GetMapping("/v1/2Tbox/upgradeProcessReq")
    public ResponseEntity upgradeProcessReq(@RequestParam String vin) {
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_UP_INSTALLED_PROCESS, vin, null, IdWorker.getId());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);

        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);

        //前置条件检查异常/取消升级
        OtaUpInstallProcess otaUpInstallProcess = new OtaUpInstallProcess();
        otaProtocol.getBody().setBusinessId(1L);
        otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);
        
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, fotaVersionCheckVerifyPo.getOtaPlanId(), fotaVersionCheckVerifyPo.getId());
//        otaUpInstallProcess.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
//        otaUpInstallProcess.setTransId(fotaVersionCheckVerifyPo.getId());
        otaUpInstallProcess.setInstalledProcessType(Enums.InstalledProcessTypeEnum.INSTALLED_PROCESSING.getType());

        otaUpInstallProcess.setTotalPkgNum(9);
        otaUpInstallProcess.setCurrentPkgIndex(2L);
        otaUpInstallProcess.setInstalledSpendTime(100L);
        otaUpInstallProcess.setInstalledRemainTime(50L);

        otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);
        testSendMsgFromTboxHandler.senMsg2Ota(otaProtocol);
        return RestResponse.ok(otaProtocol);
    }
}
