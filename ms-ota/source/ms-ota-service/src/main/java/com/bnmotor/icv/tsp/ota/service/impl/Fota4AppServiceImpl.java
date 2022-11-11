package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaCommonPayload;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownCancelCommand;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownDownloadVerifyResult;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownInstalledVerifyResult;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.*;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxDownHandler;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.*;
import com.bnmotor.icv.tsp.ota.model.req.app.BaseAppDto;
import com.bnmotor.icv.tsp.ota.model.req.app.CancelInstalledBookedDto;
import com.bnmotor.icv.tsp.ota.model.req.app.RemoteDownloadDto;
import com.bnmotor.icv.tsp.ota.model.req.app.RemoteInstalledDto;
import com.bnmotor.icv.tsp.ota.model.resp.app.SyncResultVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.TboxUpgradStatusVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.VersionCheckBodyVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: Fota4AppServiceImpl
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/20 11:02
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class Fota4AppServiceImpl implements IFota4AppService {
    @Autowired
    private IFotaObjectService fotaObjectService;

    @Autowired
    private TBoxDownHandler tBoxDownHandler;

    @Autowired
    private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;

    @Autowired
    private IFotaVersionCheckVerifyService fotaVersionCheckVerifyService;

    @Autowired
    private IReqFromAppDbService reqFromAppDbService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    @Autowired
    private IFotaFirmwareDbService fotaFirmwareDbService;

    @Override
    public TboxUpgradStatusVo getTboxUpgradStatus(String vin) {
        return fotaObjectService.getFotaUpgradeStatus(vin);
    }

    @Override
    public VersionCheckBodyVo checkVersion(BaseAppDto baseAppDto) {
        ReqFromAppPo reqFromAppPo = saveReq(baseAppDto, AppEnums.AppReqTypeEnum.NEW_VERSION.getType());

        String vin = baseAppDto.getVin();
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);

        MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_PLAN_OBJ_NOT_EXIST);
        MyAssertUtil.notNull(fotaVinCacheInfo.getObjectId(), OTARespCodeEnum.FOTA_PLAN_OBJ_NOT_EXIST);

        FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
        if (Objects.isNull(fotaVinCacheInfo.getOtaPlanObjectId())) {
            log.warn("[获取车辆升级计划信息]不存在.vin={}, fotaVinCacheInfo={}", vin, fotaVinCacheInfo);
            return noVersion(fotaObjectPo.getCurrentVersion());
        }

        if (Objects.isNull(fotaVinCacheInfo.getOtaPlanId())) {
            log.info("不存在有效的升级任务.vin={}", vin);
            return noVersion(fotaObjectPo.getCurrentVersion());
        }
        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaVinCacheInfo.getOtaPlanId());
        /*
            新版本状态为=无新版本
            1、不存在升级任务
            2、升级任务过期/没启用
         */
        if (Objects.isNull(fotaPlanPo)) {
            log.info("不存在有效的升级任务.vin={}, fotaVinCacheInfo.getOtaPlanId()={}", vin, fotaVinCacheInfo.getOtaPlanId());
            return noVersion(fotaObjectPo.getCurrentVersion());
        }

        boolean validPlanPo = MyBusinessUtil.validPlanPo(fotaPlanPo);
        if(!validPlanPo){
            log.info("升级任务已经失效..vin={}", vin);
            return noVersion(fotaObjectPo.getCurrentVersion());
        }

        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVinCacheInfo.getOtaPlanObjectId());
        if (Enums.TaskObjStatusTypeEnum.isInstalledSuccess(fotaPlanObjListPo.getStatus())) {
            //升级流程已结束
            log.info("升级流程已结束,升级成功.vin={}", vin);
            return noVersion(fotaObjectPo.getCurrentVersion());
        }

        if (Enums.TaskObjStatusTypeEnum.isInstalledPlanInvalid(fotaPlanObjListPo.getStatus())) {
            //升级流程已结束
            log.info("升级任务失效，流程终止,升级失败.vin={}", vin);
            return noVersion(fotaObjectPo.getCurrentVersion());
        }

        boolean beforeCheckVerfiy = Enums.TaskObjStatusTypeEnum.beforeCheckVerfiy(fotaPlanObjListPo.getStatus());
        //特殊情况
        boolean downloadAckBegin = Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_WAIT.getType() == fotaPlanObjListPo.getStatus();

        if (beforeCheckVerfiy || downloadAckBegin) {
            if (beforeCheckVerfiy) {
                //存在新版本：没有进入升级流程/或未完成
                log.info("存在新版本：没有进入升级流程/或未完成.vin={}", vin);
            } else if (downloadAckBegin) {
                log.info("存在新版本：已经完成升级检查，由APP驱动检查，需要重新开始升级版本检查.vin={}", vin);
            }

            return buildNewVersionInner(vin, fotaObjectPo.getCurrentVersion(), fotaPlanPo, reqFromAppPo.getDeviceType(), reqFromAppPo.getId());
        } else {
            if(Objects.isNull(fotaVinCacheInfo.getTransId())){
                log.warn("获取升级事务数据异常.otaPlanId={}, otaPlanObjectId={}", fotaVinCacheInfo.getOtaPlanId(), fotaVinCacheInfo.getOtaPlanObjectId());
                return noVersion(fotaObjectPo.getCurrentVersion());
            }

            FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(fotaVinCacheInfo.getTransId());
            if(Objects.isNull(fotaVersionCheckVerifyPo)){
                log.warn("获取升级事务数据异常.otaPlanId={}, otaPlanObjectId={}", fotaVinCacheInfo.getOtaPlanId(), fotaVinCacheInfo.getOtaPlanObjectId());
                return noVersion(fotaObjectPo.getCurrentVersion());
            }
            VersionCheckBodyVo versionCheckBodyVo = newVersion(fotaPlanObjListPo.getStatus(), fotaObjectPo.getCurrentVersion(), fotaPlanPo, fotaVersionCheckVerifyPo);

            Enums.TaskObjStatusTypeEnum taskObjStatusTypeEnum = Enums.TaskObjStatusTypeEnum.getByType(fotaPlanObjListPo.getStatus());
            boolean isDownloadImmediate = Objects.nonNull(fotaVersionCheckVerifyPo.getUpgradeVerifyImmediate()) && fotaVersionCheckVerifyPo.getUpgradeVerifyImmediate() == Enums.DownloadResultVerifyTypeEnum.IMMEDIATE.getType();
            //如果已经确认下载，但是延迟了，还是走版本检查流程
            if (Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_BEGIN.equals(taskObjStatusTypeEnum) && !isDownloadImmediate) {
                //存在新版本：升级流程检查已完成，还没有到最终结果
                log.info("存在新版本：已经确认下载，但是延迟下载了，还是走版本检查流程.vin={}", vin);
                return buildNewVersionInner(vin, fotaObjectPo.getCurrentVersion(), fotaPlanPo, reqFromAppPo.getDeviceType(), reqFromAppPo.getId());
            } else {
                //存在新版本：升级流程检查已完成，还没有到最终结果
                log.info("存在新版本：升级流程检查已完成.vin={}", vin);
                Map<String, Object> aditionalInfo = Maps.newHashMap();

                //下载中
                if (Enums.TaskObjStatusTypeEnum.CHECK_ACK_VERIFY.equals(taskObjStatusTypeEnum)
                        ||Enums.TaskObjStatusTypeEnum.DOWNLOAD_ACK_BEGIN.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.DOWNLOAD_PROCESSING_WAIT.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.DOWNLOAD_PROCESSING_STOP.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.DOWNLOAD_PROCESSING.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED.equals(taskObjStatusTypeEnum)) {
                    versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.DOWNLOADING.getType());
                    //下载中的状态需要补充剩余时间，下载进度，当前已下载大小
                    setAditionalInfo4Downloading(aditionalInfo, fotaVersionCheckVerifyPo);
                } else if (Enums.TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_SUCCESS.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_WAIT.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.INSTALLED_CANCEL.equals(taskObjStatusTypeEnum)
                        || Enums.TaskObjStatusTypeEnum.INSTALLED_PRECHECK_FAIL.equals(taskObjStatusTypeEnum)
                        //补充安装失败，回滚成功继续回到待安装确认
                        || Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.equals(taskObjStatusTypeEnum)
                ) {
                    //下载完成正常
                    versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.DOWNLOAD_COMPLETE_SUCCESS.getType());

                    //补充预计安装时间
                    OtaDownVersionCheckResponse otaDownVersionCheckResponse = fotaVersionCheckVerifyService.parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());
                    log.info("【APP版本检车:下载已完成，安装阶段】vin={}, otaDownVersionCheckResponse={}", vin, otaDownVersionCheckResponse);
                    if(Objects.nonNull(otaDownVersionCheckResponse)) {
                        aditionalInfo.put("installedRemainTime", otaDownVersionCheckResponse.getEstimatedUpgradeTime());
                    }
                } else if (Enums.TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_FAIL.equals(taskObjStatusTypeEnum)) {
                    //下载异常
                    versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.DOWNLOAD_COMPLETE_FAIL.getType());
                } else if (Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_BEGIN.equals(taskObjStatusTypeEnum)) {
                    //下载完成，且预约安装
                    //如果有预定
                    if (Objects.nonNull(fotaVersionCheckVerifyPo.getInstalledVerifyBookedTime())) {
                        aditionalInfo.put("installedTime", MyDateUtil.toEpochMilli(fotaVersionCheckVerifyPo.getInstalledVerifyBookedTime()));

                        //补充预计安装时间
                        OtaDownVersionCheckResponse otaDownVersionCheckResponse = fotaVersionCheckVerifyService.parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());
                        if(Objects.nonNull(otaDownVersionCheckResponse)) {
                            aditionalInfo.put("installedRemainTime", otaDownVersionCheckResponse.getEstimatedUpgradeTime());
                        }
                        versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.INSTALLED_VERIFY_WITH_BOOKED_TIME.getType());
                    } else {
                        //如果有安装进度
                        setAditionalInfo4InstalledProcessing(aditionalInfo, fotaVersionCheckVerifyPo);
                        versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.INSTALLING.getType());
                    }
                } else if (Enums.TaskObjStatusTypeEnum.INSTALLED_PROCESSING.equals(taskObjStatusTypeEnum)) {
                    setAditionalInfo4InstalledProcessing(aditionalInfo, fotaVersionCheckVerifyPo);
                    //进度
                    versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.INSTALLING.getType());
                } else if (Enums.TaskObjStatusTypeEnum.INSTALLED_FAIL.equals(taskObjStatusTypeEnum)) {
                    //升级失败需要单独区分
                    versionCheckBodyVo.setStatus(AppEnums.CheckVersionStatusType4AppEnum.INSTALLED_FAIL.getType());
                }
                versionCheckBodyVo.setAditionalInfo(aditionalInfo);
                return versionCheckBodyVo;
            }
        }
    }

    /**
     *
     * @param aditionalInfo
     * @param fotaVersionCheckVerifyPo
     */
    public void setAditionalInfo4Downloading(Map<String, Object> aditionalInfo, FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo){
        if(Objects.nonNull(aditionalInfo) && Objects.nonNull(fotaVersionCheckVerifyPo)) {
            aditionalInfo.put("downloadTotal", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadFullSize(), 0L));
            aditionalInfo.put("downloadFinished", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadFinishedSize(), 0L));
            aditionalInfo.put("downloadPercentRate", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadPercentRate(), 0));
            aditionalInfo.put("downloadRemainTime", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getDownloadRemainTime(), 0));
        }
    }

    /**
     *
     * @param aditionalInfo
     * @param fotaVersionCheckVerifyPo
     */
    public void setAditionalInfo4InstalledProcessing(final Map<String, Object> aditionalInfo, FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo){
        //升级中，需要携带进度信息
        //如果有安装进度
        //进度，剩余时间，当前安装模块
        //两种情况：1、安装已经开始，2=安装只是确认还未开始
        aditionalInfo.put("installedCurrentIndex", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getInstalledCurrentIndex(), 0));
        aditionalInfo.put("installedPercentRate", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getInstalledPercentRate(), 0));

        OtaDownVersionCheckResponse otaDownVersionCheckResponse = null;
        FotaFirmwarePo fotaFirmwarePo = null;
        if(/*Objects.nonNull(fotaVersionCheckVerifyPo.getInstalledTotalNum()) || */Objects.nonNull(fotaVersionCheckVerifyPo.getInstalledCurrentIndex()) && fotaVersionCheckVerifyPo.getInstalledCurrentIndex() > 0) {
            aditionalInfo.put("installedTotalNum", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getInstalledTotalNum(), 0));
            aditionalInfo.put("installedRemainTime", MyObjectUtil.getOrDefaultT(fotaVersionCheckVerifyPo.getInstalledRemainTime(), 0));
            fotaFirmwarePo =  fotaFirmwareDbService.getById(fotaVersionCheckVerifyPo.getInstalledFirmwareId());
        }else{
            otaDownVersionCheckResponse = fotaVersionCheckVerifyService.parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());

            aditionalInfo.put("installedTotalNum", MyObjectUtil.getOrDefaultT(otaDownVersionCheckResponse.getEcuFirmwareVersionInfos().size(), 0));
            aditionalInfo.put("installedRemainTime", MyObjectUtil.getOrDefaultT(otaDownVersionCheckResponse.getEstimatedUpgradeTime(), 0));
            fotaFirmwarePo =  fotaFirmwareDbService.getById(otaDownVersionCheckResponse.getEcuFirmwareVersionInfos().get(0).getFirmwareId());
        }
        if (Objects.nonNull(fotaFirmwarePo)) {
            aditionalInfo.put("installedEcuName", fotaFirmwarePo.getComponentName());
        }
    }


    /**
     * 构建存在新版本响应
     * @param vin
     * @param currentVersion
     * @param deviceType
     * @return
     */
    private VersionCheckBodyVo buildNewVersionInner(String vin, String currentVersion, FotaPlanPo fotaPlanPo, Integer deviceType, Long reqId){
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_DOWN_VERSION_CHECK_FROM_APP, vin, deviceType, reqId);

        boolean send2Tbox = tBoxDownHandler.send(otaProtocol);
        if(send2Tbox) {
            log.info("发送版本检查通知消息成功，开始同步结果到APP.vin={}", vin);
            return newVersion(AppEnums.CheckVersionStatusType4AppEnum.WAIT_CHECK.getType(), currentVersion, fotaPlanPo, null);
        }else {
            log.warn("发送版本检查通知消息失败.vin={}", vin);
            return noVersion(currentVersion);
        }
    }

    /**
     * 返回无新版本结果响应
     * @return
     */
    private VersionCheckBodyVo noVersion(String currentVersion){
        VersionCheckBodyVo versionCheckBodyVo = new VersionCheckBodyVo();
        versionCheckBodyVo.setCheckResult(AppEnums.CheckVersionResultEnum.NO_VERSION.getType());
        versionCheckBodyVo.setCurrentVersion(currentVersion);
        return versionCheckBodyVo;
    }

    /**
     * 新版本需要等待异步回调
     *
     * @param status
     * @param currentVersion
     * @param fotaPlanPo
     * @param fotaVersionCheckVerifyPo
     * @return
     */
    private VersionCheckBodyVo newVersion(Integer status, String currentVersion, FotaPlanPo fotaPlanPo, FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo){
        VersionCheckBodyVo versionCheckBodyVo = new VersionCheckBodyVo();
        versionCheckBodyVo.setCheckResult(AppEnums.CheckVersionResultEnum.NEW_VERSION.getType());
        versionCheckBodyVo.setCurrentVersion(currentVersion);
        versionCheckBodyVo.setStatus(status);

        if(Objects.nonNull(fotaPlanPo)) {
            versionCheckBodyVo.setTargetVersion(fotaPlanPo.getTargetVersion());
            versionCheckBodyVo.setTaskTips(fotaPlanPo.getTaskTips());
            versionCheckBodyVo.setNewVersionTips(fotaPlanPo.getNewVersionTips());
            versionCheckBodyVo.setDownloadTips(fotaPlanPo.getDownloadTips());
            versionCheckBodyVo.setDisclaimer(fotaPlanPo.getDisclaimer());
        }

        //如果已经完成了版本检查，则需要补充对应的属性字段
        if(Objects.nonNull(fotaVersionCheckVerifyPo)) {

            versionCheckBodyVo.setTaskId(fotaPlanPo.getId());
            versionCheckBodyVo.setTransId(fotaVersionCheckVerifyPo.getId());

            OtaDownVersionCheckResponse otaDownVersionCheckResponse = fotaVersionCheckVerifyService.parseFromFotaVersionCheckPo(fotaVersionCheckVerifyPo.getCheckReqId());
            if(Objects.nonNull(otaDownVersionCheckResponse)){
                versionCheckBodyVo.setFullPkgFileSize(otaDownVersionCheckResponse.getEstimatedDownloadPackageSize());
            }
        }
        return versionCheckBodyVo;
    }

    /**
     * 保存来自App的请求
     * @param req
     * @param reqType
     */
    private ReqFromAppPo saveReq(BaseAppDto req, int reqType){
        ReqFromAppPo reqFromAppPo = new ReqFromAppPo();
        reqFromAppPo.setId(IdWorker.getId());
        reqFromAppPo.setDeviceType(req.getDeviceType());
        reqFromAppPo.setVin(req.getVin());
        reqFromAppPo.setReqType(reqType);
        reqFromAppPo.setAckStatus(0);
        reqFromAppPo.setCreateBy(CommonConstant.USER_ID_SYSTEM);
        reqFromAppPo.setUpdateBy(CommonConstant.USER_ID_SYSTEM);

        if(reqType == AppEnums.AppReqTypeEnum.REMOTED_DOWNLOAD.getType()){
            RemoteDownloadDto remoteDownloadDto = (RemoteDownloadDto)req;
            reqFromAppPo.setTransId(remoteDownloadDto.getTransId());
            reqFromAppPo.setTaskId(remoteDownloadDto.getTaskId());
        }else if(reqType == AppEnums.AppReqTypeEnum.REMOTED_INSTALLED.getType()){
            RemoteInstalledDto remoteInstalledDto = (RemoteInstalledDto)req;
            reqFromAppPo.setTransId(remoteInstalledDto.getTransId());
            reqFromAppPo.setTaskId(remoteInstalledDto.getTaskId());
        }else if(reqType == AppEnums.AppReqTypeEnum.CANCEL_INSTALLED_BOOKED.getType()){
            RemoteInstalledDto remoteInstalledDto = (RemoteInstalledDto)req;
            reqFromAppPo.setTransId(remoteInstalledDto.getTransId());
            reqFromAppPo.setTaskId(remoteInstalledDto.getTaskId());
        }
        boolean saveReq = reqFromAppDbService.save(reqFromAppPo);
        if(!saveReq){
            log.warn("保存来自App端的请求异常.req={}", req);
            throw new AdamException(RespCode.SERVER_EXECUTE_ERROR);
        }
        return reqFromAppPo;
    }

    /**
     * 判断云端任务是否已经失效
     * @param vin
     * @param otaPlanId
     * @return
     */
    private boolean judgeFotaPlanValid(String vin, Long otaPlanId){
        //检查是否存在有效的升级任务
        FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(otaPlanId);
        return MyBusinessUtil.validPlanPo(fotaPlanPo);
    }

    @Override
    public SyncResultVo remoteDownload(RemoteDownloadDto remoteDownloadDto) {
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(remoteDownloadDto.getVin());
        MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST);
        boolean judgeFotaPlanValid = judgeFotaPlanValid(remoteDownloadDto.getVin(), fotaVinCacheInfo.getOtaPlanId());
        if(!judgeFotaPlanValid){
            log.info("云端任务已失效，无法继续操作.vin={}, otaPlanId={}", remoteDownloadDto.getVin(), fotaVinCacheInfo.getOtaPlanId());
            return SyncResultVo.builder().success(3).msg("任务失效").build();
        }

        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVinCacheInfo.getOtaPlanObjectId());
        //如果已经进入到OTA升级下载确认之后阶段，直接返回提示前端下载确认操作已经完成，无需重复设置
        //补充：如果下载失败，给予TBOX重新下载的机会
        if(Enums.TaskObjStatusTypeEnum.afterDownloadVerfiyResult(fotaPlanObjListPo.getStatus()) && Enums.TaskObjStatusTypeEnum.DOWNLOAD_COMPLETED_FAIL.getType()!= fotaPlanObjListPo.getStatus()) {
            Map<String, Object> additionalInfo = Maps.newHashMap();
            //设置获取当前的下载进度相关属性
            FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(fotaVinCacheInfo.getTransId());
            setAditionalInfo4Downloading(additionalInfo, fotaVersionCheckVerifyPo);
            return SyncResultVo.builder().success(2).content(additionalInfo).build();
        }

        ReqFromAppPo reqFromAppPo = saveReq(remoteDownloadDto, AppEnums.AppReqTypeEnum.REMOTED_DOWNLOAD.getType());

        //TOBX 需要考虑唤醒TBOX
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_DOWN_DOWNLOAD_VERIFY_RESULT, remoteDownloadDto.getVin(), remoteDownloadDto.getDeviceType(), reqFromAppPo.getId());
        OtaCommonPayload otaCommonPayload = TBoxUtil.buildOtaCommonPayload(otaProtocol);
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, remoteDownloadDto.getTaskId(), remoteDownloadDto.getTransId());
        otaCommonPayload.setReqId(reqFromAppPo.getId());
        otaProtocol.getBody().setOtaDownDownloadVerifyResult(buiildOtaDownDownloadVerifyResult(remoteDownloadDto, reqFromAppPo.getId()));
        boolean sendToBox = tBoxDownHandler.send(otaProtocol);

        if(!sendToBox){
            log.warn("[OTA云端发送消息指令到TBOX失败]remoteDownloadDto={}", remoteDownloadDto.toString());
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.APP_REMOTE_DOWNLOAD_FAIL);
        }
        return SyncResultVo.builder().success(1).build();
    }

    @Override
    public SyncResultVo remoteInstalled(RemoteInstalledDto remoteInstalledDto) {
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(remoteInstalledDto.getVin());
        MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST);

        boolean judgeFotaPlanValid = judgeFotaPlanValid(remoteInstalledDto.getVin(), fotaVinCacheInfo.getOtaPlanId());
        if(!judgeFotaPlanValid){
            log.info("云端任务已失效，无法继续操作.vin={}, otaPlanId={}", remoteInstalledDto.getVin(), fotaVinCacheInfo.getOtaPlanId());
            return SyncResultVo.builder().success(3).msg("任务失效").build();
        }

        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVinCacheInfo.getOtaPlanObjectId());
        MyAssertUtil.notNull(fotaPlanObjListPo, OTARespCodeEnum.FOTA_PLAN_OBJ_LIST_NOT_EXIST);

        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.getById(fotaVinCacheInfo.getTransId());
        //如果远程下载确认已经
        boolean isInstalledVerfiyBooked = false;
        if(fotaPlanObjListPo.getStatus().equals(Enums.TaskObjStatusTypeEnum.INSTALLED_ACK_BEGIN.getType())){
            //如果是预约安装类型，则立即安装权限优先级更高
            isInstalledVerfiyBooked = Objects.nonNull(fotaVersionCheckVerifyPo.getInstalledVerifyBookedTime());
        }

        boolean isCancle = fotaPlanObjListPo.getStatus().equals(Enums.TaskObjStatusTypeEnum.INSTALLED_CANCEL.getType());
        boolean isPrecheckFail = Enums.TaskObjStatusTypeEnum.INSTALLED_PRECHECK_FAIL.getType()== fotaPlanObjListPo.getStatus();
        //补充升级失败，给予TBOX重试安装的机会
        boolean isCompleteFail = Enums.TaskObjStatusTypeEnum.INSTALLED_COMPLETED_FAIL.getType()== fotaPlanObjListPo.getStatus();
        //如果已经进入到OTA升级下载确认之后阶段，直接返回提示前端下载确认操作已经完成，无需重复设置
        if(Enums.TaskObjStatusTypeEnum.afterInstalledVerfiyResult(fotaPlanObjListPo.getStatus()) &&  !isCompleteFail && !isPrecheckFail && !isInstalledVerfiyBooked && !isCancle) {
            Map<String, Object> additionalInfo = Maps.newHashMap();
            setAditionalInfo4InstalledProcessing(additionalInfo, fotaVersionCheckVerifyPo);

            return SyncResultVo.builder().success(2).content(additionalInfo).build();
        }

        ReqFromAppPo reqFromAppPo = saveReq(remoteInstalledDto, AppEnums.AppReqTypeEnum.REMOTED_INSTALLED.getType());
        //TOBX 需要考虑唤醒TBOX
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_DOWN_INSTALL_VERIFY_RESULT, remoteInstalledDto.getVin(), remoteInstalledDto.getDeviceType(), reqFromAppPo.getId());
        OtaCommonPayload otaCommonPayload = otaProtocol.getBody().getOtaCommonPayload();
        otaCommonPayload.setReqId(reqFromAppPo.getId());
        TBoxUtil.paddingOtaCommonPayload(otaCommonPayload, remoteInstalledDto.getTaskId(), remoteInstalledDto.getTransId());
        otaProtocol.getBody().setOtaDownInstalledVerifyResult(buiildOtaDownInstalledVerifyResult(remoteInstalledDto, reqFromAppPo.getId()));
        boolean sendToBox = tBoxDownHandler.send(otaProtocol);
        if(!sendToBox){
            log.warn("[OTA云端发送消息指令到TBOX失败]req={}", remoteInstalledDto);
        }
        if(!sendToBox){
            //TODO 需要考虑是否推送回APP
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.APP_REMOTE_INSTALLED_FAIL);
        }
        return SyncResultVo.builder().success(1).build();
    }

    @Override
    public SyncResultVo cancelInstalledBooked(CancelInstalledBookedDto cancelInstalledBookedDto) {
        FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPoByVin(cancelInstalledBookedDto.getVin());
        MyAssertUtil.notNull(fotaVersionCheckVerifyPo, OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST);

        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(cancelInstalledBookedDto.getVin());
        MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_CHECK_VERIFY_NOT_EXIST);
        boolean judgeFotaPlanValid = judgeFotaPlanValid(cancelInstalledBookedDto.getVin(), fotaVinCacheInfo.getOtaPlanId());
        if(!judgeFotaPlanValid){
            log.info("云端任务已失效，无法继续操作.vin={}, otaPlanId={}", cancelInstalledBookedDto.getVin(), fotaVinCacheInfo.getOtaPlanId());
            return SyncResultVo.builder().success(3).msg("任务失效").build();
        }

        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.getById(fotaVersionCheckVerifyPo.getOtaPlanObjectId());
        //如果已经进入到OTA升级下载确认之后阶段，直接返回提示前端下载确认操作已经完成，无需重复设置
        if(Enums.TaskObjStatusTypeEnum.INSTALLED_CANCEL.getType() == fotaPlanObjListPo.getStatus()) {
            return SyncResultVo.builder().success(2).build();
        }

        ReqFromAppPo reqFromAppPo = saveReq(cancelInstalledBookedDto, AppEnums.AppReqTypeEnum.NEW_VERSION.getType());
        //TOBX 需要考虑唤醒TBOX
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(BusinessTypeEnum.OTA_DOWN_CANCEL_INSTALLED_BOOKED, cancelInstalledBookedDto.getVin(), cancelInstalledBookedDto.getDeviceType(), reqFromAppPo.getId());
        OtaDownCancelCommand otaDownCancelCommand = buildOtaDownCancelCommand(cancelInstalledBookedDto, AppEnums.ClientSourceTypeEnum.FROM_APP.getType());
        otaProtocol.getBody().setOtaDownCancelCommand(otaDownCancelCommand);
        // TODO: 需要设置进 otaProtocol.body中
        boolean sendToBox = tBoxDownHandler.send(otaProtocol);
        if(!sendToBox){
            log.warn("[OTA云端发送消息指令到TBOX失败]cancelInstalledBookedDto={}", cancelInstalledBookedDto.toString());
        }
        if(!sendToBox){
            throw ExceptionUtil.buildAdamException(OTARespCodeEnum.APP_REMOTE_INSTALLED_FAIL);
        }
        return SyncResultVo.builder().success(1).build();
    }

    /**
     * 构建对象-OtaDownDownloadVerifyResult
     * @param remoteDownloadDto
     * @return
     */
    private OtaDownDownloadVerifyResult buiildOtaDownDownloadVerifyResult(RemoteDownloadDto remoteDownloadDto, Long reqId){
        OtaDownDownloadVerifyResult otaDownDownloadVerifyResult = new OtaDownDownloadVerifyResult();
        otaDownDownloadVerifyResult.setVerifyResult(1);
        return otaDownDownloadVerifyResult;
    }

    /**
     * 构建对象-OtaDownInstalledVerifyResult
     * @param remoteInstalledDto
     * @return
     */
    private OtaDownInstalledVerifyResult buiildOtaDownInstalledVerifyResult(RemoteInstalledDto remoteInstalledDto, Long reqId){
        OtaDownInstalledVerifyResult otaDownInstalledVerifyResult = new OtaDownInstalledVerifyResult();
        otaDownInstalledVerifyResult.setInstalledTime(remoteInstalledDto.getInstalledTime());
        otaDownInstalledVerifyResult.setInstalledType(remoteInstalledDto.getInstalledType());
        otaDownInstalledVerifyResult.setVerifyResult(remoteInstalledDto.getVerifyResult());
        return otaDownInstalledVerifyResult;
    }

    /**
     * 构建取消升级命令参数
     * @param cancelInstalledBookedDto
     * @param sourceType
     * @return
     */
    private OtaDownCancelCommand buildOtaDownCancelCommand(CancelInstalledBookedDto cancelInstalledBookedDto, Integer sourceType) {
    	OtaDownCancelCommand otaDownCancelCommand = new OtaDownCancelCommand();
    	otaDownCancelCommand.setTaskId(cancelInstalledBookedDto.getTaskId());
    	otaDownCancelCommand.setTransId(cancelInstalledBookedDto.getTransId());
    	otaDownCancelCommand.setSourceType(sourceType);
    	return otaDownCancelCommand;
    }
}
