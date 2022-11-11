package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.sdk.ota.domain.EcuFirmwareVersionInfo;
import com.bnmotor.icv.adam.sdk.ota.domain.InstallProcessDetail;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaCommonPayload;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.adam.sdk.ota.up.*;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventKit;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventType;
import com.bnmotor.icv.tsp.ota.event.OtaDispatch4OtherMessage;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeMessageBase;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeNewVersionMessage;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeOtherMessage;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.resp.app.*;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author xxc
 * @ClassName: FotaVersionCheckVerifyServiceImpl
 * @Description: OTA升级版本结果确认表服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-15
 */

@Service
@Slf4j
public class FotaVersionCheckVerifyServiceImpl implements IFotaVersionCheckVerifyService{
    @Autowired
    private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Autowired
    private IFotaFirmwareListDbService fotaFirmwareListDbService;

    @Autowired
    private IFotaPlanTaskDetailDbService fotaPlanTaskDetailDbService;

    @Autowired
    private IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private ICommonExecutor commonExecutor;

    @Autowired
    private IFotaVersionCheckDbService fotaVersionCheckDbService;

    @Autowired
    private OtaEventKit otaEventKit;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    /**
     * 获取升级事务关联的升级车辆对象
     * 添加了一些验证信息
     * @param transId
     * @return
     */
    @Override
    public FotaPlanObjListPo findFotaPlanObjListPoWithTransId(Long transId){
        /*
            数据校验：
            1、升级事务是否存在
            2、升级对象是否存在
         */
        FotaVersionCheckVerifyPo existEntity = fotaVersionCheckVerifyDbService.getById(transId);
        MyAssertUtil.notNull(existEntity, OTARespCodeEnum.FOTA_PLAN_OBJ_LIST_NOT_EXIST);

        FotaPlanObjListPo entity = fotaPlanObjListDbService.getById(existEntity.getOtaPlanObjectId());
        MyAssertUtil.notNull(entity, OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST);
        return entity;
    }

    /**
     * 获取升级事务及对象，如果数据异常，云端需要由记录
     * @param otaProtocol
     * @return
     */
    private FotaVinCacheInfo checkTrans(final OtaProtocol otaProtocol){
        String vin = otaProtocol.getOtaMessageHeader().getVin();
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
        MyAssertUtil.notNull(fotaVinCacheInfo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_OBJECT_NOT_EXIST);
        OtaCommonPayload otaCommonPayload = otaProtocol.getBody().getOtaCommonPayload();
        boolean transIdEquals = Objects.nonNull(otaCommonPayload) && Objects.nonNull(otaCommonPayload.getTransId()) && otaCommonPayload.getTransId().equals(fotaVinCacheInfo.getTransId());
        MyAssertUtil.isTrue(transIdEquals, TBoxRespCodeEnum.OTA_RESP_CODE_PARAM_ERROR_TRANS_ID);
        return fotaVinCacheInfo;
    }

    @Override
    public OtaProtocol judgeFotaPlanValid(OtaProtocol req) {
        //定义获取配置的实现
        Consumer<OtaProtocol> respConsumer = resp -> {
            boolean isValid = ifFotaPlanValid(req);
            if(isValid){
                resp.getBody().getOtaCommonPayload().setErrorCode(TBoxRespCodeEnum.SUCCESS.getValue());
                resp.getBody().getOtaCommonPayload().setErrorMsg(TBoxRespCodeEnum.SUCCESS.getDesc());
            }else{
                resp.getBody().getOtaCommonPayload().setErrorCode(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_PLAN_INVALID.getValue());
                resp.getBody().getOtaCommonPayload().setErrorMsg(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_PLAN_INVALID.getDesc());
            }
        };
        return TBoxUtil.wrapTBoxUpBusiness(req, respConsumer, BusinessTypeEnum.OTA_DOWN_PLAN_IF_VALID);
    }

