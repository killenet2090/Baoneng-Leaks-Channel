package com.bnmotor.icv.tsp.ble.service.feign;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.ParamReader;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleAuthDto;
import com.bnmotor.icv.tsp.ble.model.request.feign.*;
import com.bnmotor.icv.tsp.ble.model.request.pki.*;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDeviceSn;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;

@Service
@Slf4j
public class BleCommonFeignService {

    @Resource
    UserFeignService userFeignService;

    @Resource
    DeviceFeignService deviceFeignService;

    @Resource
    private PushFeignService pushFeignService;

    @Resource
    private SmsFeignService smsFeignService;

    @Resource
    private MessCenFeignService messCenFeignService;

    @Resource
    private DeviceIsOnlineService deviceIsOnlineService;

    @Resource
    private BlePkiFeignService blePkiFeignService;

    @Resource
    private ParamReader paramReader;

    /**
     * ???????????????????????????
     * @param uid???phoneNumber
     * @return
     */
    public UserPhoneVo bleGetFeignUserPhoneVo(String uid){
        UserPhoneVo userVo= new UserPhoneVo();
        try {
            RestResponse<UserPhoneVo> response = userFeignService.queryUserPhoneByNum(uid);
            if (RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
                if (response.getRespData() != null) {
                    userVo = response.getRespData();
                }
            } else {
                log.error("???????????????=[{}]",uid);
                log.error("?????????????????????????????????????????????[{}]", JsonUtil.toJson(response));
                throw new AdamException(response.getRespCode(), response.getRespMsg());
            }
        }catch (JsonProcessingException ex){
            log.error("??????????????????-json??????????????????[{}]", ex.getMessage());
            throw new AdamException(RespCode.OTHER_SERVICE_INVOKE_ERROR);
        }
        return userVo;
    }
    /**
     * ??????????????????????????????ID??????
     * @param uid???phoneNumber
     * @return
     */
    public UserPhoneVo bleQueryUserPushIdVo(String uid, String phoneNum){
        UserPhoneVo userVo= new UserPhoneVo();
        try {
            RestResponse<UserPhoneVo> response = userFeignService.queryUserPushId(phoneNum,uid);
            if (RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
                if (Optional.ofNullable(response.getRespData()).isPresent()) {
                    userVo = response.getRespData();
                }else {
                    throw new AdamException(RespCode.OTHER_SERVICE_PUSHID_ERROR);
                }
            } else {
                log.error("?????????????????????????????????????????????[{}]", JsonUtil.toJson(response));
                throw new AdamException(response.getRespCode(), response.getRespMsg());
            }
        }catch (JsonProcessingException ex){
            log.error("??????????????????-json??????????????????[{}]", ex.getMessage());
            throw new AdamException(RespCode.OTHER_SERVICE_INVOKE_ERROR);
        }
        return userVo;
    }

    public VehicleInfoVo bleGetFeignVehicleInfoVo(String deviceId){
    //???????????? TODO feign????????????????????????
        RestResponse<VehicleInfoVo> vehicleInfoResp = deviceFeignService.getVehicleInfo(deviceId);
        if (vehicleInfoResp == null || vehicleInfoResp.getRespData() == null) {
        log.error("???????????????????????? ???????????????????????????????????????????????????[{}]", vehicleInfoResp);
        throw new AdamException(BLE_DEVICE_UNREGISTER);
        }else {
            return vehicleInfoResp.getRespData();
        }
    }


    /**
     * ??????tsp user????????????
     * @param uid
     * @param phoneNum
     * @return
     */
    public UserVo queryTspUserVo(String uid, String phoneNum){
        UserVo userVo= new UserVo();
        try {
            RestResponse<UserVo> response = userFeignService.queryUserInfo(phoneNum,"PHONE",uid);
            if (RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
                if (response.getRespData() != null){
                    userVo = response.getRespData();
                }
            } else {
                log.error("??????????????????????????????ID??????????????????[{}]", JsonUtil.toJson(response));
                throw new AdamException(response.getRespCode(), response.getRespMsg());
            }
        }catch (JsonProcessingException ex){
            log.error("??????TSP????????????-json??????????????????[{}]", ex.getMessage());
            throw new AdamException(RespCode.OTHER_SERVICE_INVOKE_ERROR);
        }
        return userVo;
    }

