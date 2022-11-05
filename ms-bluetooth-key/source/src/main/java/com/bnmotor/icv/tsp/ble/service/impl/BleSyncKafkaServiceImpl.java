package com.bnmotor.icv.tsp.ble.service.impl;

import com.alibaba.fastjson.JSON;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.HexUtil;
import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.common.ACKContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.common.BluetoothContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.common.PSHContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.BluetoothTypeEnum;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.cmd.BluetoothCmdEnum;
import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.tsp.ble.common.ParamReader;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleNotification;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleVerSignDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleSyncKafkaService;
import com.bnmotor.icv.tsp.ble.service.BleUnbindBackService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.service.mq.BleAckSyncdbService;
import com.bnmotor.icv.tsp.ble.service.mq.GenerateContent;
import com.bnmotor.icv.tsp.ble.util.ByteUtil;
import com.bnmotor.icv.tsp.ble.util.RandomUnit;
import com.bnmotor.icv.tsp.ble.util.RedisHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.BLE_KEY_MODIFY_WITH_PUSH_PROCESS_ERROR;

@Service
@Slf4j
public class BleSyncKafkaServiceImpl implements BleSyncKafkaService {
    @Value("${ble.line-status}")
    public int  lineStatisInterver;
    @Resource
    private BleAckPushMapper bleAckPushMapper;
    @Resource
    private BleAckSyncdbService bleAckSyncdbService;
    @Resource
    private BleCommonFeignService bleCommonFeignService;
    @Resource
    private BleApplyServiceImpl bleApplyService;
    @Resource
    private BluetoothDownService bluetoothDownService;
    @Resource
    private BleDeviceInfoMapper deviceBleInfoService;
    @Resource
    private BleKeyUserMapper bleKeyUserMapper;
    @Resource
    private BleKeyUserHisMapper bleKeyUserHisMapper;
    @Resource
    private BleAckInfoMapper bleAckInfoMapper;
    @Resource
    private BleUnbindBackService bleUnbindBackService;
    @Resource
    private GenerateContent generateContent;
    @Resource
    private RedisHelper redisHelper;
    @Resource
    private ParamReader paramReader;

    @Override
    public BleAckPushPo CombineMsgToPush(String vin, Integer businessSerialNum,
                                               BluetoothTypeEnum typeEnum, int ackCmd) {
            BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                    .serialNum(businessSerialNum.longValue())
                    .cmdType(typeEnum.getType())
                    .deviceId(vin)
                    .cmdOrder(ackCmd)
                    .status(Constant.INIT_STATUS)
                    .build();
            BleAckPushPo dbBleAckPushPo = bleAckPushMapper.queryBleAckPush(bleAckPushPo);
        return dbBleAckPushPo;
    }
    @Override
    public List<BleAckPushPo> CombineMsgsToPush(String vin, Integer businessSerialNum,
                                         BluetoothTypeEnum typeEnum, int ackCmd) {
        BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                .serialNum(businessSerialNum.longValue())
                .cmdType(typeEnum.getType())
                .deviceId(vin)
                .cmdOrder(ackCmd)
                .status(Constant.INIT_STATUS)
                .build();
        List<BleAckPushPo> bleAckPushPoList = bleAckPushMapper.queryBleAckPushs(bleAckPushPo);
        return bleAckPushPoList;
    }

