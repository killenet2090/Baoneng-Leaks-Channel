package com.bnmotor.icv.tsp.ota.service.impl;

import com.alibaba.fastjson.JSON;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownVersionCheckResponse;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import com.bnmotor.icv.tsp.ota.common.enums.TBoxRespCodeEnum;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeMessageBase;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeNewVersionMessage;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeOtherMessage;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.ReqFromAppPo;
import com.bnmotor.icv.tsp.ota.model.req.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.ota.model.resp.app.BaseAppBodyVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.VersionCheckBodyVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.VersionCheckVo;
import com.bnmotor.icv.tsp.ota.model.resp.app.VoidAppVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.*;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectCacheInfoService;
import com.bnmotor.icv.tsp.ota.service.IPush4AppService;
import com.bnmotor.icv.tsp.ota.service.IReqFromAppDbService;
import com.bnmotor.icv.tsp.ota.service.feign.MessageCenterFeignService;
import com.bnmotor.icv.tsp.ota.service.feign.MsDeviceFeignService;
import com.bnmotor.icv.tsp.ota.service.feign.PushFeignService;
import com.bnmotor.icv.tsp.ota.service.feign.TspUserFeignService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.bnmotor.icv.tsp.ota.util.MyDateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @ClassName: Push4AppServiceImpl
 * @Description: ??????????????????
 * @author: xuxiaochang1
 * @date: 2020/8/20 21:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. ??????????????????????????????????????????????????????????????????????????????????????????????????????
 */
@Service
@Slf4j
public class Push4AppServiceImpl implements IPush4AppService {
    
	@Autowired
    private PushFeignService pushFeignService;

    @Autowired
    private TspUserFeignService tspUserFeignService;

    @Autowired
    private MessageCenterFeignService messageCenterFeignService;

    @Autowired
    private MsDeviceFeignService msDeviceFeignService;

    @Autowired
    private IReqFromAppDbService reqFromAppDbService;

    @Autowired
    private IFotaObjectCacheInfoService fotaObjectCacheInfoService;

    private static final String BN_OTA_VERSION_CHECK_VIEW = "BNOtaVersionCheckView";

    @Override
    public <T> ResponseEntity push2App(T t, String vin, Long reqId) {
        log.info("???????????????App-----start-----. vin={}", vin);
        ResponseEntity responseEntity = null;
        try {
            String content = JSON.toJSONString(t);
            List<String> idList= Lists.newArrayList();
            //?????????????????????????????????????????????Id
            String registerId = getUserRegistrationId(vin);
            log.info("push=> vin|{}, registerId|{}", vin, registerId);
            if(!StringUtils.isEmpty(registerId)) {
                idList.add(registerId);
            }

            /*if(vin.equals("LAEE7BC80C8X02172") && !idList.contains("100d855909cf99455cd")){
                idList.add("100d855909cf99455cd");
            }*/
            /*if(vin.equals("LJ8E3A5M5GC010480") && !idList.contains("1a1018970a72d7d80dc")){
                idList.add("1a1018970a72d7d80dc");
            }*/
            //idList.add("13165ffa4eaa7895a5a");
            if(MyCollectionUtil.isEmpty(idList)){
                log.warn("[???????????????????????????????????????id????????????]vin={}", vin);
                return RestResponse.error(RespCode.SERVER_DATA_ERROR);
            }
            JpushMsgDto build = JpushMsgDto.builder()
                    .businessId(/*Long.toString(IdWorker.getId())*/1000086+"")
                    .fromType(3)
                    .sendContent(content)
                    .sendTitle("OTA????????????????????????")
                    .sendAllFlag(false)
                    .sendRegistrationIdList(idList)
                    .sendType(1)
                    .build();
            log.info("build={}", JsonUtil.toJson(build));
            responseEntity = pushFeignService.sendMessage(build);
            log.info("???????????????App-----end-----. vin={}, responseEntity={}", vin, responseEntity);
        } catch (Throwable e) {
            log.error("?????????????????????????????????.t={}", t, e);
        }
        ResponseEntity newResponseEntity;
        if (Objects.nonNull(responseEntity) && responseEntity.getStatusCodeValue()== HttpStatus.OK.value()) {
            newResponseEntity = responseEntity;
        }else {
            newResponseEntity = RestResponse.error(RespCode.OTHER_SERVICE_INVOKE_ERROR);
        }
        //????????????????????????????????????
        if(Objects.nonNull(reqId) && reqId > 0L){
            ReqFromAppPo reqFromApp = new ReqFromAppPo();
            reqFromApp.setId(reqId);
            reqFromApp.setAckStatus(1);
            reqFromApp.setUpdateBy(CommonConstant.USER_ID_SYSTEM);
            boolean updateReqFromApp = reqFromAppDbService.updateById(reqFromApp);
            log.info("????????????APP????????????????????????.updateReqFromApp={}, reqId={}", updateReqFromApp, reqId);
        }
        return newResponseEntity;
    }