    /**
     *??????????????????
     * @param uid
     * @return
     */
    public UserVo queryTspInfo(String uid){
        UserVo userVo= new UserVo();
        try {
            RestResponse<UserVo> response = userFeignService.getUserInfo(uid);
            if (RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
                if (response.getRespData() != null){
                    userVo = response.getRespData();
                }
            } else {
                log.error("??????????????????????????????ID??????????????????[{}]", JsonUtil.toJson(response));
                throw new AdamException(response.getRespCode(), response.getRespMsg());
            }
        }catch (JsonProcessingException ex){
            log.error("??????TSP????????????-json??????????????????[{}]", ex.getMessage());
            throw new AdamException(RespCode.OTHER_SERVICE_INVOKE_ERROR);
        }
        return userVo;
    }

    /**
     * ????????????????????????????????????
     * @param tokenCheck
     * @param uid
     * @return
     */
    public void verifyServicePwd(TokenCheck tokenCheck, String uid){
        RestResponse booleanRestResponse = userFeignService.verifyServicePwd(tokenCheck, uid);
        if (!Optional.ofNullable(booleanRestResponse).isPresent()) {
            log.error("???????????????????????? ???????????????????????????????????????????????????[{}]", booleanRestResponse);
            throw new AdamException(BLE_VERY_PWD_ERROR);
        }else {
            if (!RespCode.SUCCESS.getValue().equals(booleanRestResponse.getRespCode())){
                throw new AdamException(booleanRestResponse.getRespCode(), booleanRestResponse.getRespMsg());
            }
        }
    }

    /**
     * ??????????????????token????????????
     * @param tokenDto
     * @param uid
     * @return
     */
    public void verifyToken(TokenDto tokenDto, String uid){
        RestResponse booleanRestResponse = userFeignService.tokenValid(tokenDto, uid);
        if (!Optional.ofNullable(booleanRestResponse).isPresent()) {
            log.error("???????????????????????? ????????????token?????????????????????????????????[{}]", booleanRestResponse);
            throw new AdamException(BLE_VERY_TOKEN_ERROR);
        }else {
            if (!RespCode.SUCCESS.getValue().equals(booleanRestResponse.getRespCode())){
                throw new AdamException(booleanRestResponse.getRespCode(), booleanRestResponse.getRespMsg());
            }
        }
    }
    /**
     * ????????????????????????????????????
     * @param huCallBack
     * @param uid
     * @return
     */
    public int updateAuthStatus(HuCallBack huCallBack,String uid){
        log.info("start ????????????=[{}]",huCallBack);
        log.info("start ????????????=[{}]",uid);
        RestResponse booleanRestResponse = userFeignService.updateAuthStatus(huCallBack, uid);
        if (!RespCode.SUCCESS.getValue().equals(booleanRestResponse.getRespCode())){
            log.error("????????????????????????????????????????????????????????????=[{}]",huCallBack);
            return Constant.OPERATION_FAILURE;
        }
        log.info("end ????????????=[{}]",huCallBack);
        return Constant.OPERATION_SUCCESS;
    }

    /**
     * ????????????????????????????????????
     * @param vehBindBack
     * @return
     */
    public int vehDeregisterCallback(VehBindBack vehBindBack){
        log.info("start ????????????????????????=[{}]",vehBindBack);
        RestResponse booleanRestResponse = userFeignService.vehBindCallback(vehBindBack);
        if (!RespCode.SUCCESS.getValue().equals(booleanRestResponse.getRespCode())){
            log.error("????????????????????????????????????????????????????????????=[{}]",vehBindBack);
            return Constant.OPERATION_FAILURE;
        }
        log.info("end ????????????????????????=[{}]",vehBindBack);
        return  Constant.OPERATION_SUCCESS;
    }

