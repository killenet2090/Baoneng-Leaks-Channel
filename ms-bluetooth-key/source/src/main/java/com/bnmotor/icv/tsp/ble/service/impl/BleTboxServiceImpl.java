package com.bnmotor.icv.tsp.ble.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.BleAckPushMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleAckPushService;
import com.bnmotor.icv.tsp.ble.service.BleTboxService;
import com.bnmotor.icv.tsp.ble.service.mq.BleKafkaPushMsg;
import com.bnmotor.icv.tsp.ble.util.RedisHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;

@Service
@Slf4j
public class BleTboxServiceImpl implements BleTboxService {
    @Resource
    private BleKafkaPushMsg bleKafkaPushMsg;
    @Resource
    private BleAckPushMapper bleAckPushMapper;
    @Resource
    private BluetoothDownService bluetoothDownService;
    @Resource
    private BleAckPushService bleAckPushService;
    @Resource
    private RedisHelper redisHelper;


    @Override
    public void bleApply(UserBleKeyPo userBleKeyPo, Date now, UserPhoneVo response, BleKeyGeneratorDto bleKeyGeneratorDto) {
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.createApplyMsg(userBleKeyPo, bleKeyGeneratorDto);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(response.getPushRid())
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(userBleKeyPo.getProjectId()))
                    .deviceId(userBleKeyPo.getDeviceId())
                    .blekeyId(userBleKeyPo.getBleKeyId())
                    .ownerUserId(userBleKeyPo.getOwnerUserId())
                    .usedUserId(userBleKeyPo.getUsedUserId())
                    .userTypeId(userBleKeyPo.getUserType())
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userBleKeyPo.getCreateBy())
                    .createTime(now)
                    .build();
            if (!(userBleKeyPo.getUserType().intValue() == Constant.USER_MSTER_TYPE)) {
                String authJson = JsonUtil.toJson(build);
                String authKey = userBleKeyPo.getDeviceId().concat("_")
                        .concat(userBleKeyPo.getUsedUserMobileDeviceId())
                        .concat("_").concat(userBleKeyPo.getUsedUserId()).concat("_0");
                redisHelper.setStr(authKey, authJson, 24, TimeUnit.HOURS);
                String str = redisHelper.getStr(authKey);
                log.info(str);
            }
            int result = bleAckPushMapper.addBleAckPush(build);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_CREATE_SEND_TBOX_ERR);
        }
    }

    /**
     * 删除车下某把蓝牙钥匙
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     */
    @Override
    public void delBleKey(UserBleKeyPo userBleKeyPo, Date now, String pushId, String uid, String userName) {
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.delSpecifiedBlekeyMsg(userBleKeyPo);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(pushId)
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .deviceId(userBleKeyPo.getDeviceId())
                    .blekeyId(userBleKeyPo.getBleKeyId())
                    .ownerUserId(userBleKeyPo.getOwnerUserId())
                    .usedUserId(userBleKeyPo.getUsedUserId())
                    .expireDate(userBleKeyPo.getBleKeyExpireTime())
                    .userTypeId(userBleKeyPo.getUserType())
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userName)
                    .createTime(now)
                    .build();
            if (userBleKeyPo.getBleKeyExpireTime().compareTo(Long.MAX_VALUE)!=Constant.COMPARE_EQUAL_VALUE) {
                build.setExpireDate(userBleKeyPo.getBleKeyExpireTime());
            }
            if (Optional.ofNullable(userBleKeyPo.getProjectId()).isPresent()) {
                build.setProjectId(Long.valueOf(userBleKeyPo.getProjectId()));
            }
            int result = bleAckPushMapper.addBleAckPush(build);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
            if (result <= 0) {
                log.error("删除特定蓝牙钥匙时写推送表影响行数为 [(0)]");
                throw new AdamException(BLE_KEY_DELETE_WITH_PUSH_MSG_ERROR);
            }
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_DELETE_WITH_PUSH_PROCESS_ERROR);
        }
    }

    /**
     * 删除车下所有钥匙
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     */
    @Override
    public void delDeviceBle(UserBleKeyPo userBleKeyPo, Date now, String pushId) {
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.delDevieAllBlekeyMsg(userBleKeyPo.getDeviceId());
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(pushId)
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(userBleKeyPo.getProjectId()))
                    .deviceId(userBleKeyPo.getDeviceId())
                    .blekeyId(userBleKeyPo.getBleKeyId())
                    .ownerUserId(userBleKeyPo.getOwnerUserId())
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userBleKeyPo.getCreateBy())
                    .createTime(now)
                    .build();
            int result = bleAckPushMapper.addBleAckPush(build);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
            if (result <= 0) {
                log.error("删除车辆下所有蓝牙钥匙时写推送表影响行数为 [(0)]");
            }
        } catch (JsonProcessingException ex) {
            log.error("删除车辆下所有蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_DELETE_WITH_PUSH_PROCESS_ERROR);

        } catch (Exception ex) {
            log.error("删除车辆下所有蓝牙钥匙下发阶段失败，详情参考：{}", ex.fillInStackTrace());
            throw new AdamException(BLE_KEY_DELETE_WITH_PUSH_PROCESS_ERROR);
        }
    }

    /**
     * 修改蓝牙钥匙权限
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     * @param bleRights
     * @param bleRight
     */
    @Override
    public void reviseBleKeyAuth(UserBleKeyPo userBleKeyPo, Date now, String pushId, String bleRights, Long bleRight) {
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.createBleUpdateRightMsg(userBleKeyPo, bleRight);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
            //维护消息推送表
            BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(pushId)
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(userBleKeyPo.getProjectId()))
                    .deviceId(userBleKeyPo.getDeviceId())
                    .blekeyId(userBleKeyPo.getBleKeyId())
                    .ownerUserId(userBleKeyPo.getOwnerUserId())
                    .serviceId(bleRights)
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userBleKeyPo.getCreateBy())
                    .createTime(now)
                    .build();
            int count = bleAckPushMapper.addBleAckPush(bleAckPushPo);
            if (count <= 0) {
                log.error("修改蓝牙钥匙权限写推送消息表影响函数为[()]", count);
                throw new AdamException(BLE_KEY_MODIFY_WITH_PUSH_MSG_ERROR);
            }
        } catch (JsonProcessingException ex) {
            log.error("蓝牙钥匙权限修改时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_MODIFY_WITH_PUSH_PROCESS_ERROR);
        }
    }

    /**
     * 修改蓝牙钥匙有效期
     *
     * @param userBleKeyPo
     * @param now
     * @param pushId
     */
    @Override
    public void updateBleExpireDate(UserBleKeyPo userBleKeyPo, Date now, String pushId, String uid, String userName) {
        try {
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.createBleUpdateExpireMsg(userBleKeyPo);
            BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                    .projectId(Long.valueOf(userBleKeyPo.getProjectId()))
                    .deviceId(userBleKeyPo.getDeviceId())
                    .blekeyId(userBleKeyPo.getBleKeyId())
                    .effectiveTime(userBleKeyPo.getBleKeyEffectiveTime())
                    .expireDate(userBleKeyPo.getBleKeyExpireTime())
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .ownerUserId(uid)
                    .usedUserId(userBleKeyPo.getUsedUserId())
                    .registrationId(pushId)
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(uid)
                    .createTime(now)
                    .build();
            bleAckPushService.addBleAckPushService(bleAckPushPo);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙修改有效期下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_CREATE_SEND_TBOX_ERR);
        }

    }
}