    @Override
    public ResponseEntity push2MessageCenter(String vin, AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum, Object attachInfo) {
        log.info("???????????????????????????-----start-----. vin={}, desc={}", vin, messageCenterMsgTypeEnum.getDesc());
        RestResponse<String> responseEntity = null;
        try {
            String registerId = getUserRegistrationId(vin);
            log.info("push=> call getUserRegistrationId()---vin|{}, registerId|{}", vin, registerId);
            if (StringUtils.isEmpty(registerId)) {
                log.warn("[???????????????????????????????????????id????????????]vin={}", vin);
                return RestResponse.error(RespCode.SERVER_DATA_ERROR);
            }

            Long uid = getUid(vin);
            log.info("push=> call getUid()---vin|{}, uid|{}", vin, uid);
            if (Objects.isNull(uid) || uid <= 0L) {
                log.warn("[????????????id??????]vin={}", vin);
                return RestResponse.error(RespCode.SERVER_DATA_ERROR);
            }

            RestResponse<DrivingLicPlateVo> drivingLicPlateVoResponse = msDeviceFeignService.getDrivingLicPlate(vin);
            if (Objects.isNull(drivingLicPlateVoResponse)) {
                log.warn("[??????vin???????????????????????????]vin={}", vin);
                return RestResponse.error(RespCode.SERVER_DATA_ERROR);
            }
            String licenseNo = drivingLicPlateVoResponse.getRespData().getDrivingLicPlate();
            log.info("[??????Vin?????????????????????].vin={}, licenseNo={}", vin, licenseNo);

            MessageSendReqVo messageSendReqVo = new MessageSendReqVo();
            /*
             1????????????????????????????????????????????????????????????
             2?????????????????????????????????????????????????????????
             */

            messageSendReqVo.setCategoryId(messageCenterMsgTypeEnum.getType());
            //???????????????????????????
            messageSendReqVo.setTemplateId(messageCenterMsgTypeEnum.getTemplateId());

            if(!AppEnums.MessageCenterMsgTypeEnum.INSTALLED_BOOKED_START.equals(messageCenterMsgTypeEnum)) {
                messageSendReqVo.setContent("{\"licensePlateNumber\":\"" + licenseNo + "\"}");
            }else{
                //?????????????????????????????????????????????
                try{
                    OtaUpgradeOtherMessage otaUpgradeOtherMessage = (OtaUpgradeOtherMessage) attachInfo;
                    Map<String, Object> attachMap = (Map)otaUpgradeOtherMessage.getT();
                    Long appointmentTime = (Long)attachMap.get("appointmentTime");
                    messageSendReqVo.setContent("{\"licensePlateNumber\":\"" + licenseNo + "\", \"appointmentTime\":\"" + MyDateUtil.formateDateTime(new Date(appointmentTime)) + "\"}");
                }catch(Exception e){
                    log.error("??????????????????????????????????????????.vin={}, messageCenterMsgTypeEnum={}, attachInfo={}", vin, messageCenterMsgTypeEnum, attachInfo, e);
                    return null;
                }
            }
            messageSendReqVo.setTitle(messageCenterMsgTypeEnum.getDesc());
            // 1. ????????????, 2. ????????????, 3. ????????????, 4. ????????????
            messageSendReqVo.setContentType(1);
            //????????????:1. ??????, 2. ??????
            messageSendReqVo.setTargetType(1);

            messageSendReqVo.setAction(BN_OTA_VERSION_CHECK_VIEW);

            ActionParams actionParams = new ActionParams();
            actionParams.setVin(vin);
            actionParams.setAction(BN_OTA_VERSION_CHECK_VIEW);
            messageSendReqVo.setActionParams(JsonUtil.toJson(actionParams));

            messageSendReqVo.setImmediately(Boolean.TRUE);

            //????????????????????????
            List<MessageTargetPlatformReqVo> targetPlatforms = Lists.newArrayList();
            MessageTargetPlatformReqVo messageTargetPlatformReqVo = new MessageTargetPlatformReqVo();
            //1. ??????SMS???2. ??????APP???3. ??????HU
            messageTargetPlatformReqVo.setPlatform(2);
            //?????????????????????????????????????????????
            messageTargetPlatformReqVo.setPlatformInteraction(AppEnums.MessageCenterMsgTypeEnum.messageCenterMsgType2Way(messageCenterMsgTypeEnum));

            targetPlatforms.add(messageTargetPlatformReqVo);
            messageSendReqVo.setTargetPlatforms(targetPlatforms);

            List<MessageTargetReqVo> targets = Lists.newArrayList();
            MessageTargetReqVo messageTargetReqVo = new MessageTargetReqVo();
            // 11. SMS-?????????21. APP-???????????????31. ??????-IHU?????????32. ??????-TBOX
            messageTargetReqVo.setTarget(registerId);
            messageTargetReqVo.setChannel(21);
            messageTargetReqVo.setUid(uid);
            targets.add(messageTargetReqVo);
            messageTargetPlatformReqVo.setTargets(targets);


            MessageCommonReqVo messageCommonReqVo = new MessageCommonReqVo();
            log.info("?????????????????????????????????.messageCommonReqVo={}, messageSendReqVo={}", messageCommonReqVo, JsonUtil.toJson(messageSendReqVo));
            responseEntity = messageCenterFeignService.singleSend(messageCommonReqVo, messageSendReqVo);

            log.info("???????????????????????????-----end-----. vin={}, responseEntity={}", vin, responseEntity);
        } catch (Throwable e) {
            log.error("?????????????????????????????????.vin={}", vin, e);
        }
        return ResponseEntity.ok(responseEntity.getRespData());
    }