    /**
     * ????????????????????????????????????
     * @param vin
     * @return
     */
    public void verifyVehIsOnline(String vin){
        ResponseEntity<RestResponse<Boolean>> restResponseResponseEntity = deviceIsOnlineService.onlineStatus(vin);
        if (!Optional.ofNullable(restResponseResponseEntity).isPresent() ||
                !Optional.ofNullable(restResponseResponseEntity.getBody()).isPresent()) {
            log.error("??????????????????????????????????????????[{}]", restResponseResponseEntity);
            throw new AdamException(BLE_IS_ONLINE_ERROR);
        }else {
            if (!Optional.ofNullable(restResponseResponseEntity.getBody().getRespData()).isPresent()){
                throw new AdamException(RespCode.BLE_DEVICE_OFFLINE.getValue(),
                        RespCode.BLE_DEVICE_OFFLINE.getDescription());
            }

            if(!restResponseResponseEntity.getBody().getRespData().booleanValue()){
                throw new AdamException(RespCode.BLE_DEVICE_OFFLINE.getValue(),
                        RespCode.BLE_DEVICE_OFFLINE.getDescription());
            }
        }

    }

    /**
     * ??????PKI????????????????????????
     *
     * @param bleEncryptDto
     * @return
     */
    public byte[] pkiAsymmetricEncrypt(BleEncryptDto bleEncryptDto) {
        log.info("??????????????????????????????");
        log.info(bleEncryptDto.toString());
        RestResponse<AsymmetricVo> response = blePkiFeignService.asymmetricEncrypt(bleEncryptDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode()) || !Optional.ofNullable(response.getRespData()).isPresent()) {
            log.error("??????????????????pki????????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            AsymmetricVo asymmetricVo = response.getRespData();
            return asymmetricVo.getCipherTextBytes();
        }
    }

    /**
     * ??????PKI????????????????????????
     *
     * @param bleEncrptyDto
     * @return
     */
    public String pkiEncrpty(BleEncrptyDto bleEncrptyDto) {
        RestResponse<Map> response = blePkiFeignService.encrpty(bleEncrptyDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode()) || !Optional.ofNullable(response.getRespData()).isPresent()) {
            log.error("??????????????????pki?????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            return response.getRespData().get("content").toString();
        }
    }

    /**
     * ??????PKI????????????????????????
     *
     * @param bleDecrptyDto
     * @return
     */
    public String pkiDecrpty(BleDecrptyDto bleDecrptyDto) {
        RestResponse<Map> response = blePkiFeignService.decrpty(bleDecrptyDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode()) || !Optional.ofNullable(response.getRespData()).isPresent()) {
            log.error("??????????????????pki?????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            return response.getRespData().get("content").toString();
        }
    }

    /**
     * ??????PKI??????????????????
     *
     * @param bleSignDto
     * @return
     */
    public byte[] pkiSign(BleSignDto bleSignDto) {
        RestResponse<SignVo> response = blePkiFeignService.sign(bleSignDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode()) || !Optional.ofNullable(response.getRespData()).isPresent()) {
            log.error("??????????????????pki?????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            return response.getRespData().getSignValue();
        }
    }

