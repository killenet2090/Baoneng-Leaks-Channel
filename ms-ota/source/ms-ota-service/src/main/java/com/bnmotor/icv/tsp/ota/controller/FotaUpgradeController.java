package com.bnmotor.icv.tsp.ota.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.sdk.ota.domain.*;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.adam.sdk.ota.up.*;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxDownHandler;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.app.*;
import com.bnmotor.icv.tsp.ota.model.resp.app.*;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.TBoxUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @ClassName: FotaUpgradeController
 * @Description: ota对接前端（app/tbox）升级接口Controller
 * @author: xuxiaochang1
 * @date: 2020/7/6 11:38
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RestController
@Api(value="OTA对接APP升级接口",tags={"OTA对接APP升级接口"})
@RequestMapping(value = "/v1/app/upgrade")
@Slf4j
public class FotaUpgradeController{
    @Autowired
    private IFota4AppService fota4AppService;

    @Autowired
    private IPush4AppService push4AppService;

    @Autowired
    private IFotaVersionCheckService fotaVersionCheckService;

    @Autowired
    private IFotaVersionCheckVerifyService fotaVersionCheckVerifyService;

    @Autowired
    private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;

    @Autowired
    private IFotaFirmwareVersionPathDbService fotaFirmwareVersionPathDbService;

    @Autowired
    private IFotaPlanTaskDetailDbService fotaPlanTaskDetailDbService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;
    
    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    private IFotaFirmwareVersionDbService fotaFirmwareVersionDbService;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    @Autowired
    private TBoxDownHandler tBoxDownHandler;
    
    final static long LONG_NUMBER_ONE = 1L;

    private final ThreadLocal<ExecutorService> executorService = ThreadLocal.withInitial(() -> Executors.newFixedThreadPool(5));