    @Override
    public boolean ackUpdateRegisterBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteSignsrc = Arrays.copyOfRange(bytes, 0, 5);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int status = HexUtil.byte2Int(byteStatus);
        bleAckPushPo.setStatus(status);
        int bVerify = bleCommonFeignService.verifyTbox(byteSignsrc, byteSign);
        log.info("bVerify={}",bVerify);
        if (bVerify == Constant.COMPARE_EQUAL_VALUE &&  status == Constant.ACTIVE_STATUS) {
            bleAckSyncdbService.dbUpdateRegisterBleField(bleAckPushPo,
                    String.valueOf(bleAckPushPo.getProjectId()),
                    bleAckPushPo.getDeviceId());
        }
        bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
        bleAckPushPo.setAckStatus(HexUtil.byte2HexStr(byteSignsrc));
        bleAckPushMapper.updateBleAckPushAckStatus(bleAckPushPo);
        return true;
    }

    /**
     * 查询指定蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackQuerySpecifiedBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        log.info("quey tbox ack info");
        String vin = bleAckPushPo.getDeviceId();
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteSignsrc = Arrays.copyOfRange(bytes, 0, 14);
        byte[] byteTime = Arrays.copyOf(bytes, 4);
        byte[] byteBleId = Arrays.copyOf(bytes, 12);
        byte[] byteUserType = Arrays.copyOfRange(bytes, 12, 13);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 13, 14);
        bleAckPushPo.setUserTypeId(Long.valueOf(HexUtil.byte2Int(byteUserType)));
        int status = HexUtil.byte2Int(byteStatus);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int bVerify = bleCommonFeignService.verifyTbox(byteSignsrc, byteSign);
        log.info("bVerify={}",bVerify);
        Long bleKeyId = HexUtil.bytes2Long(byteBleId);
        int bleUserType = HexUtil.byte2Int(byteUserType);
        if (bVerify == Constant.COMPARE_EQUAL_VALUE &&  status == Constant.TBOX_DEL) {
            log.info("quey tbox is exist");
            bleAckSyncdbService.dbDelSpecifiedBlekey(bleAckPushPo);
            log.info("quey tbox not exist");
        }
        if (bVerify != Constant.COMPARE_EQUAL_VALUE){
            log.error("收到tbox端的查询特定蓝牙钥匙命令反馈,验签失败,原信息为={}",contentDto.toString());
            return false;
        }
        bleAckPushPo.setStatus(status);
        bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
        return true;
    }

    /**
     * 查询指定车辆蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackQueryDeviceIdAllBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        log.info("quey tbox ack info");
        String vin = bleAckPushPo.getDeviceId();
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteTime = Arrays.copyOf(bytes, 4);
        byte[] byteBleNum = Arrays.copyOfRange(bytes, 4, 5);
        byte[] byteBleList = Arrays.copyOfRange(bytes, 5, 95);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 95, 96);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int bVerify = bleCommonFeignService.verifyTbox(bytes, byteSign);
        log.info("bVerify={}",bVerify);
        if (bVerify != Constant.COMPARE_EQUAL_VALUE) {
            log.info("查询车端所有蓝牙钥匙数量根据tbox的返回数据进行验签，验签失败，原信息为={}",contentDto.toString());
            return false;
        }
        List<BleAckPushPo> userBleKeyPoList = new ArrayList<>();
        int bleKeyCount = HexUtil.byte2Int(byteBleNum);
        int status = HexUtil.byte2Int(byteStatus);
        if ((bleKeyCount <= 0 || bleKeyCount == 255) && status == 3) {
            bleAckSyncdbService.updateDeviceBleAsCancelStatus(vin);
        } else {
            int pos = 1;
            for (int flag = 0; flag < byteBleList.length; ) {
                if (pos > bleKeyCount) {
                    break;
                }
                int endPos = flag + 9;
                byte[] bleKey = Arrays.copyOfRange(byteBleList, flag, endPos - 1);
                byte[] userType = Arrays.copyOfRange(byteBleList, endPos - 1, endPos);
                flag = endPos;
                Long bleKeyId = HexUtil.bytes2Long(bleKey);
                int bleUserType = HexUtil.byte2Int(userType);
                BleAckPushPo bleAckInfoPo = BleAckPushPo.builder()
                        .deviceId(vin)
                        .blekeyId(String.valueOf(bleKeyId))
                        .userTypeId(Long.valueOf(bleUserType))
                        .updateBy(Constant.BLEKEY_TBOX_OP)
                        .build();
                userBleKeyPoList.add(bleAckInfoPo);
                pos++;
            }
//            if (status == Constant.SUCCESS_STATUS) {
//                bleAckSyncdbService.dbUpdateSpecifidBleField(userBleKeyPoList, vin, null);
//            }
            bleAckPushPo.setStatus(status);
            log.info(bleAckPushPo.toString());
            bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, userBleKeyPoList.stream()
                    .map(BleAckPushPo::getBlekeyId)
                    .map(Long::valueOf)
                    .collect(Collectors.toList()));
        }
        return true;
    }

    /**
     * 删除指定蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackDelSpecifiedBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
        int status = HexUtil.byte2Int(byteStatus);
        bleAckPushPo.setAckStatus(String.valueOf(status));
        bleAckPushPo.setStatus(status);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int delFlag = bleCommonFeignService.verifyTbox(bytes, byteSign);
        if (!(delFlag==Constant.COMPARE_EQUAL_VALUE)){
            log.error("收到tbox端的注销命令,验签失败,原信息为={}",contentDto.toString());
            return false;
        }
        bleAckPushMapper.updateBleAckPushAckStatus(bleAckPushPo);
        if (status == Constant.SUCCESS_STATUS || status == 3) {
            bleAckSyncdbService.dbDelSpecifiedBlekey(bleAckPushPo);
            bleAckPushPo.setStatus(Constant.SUCCESS_STATUS);
        }
        log.info(bleAckPushPo.toString());
        return true;
    }

    /**
     * 更新某指定的pin码
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackUpdateSpecifiedBlePin(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
        int status = HexUtil.byte2Int(byteStatus);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int verFlag = bleCommonFeignService.verifyTbox(bytes, byteSign);
        if (verFlag==Constant.COMPARE_EQUAL_VALUE && status == Constant.ACTIVE_STATUS) {
            bleAckPushPo.setStatus(Constant.ACTIVE_STATUS);
            bleAckSyncdbService.dbUpdateSpecifiedBlePin(bleAckPushPo);
        }
        bleAckPushPo.setStatus(status);
        bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
        return true;
    }

    /**
     * 更新车下所有钥匙
     *
     * @param bleAckPushPos
     * @return
     */
    @Override
    public boolean updateDeviceIdAllBlePin(ACKContentDto contentDto, List<BleAckPushPo> bleAckPushPos) {
        bleAckPushPos.stream().forEach(elem->{
            bleAckSyncdbService.dbUpdateSpecifiedBlePin(elem);
        });
        return true;
    }

    /**
     * 删除车辆下所有蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackDelAllBlekey(ACKContentDto contentDto, BleAckPushPo bleAckPushPo) {
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
        int status = HexUtil.byte2Int(byteStatus);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        int delFlag = bleCommonFeignService.verifyTbox(bytes, byteSign);
        if (!(delFlag==Constant.COMPARE_EQUAL_VALUE)){
            log.error("收到tbox端的注销命令,验签失败,原信息为={}",contentDto.toString());
            return false;
        }
        if (status!=Constant.SUCCESS_STATUS){
            log.error("收到tbox端的注销车辆下所有蓝牙钥匙命令,发现TBOX端验签失败,原信息为={}",contentDto.toString());
            return false;
        }
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .bleKeyId(bleAckPushPo.getBlekeyId())
                .deviceId(bleAckPushPo.getDeviceId())
                .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                .build();
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(userBleKeyPo.getDeviceId());
        List<UserBleKeyPo> userBleKeyPoList = bleApplyService.queryBlekeyInfoById(userBleKeyPo);
        bleAckSyncdbService.dbDelDeviceBlekey(bleAckPushPo);
        bleAckPushPo.setStatus(status);
        bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
        if (status == Constant.TBOX_DEL_SUCCESS) {
            bleUnbindBackService.addBleUnbind(bleAckPushPo);
            userBleKeyPoList.forEach(elem -> {
                UserPhoneVo response = bleCommonFeignService.bleGetFeignUserPhoneVo(elem.getUsedUserId());
                String registrationId = response.getPushRid();
                bleCommonFeignService.delPushToMobileNotification(vehicleInfoVo, "", bleAckPushPo.getCmdType(),
                        bleAckPushPo.getCmdOrder(), status, registrationId, bleAckPushPo.getCreateBy());
            });
        }
        return true;
    }

    /**
     * 更新蓝牙钥匙有效期，并通知给APP
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackUpdateSpecifiedBlekeySecret(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int uFlag = bleCommonFeignService.verifyTbox(bytes, byteSign);
        if (!(uFlag==Constant.COMPARE_EQUAL_VALUE)){
            log.error("收到tbox端更新反馈的命令,验签失败,原信息为={}",contentDto.toString());
            return false;
        }
        int status = HexUtil.byte2Int(byteStatus);
        bleAckPushPo.setStatus(status);
        bleAckPushPo.setAckStatus(String.valueOf(status));
        //bleAckSyncdbService.dbUpdateSpecifidBleField(bleAckPushPo.getBlekeyId(),Constant.ACTIVE_STATUS);
        bleAckSyncdbService.dbUpdateSpecifiedBlekeySecret(bleAckPushPo);
        bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
        return true;
    }

    /**
     * 更新蓝牙钥匙有效期，并通知给APP
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackUpdateSpecifiedBleLimitDate(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        try {
            List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
            String contentHexData = contentDtos.get(0).getContentHexStr();
            String signHexData = contentDtos.get(1).getContentHexStr();
            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
            int signLength = HexUtil.byte2Int(byteLength);
            byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
            String signStr = new String(byteSign);
            int delFlag = bleCommonFeignService.verifyTbox(bytes, byteSign);
            if (!(delFlag==Constant.COMPARE_EQUAL_VALUE)){
                log.error("收到tbox端的注销命令,验签失败,原信息为={}",contentDto.toString());
                return false;
            }
            int status = HexUtil.byte2Int(byteStatus);
            bleAckPushPo.setStatus(status);
            bleAckPushPo.setAckStatus(String.valueOf(status));
            if (status == Constant.ACTIVE_STATUS) {
                bleAckPushPo.setStatus(Constant.ACTIVE_STATUS);
                UserBleKeyPo bleKeyPo = UserBleKeyPo.builder()
                        .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                        .deviceId(bleAckPushPo.getDeviceId())
                        .bleKeyId(String.valueOf(bleAckPushPo.getBlekeyId()))
                        .bleKeyStatus(Constant.ACTIVE_STATUS)
                        .bleKeyEffectiveTime(bleAckPushPo.getEffectiveTime())
                        .bleKeyExpireTime(bleAckPushPo.getExpireDate())
                        .build();
                bleAckSyncdbService.dbUpdateSpecifiedBleLimitDate(bleKeyPo);
                bleKeyPo = bleApplyService.getDbBlekeyData(bleKeyPo);
                BleAuthPo bleAuthPo = BleAuthPo.builder()
                        .projectId(bleKeyPo.getProjectId())
                        .deviceId(bleKeyPo.getDeviceId())
                        .id(bleKeyPo.getBleAuthId())
                        .authTime(bleAckPushPo.getEffectiveTime())
                        .authExpireTime(bleAckPushPo.getEffectiveTime().getTime())
                        .status(Constant.ACTIVE_STATUS)
                        .updateTime(Calendar.getInstance().getTime())
                        .build();
                bleAckSyncdbService.dbUpdateSpecifidBleAuthDate(bleAuthPo);
                UserPhoneVo userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(bleAckPushPo.getUsedUserId());
                VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleAckPushPo.getDeviceId());
                String userName = bleCommonFeignService.queryTspInfo(bleAckPushPo.getOwnerUserId()).getNickname();
                BleNotification bleNotification = new BleNotification();
                bleNotification.setDeviceId(bleAckPushPo.getDeviceId());
                bleNotification.setBleId(Long.valueOf(bleAckPushPo.getBlekeyId()));
                bleNotification.setUserId(Long.valueOf(bleAckPushPo.getUsedUserId()));
                bleNotification.setUserName(userName);
                bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
                bleNotification.setBrand(vehicleInfoVo.getBrandName());
                bleNotification.setType(4);
                bleNotification.setType(1);
                bleNotification.setPushId(userPhoneVo.getPushRid());
                bleNotification.setStatus(Constant.INIT_STATUS);
                bleNotification.setCategoryId(1303211738512872665L);
                bleNotification.setTemplateId(1303211738512732583L);
                String expireDate = DateUtil.formatDateToString(DateUtil.convertLongToDate(bleAckPushPo.getExpireDate()), DateUtil.TIME_MASK_3);
                bleNotification.setExpireDate(expireDate);
                bleCommonFeignService.bleBaseNotification(bleNotification);
            }
            bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
            return true;
        } catch (Exception ex) {
            log.error("修改蓝牙钥匙有效期时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_MODIFY_WITH_PUSH_PROCESS_ERROR);
        }
    }


    /**
     * 修改某把钥匙权限
     *
     * @param bleAckPushPo
     * @return
     */
    @Override
    public boolean ackUpdateSpecifidBleAuthrity(ACKContentDto contentDto,BleAckPushPo bleAckPushPo) {
        List<BluetoothContentDto> contentDtos = contentDto.getContentDtos();
        String contentHexData = contentDtos.get(0).getContentHexStr();
        String signHexData = contentDtos.get(1).getContentHexStr();
        byte[] bytes = Hex.decode(contentHexData);
        byte[] byteStatus = Arrays.copyOfRange(bytes, 4, 5);
        int status = HexUtil.byte2Int(byteStatus);
        byte[] signBytes = Hex.decode(signHexData);
        byte[] byteLength = Arrays.copyOfRange(signBytes, 0, 2);
        int signLength = HexUtil.byte2Int(byteLength);
        byte[] byteSign = Arrays.copyOfRange(signBytes, 2, signLength+2);
        String signStr = new String(byteSign);
        int verFlag = bleCommonFeignService.verifyTbox(bytes, byteSign);
        if (verFlag!=Constant.COMPARE_EQUAL_VALUE){
            log.error("验签失败");
            return true;
        }
        if (status == Constant.ACTIVE_STATUS) {
            bleAckSyncdbService.dbUpdateSpecifidBleAuthrity(bleAckPushPo);
        }
        bleAckPushPo.setStatus(status);
        bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
        return true;
    }


    /**
     * 构建更新某把蓝牙钥匙
     *
     * @param downDto
     * @return
     */
    @Override
    public boolean buildUpdateSpecifiedBlePin(BluetoothDownDto downDto) {
        try {
            PSHContentDto pshContentDto = downDto.getPshContentDto();
            List<BluetoothContentDto> contentDtos = pshContentDto.getContentDtos();
            for (BluetoothContentDto bluetoothContentDto : contentDtos) {
                byte[] decode = Hex.decode(bluetoothContentDto.getContentHexStr());
                ACKContentDto ackContentDto = generateContent.ackUpdateSpecifiedBlePin(decode);
                downDto.setAckContentDto(ackContentDto);
                //ackService.sendDownDto(downDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
            }
            return true;
        } catch (JsonProcessingException ex) {
            return false;
        }

    }

    /**
     * 构建某辆车下的所有蓝牙钥匙
     *
     * @param downDto
     * @return
     */
    @Override
    public boolean buildCreateDeviceIdAllBlePin(BluetoothDownDto downDto) {
        try {
            LocalDateTime dt = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            String vin = downDto.getVin();
            BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
            Integer businessSerialNum = downDto.getBusinessSerialNum();
            Long platformTime = downDto.getPlatformTime();
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();

            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteTime = Arrays.copyOf(bytes, 4);

            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);
            AtomicInteger ackStatus = new AtomicInteger(1);
            int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);
            if (bVerify!=Constant.COMPARE_EQUAL_VALUE) {
                log.error("接收tbox线下删除蓝牙钥匙上报验签出错，错误状态为{}", bVerify);
                ackStatus.set(4);
            }
            List<BleCaPinPo> bleCaPinPoList = new ArrayList<>();
            List<BleCaPinPo> dbBleCaPinPoList = bleAckSyncdbService.dbQueryBleDevicePin(downDto);
            if (dbBleCaPinPoList.size() < 10) {
                for (int i = 1; i < 11 - dbBleCaPinPoList.size(); i++) {
                    BleCaPinPo build = BleCaPinPo.builder()
                            .projectId(paramReader.peojectId)
                            .bleDeviceId(downDto.getVin())
                            .userTypeId(Long.valueOf(i))
                            .pinCode(RandomUnit.generateRandom(18, 1))
                            .delFlag(Constant.INIT_DEL_FLAG)
                            .status(Constant.ACTIVE_STATUS)
                            .createBy(Constant.BLEKEY_TBOX_OP)
                            .createTime(now)
                            .updateBy(Constant.BLEKEY_TBOX_OP)
                            .updateTime(now)
                            .build();
                    bleCaPinPoList.add(build);
                    bleAckSyncdbService.dbAddSpecifiedBlePin(build);
                }
            } else {
                bleCaPinPoList = dbBleCaPinPoList;
            }
            ACKContentDto ackContentDto = generateContent.ackUpdateDeviceBlePin(ackStatus.get(),bleCaPinPoList);
            downDto.setAckContentDto(ackContentDto);
            bluetoothDownService.sendBluetoothDownMsg(downDto);
            log.info(dbBleCaPinPoList.toString());
            return true;
        } catch (Exception ex) {
            log.error("接收TBOX请求生成pin码出现错误{}", ex);
            return false;
        }
    }

    /**
     * 回复蓝牙钥匙上报
     *
     * @param downDto
     * @return
     */
    @Override
    public boolean buildUpReportInfo(BluetoothDownDto downDto) {
        try {
            log.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            LocalDateTime dt = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            String vin = downDto.getVin();
            BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
            Integer businessSerialNum = downDto.getBusinessSerialNum();
            Long platformTime = downDto.getPlatformTime();
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();

            AtomicInteger ackStatus = new AtomicInteger(1);
            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteDate = Arrays.copyOf(bytes, 4);
            byte[] byteMac = Arrays.copyOfRange(bytes, 4, 10);
            byte[] byteControlSerNum = Arrays.copyOfRange(bytes, 10, 30);
            byte[] byteControlHWConfig = Arrays.copyOfRange(bytes, 30, 31);
            byte[] byteControlId = Arrays.copyOfRange(bytes, 31, 51);
            byte[] byteSoftVer = Arrays.copyOfRange(bytes, 51, 71);
            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);

            int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);
            if (bVerify!=Constant.COMPARE_EQUAL_VALUE) {
                log.error("接收tbox线下删除蓝牙钥匙上报验签出错，错误状态为{}", bVerify);
                ackStatus.set(4);
            }
            try {
                ACKContentDto ackContentDto = generateContent.ackReceiveStatus(ackStatus.get());
                downDto.setAckContentDto(ackContentDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
                return false;
            } catch (JsonProcessingException ex) {
                log.error("接收tbox线下删除蓝牙钥匙上报，回复收到信息时出错，错误信息为{}", ex);
            }

            int dateTime = HexUtil.byte2Int(byteDate);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < byteMac.length; i++) {
                byte[] macBytes = Arrays.copyOfRange(byteMac, i, i + 1);
                String s = Integer.toHexString(0xFF & byteMac[i]);
                //String macString = ByteUtil.getString(macBytes, "UTF-8");
                stringBuffer.append(s);
            }
            String macStr = stringBuffer.toString();
            String controlSerNumStr = ByteUtil.getString(byteControlSerNum, "UTF-8");
            int controlHWConfigStr = HexUtil.byte2Int(byteControlHWConfig);
            String controlIdStr = ByteUtil.getString(byteControlId, "UTF-8");
            String softVerStr = ByteUtil.getString(byteSoftVer, "UTF-8");
            String signStr = ByteUtil.getString(byteTbox, "UTF-8");
            DeviceBleInfoPo deviceBleInfoPo = DeviceBleInfoPo.builder()
                    .projectId("1")
                    .deviceId(vin)
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .createBy(Constant.BLEKEY_TBOX_OP).build();
            DeviceBleInfoPo deviceBleInfoPoQuery = deviceBleInfoService.queryBleDeviceInfo(deviceBleInfoPo);
            deviceBleInfoPo.setDeviceConfig(controlHWConfigStr);
            deviceBleInfoPo.setDeviceMac(macStr);
            deviceBleInfoPo.setProductKey(controlIdStr);
            deviceBleInfoPo.setSoftwareVesion(softVerStr);
            deviceBleInfoPo.setProductCode(controlSerNumStr);
            if (deviceBleInfoPoQuery == null) {
                deviceBleInfoPo.setCreateTime(Calendar.getInstance().getTime());
                deviceBleInfoService.addBleDeviceInfo(deviceBleInfoPo);
            } else {
                deviceBleInfoPo.setUpdateTime(Calendar.getInstance().getTime());
                deviceBleInfoPo.setUpdateBy(Constant.BLEKEY_TBOX_OP);
                deviceBleInfoService.updateBleDeviceInfo(deviceBleInfoPo);
            }
            return true;
        } catch (Exception ex) {
            log.error("接收tbox蓝牙配置信息上报，保存到数据阶段出现错误，错误信息为{}", ex);
            return false;
        }
    }

    /**
     * 车辆蓝牙钥匙上报，用于和数据库比较
     *
     * @param downDto
     * @return
     */
    @Override
    public boolean buildBleKeyReport(BluetoothDownDto downDto) {
        try {
            LocalDateTime dt = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            String vin = downDto.getVin();
            BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
            Integer businessSerialNum = downDto.getBusinessSerialNum();
            Long platformTime = downDto.getPlatformTime();
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();

            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);

            List<BleAckInfoPo> bleAckInfoPoList = new ArrayList<>();
            List<BleAckPushPo> bleAckPushPoList = new ArrayList<>();


            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteTime = Arrays.copyOf(bytes, 4);
            byte[] byteBleNum = Arrays.copyOfRange(bytes, 4, 5);
            byte[] byteBleList = Arrays.copyOfRange(bytes, 5, 95);


            AtomicInteger ackStatus = new AtomicInteger(1);

            int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);
            if (bVerify!=Constant.COMPARE_EQUAL_VALUE) {
                log.error("接收tbox线下蓝牙钥匙数量同步上报时验签出错，错误状态为{}", bVerify);
                ackStatus.set(4);
            }
            try {
                ACKContentDto ackContentDto = generateContent.ackReceiveStatus(ackStatus.get());
                downDto.setAckContentDto(ackContentDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
            } catch (JsonProcessingException ex) {
                log.error("接收tbox线下删除蓝牙钥匙上报，回复收到信息时出错，错误信息为{}", ex);
            }

            int bleKeyCount = HexUtil.byte2Int(byteBleNum);
            if (bleKeyCount <= 0 || bleKeyCount == 255) {
                BleAckInfoPo bleAckInfoPo = BleAckInfoPo.builder()
                        .deviceId(vin)
                        .bleKeyId(Constant.DEFAULT_FLAG)
                        .ackText(contentHexData)
                        .status(Constant.CANCEL_STATUS)
                        .delFlag(Constant.CANCEL_DEL_FLAG)
                        .version(Constant.INIT_VERSION)
                        .createBy(Constant.BLEKEY_EXPIRED_OP)
                        .createTime(now)
                        .build();
                bleAckInfoPoList.add(bleAckInfoPo);
                bleAckSyncdbService.updateDeviceBleAsCancelStatus(vin);
            } else {
                int pos = 1;
                for (int flag = 0; flag < 90; ) {
                    int endPos = flag + 9;
                    byte[] bleKey = Arrays.copyOfRange(byteBleList, flag, endPos - 1);
                    byte[] userType = Arrays.copyOfRange(byteBleList, endPos - 1, endPos);
                    flag = endPos;
                    Long bleKeyId = HexUtil.bytes2Long(bleKey);
                    int bleUserType = HexUtil.byte2Int(userType);
                    if (bleUserType < 1 || bleUserType > 4) {
                        continue;
                    }
                    //针对tbox的数据不确定性，做的第二重保险判断
                    if (pos > bleKeyCount) {
                        break;
                    }
                    BleAckInfoPo bleAckInfoPo = BleAckInfoPo.builder()
                            .deviceId(vin)
                            .bleKeyId(bleKeyId)
                            .userType(bleUserType)
                            .ackText(contentHexData)
                            .status(Constant.ACTIVE_STATUS)
                            .delFlag(Constant.INIT_DEL_FLAG)
                            .version(Constant.INIT_VERSION)
                            .createBy(Constant.BLEKEY_EXPIRED_OP)
                            .createTime(now)
                            .build();
                    BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                            .deviceId(vin)
                            .blekeyId(String.valueOf(bleKeyId))
                            .build();
                    bleAckInfoPoList.add(bleAckInfoPo);
                    bleAckPushPoList.add(bleAckPushPo);
                    pos++;
                }
                log.info(JSON.toJSONString(bleAckInfoPoList));
            }

            bleAckInfoPoList.stream().forEach(elem -> bleAckInfoMapper.addBleAckInfo(elem));
            bleAckSyncdbService.dbUpdateSpecifidBleField(bleAckPushPoList, vin, null);
            return true;
        } catch (Exception ex) {
            log.info("接收tbox线下蓝牙钥匙数量同步上报，用于和数据库比较,保存入库时发生异常，[0]", ex);
            return false;
        }
    }

    /**
     * tbox端主动注销的蓝牙钥匙上报
     *
     * @param downDto
     * @return
     */
    @Override
    public boolean buildCancelBleKeyId(BluetoothDownDto downDto) {
        String vin = downDto.getVin();
        BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
        Integer businessSerialNum = downDto.getBusinessSerialNum();
        Long platformTime = downDto.getPlatformTime();
        List<BleAckInfoPo> bleAckInfoPoList = new ArrayList<>();
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        try {
            AtomicInteger ackStatus = new AtomicInteger(1);
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();

            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);


            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteDate = Arrays.copyOf(bytes, 4);
            byte[] byteId = Arrays.copyOfRange(bytes, 4, 12);
            byte[] byteUserType = Arrays.copyOfRange(bytes, 12, 13);
            byte[] byteDeRegTime = Arrays.copyOfRange(bytes, 13, 17);
            try {
                int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);

                ACKContentDto ackContentDto = generateContent.ackReceiveStatus(ackStatus.get());
                downDto.setAckContentDto(ackContentDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
                if (bVerify!=Constant.COMPARE_EQUAL_VALUE) {
                    log.error("tbox主动上报注销蓝牙钥匙验签失败，错误信息为{}");
                    ackStatus.set(4);
                    return true;
                }

            } catch (JsonProcessingException ex) {
                log.error("tbox主动上报注销蓝牙钥匙出错，错误信息为{}", ex);
                return true;
            }
            int dateTime = HexUtil.byte2Int(byteDate);
            Long bleKeyId = HexUtil.bytes2Long(byteId);
            int bleUserType = HexUtil.byte2Int(byteUserType);
            int deRegTime = HexUtil.byte2Int(byteDeRegTime);
            Date DeRegDate = new Date((long) deRegTime * 1000);
            BleAckInfoPo bleAckInfoPo = BleAckInfoPo.builder()
                    .deviceId(vin)
                    .bleKeyId(bleKeyId)
                    .userType(bleUserType)
                    .destroyTime(DeRegDate)
                    .status(Constant.CANCEL_STATUS)
                    .delFlag(Constant.CANCEL_DEL_FLAG)
                    .createBy(Constant.BLEKEY_EXPIRED_OP)
                    .createTime(now)
                    .build();
            bleAckInfoPoList.add(bleAckInfoPo);

            bleAckInfoPoList.forEach(elem -> {
                bleAckInfoMapper.addBleAckInfo(elem);
                UserBleKeyPo userBleKeyPo = UserBleKeyPo
                        .builder()
                        .deviceId(elem.getDeviceId())
                        .bleKeyId(String.valueOf(elem.getBleKeyId()))
                        .build();
                UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
                if (!Optional.ofNullable(userBleKeyPoQuery).isPresent()){
                    log.info("tbox注销上报的蓝牙钥匙在数据中不存在,上报的蓝牙钥匙信息为={}",userBleKeyPo.toString());
                    return ;
                }
                VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(userBleKeyPo.getDeviceId());
                if (!Optional.ofNullable(userBleKeyPo).isPresent()) {
                    userBleKeyPoQuery = bleKeyUserHisMapper.queryBleKeyInfo(userBleKeyPo);
                }
                String userName = StringUtil.EMPTY_STRING;
                UserPhoneVo userPhoneVo = new UserPhoneVo();
                BleNotification bleNotification = new BleNotification();
                userName = bleCommonFeignService.queryTspInfo(userBleKeyPoQuery.getOwnerUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPoQuery.getUsedUserId());
                bleNotification.setCategoryId(1303211738509786376L);
                bleNotification.setTemplateId(1303211738512732601L);
                bleNotification.setUserId(Long.valueOf(userBleKeyPoQuery.getUsedUserId()));

                bleNotification.setDeviceId(userBleKeyPoQuery.getDeviceId());
                bleNotification.setBleId(Long.valueOf(userBleKeyPoQuery.getBleKeyId()));
                bleNotification.setUserName(userName);
                bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
                bleNotification.setBrand(vehicleInfoVo.getBrandName());
                bleNotification.setType(BluetoothCmdEnum.DELETE_ONE_KEY.getTypeEnum().getType());
                bleNotification.setCmd(BluetoothCmdEnum.DELETE_ONE_KEY.getCmd());
                bleNotification.setPushId(userPhoneVo.getPushRid());
                bleNotification.setExpireDate(DateUtil.formatDateToString(
                        DateUtil.convertLongToDate(userBleKeyPoQuery.getBleKeyExpireTime()), DateUtil.TIME_MASK_DEFAULT));
                bleNotification.setStatus(Constant.INIT_STATUS);
                log.info("********************************************************");
                log.info("********************************************************");
                log.info(bleNotification.toString());
                bleCommonFeignService.bleBaseNotification(bleNotification);

                userName = bleCommonFeignService.queryTspInfo(userBleKeyPoQuery.getUsedUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPoQuery.getOwnerUserId());
                bleNotification.setCategoryId(1303211738509786376L);
                bleNotification.setTemplateId(1303211738512732577L);
                bleNotification.setUserId(Long.valueOf(userBleKeyPoQuery.getOwnerUserId()));

                bleNotification.setUserName(userName);
                bleNotification.setPushId(userPhoneVo.getPushRid());
                log.info("********************************************************");
                log.info("********************************************************");
                log.info(bleNotification.toString());
                bleCommonFeignService.bleBaseNotification(bleNotification);
            });
            return true;
        } catch (Exception ex) {
            log.error("tbox主动上报注销蓝牙钥匙出错，错误信息为{}", ex);
            return false;
        }
    }

    /**
     * tbox端上报钥匙状态回复结果
     *
     * @param downDto
     * @return
     */
    @Override
    public boolean buildConnectionStatus(BluetoothDownDto downDto) {

        String vin = downDto.getVin();
        BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
        Integer businessSerialNum = downDto.getBusinessSerialNum();
        Long platformTime = downDto.getPlatformTime();
        List<BleAckInfoPo> bleAckInfoPoList = new ArrayList<>();
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        try {
            AtomicInteger ackStatus = new AtomicInteger(1);
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();
            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteDate = Arrays.copyOf(bytes, 4);
            byte[] userId = Arrays.copyOfRange(bytes, 4, 12);
            byte[] mobileDeviceId = Arrays.copyOfRange(bytes, 12, 20);
            byte[] byteStatus = Arrays.copyOfRange(bytes, 20, 21);

            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);


            int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);
            if (bVerify!=Constant.COMPARE_EQUAL_VALUE) {
                log.error("接收tbox线下删除蓝牙钥匙上报验签出错，错误状态为{}", bVerify);
                ackStatus.set(4);
            }
            try {
                ACKContentDto ackContentDto = generateContent.ackReceiveStatus(ackStatus.get());
                downDto.setAckContentDto(ackContentDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
            } catch (JsonProcessingException ex) {
                log.error("接收tbox线下删除蓝牙钥匙上报，回复收到信息时出错，错误信息为{}", ex);
            }
            String bleKey = vin.concat("_")
                    .concat(String.valueOf(HexUtil.bytes2Long(mobileDeviceId))).concat("_")
                    .concat(String.valueOf(HexUtil.bytes2Long(userId))).concat("_200");
            redisHelper.setStr(bleKey,ByteUtil.getString(byteStatus),lineStatisInterver, TimeUnit.HOURS);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean buildBleKeyDel(BluetoothDownDto downDto) {
        String vin = downDto.getVin();
        BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
        Integer businessSerialNum = downDto.getBusinessSerialNum();
        Long platformTime = downDto.getPlatformTime();
        List<BleAckInfoPo> bleAckInfoPoList = new ArrayList<>();
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        try {
            AtomicInteger ackStatus = new AtomicInteger(1);
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();
            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteDate = Arrays.copyOf(bytes, 4);
            byte[] byteUserId = Arrays.copyOfRange(bytes, 4, 12);
            byte[] byteBleId = Arrays.copyOfRange(bytes, 12, 20);
            byte[] byteDelTime = Arrays.copyOfRange(bytes, 20, 24);
            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);


            int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);
            if (bVerify!=Constant.COMPARE_EQUAL_VALUE) {
                log.error("接收tbox线下删除蓝牙钥匙上报验签出错，错误状态为{}", bVerify);
                ackStatus.set(4);
            }
            try {
                ACKContentDto ackContentDto = generateContent.ackReceiveStatus(ackStatus.get());
                downDto.setAckContentDto(ackContentDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
            } catch (JsonProcessingException ex) {
                log.error("接收tbox线下删除蓝牙钥匙上报，回复收到信息时出错，错误信息为{}", ex);
            }
            int dateTime = HexUtil.byte2Int(byteDate);
            Long bleKeyId = HexUtil.bytes2Long(byteBleId);
            Long userId = HexUtil.bytes2Long(byteUserId);
            int delTime = HexUtil.byte2Int(byteDelTime);
            long delTimeLong = Long.valueOf(delTime)*1000;
            Date DeRegDate = new Date(delTimeLong);
            BleAckInfoPo bleAckInfoPo = BleAckInfoPo.builder()
                    .deviceId(vin)
                    .bleKeyId(bleKeyId)
                    .destroyTime(DeRegDate)
                    .ackText(contentHexData)
                    .status(Constant.CANCEL_STATUS)
                    .delFlag(Constant.CANCEL_DEL_FLAG)
                    .createBy(Constant.BLEKEY_EXPIRED_OP)
                    .createTime(now)
                    .build();
            bleAckInfoPoList.add(bleAckInfoPo);
            bleAckInfoPoList.stream().forEach(elem -> {
                bleAckInfoMapper.addBleAckInfo(elem);
                UserBleKeyPo userBleKeyPo = UserBleKeyPo
                        .builder()
                        .deviceId(elem.getDeviceId())
                        .bleKeyId(String.valueOf(elem.getBleKeyId()))
                        .build();
                UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
                if (!Optional.ofNullable(userBleKeyPoQuery).isPresent()){
                    log.info("根据tbox上报的离线删除数据经过分析，发现云平台已经不存在改蓝牙钥匙,上报的信息为={}",downDto.toString());
                    return;
                }
                VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(userBleKeyPo.getDeviceId());
                if (!Optional.ofNullable(userBleKeyPo).isPresent()) {
                    userBleKeyPoQuery = bleKeyUserHisMapper.queryBleKeyInfo(userBleKeyPo);
                }
                String userName = StringUtil.EMPTY_STRING;
                UserPhoneVo userPhoneVo = new UserPhoneVo();
                BleNotification bleNotification = new BleNotification();
                userName = bleCommonFeignService.queryTspInfo(userBleKeyPoQuery.getOwnerUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPoQuery.getUsedUserId());
                bleNotification.setCategoryId(1303211738512872662L);
                bleNotification.setTemplateId(1303211738512732579L);
                bleNotification.setUserId(Long.valueOf(userBleKeyPoQuery.getUsedUserId()));

                bleNotification.setDeviceId(userBleKeyPoQuery.getDeviceId());
                bleNotification.setBleId(Long.valueOf(userBleKeyPoQuery.getBleKeyId()));
                bleNotification.setUserName(userName);
                bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
                bleNotification.setBrand(vehicleInfoVo.getBrandName());
                bleNotification.setType(BluetoothCmdEnum.DELETE_ONE_KEY.getTypeEnum().getType());
                bleNotification.setCmd(BluetoothCmdEnum.DELETE_ONE_KEY.getCmd());
                bleNotification.setPushId(userPhoneVo.getPushRid());
                bleNotification.setExpireDate(DateUtil.formatDateToString(
                        DateUtil.convertLongToDate(userBleKeyPoQuery.getBleKeyExpireTime()), DateUtil.TIME_MASK_DEFAULT));
                bleNotification.setStatus(Constant.INIT_STATUS);
                log.info("********************************************************");
                log.info("********************************************************");
                log.info(bleNotification.toString());
                bleCommonFeignService.bleBaseNotification(bleNotification);

                userName = bleCommonFeignService.queryTspInfo(userBleKeyPoQuery.getUsedUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPoQuery.getOwnerUserId());
                bleNotification.setCategoryId(1303211738512872661L);
                bleNotification.setTemplateId(1303211738512732578L);
                bleNotification.setUserId(Long.valueOf(userBleKeyPoQuery.getOwnerUserId()));

                bleNotification.setUserName(userName);
                bleNotification.setPushId(userPhoneVo.getPushRid());
                log.info("********************************************************");
                log.info("********************************************************");
                log.info(bleNotification.toString());
                bleCommonFeignService.bleBaseNotification(bleNotification);
            });
            return true;
        } catch (Exception ex) {
            log.error("tbox主动上报删除蓝牙钥匙出错，错误信息为{}", ex);
            return false;
        }
    }

    @Override
    public boolean buildBleKeyOffLineActive(BluetoothDownDto downDto) {
        String vin = downDto.getVin();
        BluetoothCmdEnum cmdEnum = downDto.getCmdEnum();
        Integer businessSerialNum = downDto.getBusinessSerialNum();
        Long platformTime = downDto.getPlatformTime();
        List<BleAckInfoPo> bleAckInfoPoList = new ArrayList<>();
        List<UserBleKeyPo> userBleKeyPoList = new ArrayList<>();
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        try {
            String contentHexData = downDto.getPshContentDto().getContentDtos().get(0).getContentHexStr();
            String signHexData = downDto.getPshContentDto().getContentDtos().get(1).getContentHexStr();
            AtomicInteger ackStatus = new AtomicInteger(1);
            byte[] bytes = Hex.decode(contentHexData);
            byte[] byteDate = Arrays.copyOf(bytes, 4);
            byte[] byteAuthCode = Arrays.copyOfRange(bytes, 4, 8);
            byte[] byteUserId = Arrays.copyOfRange(bytes, 8, 16);
            byte[] byteBleId = Arrays.copyOfRange(bytes, 16, 24);
            byte[] byteUserType = Arrays.copyOfRange(bytes, 24, 25);
            byte[] byteActTime = Arrays.copyOfRange(bytes, 25, 29);
            byte[] signBytes = Hex.decode(signHexData);
            byte[] byteSignLen = Arrays.copyOfRange(signBytes, 0, 2);
            int signLen = HexUtil.byte2Int(byteSignLen);
            byte[] byteTbox = Arrays.copyOfRange(signBytes, 2, signLen+2);
            String signData = new String(byteTbox);

            int bVerify = bleCommonFeignService.verifyTbox(bytes,byteTbox);
            if (bVerify != Constant.COMPARE_EQUAL_VALUE) {
                log.error("tbox线下激活蓝牙钥匙验签出错，错误状态为{}", bVerify);
                ackStatus.set(4);
            }
            try {
                ACKContentDto ackContentDto = generateContent.ackReceiveStatus(ackStatus.get());
                downDto.setAckContentDto(ackContentDto);
                bluetoothDownService.sendBluetoothDownMsg(downDto);
            } catch (JsonProcessingException ex) {
                log.error("收到tbox线下激活蓝牙钥匙上报，回复tbox收到上报时出错，错误信息为{}", ex);
            }
            int dateTime = HexUtil.byte2Int(byteDate);
            int actCode = HexUtil.byte2Int(byteAuthCode);
            int userType = HexUtil.byte2Int(byteUserType);
            Long bleKeyId = HexUtil.bytes2Long(byteBleId);
            Long userId = HexUtil.bytes2Long(byteUserId);
            int actTime = HexUtil.byte2Int(byteActTime);
            Date DeRegDate = new Date((long) actTime * 1000);
            BleAckInfoPo bleAckInfoPo = BleAckInfoPo.builder()
                    .deviceId(vin)
                    .bleKeyId(bleKeyId)
                    .destroyTime(DeRegDate)
                    .userType(userType)
                    .ackText(contentHexData)
                    .status(Constant.ACTIVE_STATUS)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .createBy(Constant.BLEKEY_EXPIRED_OP)
                    .createTime(now)
                    .build();
            bleAckInfoPoList.add(bleAckInfoPo);
            UserBleKeyPo build = UserBleKeyPo.builder()
                    .deviceId(vin)
                    .bleKeyId(String.valueOf(bleKeyId))
                    .userType(Long.valueOf(userType))
                    .bleKeyStatus(Constant.ACTIVE_STATUS)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .build();
            userBleKeyPoList.add(build);
            bleAckInfoPoList.stream().forEach(elem -> {
                bleAckInfoMapper.addBleAckInfo(elem);
            });

            userBleKeyPoList.forEach(elem -> {
                bleKeyUserMapper.updateBleKeyStatus(elem);

            });
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