    /**
     * 判断当前任务是否有效
     * @param req
     * @return
     */
    private boolean ifFotaPlanValid(OtaProtocol req){
        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(req);
        String vin = req.getOtaMessageHeader().getVin();
        //FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
        if(Objects.isNull(fotaVinCacheInfo)){
            log.info("获取车辆vin缓存不存在.vin={}", vin);
            return false;
        }
        //检查是否存在有效的升级任务
        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaVinCacheInfo.getOtaPlanId());
        if(Objects.isNull(fotaPlanPo)){
            log.info("获取升级任务失败.vin={}", vin);
            return false;
        }
        boolean validPlanPo = MyBusinessUtil.validPlanPo(fotaPlanPo);
        if(!validPlanPo){
            log.info("升级任务已经失效.vin={}", vin);
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadVerifyReq(OtaProtocol otaProtocol) {
        /*
            1、更新升级确认表中对应的升级（远程下载）确认请求对应字段
            2、更新升级对象表中升级状态字段信息
                需要考虑并发的情况，条件为只有当前升级对象升级状态在待更新的阶段状态之前才执行更新
         */
        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
    	Long transId = fotaVinCacheInfo.getTransId();

        FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
        entity.setId(transId);
        LocalDateTime dateTime = LocalDateTime.now();
        entity.setCheckAckVerifyStatus(1);
        entity.setCheckAckVerifyTime(dateTime);
        entity.setUpgradeReqTime(dateTime);
        entity.setUpdateTime(dateTime);
        entity.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        boolean update = fotaVersionCheckVerifyDbService.updateById(entity);

        FotaPlanObjListPo existFotaPlanObjListPo;
        if (update) {
            existFotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVinCacheInfo.getOtaPlanObjectId());
            //任务状态为“待升级下载”之前的升级任务状态才更新为“待升级下载”
            /*if (existFotaPlanObjListPo.getStatus() <= Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_WAIT.getType()) {*/
            updateFotaPlanObjList(existFotaPlanObjListPo.getId(), Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_WAIT);

            //TODO 转发到APP
            final Supplier<FotaObjectPo> fotaObjectPoSupplier = () -> fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
            //使用缓存获取
            final Supplier<FotaPlanPo> fotaPlanPoSupplier = () -> fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaVinCacheInfo.getOtaPlanId());
            final Supplier<OtaDownVersionCheckResponse> otaDownVersionCheckResponseSupplier = () -> parseFromFotaVersionCheckPo(fotaVinCacheInfo.getCheckReqId());
            commonExecutor.execute(()->{
                Long reqId = otaProtocol.getBody().getOtaCommonPayload().getReqId();
                OtaUpgradeNewVersionMessage otaUpgradeNewVersionMessage = OtaUpgradeNewVersionMessage.builder().newVersion(true).otaProtocol(otaProtocol).transId(transId).reqId(reqId).fotaObjectPoSupplier(fotaObjectPoSupplier).fotaPlanPoSupplier(fotaPlanPoSupplier).otaDownVersionCheckResponseSupplier(otaDownVersionCheckResponseSupplier).build();
                OtaUpgradeMessageBase otaUpgradeMessageBase = OtaUpgradeMessageBase.builder().otaUpgradeMessageType(Enums.OtaUpgradeMessageTypeEnum.NEW_VERSION.getType()).data(otaUpgradeNewVersionMessage).messageCenterMsgTypeEnum(AppEnums.MessageCenterMsgTypeEnum.NEW_VERSION).vin(otaProtocol.getOtaMessageHeader().getVin()).build();
                otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_UPGRADE_MESSAGE, otaUpgradeMessageBase);
            },"异步推送下载请求[包括版本检查结果]到APP");
            /*} else {
                log.warn("[升级任务状态更新]当前状态异常。existFotaPlanObjListPo={}", existFotaPlanObjListPo.toString());
            }*/
        }else{
            log.warn("[更新升级事务确认表-升级确认]异常。transId={}", transId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void installedVerifyReq(OtaProtocol otaProtocol) {
        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();

        //保证来自TBOX端的升级（下载）确认请求记录到数据库
        FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
        entity.setId(transId);
        LocalDateTime dateTime = LocalDateTime.now();
        entity.setInstalledReqTime(dateTime);
        entity.setUpdateTime(dateTime);
        entity.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        boolean update = fotaVersionCheckVerifyDbService.updateById(entity);

        if (update) {
            FotaPlanObjListPo existFotaPlanObjListPo = findFotaPlanObjListPoWithTransId(transId);
            //任务状态为“待安装确认”确认之前的升级阶段状态才更新为“待安装确认”
            if (existFotaPlanObjListPo.getStatus() <= Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_WAIT.getType()) {
                boolean updateStatus = updateFotaPlanObjList(existFotaPlanObjListPo.getId(), dateTime, Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_WAIT);
                log.info("[升级任务状态更新]。updateStatus={}", updateStatus);
                /*if(!updateStatus) {
                    log.warn("[升级任务状态更新]出现并发异常。updateStatus={}", updateStatus);
                }*/
            } else {
                log.warn("[更新OTA任务升级对象状态异常].existFotaPlanObjListPo={}", existFotaPlanObjListPo.toString());
            }
        }else{
            log.warn("[更新升级事务确认表-安装确认请求字段]异常。transId={}", transId);
        }
    }

    /**
     * 更新升级常车辆升级状态
     * @param otaPlanObjectId
     * @param taskObjStatusTypeEnum
     * @return
     */
    private boolean updateFotaPlanObjList(Long otaPlanObjectId, Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum) {
        return updateFotaPlanObjList(otaPlanObjectId, LocalDateTime.now(), taskObjStatusTypeEnum);
    }

    /**
     * 更新升级车辆升级状态
     * @param otaPlanObjectId
     * @param now
     * @param taskObjStatusTypeEnum
     * @return
     */
    private boolean updateFotaPlanObjList(Long otaPlanObjectId, final LocalDateTime now, Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum) {
        FotaPlanObjListPo fotaPlanObjListPo = new FotaPlanObjListPo();
        fotaPlanObjListPo.setId(otaPlanObjectId);
        fotaPlanObjListPo.setUpdateTime(now);
        fotaPlanObjListPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        fotaPlanObjListPo.setStatus(taskObjStatusTypeEnum.getType());
        if(Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS.equals(taskObjStatusTypeEnum)) {
            fotaPlanObjListPo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_COMPLETED.getType());
        }
        else if(Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.equals(taskObjStatusTypeEnum)){
            fotaPlanObjListPo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_UNCOMPLETED.getType());
        } else if(Enums.TaskObjStatusTypeEnum.INSTALLED_FAIL.equals(taskObjStatusTypeEnum)){
            fotaPlanObjListPo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_FAIL.getType());
        }

        return fotaPlanObjListDbService.updateById(fotaPlanObjListPo);
    }

    /**
     * 更新状态-升级结果
     * @param otaPlanObjectId
     * @param taskObjStatusTypeEnum
     * @return
     */
    private boolean updateFotaPlanObjList4Result(Long otaPlanObjectId, String otaPlanTargetVersion, Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum) {
        FotaPlanObjListPo fotaPlanObjListPo = new FotaPlanObjListPo();
        fotaPlanObjListPo.setId(otaPlanObjectId);
        fotaPlanObjListPo.setUpdateTime(LocalDateTime.now());
        fotaPlanObjListPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        fotaPlanObjListPo.setStatus(taskObjStatusTypeEnum.getType());
        if(Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_SUCCESS.equals(taskObjStatusTypeEnum)) {
            fotaPlanObjListPo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_COMPLETED.getType());
            fotaPlanObjListPo.setCurrentVersion(otaPlanTargetVersion);
        }
        else if(Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.equals(taskObjStatusTypeEnum)){
            fotaPlanObjListPo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_UNCOMPLETED.getType());
        } else if(Enums.TaskObjStatusTypeEnum.INSTALLED_FAIL.equals(taskObjStatusTypeEnum)){
            fotaPlanObjListPo.setResult(Enums.UpgradeResultTypeEnum.UPGRADE_FAIL.getType());
        }

        return fotaPlanObjListDbService.updateById(fotaPlanObjListPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadVerifyResult(OtaProtocol otaProtocol) {
        OtaUpUpgradeVerifyResult otaUpUpgradeVerifyResult = otaProtocol.getBody().getOtaUpUpgradeVerifyResult();
        MyAssertUtil.notNull(otaUpUpgradeVerifyResult, "[安装确认结果参数不能为空]");


        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();

        FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
        entity.setId(transId);
        entity.setUpgradeVerifyStatus(otaUpUpgradeVerifyResult.getVerifyResult());
        entity.setUpgradeVerifySource(otaProtocol.getBody().getOtaCommonPayload().getSourceType());
        entity.setUpgradeVerifyImmediate(otaUpUpgradeVerifyResult.getImmediateDownload());
        LocalDateTime dateTime = LocalDateTime.now();
        entity.setUpgradeVerifyTime(dateTime);
        entity.setUpdateTime(dateTime);
        entity.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        boolean update =  fotaVersionCheckVerifyDbService.updateById(entity);

        if (update) {
            FotaPlanObjListPo existFotaPlanObjListPo = findFotaPlanObjListPoWithTransId(transId);
            boolean isDownloadCompletedFail = existFotaPlanObjListPo.getStatus() == Enums.TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_FAIL.getType();
            /*
                1、任务状态为“待升级下载”才更新为“升级下载中”
                2、下载失败的情况允许重新操作
             */
            //TODO
            if (isDownloadCompletedFail || existFotaPlanObjListPo.getStatus() <= Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_BEGIN.getType()) {
                /*boolean updateStatus = */updateFotaPlanObjList(existFotaPlanObjListPo.getId(), Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_BEGIN);
                /*log.info("更新任务状态:updateStatus={}, vin={}", updateStatus, otaProtocol.getOtaMessageHeader().getVin());
                if(updateStatus) {*/
                    String vin = otaProtocol.getOtaMessageHeader().getVin();
                    DownloadVerfiyResultVo downloadVerfiyResultVo = new DownloadVerfiyResultVo();
                    buildBaseAppVo(downloadVerfiyResultVo, otaProtocol);
                    downloadVerfiyResultVo.setBusinessType(AppEnums.AppResponseTypeEnum.DOWNLOAD_VERIFIED_RESPONSE.getType());

                    DownloadVerfiyResultBodyVo downloadVerfiyResultBodyVo = new DownloadVerfiyResultBodyVo();
                    buildBaseAppBodyVo(downloadVerfiyResultBodyVo, transId, fotaVinCacheInfo.getOtaPlanId());
                    downloadVerfiyResultBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_VERIFY_RESULT.getType());
                    downloadVerfiyResultBodyVo.setVerifyResult(otaUpUpgradeVerifyResult.getVerifyResult());
                    downloadVerfiyResultBodyVo.setVerifySource(otaProtocol.getBody().getOtaCommonPayload().getSourceType());
                    int immediateDownload = Objects.nonNull(otaUpUpgradeVerifyResult.getImmediateDownload()) ? otaUpUpgradeVerifyResult.getImmediateDownload() : 0;
                    downloadVerfiyResultBodyVo.setImmediateDownload(immediateDownload);
                    //延迟下载

                    if (immediateDownload == Enums.DownloadResultVerifyTypeEnum.DELAY.getType()) {
                        downloadVerfiyResultBodyVo.setRespCode(otaProtocol.getBody().getOtaCommonPayload().getErrorCode());
                        downloadVerfiyResultBodyVo.setMsg(otaProtocol.getBody().getOtaCommonPayload().getErrorMsg());
                    }else if(immediateDownload == Enums.DownloadResultVerifyTypeEnum.IMMEDIATE.getType()){
                        //立即下载补充
                        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(fotaVinCacheInfo.getTransId());
                        if(Objects.nonNull(fotaVersionCheckVerifyPo)){
                            //此处需要注意TBOX端上传的总大小和数据库中保存的升级包总大小
                            downloadVerfiyResultBodyVo.setDownloadTotal(fotaVersionCheckVerifyPo.getDownloadFullSize());
                            downloadVerfiyResultBodyVo.setDownloadFinished(MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadFinishedSize(), 0L));
                            downloadVerfiyResultBodyVo.setDownloadPercent(MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadPercentRate(), 0));
                            downloadVerfiyResultBodyVo.setDownloadRemainTime(MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadRemainTime(), 0));
                        }else{
                            log.warn("fotaVersionCheckVerifyPo异常.fotaVinCacheInfo.getTransId()={}", fotaVinCacheInfo.getTransId());
                        }
                    }

                    downloadVerfiyResultVo.setBody(downloadVerfiyResultBodyVo);
                    commonExecutor.execute(() -> {
                        OtaUpgradeOtherMessage otaUpgradeOtherMessage = OtaUpgradeOtherMessage.builder().t(downloadVerfiyResultVo).reqId(0L).build();
                        OtaUpgradeMessageBase otaUpgradeMessageBase = OtaUpgradeMessageBase.builder().data(otaUpgradeOtherMessage).vin(vin).otaUpgradeMessageType(Enums.OtaUpgradeMessageTypeEnum.OTHER.getType()).build();
                        otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_UPGRADE_MESSAGE, otaUpgradeMessageBase);
                    }, "推送下载确认结果异常");
                /*}*/
            } else {
                log.warn("[更新升级对象升级任务状态异常].existFotaPlanObjListPo={}", existFotaPlanObjListPo.toString());
            }
        } else {
            log.warn("[更新升级事务确认表对应下载确认相关字段异常].req={}", otaProtocol.toString());
        }
    }

    /**
     * 发送事务消息
     * @param t
     * @param otaProtocol
     * @param messageCenterMsgTypeEnum
     * @param <T>
     */
    private <T> void publishOtaTransactionEvent(T t, OtaProtocol otaProtocol, AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum, OtaDispatch4OtherMessage otaDispatch4OtherMessage){
        OtaUpgradeOtherMessage otaUpgradeOtherMessage = OtaUpgradeOtherMessage.builder().t(t).reqId(otaProtocol.getBody().getOtaCommonPayload().getReqId()).build();
        OtaUpgradeMessageBase otaUpgradeMessageBase = OtaUpgradeMessageBase.builder().data(otaUpgradeOtherMessage).vin(otaProtocol.getOtaMessageHeader().getVin()).otaUpgradeMessageType(Enums.OtaUpgradeMessageTypeEnum.OTHER.getType()).messageCenterMsgTypeEnum(messageCenterMsgTypeEnum).otaDispatch4OtherMessage(otaDispatch4OtherMessage).build();
        otaEventKit.publishOtaTransactionEvent(OtaEventType.OTA_UPGRADE_MESSAGE, otaUpgradeMessageBase);
    }

    /**
     * 发送事务消息
     * @param t
     * @param otaProtocol
     * @param <T>
     */
    private <T> void publishOtaTransactionEvent(T t, OtaProtocol otaProtocol){
        publishOtaTransactionEvent(t, otaProtocol, null, null);
    }

    /**
     * 推送到通知中心
     * @param otaProtocol
     * @param messageCenterMsgTypeEnum
     * @param <T>
     */
    private <T> void publishOtaTransactionEvent(OtaProtocol otaProtocol, AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum){
        publishOtaTransactionEvent(null, otaProtocol, messageCenterMsgTypeEnum, null);
    }

    /*
     * 下载开始消息推送给APP--实际借助下载中消息来实现APP端显示“下载中”效果
     * @param otaProtocol
     * @param transId
     * @param taskId
     */
    /*private void pushDownloadStart4App(OtaProtocol otaProtocol, Long transId, Long taskId){
        DownloadVerfiyResultVo downloadVerfiyResultVo = new DownloadVerfiyResultVo();
        downloadVerfiyResultVo.setBusinessId(otaProtocol.getBody().getBusinessId());

        DownloadVerfiyResultBodyVo downloadVerfiyResultBodyVo = new DownloadVerfiyResultBodyVo();
        buildBaseAppBodyVo(downloadVerfiyResultBodyVo, transId, taskId);
        downloadVerfiyResultBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_VERIFY_RESULT.getType());
        downloadVerfiyResultBodyVo.setImmediateDownload(Enums.DownloadResultVerifyTypeEnum.IMMEDIATE.getType());
        downloadVerfiyResultVo.setBody(downloadVerfiyResultBodyVo);

        commonExecutor.execute(() -> publishOtaTransactionEvent(downloadVerfiyResultVo, otaProtocol), "推送下载确认结果异常");
    }*/

    /**
     * 下载完成，推送结果到APP
     * @param otaProtocol
     * @param fotaVinCacheInfo
     */
    private void pushDownloadFinished4App(OtaProtocol otaProtocol, FotaVinCacheInfo fotaVinCacheInfo){
        //转发到APP
        OtaCommonPayload otaCommonPayload = otaProtocol.getBody().getOtaCommonPayload();
        log.info("otaCommonPayload={}", otaCommonPayload);
        /*DownloadResultVo downloadProcessVo = new DownloadResultVo();*/
        DownloadResultBodyVo downloadResultBodyVo = new DownloadResultBodyVo();

        AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum = null;
        //面向APP的下载阶段返回与系统定义的下载阶段不一致，需要进行转换
        boolean downloadSuccess = Enums.DownloadProcessTypeEnum.DOWNLOAD_FINISHED_SUCCESS.getType() == otaProtocol.getBody().getOtaUpDownloadProcess().getDownloadProcessType();
        if (downloadSuccess) {
            messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_SUCCESS;

            downloadResultBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_FINISHED_SUCCESS.getType());
            //下载成功补充添加预计安装时间
            OtaDownVersionCheckResponse otaDownVersionCheckResponse = parseFromFotaVersionCheckPo(fotaVinCacheInfo.getCheckReqId());
            //downloadResultBodyVo.setEstimateInstalledTime(MyObjectUtil.getOrDefaultT(otaDownVersionCheckResponse.getEstimatedUpgradeTime(), 0));
            downloadResultBodyVo.setInstalledRemainTime(MyObjectUtil.getOrDefaultT(otaDownVersionCheckResponse.getEstimatedUpgradeTime(), 0));
        } else{
            boolean checkFail = TBoxRespCodeEnum.downloadFail(otaCommonPayload.getErrorCode());
            log.info("checkFail={}", checkFail);
            if(checkFail) {
                //硬盘空间不足
                if (TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getCode().equals(otaCommonPayload.getErrorCode())) {
                    messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL_WITHOUT_ENOUGH_SPACE;
                } else if (TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode().equals(otaCommonPayload.getErrorCode())) {
                    //下载包校验失败
                    messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL;
                }
                downloadResultBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_FINISHED_FAIL.getType());
            }else{
                log.warn("下载完成推送消息,判断下载失败为假,未知下载结果类型");
            }
        }
        if (Objects.nonNull(messageCenterMsgTypeEnum)) {
            log.info("推送下载进度消息：messageCenterMsgTypeEnum={}", messageCenterMsgTypeEnum);
            //publishOtaTransactionEvent(downloadProcessVo, otaProtocol, messageCenterMsgTypeEnum, null);

            DownloadResultVo downloadProcessVo = new DownloadResultVo();
            buildBaseAppVo(downloadProcessVo, otaProtocol);
            downloadProcessVo.setBusinessType(AppEnums.AppResponseTypeEnum.DOWNLOAD_VERIFIED_RESPONSE.getType());
            /*DownloadResultBodyVo downloadResultBodyVo = new DownloadResultBodyVo();*/
            buildBaseAppBodyVo(downloadResultBodyVo, fotaVinCacheInfo.getTransId(), fotaVinCacheInfo.getOtaPlanId());

            //面向APP的下载阶段返回与系统定义的下载阶段不一致，需要进行转换
            //boolean downloadSuccess = Enums.DownloadProcessTypeEnum.DOWNLOAD_FINISHED_SUCCESS.getType() == otaProtocol.getBody().getOtaUpDownloadProcess().getDownloadProcessType();
            /*if(downloadSuccess){
                downloadResultBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_FINISHED_SUCCESS.getType());
                //下载成功补充添加预计安装时间
                OtaDownVersionCheckResponse otaDownVersionCheckResponse = parseFromFotaVersionCheckPo(fotaVinCacheInfo.getCheckReqId());
                //downloadResultBodyVo.setEstimateInstalledTime(MyObjectUtil.getOrDefaultT(otaDownVersionCheckResponse.getEstimatedUpgradeTime(), 0));
                downloadResultBodyVo.setInstalledRemainTime(MyObjectUtil.getOrDefaultT(otaDownVersionCheckResponse.getEstimatedUpgradeTime(), 0));
            }else{
                downloadResultBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_FINISHED_FAIL.getType());
            }*/


            downloadResultBodyVo.setRespCode(otaCommonPayload.getErrorCode());
            downloadResultBodyVo.setMsg(otaCommonPayload.getErrorMsg());

            downloadProcessVo.setBody(downloadResultBodyVo);
            AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnumT = messageCenterMsgTypeEnum;
            commonExecutor.execute(() -> {
                //添加推送到消息中心:需要区分推送到APP的消息类型
                /*boolean checkFail = TBoxRespCodeEnum.downloadFail(otaCommonPayload.getErrorCode());
                AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum = null;
                if (downloadSuccess) {
                    messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_SUCCESS;
                } else if (checkFail) {
                    //硬盘空间空间不足
                    if (TBoxRespCodeEnum.APP_RESP_CODE_TBOX_NOT_ENOUGH_SPACE_4DOWNLOAD.getCode().equals(otaCommonPayload.getErrorCode())) {
                        messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL_WITHOUT_ENOUGH_SPACE;
                    } else if (TBoxRespCodeEnum.APP_RESP_CODE_TBOX_CHECK_FAIL_4DOWNLOAD.getCode().equals(otaCommonPayload.getErrorCode())) {
                        //下载包校验失败
                        messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_COMPLETE_FAIL;
                    }
                }*/
                publishOtaTransactionEvent(downloadProcessVo, otaProtocol, messageCenterMsgTypeEnumT, null);
                /*if (Objects.nonNull(messageCenterMsgTypeEnum)) {
                    log.info("推送下载进度消息：messageCenterMsgTypeEnum={}", messageCenterMsgTypeEnum);
                    publishOtaTransactionEvent(downloadProcessVo, otaProtocol, messageCenterMsgTypeEnum, null);
                }else{
                    log.warn("推送下载进度消息类型异常：messageCenterMsgTypeEnum={}", messageCenterMsgTypeEnum);
                }*/
            }, "推送下载进度消息到APP异常");

        }else{
            log.warn("推送下载进度消息类型异常：messageCenterMsgTypeEnum={}", messageCenterMsgTypeEnum);
        }
    }

    /**
     * 推送下载中进度
     *
     * @param otaProtocol
     * @param fotaVinCacheInfo
     */
    private void pushDownloading4App(OtaProtocol otaProtocol, FotaVinCacheInfo fotaVinCacheInfo){
        if(Objects.isNull(otaProtocol.getBody().getOtaUpDownloadProcess().getDownloadFullSize()) || Objects.isNull(otaProtocol.getBody().getOtaUpDownloadProcess().getDownloadFinishedSize())){
            log.warn("第一次上传进度，无实际数据，放弃推送.fotaVinCacheInfo={}", fotaVinCacheInfo);
            return;
        }
        DownloadProcessVo downloadProcessVo = new DownloadProcessVo();
        downloadProcessVo.setBusinessId(otaProtocol.getBody().getBusinessId());

        DownloadProcessBodyVo downloadProcessBodyVo = new DownloadProcessBodyVo();
        buildBaseAppBodyVo(downloadProcessBodyVo, fotaVinCacheInfo.getTransId(), fotaVinCacheInfo.getOtaPlanId());
        downloadProcessBodyVo.setDownloadProcessType(AppEnums.DownloadProcessType4AppEnum.DOWNLOAD_PROGRESS.getType());

        OtaUpDownloadProcess otaUpDownloadProcess = otaProtocol.getBody().getOtaUpDownloadProcess();

        downloadProcessBodyVo.setDownloadTotal(otaUpDownloadProcess.getDownloadFullSize());
        downloadProcessBodyVo.setDownloadFinished(otaUpDownloadProcess.getDownloadFinishedSize());
        //设置默认上行过来的预计下载剩余时间
        downloadProcessBodyVo.setDownloadRemainTime(otaUpDownloadProcess.getEstimateRemainTime());
        //计算下载百分比
        long finished = Objects.nonNull(otaUpDownloadProcess.getDownloadFinishedSize()) ? otaUpDownloadProcess.getDownloadFinishedSize().longValue() : 0L;
        long all = Objects.nonNull(otaUpDownloadProcess.getDownloadFullSize()) ? otaUpDownloadProcess.getDownloadFullSize().longValue() : 0L;
        int downloadPercentRate = (int)(new BigDecimal(finished).divide(new BigDecimal(all), 2, RoundingMode.CEILING).doubleValue()*100);
        downloadProcessBodyVo.setDownloadPercentRate(downloadPercentRate);
        downloadProcessVo.setBody(downloadProcessBodyVo);

        commonExecutor.execute(() -> publishOtaTransactionEvent(downloadProcessVo, otaProtocol), "推送下载确认结果异常");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void installedVerifyResult(OtaProtocol otaProtocol) {
        OtaUpInstallVerifyResult otaUpInstallVerifyResult = otaProtocol.getBody().getOtaUpInstallVerifyResult();
        MyAssertUtil.notNull(otaUpInstallVerifyResult, "[安装确认结果参数不能为空]");

        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();

        LocalDateTime dateTime = LocalDateTime.now();
        FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
        entity.setId(transId);
        entity.setInstalledAckVerifyStatus(1);
        entity.setInstalledAckVerifyTime(dateTime);

        //安装确认结果：1=安装，2=取消安装
        entity.setInstalledVerifyStatus(otaUpInstallVerifyResult.getVerifyResult());
        //安装确认来源
        entity.setInstalledVerifySource(otaProtocol.getBody().getOtaCommonPayload().getSourceType());

        entity.setInstalledVerifyTime(dateTime);
        CommonUtil.wrapBasePo4Update(entity, null, dateTime);

        boolean verifyInstalled = otaUpInstallVerifyResult.getVerifyResult().intValue() == 1;
        //确认安装
        Date installedVerifyBookedTime = null;
        if(verifyInstalled){
            if (otaUpInstallVerifyResult.getInstalledType().equals(AppEnums.InstalledType4AppEnum.INSTALLED_BOOKED.getType()) && Objects.nonNull(otaUpInstallVerifyResult.getInstalledTime())) {
                installedVerifyBookedTime = new Date(otaUpInstallVerifyResult.getInstalledTime());
            }
        }
        UpdateWrapper<FotaVersionCheckVerifyPo> updateWrapper = fotaVersionCheckVerifyDbService.build4InstalledVerifyBookedTime(entity.getId(), installedVerifyBookedTime);
        boolean update =  fotaVersionCheckVerifyDbService.update(entity, updateWrapper);
        if (update) {
            FotaPlanObjListPo existFotaPlanObjListPo = findFotaPlanObjListPoWithTransId(transId);

            /*
                允许“安装确认”操作情况
                1、任务状态为“待安装确认”才更新为“升级安装中”
                2、已取消
                3、前置条件检查失败
                4、安装失败
             */
            boolean isCancle = existFotaPlanObjListPo.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_CANCEL.getType();
            boolean isPrecheckFail = existFotaPlanObjListPo.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_PRECHECK_FAIL.getType();
            boolean isInstalledCompleteFail = existFotaPlanObjListPo.getStatus() == Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.getType();
            if (isCancle || isPrecheckFail || isInstalledCompleteFail || existFotaPlanObjListPo.getStatus() <= Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_BEGIN.getType()) {
                /*boolean updateStatus = */updateFotaPlanObjList(existFotaPlanObjListPo.getId(), Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_BEGIN);

                /*log.info("updateStatus={}", updateStatus);
                if(!updateStatus){
                    log.warn("更新升级对象进度信息异常.existFotaPlanObjListPo={}", existFotaPlanObjListPo.toString());
                    return;
                }*/

                //确认安装
                if(verifyInstalled) {
                    //推送消息到APP
                    String vin = otaProtocol.getOtaMessageHeader().getVin();

                    Temp4InstalledProcess t = null;
                    //立即安装
                    if (otaUpInstallVerifyResult.getInstalledType() == AppEnums.InstalledType4AppEnum.INSTALLED_IMI.getType()) {
                        t = wrap4InstalledImi(otaProtocol, fotaVinCacheInfo);
                    }
                    //预约安装
                    else if (otaUpInstallVerifyResult.getInstalledType() == AppEnums.InstalledType4AppEnum.INSTALLED_BOOKED.getType()) {
                        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(transId);
                        t = wrap4InstalledBooked(otaProtocol, otaUpInstallVerifyResult, fotaVinCacheInfo, fotaVersionCheckVerifyPo);
                    }

                    if(Objects.nonNull(t)) {
                        final Object newT = t.getT();
                        final OtaDispatch4OtherMessage otaDispatch4OtherMessage = buildOtaDispatch4OtherMessage(otaProtocol, true);
                        commonExecutor.execute(() -> {
                            publishOtaTransactionEvent(newT, otaProtocol, null, otaDispatch4OtherMessage);
                        }, "推送安装结果消息异常");
                    }else{
                        log.warn("包装推送安装确认结果对象异常.vin={}, otaUpInstallVerifyResult={}", vin, otaUpInstallVerifyResult);
                    }
                }
            } else {
                log.warn("[更新升级确认状态]升级对象当前状态异常，不需要操作.transId={}", transId);
            }
        }else {
            log.warn("[更新升级事务确认表对应下载确认相关字段异常].transId={}", transId);
        }
    }

    /**
     *
     * @param otaProtocol
     * @param fotaVinCacheInfo
     * @return
     */
    private Temp4InstalledProcess wrap4InstalledImi(OtaProtocol otaProtocol, FotaVinCacheInfo fotaVinCacheInfo/*Long transId, Long taskId, Long reqCheckId*/){
        InstalledVerifyResultVo installedVerifyResultVo = new InstalledVerifyResultVo();
        buildBaseAppVo(installedVerifyResultVo, otaProtocol);
        installedVerifyResultVo.setBusinessType(AppEnums.AppResponseTypeEnum.INSTALLED_VERIFIED_RESPONSE.getType());

        InstalledVerifyResultBodyVo installedVerifyResultBodyVo = new InstalledVerifyResultBodyVo();
        buildBaseAppBodyVo(installedVerifyResultBodyVo, fotaVinCacheInfo.getTransId(), fotaVinCacheInfo.getOtaPlanId());
        installedVerifyResultBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_VERIFY_RESULT.getType());
        installedVerifyResultBodyVo.setVerifySource(otaProtocol.getBody().getOtaCommonPayload().getSourceType());

        //补充需要添加的信息属性
        OtaDownVersionCheckResponse otaDownVersionCheckResponse = parseFromFotaVersionCheckPo(fotaVinCacheInfo.getCheckReqId());
        if(Objects.nonNull(otaDownVersionCheckResponse)) {
            installedVerifyResultBodyVo.setInstalledTotalNum(MyCollectionUtil.size(otaDownVersionCheckResponse.getEcuFirmwareVersionInfos()));
            installedVerifyResultBodyVo.setInstalledCurrentIndex(0);

            //兼容之间的代码
            installedVerifyResultBodyVo.setTotalPkgNum(installedVerifyResultBodyVo.getInstalledTotalNum());
            installedVerifyResultBodyVo.setCurrentPkgIndex(installedVerifyResultBodyVo.getInstalledCurrentIndex());
            installedVerifyResultBodyVo.setInstalledPercentRate(0);
            installedVerifyResultBodyVo.setInstalledRemainTime(otaDownVersionCheckResponse.getEstimatedUpgradeTime());

            //设置第一个待安装的ECU零件名称
            if(MyCollectionUtil.isNotEmpty(otaDownVersionCheckResponse.getEcuFirmwareVersionInfos())) {
                EcuFirmwareVersionInfo ecuFirmwareVersionInfo = otaDownVersionCheckResponse.getEcuFirmwareVersionInfos().stream().sorted(Comparator.comparing(EcuFirmwareVersionInfo::getGroupSeq)).findFirst().orElse(null);
                if(Objects.nonNull(ecuFirmwareVersionInfo)){
                    FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(ecuFirmwareVersionInfo.getFirmwareId());
                    installedVerifyResultBodyVo.setInstalledEcuName(fotaFirmwarePo.getComponentName());
                }
            }
        }else{
            installedVerifyResultBodyVo.setInstalledTotalNum(0);
            installedVerifyResultBodyVo.setInstalledCurrentIndex(0);
            installedVerifyResultBodyVo.setTotalPkgNum(installedVerifyResultBodyVo.getInstalledTotalNum());
            installedVerifyResultBodyVo.setCurrentPkgIndex(installedVerifyResultBodyVo.getInstalledCurrentIndex());
        }

        installedVerifyResultVo.setBody(installedVerifyResultBodyVo);
        return Temp4InstalledProcess.builder().t(installedVerifyResultVo).build();
    }

    /**
     *
     * @param otaProtocol
     * @param otaUpInstallVerifyResult
     * @param fotaVinCacheInfo
     * @return
     */
    private Temp4InstalledProcess wrap4InstalledBooked(OtaProtocol otaProtocol, OtaUpInstallVerifyResult otaUpInstallVerifyResult, FotaVinCacheInfo fotaVinCacheInfo, FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo){
        InstalledBookedVerifyResultVo installedBookedVerifyResultVo = new InstalledBookedVerifyResultVo();
        buildBaseAppVo(installedBookedVerifyResultVo, otaProtocol);
        installedBookedVerifyResultVo.setBusinessType(AppEnums.AppResponseTypeEnum.INSTALLED_VERIFIED_RESPONSE.getType());

        InstalledBookedVerifyResultBodyVo installedBookedVerifyResultBodyVo = new InstalledBookedVerifyResultBodyVo();
        buildBaseAppBodyVo(installedBookedVerifyResultBodyVo, fotaVinCacheInfo.getTransId(), fotaVinCacheInfo.getOtaPlanId());
        installedBookedVerifyResultBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_BOOKED_VERIFY_RESULT.getType());
        installedBookedVerifyResultVo.setBody(installedBookedVerifyResultBodyVo);

        installedBookedVerifyResultVo.getBody().setInstalledTime(otaUpInstallVerifyResult.getInstalledTime());

        //补充升级预估时间
        OtaDownVersionCheckResponse otaDownVersionCheckResponse = parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());
        if(Objects.nonNull(otaDownVersionCheckResponse.getEstimatedUpgradeTime())) {
            installedBookedVerifyResultVo.getBody().setInstalledRemainTime(Long.valueOf((long)otaDownVersionCheckResponse.getEstimatedUpgradeTime()));
        }
        installedBookedVerifyResultVo.getBody().setVerifySource(otaProtocol.getBody().getOtaCommonPayload().getSourceType());
        return Temp4InstalledProcess.builder().t(installedBookedVerifyResultVo).build();
    }

    /**
     * 构建升级开始-结束转发消息
     * @param otaProtocol
     * @param start
     * @return
     */
    private OtaDispatch4OtherMessage buildOtaDispatch4OtherMessage(OtaProtocol otaProtocol, boolean start){
        final OtaDispatch4OtherMessage otaDispatch4OtherMessage = new OtaDispatch4OtherMessage();
        otaDispatch4OtherMessage.setBusinessId(otaProtocol.getBody().getBusinessId());
        otaDispatch4OtherMessage.setTimestamp(Instant.EPOCH.getEpochSecond());
        otaDispatch4OtherMessage.setVin(otaProtocol.getOtaMessageHeader().getVin());
        try {
            String jspPayLoad;
            if(start){
                jspPayLoad = JsonUtil.toJson(OtaDispatch4OtherMessage.start4UpgradeProcess());
            }else{
                jspPayLoad = JsonUtil.toJson(OtaDispatch4OtherMessage.end4UpgradeProcess());
            }
            otaDispatch4OtherMessage.setJsonPayload(jspPayLoad);
        }catch (Exception e){
            log.error("包装发送到其他应用的升级开始/结束信息异常.otaProtocol={}", otaProtocol, e);
        }
        return otaDispatch4OtherMessage;
    }


    @Override
    public OtaDownVersionCheckResponse parseFromFotaVersionCheckPo(Long checkReqId){
        log.info("checkReqId={}", checkReqId);
        FotaVersionCheckPo fotaVersionCheckPo = fotaVersionCheckDbService.getById(checkReqId);
        String checkResponse = fotaVersionCheckPo.getCheckResponse();
        try {
            return JsonUtil.toObject(checkResponse, OtaDownVersionCheckResponse.class);
        } catch (IOException e) {
            log.error("解析版本检查结果异常.checkReqId={}, checkResponse={}", checkReqId, checkResponse, e);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadProcessReq(OtaProtocol otaProtocol) {
        OtaUpDownloadProcess otaUpDownloadProcess = otaProtocol.getBody().getOtaUpDownloadProcess();
        MyAssertUtil.notNull(otaUpDownloadProcess, "下载进度参数不能为空");

        Enums.DownloadProcessTypeEnum downloadProcessTypeEnum = Enums.DownloadProcessTypeEnum.getByType(otaUpDownloadProcess.getDownloadProcessType());
        MyAssertUtil.notNull(downloadProcessTypeEnum, "下载进度类型不能为空");

        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();
        String vin = otaProtocol.getOtaMessageHeader().getVin();
        try {
            boolean isFinished = Enums.DownloadProcessTypeEnum.isFinished(downloadProcessTypeEnum);
            boolean isDownloading = Enums.DownloadProcessTypeEnum.isDownloading(downloadProcessTypeEnum);

            FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
            entity.setId(transId);
            entity.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
            //如果是下载过程中
            if(isDownloading){
                //上报进度
                if(Objects.nonNull(otaUpDownloadProcess.getDownloadPercentRate())) {
                    entity.setDownloadPercentRate(otaUpDownloadProcess.getDownloadPercentRate().intValue());
                }
                if(Objects.nonNull(otaUpDownloadProcess.getEstimateRemainTime())) {
                    entity.setDownloadRemainTime(otaUpDownloadProcess.getEstimateRemainTime().intValue());
                }
                entity.setDownloadFullSize(otaUpDownloadProcess.getDownloadFullSize());
                entity.setDownloadFinishedSize(otaUpDownloadProcess.getDownloadFinishedSize());
            }else if(isFinished){
            	boolean isFailed = Enums.DownloadProcessTypeEnum.isFailed(downloadProcessTypeEnum);
            	if (!isFailed) { // 非失败的情况需要检查下载详细信息字段
            		MyAssertUtil.notNull(otaUpDownloadProcess.getDownloadProcessDetails(), "下载细节不能为空");
            		//补充累加下载时间
                    entity.setDownloadSpendTime(otaUpDownloadProcess.getDownloadProcessDetails().stream().mapToInt(item -> Objects.nonNull(item.getDownloadSpendTime()) ? item.getDownloadSpendTime() : 0 ).sum());
            	}
            }
            //设置下载上报时间为当前系统时间
            entity.setDownloadReportTime(LocalDateTime.now());
            entity.setDownloadStatus(downloadProcessTypeEnum.getType());
            boolean update = fotaVersionCheckVerifyDbService.updateById(entity);

            if(update){
                FotaPlanObjListPo existFotaPlanObjListPo = findFotaPlanObjListPoWithTransId(transId);

                Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = Enums.getTaskObjStatusTypeEnum(downloadProcessTypeEnum);
                MyAssertUtil.notNull(taskObjStatusTypeEnum, "升级对象状态枚举类型参数转换异常");
                //任务状态为“待安装确认”才更新为“升级安装中”
                boolean updateStatus = updateFotaPlanObjList(existFotaPlanObjListPo.getId(), taskObjStatusTypeEnum);
                log.info("更新升级状态。updateStatus={}, vin={}", updateStatus, vin);

                if(isFinished){
                    log.info("升级下载阶段类型为已完成，转发消息到APP.transId={}", transId);
                    //转发到APP
                    pushDownloadFinished4App(otaProtocol, fotaVinCacheInfo);
                }else if(isDownloading){
                    //推送下载进度消息到APP
                    pushDownloading4App(otaProtocol, fotaVinCacheInfo);
                }else {
                    //推送下载等待消息到APP消息中心
                    boolean isDownloadWait = Enums.DownloadProcessTypeEnum.isDownloadWait(downloadProcessTypeEnum);
                    if(isDownloadWait){
                        commonExecutor.execute(() -> publishOtaTransactionEvent(otaProtocol, AppEnums.MessageCenterMsgTypeEnum.DOWNLOAD_WAIT), "推送下载等待消息异常");
                    }
                }
            }else{
                log.warn("更新升级确认表信息异常.fotaVersionCheckVerifyPo={}", entity.toString());
            }
        } catch (Exception e) {
            log.error("存储下载进度汇报到mysql异常.", e);
        }
    }

    /**
     * 构建推送消息基类
     * @param otaProtocol
     * @return
     */
    private void buildBaseAppVo(final BaseAppVo baseAppVo, final OtaProtocol otaProtocol){
        baseAppVo.setBusinessId(otaProtocol.getBody().getBusinessId());
    }

    /**
     * 构建推送消息body基类
     * @param transId
     * @param taskId
     * @return
     */
    private void buildBaseAppBodyVo(final BaseAppBodyVo baseAppBodyVo, Long transId, Long taskId){
        baseAppBodyVo.setTransId(transId);
        baseAppBodyVo.setTaskId(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void installedProcessReq(OtaProtocol otaProtocol) {
        OtaUpInstallProcess otaUpInstallProcess = otaProtocol.getBody().getOtaUpInstallProcess();
        MyAssertUtil.notNull(otaUpInstallProcess, "安装进度参数不能为空");

        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();
        String vin = otaProtocol.getOtaMessageHeader().getVin();

        try {
            Enums.InstalledProcessTypeEnum installedProcessTypeEnum = Enums.InstalledProcessTypeEnum.getByType(otaUpInstallProcess.getInstalledProcessType());
            MyAssertUtil.notNull(installedProcessTypeEnum, "升级过程阶段类型异常");

            Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = Enums.getTaskObjStatusTypeEnum(installedProcessTypeEnum);
            MyAssertUtil.notNull(taskObjStatusTypeEnum, "升级对象状态枚举类型参数转换异常");

            FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
            entity.setId(transId);
            entity.setUpdateBy(CommonConstant.USER_ID_SYSTEM);

            //如果是取消安装状态，则需要清除当前可能存在的预约安装时间
            //boolean isInstalledVerifyBooked = Objects.nonNull(entity.getInstalledVerifyBookedTime());
            if(taskObjStatusTypeEnum.equals(Enums.TaskObjStatusTypeEnum.INSTALLED_CANCEL)){
                entity.setInstalledVerifyBookedTime(null);
                /*if(isInstalledVerifyBooked) {
                }*/
            }

            //补充安装上报时间
            entity.setInstalledReportTime(LocalDateTime.now());
            entity.setInstalledStatus(otaUpInstallProcess.getInstalledProcessType());

            //补充升级进度中间进度信息
            Temp4InstalledProcess t = null;
            boolean installedProcessing = installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_PROCESSING);
            if(installedProcessing){
                MyAssertUtil.notNull(otaUpInstallProcess.getTotalPkgNum(), TBoxRespCodeEnum.OTA_RESP_CODE_TBOX_PARAM_ERROR_INSTALLED_TOTAL_PKG_NUM);
                MyAssertUtil.notNull(otaUpInstallProcess.getCurrentPkgIndex(), TBoxRespCodeEnum.OTA_RESP_CODE_TBOX_PARAM_ERROR_CURRENT_INSTALLED_INDEX);

                entity.setInstalledTotalNum(MyObjectUtil.getOrDefaultT(otaUpInstallProcess.getTotalPkgNum(), entity.getInstalledTotalNum()));
                Integer currentPkgIndex = 0;
                if(Objects.nonNull(otaUpInstallProcess.getCurrentPkgIndex())){
                    currentPkgIndex = otaUpInstallProcess.getCurrentPkgIndex().intValue();
                }
                entity.setInstalledCurrentIndex(MyObjectUtil.getOrDefaultT(currentPkgIndex, entity.getInstalledCurrentIndex()));

                //升级进度
                t = wrap4InstalledProcess(otaProtocol, otaUpInstallProcess, fotaVinCacheInfo);

                if(Objects.nonNull(t.getT())) {
                    InstalledProcessVo installedProcessVo = (InstalledProcessVo)t.getT();
                    InstalledProcessBodyVo installedProcessBodyVo = installedProcessVo.getBody();

                    entity.setInstalledRemainTime(installedProcessBodyVo.getInstalledRemainTime());
                    entity.setInstalledFirmwareId(installedProcessBodyVo.getInstalledFirmwareId());
                    entity.setInstalledPercentRate(installedProcessBodyVo.getInstalledPercentRate());
                }
                //补充这两个字段
            }

            boolean update = fotaVersionCheckVerifyDbService.updateById(entity);

            if(update) {
                FotaPlanObjListPo existFotaPlanObjListPo = findFotaPlanObjListPoWithTransId(transId);

                boolean isFinished = Enums.TaskObjStatusTypeEnum.isInstalledFinished(taskObjStatusTypeEnum);
                log.info("更新升级状态。当前状态={}, 目标更新状态={}", existFotaPlanObjListPo.getStatus(), taskObjStatusTypeEnum.getType());
                //任务状态为“非安装结束状态”才更新
                if (!isFinished) {
                    boolean updateStatus = updateFotaPlanObjList(existFotaPlanObjListPo.getId(), taskObjStatusTypeEnum);
                    log.info("更新升级状态。updateStatus={}", updateStatus);

                    //转发到APP
                    Long taskId = fotaVinCacheInfo.getOtaPlanId();

                    /*boolean installedCancel = installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_CANCEL);
                    boolean installedPrecheckFail = installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_PRECHECK_FAIL);*/
                    /*boolean needPush =  installedCancel || installedPrecheckFail || installedProcessing;
                    //取消升级或前置版本检查失败
                    if(needPush) {*/
                    //添加升级映射类型
                    if(!installedProcessing){
                        //区分是否为开始升级
                        boolean installedStart = installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_START);
                        if(installedStart){
                            FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(transId);
                            if(Objects.nonNull(fotaVersionCheckVerifyPo.getInstalledVerifyBookedTime())) {
                                Map<String, Object> attachmentMap = Maps.newConcurrentMap();
                                attachmentMap.put("appointmentTime", fotaVersionCheckVerifyPo.getInstalledVerifyBookedTime());
                                t = Temp4InstalledProcess.builder().t(attachmentMap).messageCenterMsgTypeEnum(AppEnums.MessageCenterMsgTypeEnum.INSTALLED_BOOKED_START).build();
                            }
                        }else {
                            t = wrap4InstalledProccess(otaProtocol, installedProcessTypeEnum, transId, taskId);
                        }
                    }
                    if(Objects.nonNull(t)) {
                        final Temp4InstalledProcess newTemp4InstalledProcess = t;
                        final Object newT = t.getT();
                        commonExecutor.execute(() -> {
                            /**
                             * 预约安装失败推送放在前置条件检查消息逻辑中处理
                             */
                            publishOtaTransactionEvent(newT, otaProtocol, newTemp4InstalledProcess.getMessageCenterMsgTypeEnum(), null);
                        }, "推送安装进度信息到APP异常");
                    }else{
                        log.warn("对象异常,放弃推送.vin={}, otaProtocol={}", vin, otaProtocol);
                    }
                }
                /*}*/
            }
        } catch (Exception e) {
            log.error("存储下载进度汇报到mysql异常.vin={}", vin, e);
        }
    }

    @Data
    @Builder
    public static class Temp4InstalledProcess{
        private Object t;
        private AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum;
    }

    /**
     *
     * @param otaProtocol
     * @param installedProcessTypeEnum
     * @param transId
     * @param taskId
     * @return
     */
    private Temp4InstalledProcess wrap4InstalledProccess(OtaProtocol otaProtocol, Enums.InstalledProcessTypeEnum installedProcessTypeEnum, Long transId, Long taskId){
        try {
            RemoteInstalledCallbackVo remoteInstalledCallbackVo = new RemoteInstalledCallbackVo();
            buildBaseAppVo(remoteInstalledCallbackVo, otaProtocol);

            RemoteInstalledCallbackBodyVo remoteInstalledCallbackBodyVo = new RemoteInstalledCallbackBodyVo();
            buildBaseAppBodyVo(remoteInstalledCallbackBodyVo, transId, taskId);

            AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum = null;
            if (installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_CANCEL)) {
                remoteInstalledCallbackBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_CANCEL.getType());
            } else if (installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_PRECHECK_FAIL)) {
                remoteInstalledCallbackBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_PRECHECKED_FAIL.getType());

                if(MyCollectionUtil.isNotEmpty(otaProtocol.getBody().getOtaUpInstallProcess().getUpgradeConditions())){
                    remoteInstalledCallbackBodyVo.setInstalledPrecheckFails(MyCollectionUtil.newCollection(otaProtocol.getBody().getOtaUpInstallProcess().getUpgradeConditions(), item -> item.getCondValue()));
                }
                //判断是否为预约安装时的，前置条件检查失败
                messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.INSTALLED_BOOKED_FAIL;
            } /*else if (installedProcessTypeEnum.equals(Enums.InstalledProcessTypeEnum.INSTALLED_PRECHECK_FAIL)) {
                remoteInstalledCallbackBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_START.getType());
                //判断是否为预约安装时的，前置条件检查失败
                messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.INSTALLED_BOOKED_START;
            }*/

            remoteInstalledCallbackBodyVo.setRespCode(otaProtocol.getBody().getOtaCommonPayload().getErrorCode());
            remoteInstalledCallbackBodyVo.setMsg(otaProtocol.getBody().getOtaCommonPayload().getErrorMsg());
            remoteInstalledCallbackVo.setBody(remoteInstalledCallbackBodyVo);
            return Temp4InstalledProcess.builder().t(remoteInstalledCallbackVo).messageCenterMsgTypeEnum(messageCenterMsgTypeEnum).build();
        }catch(Exception e){
            log.error("包装安装进度对象异常。otaProtocol={}, transId={}", otaProtocol, transId, e);
        }
        return null;
    }

    /**
     * 包装返回升级过程上报需要推送的对象
     * @param otaProtocol
     * @param otaUpInstallProcess
     * @param fotaVinCacheInfo
     * @return
     */
    private Temp4InstalledProcess wrap4InstalledProcess(OtaProtocol otaProtocol, OtaUpInstallProcess otaUpInstallProcess, FotaVinCacheInfo fotaVinCacheInfo){
        try {
            //升级进度
            InstalledProcessVo installedProcessVo = new InstalledProcessVo();
            buildBaseAppVo(installedProcessVo, otaProtocol);

            InstalledProcessBodyVo installedProcessBodyVo = new InstalledProcessBodyVo();
            buildBaseAppBodyVo(installedProcessBodyVo, fotaVinCacheInfo.getTransId(), fotaVinCacheInfo.getOtaPlanId());

            installedProcessBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_PROCESSING.getType());

            //添加升级进度信息
            Integer InstalledTotalNum = MyObjectUtil.getOrDefaultT(otaUpInstallProcess.getTotalPkgNum(), 0);
            installedProcessBodyVo.setInstalledTotalNum(InstalledTotalNum);
            installedProcessBodyVo.setTotalPkgNum(InstalledTotalNum);

            Integer currentPkgIndex = null;
            if(Objects.nonNull(otaUpInstallProcess.getCurrentPkgIndex())){
                currentPkgIndex = MyObjectUtil.getOrDefaultT(Integer.valueOf(otaUpInstallProcess.getCurrentPkgIndex().intValue()), 0);
            }
            installedProcessBodyVo.setInstalledCurrentIndex(currentPkgIndex);
            installedProcessBodyVo.setCurrentPkgIndex(currentPkgIndex);

            OtaDownVersionCheckResponse otaDownVersionCheckResponse = parseFromFotaVersionCheckPo(fotaVinCacheInfo.getCheckReqId());
            Integer estimatedUpgradeTime = otaDownVersionCheckResponse.getEstimatedUpgradeTime();
            if (Objects.nonNull(estimatedUpgradeTime) && estimatedUpgradeTime.intValue() > 0) {
                int installedTotalTime = estimatedUpgradeTime;
                int installedRemainTime = installedTotalTime;
                int secondsPerEcu = (int) Math.ceil(estimatedUpgradeTime / InstalledTotalNum);
                int installedCurrentIndex = currentPkgIndex;
                //获取当前正在下载的ECU零件名
                InstallProcessDetail installProcessDetail = otaProtocol.getBody().getOtaUpInstallProcess().getInstallProcessDetails().get(0);
                //单个ecu升级进度0-100
                double percentRate = (double) installProcessDetail.getInstalledPercentRate() / 100;

                int percentRateTotal = 0;
                if (installedCurrentIndex > 0) {
                    //剩余的时间
                    installedRemainTime = installedTotalTime - (installedCurrentIndex - 1) * secondsPerEcu - (int) Math.floor(secondsPerEcu * percentRate);
                    percentRateTotal = (int)Math.floor((installedTotalTime - installedRemainTime) * 100/ installedTotalTime);
                    //percentRateTotal = (int) Math.floor((installedCurrentIndex - 1) * secondsPerEcu + (int) Math.floor(secondsPerEcu * percentRate) / installedRemainTime);
                }
                installedProcessBodyVo.setInstalledRemainTime(installedRemainTime);
                installedProcessBodyVo.setInstalledPercentRate(percentRateTotal);

                FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getByFrimwareCode(installProcessDetail.getFirmwareCode());
                if (Objects.isNull(fotaFirmwarePo)) {
                    log.warn("固件信息异常.installProcessDetail.getFirmwareCode()={}", installProcessDetail.getFirmwareCode());
                } else {
                    installedProcessBodyVo.setInstalledEcuName(fotaFirmwarePo.getComponentName());
                    installedProcessBodyVo.setInstalledFirmwareId(fotaFirmwarePo.getId());
                }
            } else {
                log.warn("获取升级预估刷写时长异常.vin={}, estimatedUpgradeTime={}", otaProtocol.getOtaMessageHeader().getVin(), estimatedUpgradeTime);
            }

            installedProcessBodyVo.setRespCode(otaProtocol.getBody().getOtaCommonPayload().getErrorCode());
            installedProcessBodyVo.setMsg(otaProtocol.getBody().getOtaCommonPayload().getErrorMsg());
            installedProcessVo.setBody(installedProcessBodyVo);
            return Temp4InstalledProcess.builder().t(installedProcessVo).build();
        }catch (Exception e){
            log.error("包装安装进度对象异常。otaProtocol={}, fotaVinCacheInfo={}", otaProtocol, fotaVinCacheInfo, e);
        }
        return null;
    }

    /**
     * 构建升级进度终止响应对象
     * @param otaProtocol
     * @return
     */
    /*private UpgradeTerminateVo wrap4TerminateResponse(OtaProtocol otaProtocol, FotaVinCacheInfo fotaVinCacheInfo){
        UpgradeTerminateVo upgradeTerminateVo = new UpgradeTerminateVo();
        buildBaseAppVo(upgradeTerminateVo, otaProtocol);

        BaseAppBodyVo baseAppBodyVo = new BaseAppBodyVo();
        buildBaseAppBodyVo(baseAppBodyVo, fotaVinCacheInfo.getTransId(), fotaVinCacheInfo.getOtaPlanId());
        upgradeTerminateVo.setBody(baseAppBodyVo);

        return upgradeTerminateVo;
    }*/


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upgradeResultReq(OtaProtocol otaProtocol) {
        // 更新数据库中的表 ---tb_fota_version_check_verify、tb_fota_plan_obj_list、tb_fota_firmware_list、tb_fota_plan_task_detail
        OtaUpUpgradeResult otaUpUpgradeResult = otaProtocol.getBody().getOtaUpUpgradeResult();
        MyAssertUtil.notNull(otaUpUpgradeResult, "安装结果参数不能为空");

        Enums.UpgradeResultTypeEnum upgradeResultTypeEnum = Enums.UpgradeResultTypeEnum.getByType(otaUpUpgradeResult.getInstalledResult());
        MyAssertUtil.notNull(upgradeResultTypeEnum, "安装结果类型异常");

        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();

        //TODO
        FotaPlanObjListPo existFotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVinCacheInfo.getOtaPlanObjectId());
        MyAssertUtil.notNull(existFotaPlanObjListPo, OTARespCodeEnum.DATA_NOT_FOUND);

        Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = Enums.getTaskObjStatusTypeEnum(upgradeResultTypeEnum);
        MyAssertUtil.notNull(taskObjStatusTypeEnum, "升级结果类型异常");
        //更新升级结果
        updateFotaPlanObjList4Result(existFotaPlanObjListPo.getId(), existFotaPlanObjListPo.getTargetVersion(), taskObjStatusTypeEnum);

        Long otaObjectId = fotaVinCacheInfo.getObjectId();
        Long otaPlanId = fotaVinCacheInfo.getOtaPlanId();

        List<com.bnmotor.icv.adam.sdk.ota.domain.UpgradeResultDetail> upgradeResultDetails = otaUpUpgradeResult.getUpgradeResultDetails();
        List<Long> firmwareIds = upgradeResultDetails.stream().map(item -> item.getFirmwareId()).collect(Collectors.toList());
        // tb_fota_plan_firmware_list 和 tb_fota_plan_task_detail
        List<FotaFirmwareListPo> fotaFirmwareLists = fotaFirmwareListDbService.getByOtaObjIdWithFirmwareId(otaObjectId, firmwareIds);
        if (MyCollectionUtil.isEmpty(fotaFirmwareLists)) {
            log.warn("更新车辆固件清单,获取列表异常.transId={}, otaObjectId={},firmwareIds={}", transId, otaObjectId, firmwareIds);
            return;
        }
        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(otaPlanId);
        List<FotaStrategyFirmwareListPo> fotaStrategyFirmwareListPos = fotaStrategyFirmwareListDbService.listByOtaStrategyId(fotaPlanPo.getFotaStrategyId())/*.getByPlanIdWithFirmwareIds(otaPlanId, firmwareIds)*/;
        if (MyCollectionUtil.isEmpty(fotaStrategyFirmwareListPos)) {
            log.warn("获取任务升级固件清单列表为空.transId={}, otaPlanId={}, firmwareIds={}", transId, otaPlanId, firmwareIds);
            return;
        }

        List<Long> strategyFirmwareListIds = fotaStrategyFirmwareListPos.stream().map(FotaStrategyFirmwareListPo::getId).collect(Collectors.toList());
        List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPoLists = fotaPlanTaskDetailDbService.listByOtaPlanObjIdWithPlanFirmwareId(fotaVinCacheInfo.getOtaPlanObjectId(), strategyFirmwareListIds);
        if (MyCollectionUtil.isEmpty(fotaPlanTaskDetailPoLists)) {
            log.warn("获取任务升级详情列表为空..otaPlanObjectId={},planFirmwareIds={}", fotaVinCacheInfo.getOtaPlanObjectId(), strategyFirmwareListIds);
            return;
        }

        LocalDateTime dateTime = LocalDateTime.now();
        FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
        entity.setId(transId);
        //标识升级安装流程已经结束
        entity.setInstalledCompleteStatus(otaUpUpgradeResult.getInstalledResult());
        entity.setInstalledCompleteTime(dateTime);

        entity.setInstalledStartTime(MyDateUtil.epochSecondsToLocalDateTime(otaUpUpgradeResult.getStartTime()));
        entity.setInstalledEndTime(MyDateUtil.epochSecondsToLocalDateTime(otaUpUpgradeResult.getEndTime()));

        Integer spendTime = MyDateUtil.spendTime(otaUpUpgradeResult.getStartTime(), otaUpUpgradeResult.getEndTime());
        entity.setInstalledSpendTime(spendTime);

        entity.setUpdateTime(dateTime);
        entity.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
        boolean update = fotaVersionCheckVerifyDbService.updateById(entity);
        if (update) {
            List<FotaFirmwareListPo> fotaFirmwareListPos = Lists.newArrayList();
            List<FotaPlanTaskDetailPo> fotaPlanTaskDetailPos = Lists.newArrayList();
            upgradeResultDetails.forEach(
                item -> {
                    // 更新tb_fota_firmware_list表的属性
                    Optional<FotaStrategyFirmwareListPo> firstPlanFirmwareListDo = fotaStrategyFirmwareListPos.stream().filter(item1 -> item1.getFirmwareId().equals(item.getFirmwareId())).findFirst();
                    if (!firstPlanFirmwareListDo.isPresent()) {
                        log.warn("升级结果上报获取升级固件信息异常.item={}", item.toString());
                        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_FRIMWARE_LIST_NOT_EXIST);
                    }
                    FotaFirmwareListPo fotaFirmwareListPo = new FotaFirmwareListPo();

                    Optional<FotaFirmwareListPo> firstFotaFirmwareListPo = fotaFirmwareLists.stream().filter(item1 -> item1.getFirmwareId().equals(item.getFirmwareId())).findFirst();
                    if (firstFotaFirmwareListPo.isPresent()) {
                        fotaFirmwareListPo.setId(firstFotaFirmwareListPo.get().getId());
                        fotaFirmwareListPos.add(fotaFirmwareListPo);
                    } else {
                        log.warn("升级结果上报获取升级对象获异常.item={}", item.toString());
                        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_OBJECT_FRIMWARE_LIST_NOT_EXIST);
                    }
                    fotaFirmwareListPo.setFirmwareId(item.getFirmwareId());
                    fotaFirmwareListPo.setOtaObjectId(fotaVinCacheInfo.getObjectId());
                    if (Objects.nonNull(item.getEndTime())) {
                        fotaFirmwareListPo.setUpgradeTime(new Date(item.getEndTime()));
                    }
                    fotaFirmwareListPo.setRunningFirmwareVersion(item.getFirmwareVersionNo());
                    fotaFirmwareListPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                    fotaFirmwareListPo.setUpdateTime(dateTime);

                    FotaPlanTaskDetailPo fotaPlanTaskDetailPo = new FotaPlanTaskDetailPo();
                    Optional<FotaPlanTaskDetailPo> firstFotaPlanTaskDetailPo = fotaPlanTaskDetailPoLists.stream().filter(fotaPlanTaskDetail -> fotaPlanTaskDetail.getOtaPlanFirmwareId().equals(firstPlanFirmwareListDo.get().getId())).findFirst();
                    if (firstFotaPlanTaskDetailPo.isPresent()) {
                        fotaPlanTaskDetailPo.setId(firstFotaPlanTaskDetailPo.get().getId());
                        fotaPlanTaskDetailPos.add(fotaPlanTaskDetailPo);
                    } else {
                        log.warn("升级结果上报获取升级任务详情异常.item={}", item.toString());
                        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.FOTA_PLAN_TASK_DETAIL_NOT_EXIST);
                    }

                    // 更新tb_fota_plan_task_detail表的属性
                    // 更新该固件开始时间
                    fotaPlanTaskDetailPo.setFinishTime(Objects.nonNull(item.getEndTime()) ? new Date(item.getEndTime()) : null);
                    fotaPlanTaskDetailPo.setFailedTime(Objects.nonNull(item.getLastFailTIme()) ? new Date(item.getLastFailTIme()) : null);
                    //更新失败原因
                    fotaPlanTaskDetailPo.setFailedReason(item.getLastFailReason());
                    fotaPlanTaskDetailPo.setRetryCount(item.getRetryNum());
                    if (Objects.nonNull(item.getRetryTime())) {
                        fotaPlanTaskDetailPo.setRetryTime(new Date(item.getRetryTime()));
                    }
                    //设置当前版本
                    fotaPlanTaskDetailPo.setCurrentVersion(item.getFirmwareVersionNo());
                    fotaPlanTaskDetailPo.setStatus(item.getStatus());
                    fotaPlanTaskDetailPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                    fotaPlanTaskDetailPo.setUpdateTime(dateTime);
                }
            );
            log.info("升级结果汇报需要更新的固件清单列表:size={}, fotaFirmwareListPos={}", fotaFirmwareListPos.size(), fotaFirmwareListPos.toString());
            log.info("升级结果汇报需要更新的升级任务详情列表:size={}, fotaPlanTaskDetailPos={}", fotaPlanTaskDetailPos.size(), fotaPlanTaskDetailPos.toString());
            fotaFirmwareListDbService.saveOrUpdateBatch(fotaFirmwareListPos);
            fotaPlanTaskDetailDbService.saveOrUpdateBatch(fotaPlanTaskDetailPos);


            //如果升级成功，更新升级对象版本号
            if(otaUpUpgradeResult.getInstalledResult() == 1) {
                //FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaVinCacheInfo.getOtaPlanId());
                FotaObjectPo newFotaObjectPo = new FotaObjectPo();
                newFotaObjectPo.setId(fotaVinCacheInfo.getObjectId());
                newFotaObjectPo.setCurrentVersion(fotaPlanPo.getTargetVersion());
                newFotaObjectPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
                fotaObjectDbService.updateById(newFotaObjectPo);
                //更新缓存
                fotaObjectCacheInfoService.setFotaObjectCacheInfo(fotaObjectDbService.getById(fotaVinCacheInfo.getObjectId()));
            }

            //转发到App
            commonExecutor.execute(() -> pushUpgradeResult2App(otaProtocol, otaUpUpgradeResult), "推送安装结果消息到APP异常");
        }
    }

    @Override
    public void handle4PlanInvalid(OtaProtocol otaProtocol) {
        log.info("接收到TBOX端检查到任务已失效消息通知，设置该次升级未任务失效状态");
        FotaVinCacheInfo fotaVinCacheInfo = checkTrans(otaProtocol);
        Long transId = fotaVinCacheInfo.getTransId();
        String vin = otaProtocol.getOtaMessageHeader().getVin();
        try {
            FotaVersionCheckVerifyPo entity = new FotaVersionCheckVerifyPo();
            entity.setId(transId);
            entity.setInstalledCompleteStatus(Enums.InstalledCompletedStatusTypeEnum.INSTALLED_TERMINATED.getType());
            CommonUtil.wrapBasePo4Update(entity);
            boolean update = fotaVersionCheckVerifyDbService.updateById(entity);
            if (update) {
                log.info("设置该次升级事务失效.vin={}, id={}", vin, transId);
                Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = Enums.TaskObjStatusTypeEnum.INSTALLED_PLAN_INVALID;
                boolean updateStatus = updateFotaPlanObjList(fotaVinCacheInfo.getOtaPlanObjectId(), taskObjStatusTypeEnum);
                log.info("更新升级事务状态。updateStatus={}, vin={}, otaPlanObjId={}", updateStatus, vin, fotaVinCacheInfo.getOtaPlanObjectId());
                if(updateStatus){
                    /*UpgradeTerminateVo upgradeTerminateVo = wrap4TerminateResponse(otaProtocol, fotaVinCacheInfo);
                    //是否需要推送到消息中心，需要确认
                    commonExecutor.execute(() -> publishOtaTransactionEvent(upgradeTerminateVo, otaProtocol, null, buildOtaDispatch4OtherMessage(otaProtocol, false)), "推送升级任务终止消息异常");*/
                }
            }
        }catch (Exception e){
            log.error("处理任务失效消息异常.otaProtocol={}", otaProtocol, e.getMessage());
        }
    }

    /**
     * 转发升级结果到APP
     * @param otaProtocol
     * @param otaUpUpgradeResult
     */
    private void pushUpgradeResult2App(OtaProtocol otaProtocol, OtaUpUpgradeResult otaUpUpgradeResult){
        //转发到App
        InstalledResultVo installedResultVo = new InstalledResultVo();
        buildBaseAppVo(installedResultVo, otaProtocol);
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);

        InstalledResultBodyVo installedResultBodyVo = new InstalledResultBodyVo();
        buildBaseAppBodyVo(installedResultBodyVo, otaCommonPayload.getTransId(), otaCommonPayload.getTaskId());
        installedResultBodyVo.setInstalledProcessType(AppEnums.InstalledProcessType4AppEnum.INSTALLED_RESULT.getType());
        installedResultBodyVo.setResult(otaUpUpgradeResult.getInstalledResult());
        //成功
        boolean installedComplete = otaUpUpgradeResult.getInstalledResult() == Enums.UpgradeResultTypeEnum.UPGRADE_COMPLETED.getType();
        boolean installedFail = otaUpUpgradeResult.getInstalledResult() == Enums.UpgradeResultTypeEnum.UPGRADE_FAIL.getType();
        boolean installedUnComplete = otaUpUpgradeResult.getInstalledResult() == Enums.UpgradeResultTypeEnum.UPGRADE_UNCOMPLETED.getType();
        AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum = null;
        if(installedComplete){
            setCodeAndMsg4BaseAppBodyVo(installedResultBodyVo, TBoxRespCodeEnum.SUCCESS);
            messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.INSTALLED_COMLETE_SUCCESS;
        }else if(installedFail){
            //安装失败，回滚失败
            setCodeAndMsg4BaseAppBodyVo(installedResultBodyVo, TBoxRespCodeEnum.APP_RESP_CODE_TBOX_COMPLETED_FAIL_4INSTALLED);
            messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.INSTALLED_FAIL;
        }else if(installedUnComplete){
            //安装未完成，回滚成功
            setCodeAndMsg4BaseAppBodyVo(installedResultBodyVo, TBoxRespCodeEnum.APP_RESP_CODE_TBOX_COMPLETED_FAIL_WITH_ROLLBACK_SUCCESS_4INSTALLED);
            messageCenterMsgTypeEnum = AppEnums.MessageCenterMsgTypeEnum.INSTALLED_COMLETE_FAIL;
        }
        installedResultVo.setBusinessType(AppEnums.AppResponseTypeEnum.INSTALLED_VERIFIED_RESPONSE.getType());
        installedResultVo.setBody(installedResultBodyVo);

        //构建kafka消息推送到APP端
        OtaDispatch4OtherMessage otaDispatch4OtherMessage = buildOtaDispatch4OtherMessage(otaProtocol, false);
        publishOtaTransactionEvent(installedResultVo, otaProtocol, messageCenterMsgTypeEnum, otaDispatch4OtherMessage);
    }

    /**
     *
     * @param baseAppBodyVo
     */
    private void setCodeAndMsg4BaseAppBodyVo(BaseAppBodyVo baseAppBodyVo, TBoxRespCodeEnum tBoxRespCodeEnum){
        baseAppBodyVo.setRespCode(tBoxRespCodeEnum.getCode());
        baseAppBodyVo.setMsg(tBoxRespCodeEnum.getDesc());
    }

    /*@Override
    public List<com.bnmotor.icv.adam.sdk.ota.domain.DownloadProcessDetail> listDownloadProcessByTransId(Long transId) {
        //return mongoService.listDownloadProcessByTransId(transId);
        return null;
    }

    @Override
    public List<com.bnmotor.icv.adam.sdk.ota.domain.InstallProcessDetail> listUpgradeProcessByTransId(Long transId) {
        //return mongoService.listUpgradeProcessByTransId(transId);
        return null;
    }

    @Override
    public List<com.bnmotor.icv.adam.sdk.ota.domain.UpgradeResultDetail> listUpgradeResultByTransId(Long transId) {
        //return mongoService.listUpgradeResultByTransId(transId);
        return null;
    }*/

    /*@Override
    public void setAditionalInfoForInstalledProcessing(Map<String ,Object> aditionalInfo, FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo){
        //升级中，需要携带进度信息
        //如果有安装进度
        //进度，剩余时间，当前安装模块
        aditionalInfo.put("installedTotalNum", fotaVersionCheckVerifyPo.getInstalledTotalNum());
        aditionalInfo.put("installedCurrentIndex", fotaVersionCheckVerifyPo.getInstalledCurrentIndex());
        aditionalInfo.put("installedRemainTime", fotaVersionCheckVerifyPo.getInstalledRemainTime());
        aditionalInfo.put("installedPercentRate", fotaVersionCheckVerifyPo.getInstalledPercentRate());
        if(Objects.nonNull(fotaVersionCheckVerifyPo.getInstalledFirmwareId())) {
            FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getById(fotaVersionCheckVerifyPo.getInstalledFirmwareId());
            aditionalInfo.put("installedEcuName", Objects.nonNull(fotaFirmwarePo) ? fotaFirmwarePo.getComponentName() : null);
        }
    }*/
}
