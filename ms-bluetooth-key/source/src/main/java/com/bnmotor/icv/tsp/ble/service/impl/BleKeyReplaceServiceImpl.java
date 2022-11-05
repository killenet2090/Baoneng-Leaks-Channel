package com.bnmotor.icv.tsp.ble.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum;
import com.bnmotor.icv.tsp.ble.mapper.BleAckPushMapper;
import com.bnmotor.icv.tsp.ble.mapper.BleKeyUserMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleReplaceResultVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleKeyReplaceService;
import com.bnmotor.icv.tsp.ble.service.BlePkiService;
import com.bnmotor.icv.tsp.ble.service.BleUserService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.service.feign.BlePkiFeignService;
import com.bnmotor.icv.tsp.ble.service.mq.BleKafkaPushMsg;
import com.bnmotor.icv.tsp.ble.util.BleKeyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;

/**
 * @ClassName: BleKeyReplaceServiceImpl
 * @Description: 车主蓝牙钥匙更新替换服务类
 * @author: liuyiwei
 * @date: 2020/7/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部
 * 传阅， 禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class BleKeyReplaceServiceImpl implements BleKeyReplaceService {

    /**
     * 蓝牙钥匙操作mapper.
     */
    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    /**
     * 消息发送.
     */
    @Resource
    private BleKafkaPushMsg bleKafkaPushMsg;

    /**
     * 消息发送接口.
     */
    @Resource
    private BluetoothDownService bluetoothDownService;

    /**
     * feign通用类.
     */
    @Resource
    private BleCommonFeignService bleCommonFeignService;

    /**
     * 查询用户名.
     */
    @Resource
    private BleUserService bleUserService;

    /**
     * 发送消息dao类.
     */
    @Resource
    private BleAckPushMapper bleAckPushMapper;

    /**
     * Pki服务
     */
    @Resource
    private BlePkiService blePkiService;

    /**
     * 更新蓝牙钥匙.
     *
     * @param dbUserBleKeyPo
     * @param projectId
     * @param userId
     * @return
     */
    @Override
    public BleReplaceResultVo updateBleKey(UserBleKeyPo dbUserBleKeyPo, String projectId, String userId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleReplaceSign(now,dbUserBleKeyPo);

        //2.生成蓝牙钥匙及数据库记录， 失效原蓝牙钥匙
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        dbUserBleKeyPo.setEncryptAppBleKey(bleKeyGeneratorDto.getEncrypAppBleKey());
        dbUserBleKeyPo.setBleKeyRefreshTime(now);
        dbUserBleKeyPo.setUpdateTime(now);
        dbUserBleKeyPo.setUpdateBy(userName);
        bleKeyUserMapper.updateAppEncryptFieldByBleId(dbUserBleKeyPo);

        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.replaceBlekeyMsg(dbUserBleKeyPo, bleKeyGeneratorDto);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(userVo.getPushRid())
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(projectId))
                    .deviceId(dbUserBleKeyPo.getDeviceId())
                    .blekeyId(dbUserBleKeyPo.getBleKeyId())
                    .ownerUserId(userId)
                    .userTypeId(dbUserBleKeyPo.getUserType())
                    .usedUserId(dbUserBleKeyPo.getUsedUserId())
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userName)
                    .createTime(now)
                    .build();
            int result = bleAckPushMapper.addBleAckPush(build);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_CREATE_SEND_TBOX_ERR);
        }

        BleReplaceResultVo bleReplaceResultVo = BleReplaceResultVo.builder()
                .build();
        BeanUtils.copyProperties(dbUserBleKeyPo, bleReplaceResultVo);
        bleReplaceResultVo.setBleKeyEffectiveTime(dbUserBleKeyPo.getBleKeyEffectiveTime().getTime());
        bleReplaceResultVo.setBleKeyStatus(dbUserBleKeyPo.getBleKeyStatus().toString());
        bleReplaceResultVo.setBleKeyRefreshTime(dbUserBleKeyPo.getBleKeyRefreshTime().getTime());
        bleReplaceResultVo.setBlekeyExpireTime(dbUserBleKeyPo.getBleKeyExpireTime());
        bleReplaceResultVo.setEncryptAppBleKeySign(bleKeyGeneratorDto.getAppSign());
        //TODO mock value for ble conn
        bleReplaceResultVo.setBleConName("d8:3b:bf:ac:19:1b-88:4a:70:b6:5c:48");
        bleReplaceResultVo.setBleConPin("0000");

        return bleReplaceResultVo;
    }

    /**
     * 校验车主与蓝牙钥匙
     *
     * @param userBleKeyPo
     */
    @Override
    public UserBleKeyPo queryBleKeyInfo(UserBleKeyPo userBleKeyPo) {
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
        if (userBleKeyPoQuery == null) {
            throw new AdamException(BizExceptionEnum.BLE_KEY_REPLACE_REQ_ILLEGAL);
        }
        return userBleKeyPoQuery;
    }

    /**
     * 判断蓝牙钥匙刷新时间是否已经超过3个月
     *
     * @param userBleKeyPo
     * @return
     */
    @Override
    public Long querySubBleKeyRefresh(UserBleKeyPo userBleKeyPo) {
        LocalDateTime dt = LocalDateTime.now();
        LocalDateTime localDateTime = dt.plusHours(Constant.BEFORE_FT_HOUR);
        Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
        long sub = userBleKeyPo.getBleKeyRefreshTime().getTime() - now.getTime();
        return sub;
    }

    @Override
    public void SendAuthUpdate(String tboxJson,String kafkaJson) {
        try {
            BleAckPushPo bleAckPushPo = JsonUtil.toObject(tboxJson, BleAckPushPo.class);
            BluetoothDownDto bluetoothDownDto = JsonUtil.toObject(kafkaJson, BluetoothDownDto.class);
            int result = bleAckPushMapper.addBleAckPush(bleAckPushPo);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
        }catch (IOException ex){
            throw new AdamException(BizExceptionEnum.BLE_KEY_REPLACE_REQ_ILLEGAL);
        }
    }
}