    /**
     * 提供给APP客户端检查新版本
     * 1、存在新版本，需要等待TBOX完成后续逻辑
     * 2、不存在新版本，不需要等待
     * 3、
     * @param req
     * @return
     */
    @PostMapping(value = "checkVersion")
    @ApiOperation(value="检查软件新版本", notes="检查软件新版本", response = VersionCheckBodyVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录后系统给分配的token，鉴别用户身份需要", example="token1", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity checkVersion(/*HttpServletRequest httpServletRequest, */@RequestBody BaseAppDto req){
        VersionCheckBodyVo versionCheckBodyVo = fota4AppService.checkVersion(req);

        if(req.getIsPush() == 1) {

            Runnable r = () -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (versionCheckBodyVo.getCheckResult() == 1 && versionCheckBodyVo.getStatus() == AppEnums.CheckVersionStatusType4AppEnum.WAIT_CHECK.getType()) {
                    PushMockDto pushMockDto = new PushMockDto();
                    pushMockDto.setVin(req.getVin());
                    OtaProtocol otaProtocol = checkVersionReqSync(pushMockDto);

                    if (Objects.nonNull(otaProtocol) && otaProtocol.getBody().getOtaDownVersionCheckResponse().getCheckResult() == 1) {
                        PushMockDto pushMockDto1 = new PushMockDto();
                        pushMockDto1.setVin(req.getVin());
                        downloadVerifyReqSync(pushMockDto1);
                    }
                }
            };
            executorService.get().submit(r);
        }
        //与APP联调测试用临时添加
        return RestResponse.ok(versionCheckBodyVo);
    }

    @PostMapping(value = "remoteDownload")
    @ApiOperation(value="请求远程下载", notes="请求远程下载", response = SyncResultVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录后系统给分配的token，鉴别用户身份需要", example="token1", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity remoteDownload(@RequestBody RemoteDownloadDto req){
        SyncResultVo syncResultVo = fota4AppService.remoteDownload(req);

        /*Runnable r = ()-> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (syncResultVo.getSuccess() == 1) {
                OtaProtocol otaProtocal = new OtaProtocol();
                otaProtocal.setOtaMessageHeader(new OtaMessageHeader());
                otaProtocal.getOtaMessageHeader().setVin(req.getVin());
                otaProtocal.setBody(new OtaMessage());
                OtaUpUpgradeVerifyResult otaUpUpgradeVerifyResult = new OtaUpUpgradeVerifyResult();
                otaProtocal.getBody().setBusinessId(1L);
                otaProtocal.getBody().setOtaUpUpgradeVerifyResult(otaUpUpgradeVerifyResult);

                *//*otaUpUpgradeVerifyResult.setVerifySource(Enums..FROM_APP.getType());*//*
                otaUpUpgradeVerifyResult.setVerifyResult(1);
                otaUpUpgradeVerifyResult.setImmediateDownload(1);

                FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(req.getVin());
                OtaCommonPayload otaCommonPayload = new OtaCommonPayload();
                otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
                otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());
                otaCommonPayload.setSourceType(Enums.DownloadVerifySourceEnum.FROM_APP.getType());
                fotaVersionCheckVerifyService.downloadVerifyResult(otaProtocal);
            }
        };
        executorService.submit(r);*/

        return RestResponse.ok(syncResultVo);
    }

    @PostMapping(value = "cancelInstalledBooked")
    @ApiOperation(value="取消预约安装", notes="取消预约安装", response = SyncResultVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录后系统给分配的token，鉴别用户身份需要", example="token1", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity cancelInstalledBooked(@RequestBody CancelInstalledBookedDto req){
        SyncResultVo syncResultVo = fota4AppService.cancelInstalledBooked(req);
        return RestResponse.ok(syncResultVo);
    }

    @PostMapping(value = "remoteInstalled")
    @ApiOperation(value="请求远程安装", notes="请求远程安装", response = SyncResultVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录后系统给分配的token，鉴别用户身份需要", example="token1", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity remoteInstalled(@RequestBody RemoteInstalledDto req){
        SyncResultVo syncResultVo = fota4AppService.remoteInstalled(req);
        return RestResponse.ok(syncResultVo);
    }

    @PostMapping(value = "getTboxUpgradeStatus")
    @ApiOperation(value="获取TBox当前升级过程状态", notes="获取TBox当前升级过程状态", response = TboxUpgradStatusVo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "用户登录后系统给分配的token，鉴别用户身份需要", example="token1", required = true, paramType = "header", dataType = "string")
    })
    public ResponseEntity getTboxUpgradeStatus(@RequestBody GetTboxUpgradStatusDto req){
        return RestResponse.ok(fota4AppService.getTboxUpgradStatus(req.getVin()));
    }

    /**
     *
     * @param baseAppVo
     * @param businessTypeEnum
     * @param <T>
     */
    private <T> void set4BaseAppVo(BaseAppVo<T> baseAppVo, String vin, com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum businessTypeEnum){
        baseAppVo.setBusinessType(businessTypeEnum.getType());
        baseAppVo.setTimestamp(Instant.now().toEpochMilli());
        baseAppVo.setBusinessId(IdWorker.getId());
        baseAppVo.setType(3);
        baseAppVo.setVin(vin);
    }

    @PostMapping(value = "checkVersionReqSync" )
    @ApiOperation(value="[mock接口-手动触发回调转发消息到APP]__模拟TBOX上行消息到OTA云端进行新版本检查", notes="[mock接口-供APP开发手动触发模拟消息回调]____模拟TBOX上行消息到OTA云端进行新版本检查", response = OtaUpInstallVerifyResult.class)
    public OtaProtocol checkVersionReqSync(@RequestBody PushMockDto req){
        OtaProtocol otaProtocal = buildCheckVersionOtaProtocol(req.getVin());
        OtaProtocol respOtaProtocol = fotaVersionCheckService.checkVersion(otaProtocal);
        tBoxDownHandler.send(respOtaProtocol);

        return respOtaProtocol;
    }

    /**
     *
     * @param vin
     * @return
     */
    public OtaProtocol buildCheckVersionOtaProtocol(String vin){
    	OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(vin);
    	OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
    	
    	OtaUpVersionCheck otaUpVersionCheck = new OtaUpVersionCheck();
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
        otaProtocol.getBody().setBusinessType(BusinessTypeEnum.OTA_UP_VERSION_CHECK.getType());
        otaProtocol.getBody().setOtaUpVersionCheck(otaUpVersionCheck);

        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, AppEnums.ClientSourceTypeEnum.FROM_APP.getType());
        List<EcuModule> ecuModules = Lists.newArrayList();
        otaUpVersionCheck.setEcuModules(ecuModules);

        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
        FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
        otaUpVersionCheck.setConfVersion(fotaObjectPo.getConfVersion());

        List<FotaFirmwarePo> fotaFirmwarePos = fotaVersionCheckService.queryFotaFirmwarePos(vin);
        if(MyCollectionUtil.isNotEmpty(fotaFirmwarePos)){
            for(FotaFirmwarePo fotaFirmwarePo : fotaFirmwarePos){
                EcuModule ecuModule = new EcuModule();
                ecuModule.setDiagnose(fotaFirmwarePo.getDiagnose());
                ecuModule.setEcuId(fotaFirmwarePo.getComponentCode());
                ecuModule.setFirmwareCode(fotaFirmwarePo.getFirmwareCode());
                //ecuModules.add(ecuModule);

                List<FotaFirmwareVersionPo> fotaFirmwareVersionPos = fotaFirmwareVersionDbService.list(null, fotaFirmwarePo.getId());
                List<FotaFirmwareVersionPo> newFotaFirmwareVersionPos = MyCollectionUtil.newSortedList(fotaFirmwareVersionPos, Comparator.comparing(FotaFirmwareVersionPo::getCreateTime));


                List<FotaFirmwareVersionPathPo> fotaFirmwareVersionPathPos = fotaFirmwareVersionPathDbService.listByTargetVersionId(newFotaFirmwareVersionPos.get(newFotaFirmwareVersionPos.size()-1).getId());
                if(MyCollectionUtil.isNotEmpty(fotaFirmwareVersionPathPos)){
                    FotaFirmwareVersionPo fotaFirmwareVersionPo = fotaFirmwareVersionDbService.getById(fotaFirmwareVersionPathPos.get(0).getStartFirmwareVerId());
                    ecuModule.setFirmwareVersion(fotaFirmwareVersionPo.getFirmwareVersionNo());
                    ecuModules.add(ecuModule);
                    log.info("get ecuModule={}", ecuModule);
                }
            }
        }
        return otaProtocol;
    }

    @PostMapping(value = "downloadVerifyReqSync")
    @ApiOperation(value="[mock接口-供APP开发文档参考]__模拟来自TBOX的升级请求（携带版本检查结果）", notes = "[mock接口-供APP开发文档参考]__模拟下载确认（携带版本检查结果）", response = VersionCheckVo.class)
    public ResponseEntity downloadVerifyReqSync(@RequestBody PushMockDto req){

        OtaProtocol otaProtocol = buildDownloadVerifyReqOtaProtocal(req.getVin());
        fotaVersionCheckVerifyService.downloadVerifyReq(otaProtocol);
        return RestResponse.ok(null);
    }

    public OtaProtocol buildDownloadVerifyReqOtaProtocal(String vin){
    	OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(vin);
        OtaUpUpgradeVerify otaUpUpgradeVerify = new OtaUpUpgradeVerify();
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
        otaProtocol.getBody().setBusinessType(BusinessTypeEnum.OTA_UP_DOWNLOAD_VERIFY_REQ.getType());
        otaProtocol.getBody().setOtaUpUpgradeVerify(otaUpUpgradeVerify);
        otaUpUpgradeVerify.setReqId(IdWorker.getId());
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(vin);
        otaUpUpgradeVerify.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaUpUpgradeVerify.setTransId(fotaVersionCheckVerifyPo.getId());
        otaProtocol.getBody().getOtaCommonPayload().setTransId(fotaVersionCheckVerifyPo.getId());
        otaProtocol.getBody().getOtaCommonPayload().setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        return otaProtocol;
    }

    @PostMapping(value = "installedVerifyReqSync")
    @ApiOperation(value="[mock接口-供APP开发文档参考]__模拟来自TBOX的安装请求", notes = "[mock接口-供APP开发文档参考]__模拟来自TBOX的安装请求", response = VoidAppVo.class)
    public ResponseEntity installedVerifyReqSync(@RequestBody PushMockDto req){

    	OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(req.getVin());
        OtaUpInstallVerify otaUpInstallVerify = new OtaUpInstallVerify();
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
        otaProtocol.getBody().setOtaUpInstallVerify(otaUpInstallVerify);
        //otaUpInstallVerify.setReqId(IdWorker.getId());
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(req.getVin());
        otaUpInstallVerify.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaUpInstallVerify.setTransId(fotaVersionCheckVerifyPo.getId());

        fotaVersionCheckVerifyService.installedVerifyReq(otaProtocol);
        return RestResponse.ok(null);
    }

    @PostMapping(value = "downloadCallbackSync")
    @ApiOperation(value="[mock接口-供APP开发文档参考]__远程下载异步回调", notes="[mock接口-供APP开发文档参考]__远程下载异步回调", response = MockRemoteDownloadCallbackVo.class)
    public ResponseEntity downloadCallbackSync(@RequestBody DownloadProcessPushMockDo req){
    	OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(req.getVin());
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);

        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(req.getVin());
        if(Objects.isNull(fotaVersionCheckVerifyPo)) {
            log.warn("升级事务异常.req={}", req);
            return RestResponse.error(OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getCode(), OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getDescription());
        }

        otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());
        otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        Object t = null;
        //下载确认
        if(req.getDownloadProcessType() == 1){
            OtaUpUpgradeVerifyResult otaUpUpgradeVerifyResult = new OtaUpUpgradeVerifyResult();
            otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
            otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());
            otaUpUpgradeVerifyResult.setImmediateDownload(req.getImmediateDownload());

            Integer immediateDownload = req.getImmediateDownload();
            if(immediateDownload==2){
                otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_SLEEPED_4DOWNLOAD.getCode());
                otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_SLEEPED_4DOWNLOAD.getDesc());
            }else{
                otaCommonPayload.setErrorCode(TBoxRespCodeEnum.SUCCESS.getCode());
                otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.SUCCESS.getDesc());
            }

            otaCommonPayload.setSourceType(Enums.DownloadVerifySourceEnum.FROM_APP.getType());
            otaUpUpgradeVerifyResult.setVerifyResult(1);
            otaProtocol.getBody().setOtaUpUpgradeVerifyResult(otaUpUpgradeVerifyResult);
            fotaVersionCheckVerifyService.downloadVerifyResult(otaProtocol);

            DownloadVerfiyResultVo downloadVerfiyResultVo = new DownloadVerfiyResultVo();
            DownloadVerfiyResultBodyVo downloadVerfiyResultBodyVo = new DownloadVerfiyResultBodyVo();
            downloadVerfiyResultVo.setBody(downloadVerfiyResultBodyVo);
            t = downloadVerfiyResultVo;
        } else if(req.getDownloadProcessType() == 2 || req.getDownloadProcessType() == 3){
            OtaUpDownloadProcess otaUpDownloadProcess = new OtaUpDownloadProcess();
            otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
            otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
            otaUpDownloadProcess.setTransId(fotaVersionCheckVerifyPo.getId());
            if(req.getDownloadProcessType() == 2){
                otaUpDownloadProcess.setDownloadProcessType(5);

                otaCommonPayload.setErrorCode(TBoxRespCodeEnum.SUCCESS.getCode());
                otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.SUCCESS.getDesc());
            }else{
                otaUpDownloadProcess.setDownloadProcessType(6);
                /*if(req.getFailType() == 102){
                    otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD.getCode());
                    otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD.getDesc());
                }else if(req.getFailType() == 103){
                    otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getCode());
                    otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getDesc());
                }else if(req.getFailType() == 104){
                    otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode());
                    otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getDesc());*/
	                if(req.getFailType().equals(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD.getCode())){
	                	otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD.getCode());
	                	otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_FAIL_4DOWNLOAD.getDesc());
	                }else if(req.getFailType().equals(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getCode())){
	                	otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getCode());
	                	otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getDesc());
	                }else if(req.getFailType().equals(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode())){
	                	otaCommonPayload.setErrorCode(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode());
	                	otaCommonPayload.setErrorMsg(TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getDesc());
	                }
                /*}*/
            }
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
            DownloadResultVo downloadResultVo = new DownloadResultVo();
            downloadResultVo.setBody(new DownloadResultBodyVo());
            downloadResultVo.setBusinessType(AppEnums.AppResponseTypeEnum.DOWNLOAD_VERIFIED_RESPONSE.getType());
            t = downloadResultVo;

            otaProtocol.getBody().setOtaUpDownloadProcess(otaUpDownloadProcess);
            fotaVersionCheckVerifyService.downloadProcessReq(otaProtocol);
        }else if(req.getDownloadProcessType() == 4){
            OtaDownVersionCheckResponse otaDownVersionCheckResponse = fotaVersionCheckVerifyService.parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());
            Runnable r = () -> {
                int totleTime = 60;
                int max = 30;
                long total = Objects.nonNull(otaDownVersionCheckResponse) ? otaDownVersionCheckResponse.getEstimatedDownloadPackageSize() : 1000000L;
                long per = Objects.nonNull(otaDownVersionCheckResponse) ? (long)Math.ceil(otaDownVersionCheckResponse.getEstimatedDownloadPackageSize()/max) : 200000L;
                for(int i =0 ;i<max;i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    OtaUpDownloadProcess otaUpDownloadProcess = new OtaUpDownloadProcess();
                    otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
                    otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
                    otaUpDownloadProcess.setTransId(fotaVersionCheckVerifyPo.getId());
                    otaUpDownloadProcess.setDownloadProcessType(1);

                    otaUpDownloadProcess.setDownloadFullSize(total);
                    otaUpDownloadProcess.setDownloadFinishedSize(per * (i+1));
                    otaUpDownloadProcess.setDownloadPercentRate((double)(per*(i+1)*100/total));
                    otaUpDownloadProcess.setEstimateRemainTime((long)totleTime - (i+1) * 2);

                    otaProtocol.getBody().setOtaUpDownloadProcess(otaUpDownloadProcess);
                    otaProtocol.getBody().setOtaCommonPayload(otaCommonPayload);
                    fotaVersionCheckVerifyService.downloadProcessReq(otaProtocol);
                }
            };
            executorService.get().submit(r);

            DownloadProcessVo downloadProcessVo = new DownloadProcessVo();
            DownloadProcessBodyVo downloadProcessBodyVo = new DownloadProcessBodyVo();
            downloadProcessVo.setBody(downloadProcessBodyVo);

            t = downloadProcessVo;
        }
        return RestResponse.ok(t);
    }

    @PostMapping(value = "installedCallbackSync")
    @ApiOperation(value="[mock接口-供APP开发文档参考]__远程安装异步回调", notes="[mock接口-供APP开发文档参考]__远程安装异步回调", response = MockRemoteInstalledCallbackVo.class)
    public ResponseEntity installedCallbackSync(@RequestBody InstalledProcessPushMockDo req){
        /*MockRemoteInstalledCallbackVo mockRemoteInstalledCallbackVo = new MockRemoteInstalledCallbackVo();*/

        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(req.getVin());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);

        OtaUpInstallVerifyResult otaUpInstallVerifyResult = new OtaUpInstallVerifyResult();
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);

        //otaUpInstallVerify.setReqId(IdWorker.getId());
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(req.getVin());
        if(Objects.isNull(fotaVersionCheckVerifyPo)) {
            log.warn("升级事务异常.req={}", Objects.toString(req));
            return RestResponse.error(OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getCode(), OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getDescription());
        }

        otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());
        otaCommonPayload.setSourceType(AppEnums.ClientSourceTypeEnum.FROM_APP.getType());
        otaCommonPayload.setErrorMsg(req.getErrorMsg());
        otaCommonPayload.setErrorCode(req.getErrorCode());

        Object t = null;
        //立即安装/预约安装
        if(req.getInstalledProcessType() == 1 || req.getInstalledProcessType() == 2){
            long installedTime = Instant.now().toEpochMilli() +  86400000L;

            
            otaUpInstallVerifyResult.setInstalledType(req.getInstalledProcessType() == 1 ? 1 : 2);
            otaUpInstallVerifyResult.setVerifyResult(1);
            otaUpInstallVerifyResult.setInstalledTime(installedTime);
            otaProtocol.getBody().setOtaUpInstallVerifyResult(otaUpInstallVerifyResult);

            fotaVersionCheckVerifyService.installedVerifyResult(otaProtocol);
            if(req.getInstalledProcessType() == 1) {
                InstalledVerifyResultVo installedVerifyResultVo = new InstalledVerifyResultVo();
                installedVerifyResultVo.setBody(new InstalledVerifyResultBodyVo());
                t = installedVerifyResultVo;
            }
            if(req.getInstalledProcessType() == 2) {
                InstalledBookedVerifyResultVo installedBookedVerifyResultVo = new InstalledBookedVerifyResultVo();
                installedBookedVerifyResultVo.setBody(new InstalledBookedVerifyResultBodyVo());
                t = installedBookedVerifyResultVo;
            }
        }else if(req.getInstalledProcessType() == 3 || req.getInstalledProcessType() == 4 || req.getInstalledProcessType() == 5){
            //前置条件检查异常/取消升级
            OtaUpInstallProcess otaUpInstallProcess = new OtaUpInstallProcess();
            otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
            otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);

            otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
            otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());

            if(req.getInstalledProcessType().equals(AppEnums.InstalledProcessType4AppEnum.INSTALLED_PRECHECKED_FAIL.getType())){
                otaUpInstallProcess.setInstalledProcessType(Enums.InstalledProcessTypeEnum.INSTALLED_PRECHECK_FAIL.getType());

                otaUpInstallProcess.setUpgradeConditions(Lists.newArrayList());
                IntStream.rangeClosed(0, 5).forEach(item ->{
                    UpgradeCondition upgradeCondition = new UpgradeCondition();
                    upgradeCondition.setCondValue("测试前置条件检查失败"+item);
                    otaUpInstallProcess.getUpgradeConditions().add(upgradeCondition);
                });

                otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);

                //模拟升级进度上报
                fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);
            }else if(req.getInstalledProcessType().equals(AppEnums.InstalledProcessType4AppEnum.INSTALLED_CANCEL.getType())){
                otaUpInstallProcess.setInstalledProcessType(Enums.InstalledProcessTypeEnum.INSTALLED_CANCEL.getType());

                otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);

                //模拟升级进度上报
                fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);
            }
            //安装进度
            else if(req.getInstalledProcessType().equals(AppEnums.InstalledProcessType4AppEnum.INSTALLED_PROCESSING.getType())){
                Runnable r = () -> {
                    OtaDownVersionCheckResponse otaDownVersionCheckResponse = fotaVersionCheckVerifyService.parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());
                    int totalPkgNum = otaDownVersionCheckResponse.getEcuFirmwareVersionInfos().size();
                    otaUpInstallProcess.setTotalPkgNum(totalPkgNum);

                    for(int i=0;i<totalPkgNum;i++){
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        otaUpInstallProcess.setCurrentPkgIndex((long)i+1);
                        otaUpInstallProcess.setInstalledSpendTime(100L);
                        otaUpInstallProcess.setInstalledRemainTime(50L);
                        otaUpInstallProcess.setInstalledProcessType(Enums.InstalledProcessTypeEnum.INSTALLED_PROCESSING.getType());

                        otaUpInstallProcess.setInstallProcessDetails(Lists.newArrayList());
                        InstallProcessDetail installProcessDetail = new InstallProcessDetail();
                        installProcessDetail.setFirmwareCode(otaDownVersionCheckResponse.getEcuFirmwareVersionInfos().get(i).getFirmwareCode());
                        //installProcessDetail.set(otaDownVersionCheckResponse.getEcuFirmwareVersionInfos().get(i).getFirmwareCode());

                        for(int j=0;j<10;j++) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(800);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            installProcessDetail.setInstalledPercentRate((long)(j+1)*10);
                            otaUpInstallProcess.getInstallProcessDetails().add(installProcessDetail);

                            otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);
                            //模拟升级进度上报
                            fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);
                        }
                    }
                };
                executorService.get().submit(r);

                InstalledProcessVo installedProcessVo = new InstalledProcessVo();
                installedProcessVo.setBody(new InstalledProcessBodyVo());
                t = installedProcessVo;
            }else{
                RemoteInstalledCallbackVo remoteInstalledCallbackVo = new RemoteInstalledCallbackVo();
                remoteInstalledCallbackVo.setBody(new RemoteInstalledCallbackBodyVo());
                t = remoteInstalledCallbackVo;

                otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);

                //模拟升级进度上报
                fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);
            }
            /*otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);

            //模拟升级进度上报
            fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);*/

        }else if(req.getInstalledProcessType() == 6){
            //升级成功/升级失败
            OtaUpUpgradeResult otaUpUpgradeResult = new OtaUpUpgradeResult();

            otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
            otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());
            
            otaUpUpgradeResult.setInstalledResult(req.getInstalledResult());

            Long planObjectId = fotaVersionCheckVerifyPo.getOtaPlanObjectId();


            FotaPlanPo fotaPlanPo = fotaPlanDbService.getById(fotaVersionCheckVerifyPo.getOtaPlanId());
            List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.listByOtaStrategyId(fotaPlanPo.getFotaStrategyId());

            Map<Long, FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPoMap = fotaStrategyFirmwareListPos.stream().collect(Collectors.toMap(FotaStrategyFirmwareListPo::getId, Function.identity(), (x, y) -> x));
            //FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListService.getById(planObjectId);
            List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = fotaPlanTaskDetailDbService.listByOtaPlanObjId(planObjectId);

            otaUpUpgradeResult.setStartTime(System.currentTimeMillis() - 18088L);
            otaUpUpgradeResult.setEndTime(System.currentTimeMillis() + 18088L);
            otaUpUpgradeResult.setUpgradeResultDetails(Lists.newArrayList());

            fotaPlanTaskDetailPos.forEach(item -> {
                //FotaPlanFirmwareListPo fotaPlanFirmwareListPo = fotaPlanFirmwareListDbService.getById(item.getOtaPlanFirmwareId());
                FotaStrategyFirmwareListPo fotaStrategyFirmwareListPo = fotaStrategyFirmwareListPoMap.get(item.getOtaPlanFirmwareId());

                FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(fotaStrategyFirmwareListPo.getFirmwareId());
                //fotaFirmwareService.getById(fotaPlanFirmwareListPo.getFirmwareId());
                UpgradeResultDetail upgradeResultDetail1 = new UpgradeResultDetail();
                upgradeResultDetail1.setDiagnose(fotaFirmwarePo.getDiagnose());
                upgradeResultDetail1.setFirmwareCode(fotaFirmwarePo.getFirmwareCode());
                upgradeResultDetail1.setEcuId(fotaFirmwarePo.getComponentCode());
                upgradeResultDetail1.setFirmwareId(fotaFirmwarePo.getId());
                upgradeResultDetail1.setFirmwareVersionNo("V1.2.0");
                otaUpUpgradeResult.getUpgradeResultDetails().add(upgradeResultDetail1);
            });
            if(otaUpUpgradeResult.getInstalledResult() == 1){

            }else if(otaUpUpgradeResult.getInstalledResult() == 2){
                otaUpUpgradeResult.getUpgradeResultDetails().forEach(item ->{
                    item.setErrorCode("100");
                    item.setErrorMsg("出了什么问题我也不知道");
                });
            }else if(otaUpUpgradeResult.getInstalledResult() == 3){

                otaUpUpgradeResult.getUpgradeResultDetails().forEach(item ->{
                    item.setErrorCode("100");
                    item.setErrorMsg("出了什么问题我也不知道");
                });
            }
            otaProtocol.getBody().setOtaCommonPayload(otaCommonPayload);
            otaProtocol.getBody().setOtaUpUpgradeResult(otaUpUpgradeResult);
            fotaVersionCheckVerifyService.upgradeResultReq(otaProtocol);

            InstalledResultVo installedResultVo = new InstalledResultVo();
            installedResultVo.setBody(new InstalledResultBodyVo());
            t = installedResultVo;
        }
        return RestResponse.ok(t);
    }

    @PostMapping(value = "developer4DownloadProcessSync")
    @ApiOperation(value="[供OTA云端调用]__模拟下载进度汇报", notes="[mock接口-供OTA云端调用]__模拟下载进度汇报", response = DownloadResultVo.class)
    public ResponseEntity developer4DownloadProcessSync(@RequestBody DownloadProcessPushMockDo req){
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(req.getVin());
        if(Objects.isNull(fotaVersionCheckVerifyPo)) {
            return RestResponse.error(OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getCode(), OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getDescription());
        }
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(req.getVin());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        
        OtaUpDownloadProcess otaUpDownloadProcess = new OtaUpDownloadProcess();
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
        otaProtocol.getBody().setOtaUpDownloadProcess(otaUpDownloadProcess);
        
        otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getId());