    /**
     * ??????PKI??????????????????
     *
     * @param bleVerSignDto
     * @return
     */
    public boolean pkiVerifySign(BleVerSignDto bleVerSignDto) {
        RestResponse<VerifySignVo> response = blePkiFeignService.verifySign(bleVerSignDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            log.error("?????????????????????={}",bleVerSignDto);
            log.error("??????????????????pki?????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            return true;
        }
    }


    /**
     * ??????PKI????????????
     * @param bleImportDto
     * @return
     */
    public boolean pkiImportKey(BleImportDto bleImportDto) {
        RestResponse<Map> response = blePkiFeignService.importKey(bleImportDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            log.error("?????????????????????={}",bleImportDto);
            log.error("?????????????????????????????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            return true;
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param bleVerifyAuthCodeDto
     * @return
     */
    public String delBlueKeyCallback(BleVerifyAuthCodeDto bleVerifyAuthCodeDto) {
        RestResponse<String> response = deviceFeignService.delBlueKeyCallback(bleVerifyAuthCodeDto);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            log.error("????????????????????????????????????{}", response);
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        } else {
            return response.getRespCode();
        }
    }

    /**
     * ??????????????????SN???
     * @param deviceType,vin
     * @return
     */
    public List<BleDeviceSn> queryBleDeviceSn(int deviceType, String vin) {
        RestResponse<List<BleDeviceSn>> response = deviceFeignService.queryBleDeviceSn(deviceType,vin);
        if (!RespCode.SUCCESS.getValue().equals(response.getRespCode()) ||
                !Optional.ofNullable(response.getRespData()).isPresent()||
                response.getRespData().size()==Constant.COMPARE_EQUAL_VALUE) {
            log.error("???????????????????????? ???????????????????????????????????????????????????{}", response);
            throw new AdamException(BLE_KEY_QUERY_DEVICE_sn_ERR);
        } else {
            return response.getRespData();
        }
    }

    /**
     * ??????tbox??????????????????tbox????????????????????????????????????
     * @param bytes
     * @param byteTbox
     * @return
     */
    public int verifyTbox(byte[] bytes,byte[] byteTbox) {
        BleVerSignDto bleVerSignDto = BleVerSignDto
                .builder()
                .algorithm(Constant.ALGORITHM_RSA)
                .applicationName(paramReader.applicationName)
                .inData(bytes)
                .signValue(byteTbox)
                .build();
        boolean bVerify = pkiVerifySign(bleVerSignDto);
        if (!bVerify) {
            log.error("??????tbox??????????????????????????????????????????{}", bVerify);
            return 4;
        }
        return 0;
    }
    /**
     * ????????????????????????
     * @param bleAckPushPo
     * @return
     */
    public boolean ackBasePushToMobileMsg(BleAckPushPo bleAckPushPo, List<Long> userBleKeyIdList){
        try{
            UserPhoneVo userPhoneVo = bleGetFeignUserPhoneVo(bleAckPushPo.getOwnerUserId());
            UserPhoneVo usedUserPhoneVo = bleGetFeignUserPhoneVo(bleAckPushPo.getUsedUserId());
            Map<String, String> extraData = new ConcurrentHashMap<>();
            extraData.put("fromType", "2");
            Map<String,String> contentData = new ConcurrentHashMap<String,String>();
            contentData.put("type",String.valueOf(bleAckPushPo.getCmdType()));
            contentData.put("cmd",String.valueOf(bleAckPushPo.getCmdOrder()));
            contentData.put("status",String.valueOf(bleAckPushPo.getStatus()));
            contentData.put("deviceId",bleAckPushPo.getDeviceId());
            if (bleAckPushPo.getBlekeyId() != null) {
                contentData.put("bleKeyId", bleAckPushPo.getBlekeyId());
            }
            if (userBleKeyIdList != null){
                contentData.put("bleKeyId", JsonUtil.toJson(userBleKeyIdList));
            }
            log.info(contentData.toString());
            Map contentMap = new ConcurrentHashMap<>();
            contentMap.put("type","2");
            contentMap.put("data",contentData);
            contentMap.put("respCode","00000");
            contentMap.put("respMsg","??????");
            List<String> stringList = new ArrayList<>();
            stringList.add(bleAckPushPo.getRegistrationId());
            if (!Optional.ofNullable(userPhoneVo.getPushRid()).isPresent()){
                log.error("????????????ID??????????????????ID???NULL");
            }else {
                if(!bleAckPushPo.getRegistrationId().equals(userPhoneVo.getPushRid())) {
                    stringList.add(userPhoneVo.getPushRid());
                }
            }
            if (Optional.ofNullable(usedUserPhoneVo.getPushRid()).isPresent()){
                stringList.add(usedUserPhoneVo.getPushRid());
            }else {
                log.error("???????????????ID????????????ID???NULL");
            }
            JpushMsgDto jpushMsgDto = JpushMsgDto.builder()
                    .businessId(String.valueOf(bleAckPushPo.getSerialNum()))
                    .fromType(2)
                    .sendAllFlag(false)
                    .sendContent(JsonUtil.toJson(contentMap))
                    .sendTitle("????????????????????????????????????")
                    .sendType(1)
                    .extraData(extraData)
                    .sendRegistrationIdList(stringList)
                    //.sendTagsList()
                    .build();
            log.info("*******************start push msg*****************************");
            log.info(jpushMsgDto.toString());
            pushFeignService.sendMessage(jpushMsgDto);
            log.info("*******************end push msg*****************************");
            return true;
        }catch (JsonProcessingException ex){
            log.error("??????mqtt??????????????????Json??????????????????????????????[(0)]",ex);
            return  false;
        }catch (Exception ex){
            log.error("??????mqtt??????????????????????????????????????????[(0)]",ex);
            return false;
        }
    }

    private List<TargetPlatforms> assembleTargetPlatforms(String uid, String registrationId){
        List<Targets> targetsList = new ArrayList<>();
        List<TargetPlatforms> targetPlatformsList = new ArrayList<>();
        Targets targets = Targets.builder()
                .uid(uid)
                .channel(21)
                .target(registrationId)
                .build();
        targetsList.add(targets);

        TargetPlatforms targetPlatforms = TargetPlatforms.builder()
                .targets(targetsList)
                .platform(2)
                .platformInteraction(String.valueOf(21))
                .build();
        targetPlatformsList.add(targetPlatforms);
        return targetPlatformsList;
    }


    /**
     * ??????????????????
     * @param vehicleInfoVo
     * @param registrationId
     * @param userName
     * @return
     */
    public  void delPushToMobileNotification(VehicleInfoVo vehicleInfoVo, String uid, int cmdType, int cmdOrder,
                                             int status, String  registrationId, String userName){
        try{
            log.info("#####################################################");
            log.info("start delPushToMobileNotification");
            LocalDateTime dt = LocalDateTime.now();
            LocalDateTime localDateTime = dt.plusHours(24L);
            Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
            List<TargetPlatforms> targetPlatformsList = assembleTargetPlatforms(uid,registrationId);
            Map<String, String> content = new ConcurrentHashMap<>();
            content.put("licensePlateNumber",vehicleInfoVo.getDrivingLicPlate());
            Map<String, String> actionParam = new ConcurrentHashMap<>();
            actionParam.put("bId",vehicleInfoVo.getDrivingLicPlate());
            actionParam.put("brand",vehicleInfoVo.getBrandName());
            actionParam.put("deviceId",vehicleInfoVo.getVin());
            actionParam.put("type",String.valueOf(cmdType));
            actionParam.put("cmd",String.valueOf(cmdOrder));
            actionParam.put("status",String.valueOf(status));
            MessageCenterDto messageCenterDto =  MessageCenterDto.builder()
                    .categoryId("1303211738512872661")
                    .templateId("1303211738512732578")
                    .title("????????????????????????????????????")
                    .content(JsonUtil.toJson(content))
                    .contentType(1)
                    .targetType(1)
                    .immediately(true)
                    .effectiveDeadline(DateUtil.formatDateToString(now, DateUtil.TIME_MASK_DEFAULT))
                    .targetPlatforms(targetPlatformsList)
                    .actionParams(JsonUtil.toJson(actionParam))
                    .build();
            RestResponse<String> stringRestResponse = messCenFeignService.singleSend(messageCenterDto);
            log.info(JsonUtil.toJson(messageCenterDto));
            log.info(stringRestResponse.getRespData());
            log.info("success delPushToMobileNotification");
        }catch (Exception ex){
            log.error("??????????????????????????????????????????????????????[()]",ex);
        }
    }


    public void bleBaseNotification(BleNotification bleNotification){
        log.info("#####################################################");
        log.info("start MobileNotification");
        try{
            LocalDateTime dt = LocalDateTime.now();
            LocalDateTime localDateTime = dt.plusHours(24L);
            Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
            List<TargetPlatforms> targetPlatformsList = assembleTargetPlatforms(String.valueOf(bleNotification.getUserId()),
                    bleNotification.getPushId());
            Map<String, String> content = new ConcurrentHashMap<>();
            content.put("username",bleNotification.getUserName());
            content.put("licensePlateNumber",bleNotification.getLicensePlateNumber());
            if (Optional.ofNullable(bleNotification.getExpireDate()).isPresent()){
                content.put("expiredTime",bleNotification.getExpireDate());
            }
            Map<String, String> actionParam = new ConcurrentHashMap<>();
            actionParam.put("bId",String.valueOf(bleNotification.getBleId()));
            actionParam.put("brand",bleNotification.getBrand());
            actionParam.put("deviceId",bleNotification.getDeviceId());
            actionParam.put("type",String.valueOf(bleNotification.getType()));
            actionParam.put("cmd",String.valueOf(bleNotification.getCmd()));
            actionParam.put("status",String.valueOf(bleNotification.getStatus()));
            actionParam.put("username",bleNotification.getUserName());
            actionParam.put("licensePlateNumber",bleNotification.getLicensePlateNumber());
            MessageCenterDto messageCenterDto =  MessageCenterDto.builder()
                    .categoryId(String.valueOf(bleNotification.getCategoryId()))
                    .templateId(String.valueOf(bleNotification.getTemplateId()))
                    .title("????????????????????????????????????")
                    .content(JsonUtil.toJson(content))
                    .contentType(1)
                    .targetType(1)
                    .immediately(true)
                    .effectiveDeadline(DateUtil.formatDateToString(now, DateUtil.TIME_MASK_DEFAULT))
                    .targetPlatforms(targetPlatformsList)
                    .actionParams(JsonUtil.toJson(actionParam))
                    .build();
            RestResponse<String> stringRestResponse = messCenFeignService.singleSend(messageCenterDto);
            log.info(JsonUtil.toJson(messageCenterDto));
            log.info(stringRestResponse.getRespData());
            log.info("success MobileNotification");
        }catch (Exception ex){
            log.error("???????????????????????????????????????????????????????????????[()]",ex);
            log.error("failed OwnerMobileNotification");
        }
    }




    public RestResponse SendSms(BleAuthDto bleAuthDto, VehicleInfoVo vehicleInfoVo, String projectId,
                                String userName, String authCode) {
        HashMap<String, String> extraDataHashMap = new HashMap<>();
        extraDataHashMap.put("userName",userName);
        extraDataHashMap.put("authCode",authCode);
        extraDataHashMap.put("span",String.valueOf(Constant.AUTH_EXPIRE_SPAN));
        String vehColor = Optional.ofNullable(vehicleInfoVo.getColor()).orElse(StringUtil.EMPTY_STRING);
        String caInfo = vehicleInfoVo.getDrivingLicPlate().concat(" ").concat(vehColor).concat(" ")
                .concat(vehicleInfoVo.getVehModelName());
        extraDataHashMap.put("carInfo",caInfo);
        try {
            if ( DateUtil.formatDateToString(DateUtil.parseStringToDate(bleAuthDto.getBleKeyExpireTime(),
                    DateUtil.TIME_MASK_3), DateUtil.TIME_MASK_3).equals("2286-11-21")){
                extraDataHashMap.put("validTimeStr", " ???????????? ");
            }else {
                String validTimeInfoStart =  DateUtil.formatDateToString(DateUtil.parseStringToDate(
                        bleAuthDto.getBleKeyEffectiveTime(), DateUtil.TIME_MASK_3), DateUtil.TIME_MASK_3);
                String validTimeInfoEnd  = DateUtil.formatDateToString(DateUtil.parseStringToDate(
                        bleAuthDto.getBleKeyExpireTime(), DateUtil.TIME_MASK_3), DateUtil.TIME_MASK_3);
                extraDataHashMap.put("validTimeStr", validTimeInfoStart.concat("???").concat(validTimeInfoEnd));
            }
        }catch (ParseException ex){
            log.error("????????????????????????????????????=",ex);
        }
        TemplateMsgDto templateMsgDto = new TemplateMsgDto();
        templateMsgDto.setBusinessId(IdWorker.getIdStr());
        templateMsgDto.setFromType(2);
        templateMsgDto.setExtraData(extraDataHashMap);
        templateMsgDto.setMappingTemplateId(200010001);
        templateMsgDto.setSendPhone(bleAuthDto.getPhoneNumber());
        templateMsgDto.setSendChannel(1);
        RestResponse sendSmsResponse =  smsFeignService.sendByTemplate(templateMsgDto);
        log.info(templateMsgDto.toString());
        log.info("??????????????????????????????[{}]", sendSmsResponse.getRespMsg());
        if(!RespCode.SUCCESS.getValue().equals(sendSmsResponse.getRespCode())) {
            log.error("????????????????????????[{}]", sendSmsResponse.getRespMsg());
            throw new AdamException(RespCode.SER_SMS_SEND_ERROR);
        }
        return sendSmsResponse;
    }

}