    @Override
    public ResponseEntity push2MessageCenter(OtaUpgradeMessageBase otaUpgradeMessageBase) {
        String vin = otaUpgradeMessageBase.getVin();
        AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum = otaUpgradeMessageBase.getMessageCenterMsgTypeEnum();
        return push2MessageCenter(vin, messageCenterMsgTypeEnum, otaUpgradeMessageBase.getData());
    }

    @Override
    public String getUserRegistrationId(String vin) {
        RestResponse<GetDeviceInfoVo> response = tspUserFeignService.device(String.valueOf(vin));
        if(Objects.nonNull(response) && RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            return Objects.nonNull(response.getRespData()) ? response.getRespData().getPushRid() : null;
        }
        log.warn("??????vin????????????????????????Id??????.vin={}", vin);
        return null;
    }

    @Override
    public Long getUid(String vin) {
        RestResponse<VehicleUserVo> response = tspUserFeignService.getUserInfo(vin);
        if(Objects.nonNull(response) && RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            return Objects.nonNull(response.getRespData()) ? Long.parseLong(response.getRespData().getUid()) : null;
        }
        log.warn("??????vin????????????Id??????.vin={}", vin);
        return null;
    }

    @Override
    public void pushNewVersionCheck2App(OtaUpgradeNewVersionMessage otaUpgradeNewVersionMessage /*OtaProtocol otaProtocol, Long transId, Supplier<FotaObjectPo> fotaObjectPoSupplier, Supplier<FotaPlanPo> fotaPlanPoSupplier, Supplier<FotaVersionCheckPo> fotaVersionCheckPoSupplier, Long reqId, boolean newVersion*/){
        boolean newVersion = otaUpgradeNewVersionMessage.getNewVersion();
        OtaProtocol otaProtocol = otaUpgradeNewVersionMessage.getOtaProtocol();
        Supplier<FotaObjectPo> fotaObjectPoSupplier = otaUpgradeNewVersionMessage.getFotaObjectPoSupplier();
        Long reqId = otaUpgradeNewVersionMessage.getReqId();
        if(newVersion) {
            Long transId = otaUpgradeNewVersionMessage.getTransId();
            VersionCheckBodyVo versionCheckBodyVo = newVersion(transId, fotaObjectPoSupplier.get(), otaUpgradeNewVersionMessage.getFotaPlanPoSupplier().get(), otaUpgradeNewVersionMessage.getOtaDownVersionCheckResponseSupplier().get());
            VersionCheckVo versionCheckVo = new VersionCheckVo();
            versionCheckVo.setBusinessId(otaUpgradeNewVersionMessage.getOtaProtocol().getBody().getBusinessId());
            versionCheckVo.setBody(versionCheckBodyVo);
            push2App(versionCheckVo, otaUpgradeNewVersionMessage.getOtaProtocol().getOtaMessageHeader().getVin(), reqId);
        }else{
            pushNoVersionCheck2App(otaProtocol, otaProtocol.getOtaMessageHeader().getVin(), fotaObjectPoSupplier, reqId);
        }
    }