//        otaUpDownloadProcess.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaUpDownloadProcess.setTransId(fotaVersionCheckVerifyPo.getId());

        otaUpDownloadProcess.setDownloadProcessType(req.getDownloadProcessType());

        if(req.getDownloadProcessType().equals(Enums.DownloadProcessTypeEnum.DOWNLOADING.getType())){
            otaUpDownloadProcess.setDownloadFinishedSize(12033L);
            otaUpDownloadProcess.setDownloadFullSize(12033L);
            otaUpDownloadProcess.setDownloadPercentRate(50d);


        }else if(req.getDownloadProcessType().equals(Enums.DownloadProcessTypeEnum.DOWNLOAD_WAIT.getType())){

        }else if(req.getDownloadProcessType().equals(Enums.DownloadProcessTypeEnum.DOWNLOAD_STOP.getType())){

        }else if(req.getDownloadProcessType().equals(Enums.DownloadProcessTypeEnum.DOWNLOAD_FINISHED.getType())){

        }else if(req.getDownloadProcessType().equals(Enums.DownloadProcessTypeEnum.DOWNLOAD_FINISHED_SUCCESS.getType())){
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
        }else if(req.getDownloadProcessType().equals(Enums.DownloadProcessTypeEnum.DOWNLOAD_FINISHED_FAIL.getType())){
            otaUpDownloadProcess.setDownloadProcessDetails(Lists.newArrayList());
            DownloadProcessDetail detail1 = new DownloadProcessDetail();
            detail1.setDownloadSize(1000L);
            detail1.setDownloadStatus(2);
            detail1.setPkgId(200L);
            detail1.setAccumulateNum(3);
            Lists.newArrayList().add(detail1);

            DownloadProcessDetail detail2 = new DownloadProcessDetail();
            detail2.setDownloadSize(1000L);
            detail2.setDownloadStatus(2);
            detail2.setPkgId(200L);
            detail2.setAccumulateNum(3);
            Lists.newArrayList().add(detail2);
        }

        fotaVersionCheckVerifyService.downloadProcessReq(otaProtocol);

        /*DownloadResultVo downloadResultVO = new DownloadResultVo();
        set4BaseAppVo(downloadResultVO, req.getVin(), com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum.OTA_UP_DOWNLOAD_PROCESS);
        DownloadProcessBodyVo downloadProcessBodyVO = new DownloadProcessBodyVo();
        downloadProcessBodyVO = OtaMessageMapper.INSTANCE.otaUpDownloadProcess2DownloadProcessBodyVo(otaUpDownloadProcess);
        downloadResultVO.setBody(downloadProcessBodyVO);*/
        return RestResponse.ok(null);
    }

    @PostMapping(value = "developer4InstalledProcessSync")
    @ApiOperation(value="[供OTA云端调用]__模拟安装进度汇报", notes="[mock接口-供OTA云端调用]__模拟安装进度汇报", response = InstalledProcessVo.class)
    public ResponseEntity developer4InstalledProcessSync(@RequestBody InstalledProcessPushMockDo req){
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(req.getVin());
        if(Objects.isNull(fotaVersionCheckVerifyPo)) {
            return RestResponse.error(OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getCode(), OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST.getDescription());
        }

        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(req.getVin());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        
        OtaUpInstallProcess otaUpInstallProcess = new OtaUpInstallProcess();
        otaProtocol.getBody().setBusinessId(LONG_NUMBER_ONE);
        otaProtocol.getBody().setOtaUpInstallProcess(otaUpInstallProcess);
        
        otaCommonPayload.setTaskId(fotaVersionCheckVerifyPo.getOtaPlanId());
        otaCommonPayload.setTransId(fotaVersionCheckVerifyPo.getOtaPlanId());
        
        otaUpInstallProcess.setInstalledProcessType(req.getInstalledProcessType());

        InstalledProcessVo installedProcessVO = new InstalledProcessVo();
        set4BaseAppVo(installedProcessVO, req.getVin(), com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum.OTA_UP_INSTALLED_PROCESS);
        InstalledProcessBodyVo installedProcessBodyVO = new InstalledProcessBodyVo();

        if(req.getInstalledProcessType().equals(Enums.InstalledProcessTypeEnum.INSTALLED_PRECHECK_FAIL.getType())){

        }else if(req.getInstalledProcessType().equals(Enums.InstalledProcessTypeEnum.INSTALLED_CANCEL.getType())){

        }else if(req.getInstalledProcessType().equals(Enums.InstalledProcessTypeEnum.INSTALLED_PROCESSING.getType())){
            otaUpInstallProcess.setTotalPkgNum(9);
            otaUpInstallProcess.setCurrentPkgIndex(2L);
            otaUpInstallProcess.setInstalledSpendTime(100L);
            otaUpInstallProcess.setInstalledRemainTime(50L);
        }

        //模拟升级进度上报
        fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);

        return RestResponse.ok(null);
    }

    @PostMapping(value = "getUserRegistrationId")
    @ApiOperation(value="测试获取TSP用户", notes="测试获取TSP用户", response = InstalledResultVo.class)
    public ResponseEntity getUserRegistrationId(@RequestBody PushMockDto req){
        return RestResponse.ok(push4AppService.getUserRegistrationId(req.getVin()));
    }

    @PostMapping(value = "testPush2App")
    @ApiOperation(value="测试推送消息到APp", notes="测试推送消息到APp")
    public ResponseEntity testPush2App(@RequestBody MessgeCenterMockDto messgeCenterMockDto){
        String vin = Objects.isNull(messgeCenterMockDto) ? "20200712082200001" : messgeCenterMockDto.getVin();
        return RestResponse.ok(push4AppService.push2App("10086", vin, 1L));
    }

    @PostMapping(value = "push2MessageCenter")
    @ApiOperation(value="测试推送消息到消息中心", notes="测试推送消息到消息中心")
    public ResponseEntity push2MessageCenter(@RequestBody MessgeCenterMockDto messgeCenterMockDto){
        String vin = Objects.isNull(messgeCenterMockDto) ? "20200712082200001" : messgeCenterMockDto.getVin();
        AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.getByType(messgeCenterMockDto.getTypeId());
        /*if(AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL.equals(messageCenterMsgTypeEnum) || AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL_WITHOUT_ENOUGH_SPACE.equals(messageCenterMsgTypeEnum)){
            messageCenterMsgTypeEnum = messgeCenterMockDto.getFailType() == 1 ? AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL_WITHOUT_ENOUGH_SPACE : AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL;
        }

        if(AppEnums.MessageCenterMsgTypeEnum.INSTALLED_COMLETE_FAIL.equals(messageCenterMsgTypeEnum) || AppEnums.MessageCenterMsgTypeEnum.INSTALLED_FAIL.equals(messageCenterMsgTypeEnum)){
            messageCenterMsgTypeEnum = messgeCenterMockDto.getFailType() == 1 ? AppEnums.MessageCenterMsgTypeEnum.INSTALLED_COMLETE_FAIL : AppEnums.MessageCenterMsgTypeEnum.INSTALLED_FAIL;
        }

        if(null == messageCenterMsgTypeEnum) {
            messageCenterMsgTypeEnum = messgeCenterMockDto.getFailType() == 1 ? AppEnums.MessageCenterMsgTypeEnum.INSTALLED_COMLETE_FAIL : AppEnums.MessageCenterMsgTypeEnum.INSTALLED_FAIL;
        }*/
        return RestResponse.ok(push4AppService.push2MessageCenter(/*"10086",*/ vin, messageCenterMsgTypeEnum, null));
    }

    @GetMapping(value = "newVersionFromOta")
    @ApiOperation(value="测试推送云端新版本通知到TBOX", notes="测试推送云端新版本通知到TBOX")
    public ResponseEntity newVersionFromOta(@RequestParam String vin){
        return RestResponse.ok(fotaVersionCheckService.newVersionFromOta(vin));
    }

    @GetMapping(value = "getDescriptionList")
    @ApiOperation(value="获取状态码列表", notes="获取状态码列表", response = TBoxRespCodeEnum.class)
    public ResponseEntity getDescriptionList(){
        return RestResponse.ok(TBoxRespCodeEnum.getDescriptionList());
    }
}
