package com.bnmotor.icv.tsp.ble.service.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyQueryDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUidKeyDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyQueryVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleUidKey;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleKeyQueryService;
import com.bnmotor.icv.tsp.ble.service.BleTboxService;
import com.bnmotor.icv.tsp.ble.service.BleUserService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.service.mq.BleKafkaPushMsg;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.BLE_KEY_DELETE_WITH_PUSH_MSG_ERROR;
import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.BLE_KEY_QUERY_WITH_PUSH_MSG_ERROR;
import static com.bnmotor.icv.tsp.ble.common.RespCode.VEOWNER_APPLY_ERROR;
import static com.bnmotor.icv.tsp.ble.common.RespCode.BLE_APPLY_FREQUENTLY;

/**
 * @ClassName: BleKeyQueryServiceImpl
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class BleKeyQueryServiceImpl implements BleKeyQueryService {

    @Resource
    BleKeyUserMapper bleKeyUserMapper;

    @Resource
    BleKafkaPushMsg bleKafkaPushMsg;

    @Resource
    BluetoothDownService bluetoothDownService;

    @Resource
    BleUserService bleUserService;

    @Resource
    BleCommonFeignService bleCommonFeignService;

    @Resource
    BleAckPushMapper bleAckPushMapper;

    @Resource
    BleAuthMapper bleAuthMapper;

    @Resource
    BleAuthServiceMapper bleAuthServiceMapper;

    @Resource
    BleKeyMapMapper bleKeyMapMapper;

    @Resource
    BleTboxService bleTboxService;

    @Override
    public List<BleKeyQueryVo> queryByVehOwner(String userId, String projectId, BleKeyQueryDto querydto) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(querydto.getDeviceId())
                .ownerUserId(userId)
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG).build();
        List<UserBleKeyPo> queryUserBleKeyPo = bleKeyUserMapper.queryBleOwnerKeyInfo(userBleKeyPo);
        List<BleKeyQueryVo> queryVoList = queryUserBleKeyPo.stream().map(entry -> {

            BleKeyQueryVo queryVo = BleKeyQueryVo.builder().build();
            BeanUtils.copyProperties(entry, queryVo);
            queryVo.setBleKeyRefreshTime(entry.getBleKeyRefreshTime().getTime());
            queryVo.setMobileDeviceId(entry.getUsedUserMobileDeviceId());
            queryVo.setUsedUserMobileModel(entry.getUsedUserMobileModel());
            if (entry.getBleKeyEffectiveTime() != null) {
                queryVo.setBleKeyEffectiveTime(entry.getBleKeyEffectiveTime().getTime());
            }
            if (entry.getBleKeyExpireTime() != null) {
                if (entry.getBleKeyExpireTime() == Long.MAX_VALUE) {
                    queryVo.setBleKeyExpireTime(String.format("%8x", 0xFFFFFFFF));
                } else {
                    queryVo.setBleKeyExpireTime(String.valueOf(entry.getBleKeyExpireTime()));
                }
            }

            return queryVo;
        }).collect(Collectors.toList());


        Collections.sort(queryVoList, new Comparator<BleKeyQueryVo>() {
            @Override
            public int compare(BleKeyQueryVo o1, BleKeyQueryVo o2) {
                return o2.getBleKeyRefreshTime().intValue() - o1.getBleKeyRefreshTime().intValue();
            }
        });
        return queryVoList;
    }


    @Override
    public BleKeyQueryDto queryCheckByVehOwner(String userId, String projectId, BleKeyQueryDto querydto) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.createAllBleQueryMsg(querydto.getDeviceId());
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(userVo.getPushRid())
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(projectId))
                    .deviceId(querydto.getDeviceId())
                    .ownerUserId(userId)
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userName)
                    .createTime(now)
                    .build();
            int result = bleAckPushMapper.addBleAckPush(build);
            if (result <= 0) {
                log.error("从车端[(0)]查询蓝牙钥匙[(1)]时，写推送表数据出错", build.getDeviceId());
                throw new AdamException(BLE_KEY_DELETE_WITH_PUSH_MSG_ERROR);
            }
        } catch (JsonProcessingException ex) {
            log.error("查询车辆[(0)]下蓝牙钥匙时，写推送表数据错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_DELETE_WITH_PUSH_MSG_ERROR);
        }

        return querydto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkExpireHisData(String projectId, String deviceId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .deviceId(deviceId)
                .authExpireTime(now.getTime())
                .authExpireTime(now.getTime())
                .build();
        List<BleAuthPo> queryExpiredBleAuthCode = bleAuthMapper.queryExpiredBleAuthCode(bleAuthPo);
        if (queryExpiredBleAuthCode != null) {
            queryExpiredBleAuthCode.stream().forEach(elem -> {
                elem.setUpdateBy(Constant.BLEKEY_EXPIRED_OP);
                elem.setUpdateTime(now);
                cleanExpireHisData(elem);
            });
        }
        List<BleAuthPo> queryExpiredBleAuthLimit = bleAuthMapper.queryExpiredBleAuthLimit(bleAuthPo);
        if (queryExpiredBleAuthLimit != null) {
            queryExpiredBleAuthLimit.stream().forEach(elem -> {
                elem.setUpdateBy(Constant.BLEKEY_EXPIRED_OP);
                elem.setUpdateTime(now);
                cleanExpireHisData(elem);
            });
        }
    }

    @Override
    public void cleanExpireHisData(BleAuthPo elem) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        elem.setUpdateTime(now);
        elem.setDelFlag(Constant.CANCEL_DEL_FLAG);
        bleAuthMapper.updateBleAuth(elem);
        bleAuthMapper.moveAuthHisData(elem);
        int delCount=bleAuthMapper.deleteAuthHisData(elem);
        if (delCount==Constant.COMPARE_EQUAL_VALUE){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        bleAuthServiceMapper.delBleAuthService(elem);
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(elem.getProjectId())
                .deviceId(elem.getDeviceId())
                .delFlag(Constant.INIT_STATUS)
                .bleAuthId(elem.getId())
                .build();
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfo(userBleKeyPo);
        if (Optional.ofNullable(userBleKeyPoQuery).isPresent()) {
            userBleKeyPoQuery.setBleKeyStatus(Constant.EXPIRE_STATUS);
            userBleKeyPoQuery.setUpdateBy(elem.getUpdateBy());
            userBleKeyPoQuery.setUpdateTime(now);
            userBleKeyPoQuery.setBleKeyDestroyTime(now);
            userBleKeyPoQuery.setDelFlag(Constant.CANCEL_DEL_FLAG);
            bleKeyUserMapper.updateBleKeyDestroy(userBleKeyPoQuery);
            bleKeyUserMapper.moveBlekeyHisData(userBleKeyPoQuery);
            delCount=bleKeyUserMapper.deleteBlekeyHisData(userBleKeyPoQuery);
            if (delCount==Constant.COMPARE_EQUAL_VALUE){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleKeyMapMapper.deleteBleKeyServiceById(userBleKeyPoQuery.getProjectId(), userBleKeyPoQuery.getBleKeyId(),null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOwnerExpireHisData(String projectId, String deviceId, String uid) {

            LocalDateTime dt = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                    .projectId(projectId)
                    .deviceId(deviceId).userType(Long.valueOf(Constant.USER_MSTER_TYPE))
                    .ownerUserId(uid)
                    .bleKeyStatus(Constant.INIT_STATUS)
                    .build();
            List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.queryBleKeyListByPrimary(userBleKeyPo);
            if (userBleKeyPoList != null) {
                userBleKeyPoList.stream().forEach(elem -> {
                    if (DateUtil.getDiscrepantSeconds(elem.getBleKeyEffectiveTime(),now)<10){
                        throw new AdamException(BLE_APPLY_FREQUENTLY);
                    }
                    try {
                        elem.setBleKeyDestroyTime(now);
                        elem.setDelFlag(Constant.CANCEL_DEL_FLAG);
                        bleKeyUserMapper.updateBleKeyDestroy(elem);
                        bleKeyUserMapper.moveBlekeyHisData(elem);
                        bleKeyUserMapper.deleteBlekeyHisData(elem);
                        bleKeyMapMapper.deleteBleKeyServiceById(elem.getProjectId(), elem.getBleKeyId(),null);
                    } catch (Exception ex) {
                        log.error("清理车主申请未成功的钥匙出错，错误信息为=", ex);
                        throw new AdamException(VEOWNER_APPLY_ERROR);
                    }
                });
            }


    }

    @Override
    public BleKeyDto queryCheckSpeciByVehOwner(String userId, String projectId, BleKeyDto querydto) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.createSpecifiedBleQueryMsg(querydto.getDeviceId(), Long.valueOf(querydto.getBleKeyId()));
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(userVo.getPushRid())
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .blekeyId(querydto.getBleKeyId())
                    .projectId(Long.valueOf(projectId))
                    .deviceId(querydto.getDeviceId())
                    .ownerUserId(userId)
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userName)
                    .createTime(now)
                    .build();
            int result = bleAckPushMapper.addBleAckPush(build);
            if (result <= 0) {
                log.error("删除特定蓝牙钥匙时写推送表影响行数为 [(0)]");
                throw new AdamException(BLE_KEY_QUERY_WITH_PUSH_MSG_ERROR);
            }
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_QUERY_WITH_PUSH_MSG_ERROR);
        }

        return querydto;
    }

    @Override
    public String queryCheckAllByVehOwner(String projectId, String userId, String deviceId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.createAllBleQueryMsg(deviceId);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(userVo.getPushRid())
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(projectId))
                    .deviceId(deviceId)
                    .ownerUserId(userId)
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .createBy(userName)
                    .createTime(now)
                    .build();
            int result = bleAckPushMapper.addBleAckPush(build);
            if (result <= 0) {
                log.error("删除特定蓝牙钥匙时写推送表影响行数为 [(0)]");
                throw new AdamException(BLE_KEY_QUERY_WITH_PUSH_MSG_ERROR);
            }
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_QUERY_WITH_PUSH_MSG_ERROR);
        }

        return deviceId;
    }

    @Override
    public List<BleUidKey> queryBleUids(BleUidKeyDto bleUidKeyDto) {
        List<BleUidKey> userBleKeyPos = bleKeyUserMapper.queryBleKeysByUids(bleUidKeyDto);
        return userBleKeyPos;
    }
}