    /**
     * ??????????????????????????????APP
     * @param otaProtocol
     * @param vin
     * @param fotaObjectDoSupplier
     * @param reqId
     */
    private void pushNoVersionCheck2App(OtaProtocol otaProtocol, String vin, Supplier<FotaObjectPo> fotaObjectDoSupplier, Long reqId){
        VersionCheckBodyVo versionCheckBodyVo = noVersion(fotaObjectDoSupplier.get());
        VersionCheckVo versionCheckVo =  new VersionCheckVo();
        versionCheckVo.setBusinessId(otaProtocol.getBody().getBusinessId());
        versionCheckVo.setBody(versionCheckBodyVo);
        push2App(versionCheckVo, vin, reqId);
    }

    @Override
    public void pushInstalledVerifyReq2App(String vin, Long transId, Long taskId) {
        VoidAppVo voidAppBodyVo = new VoidAppVo();
        voidAppBodyVo.setBody(new BaseAppBodyVo());
        voidAppBodyVo.getBody().setTransId(transId);
        voidAppBodyVo.getBody().setTransId(taskId);
        push2App(voidAppBodyVo, vin, 0L);
    }

    /**
     * ?????????????????????????????????
     * @param transId
     * @param fotaObjectPo
     * @param fotaPlanPo
     * @return
     */
    private VersionCheckBodyVo newVersion(Long transId, FotaObjectPo fotaObjectPo, FotaPlanPo fotaPlanPo, OtaDownVersionCheckResponse otaDownVersionCheckResponse){
        VersionCheckBodyVo versionCheckBodyVo = new VersionCheckBodyVo();
        versionCheckBodyVo.setCheckResult(AppEnums.CheckVersionResultEnum.NEW_VERSION.getType());
        versionCheckBodyVo.setCurrentVersion(fotaObjectPo.getCurrentVersion());
        //????????????????????????????????????????????????????????????????????????
        versionCheckBodyVo.setTargetVersion(fotaPlanPo.getTargetVersion());
        /*versionCheckBodyVo.setTaskTips(fotaPlanPo.getTaskTips());
        versionCheckBodyVo.setNewVersionTips(fotaPlanPo.getNewVersionTips());
        versionCheckBodyVo.setDownloadTips(fotaPlanPo.getDownloadTips());
        versionCheckBodyVo.setDisclaimer(fotaPlanPo.getDisclaimer());*/
        versionCheckBodyVo.setTaskId(fotaPlanPo.getId());
        versionCheckBodyVo.setTransId(transId);
        versionCheckBodyVo.setRespCode(TBoxRespCodeEnum.SUCCESS.getCode());

        //??????APP?????????????????????????????????
        if(Objects.nonNull(otaDownVersionCheckResponse)){
            versionCheckBodyVo.setFullPkgFileSize(otaDownVersionCheckResponse.getEstimatedDownloadPackageSize());
            if(Objects.nonNull(otaDownVersionCheckResponse.getEstimatedUpgradeTime())) {
                versionCheckBodyVo.setIntalledRemainTime((long) otaDownVersionCheckResponse.getEstimatedUpgradeTime());
            }
        }

        return versionCheckBodyVo;
    }

    /**
     * ????????????????????????????????????
     * @param fotaObjectPo
     * @return
     */
    private VersionCheckBodyVo noVersion(FotaObjectPo fotaObjectPo){
        VersionCheckBodyVo versionCheckBodyVo = new VersionCheckBodyVo();
        versionCheckBodyVo.setCheckResult(AppEnums.CheckVersionResultEnum.NO_VERSION.getType());
        versionCheckBodyVo.setCurrentVersion(fotaObjectPo.getCurrentVersion());
        return versionCheckBodyVo;
    }
}
