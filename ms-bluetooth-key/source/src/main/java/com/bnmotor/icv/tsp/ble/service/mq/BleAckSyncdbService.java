package com.bnmotor.icv.tsp.ble.service.mq;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.cmd.BluetoothCmdEnum;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleNotification;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleTboxService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;

/**
 * @ClassName: BleUserServiceImpl
 * @Description: 与用户中心交互实现类
 * @author: shuqi1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class BleAckSyncdbService {
    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Resource
    private BleCaPinMapper bleCaPinMapper;

    @Resource
    private BleAuthMapper bleAuthMapper;

    @Resource
    private BleAckPushMapper bleAckPushMapper;

    @Resource
    private BleKeyMapMapper bleKeyMapMapper;

    @Resource
    private BleAuthServiceMapper bleAuthServiceMapper;

    @Resource
    private BleTboxService bleTboxService;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleKeyUserHisMapper bleKeyUserHisMapper;

    /**
     * 更新钥匙状态和授权状态
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void dbUpdateSpecifidBleField(List<BleAckPushPo> bleAckPushPoList, String deviceId, String bleId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .deviceId(deviceId)
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .build();
        if (Optional.ofNullable(bleId).isPresent()) {
            userBleKeyPo.setBleKeyId(bleId);
        }
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.queryBleKeyListByPrimary(userBleKeyPo);
        //数据库比tbox端多
        userBleKeyPoList.forEach(elem -> {
            List<BleAckPushPo> collect = bleAckPushPoList.stream()
                    .filter(item -> item.getBlekeyId()
                            .equals(elem.getBleKeyId()))
                    .collect(Collectors.toList());
            if (collect.size() == 0) {

                //删除BleAckPushPo
                BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                        .projectId(Long.valueOf(elem.getProjectId()))
                        .deviceId(elem.getDeviceId())
                        .blekeyId(elem.getBleKeyId())
                        .build();
                dbDelSpecifiedBlekey(bleAckPushPo);

            }
        });
        //车端比数据库多
        bleAckPushPoList.forEach(item -> {
            List<UserBleKeyPo> collect = userBleKeyPoList.stream().
                    filter(elem -> elem.getBleKeyId()
                            .equals(item.getBlekeyId()))
                    .collect(Collectors.toList());
            delBleAsCancelStatus(collect.size(), item.getProjectId(), deviceId, item.getBlekeyId(), now);
            //下发删除

        });
    }


    @Transactional(rollbackFor = Exception.class)
    public void delBleAsCancelStatus(int size, Long projectId, String deviceId, String bleId, Date now) {
        if (size == 0) {
            UserBleKeyPo userBleCondition = UserBleKeyPo.builder()
                    .projectId(String.valueOf(projectId))
                    .deviceId(deviceId)
                    .bleKeyId(bleId)
                    .bleKeyStatus(Constant.ACTIVE_STATUS)
                    .build();

            //删除DB
            UserBleKeyPo userBle = bleKeyUserHisMapper.queryBleKeyInfo(userBleCondition);
            String pushid = "";
            String uid = "";
            if (Optional.ofNullable(userBle).isPresent()) {
                UserPhoneVo userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBle.getUsedUserId());
                pushid = userPhoneVo.getPushRid();
                uid = userBle.getUsedUserId();
                VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(deviceId);
                bleCommonFeignService.delPushToMobileNotification(vehicleInfoVo, "",
                        BluetoothCmdEnum.DELETE_ONE_KEY.getTypeEnum().getType(),
                        BluetoothCmdEnum.DELETE_ONE_KEY.getCmd(),
                        Constant.CANCEL_STATUS, pushid,
                        Constant.BLEKEY_EXPIRED_OP);
            } else {
                userBle = UserBleKeyPo.builder().deviceId(deviceId).bleKeyId(bleId).build();
            }
            bleTboxService.delBleKey(userBle, now, pushid, uid, Constant.BLEKEY_EXPIRED_OP);
        }
    }

    /**
     * 删除车下所有的蓝牙钥匙
     *
     * @param deviceId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceBleAsCancelStatus(String deviceId) {
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(deviceId);
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleCondition = UserBleKeyPo.builder()
                .deviceId(deviceId)
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .build();
        //删除DB
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.queryBleKeyListByPrimary(userBleCondition);
        userBleKeyPoList.forEach(userBle -> {
            UserPhoneVo userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBle.getUsedUserId());
            userBle.setBleKeyStatus(Constant.CANCEL_STATUS);
            userBle.setDelFlag(Constant.CANCEL_DEL_FLAG);
            userBle.setUpdateTime(now);
            userBle.setUpdateBy(Constant.BLEKEY_TBOX_OP);
            bleKeyUserMapper.updateBleKeyDestroy(userBle);
            bleKeyUserMapper.moveBlekeyHisData(userBle);
            int delCount = bleKeyUserMapper.deleteBlekeyHisData(userBle);
            if (delCount == Constant.COMPARE_EQUAL_VALUE) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleCommonFeignService.delPushToMobileNotification(vehicleInfoVo, "",
                    BluetoothCmdEnum.DELETE_ONE_KEY.getTypeEnum().getType(),
                    BluetoothCmdEnum.DELETE_ONE_KEY.getCmd(), Constant.CANCEL_STATUS,
                    userPhoneVo.getPushRid(), Constant.BLEKEY_EXPIRED_OP);
            if (Optional.ofNullable(userBle.getBleAuthId()).isPresent()) {
                BleAuthPo bleAuthPo = BleAuthPo.builder()
                        .projectId(userBle.getProjectId())
                        .id(userBle.getBleAuthId())
                        .status(Constant.CANCEL_STATUS)
                        .delFlag(Constant.CANCEL_DEL_FLAG)
                        .updateBy(Constant.BLEKEY_TBOX_OP)
                        .updateTime(now)
                        .build();
                bleAuthMapper.updateBleAuth(bleAuthPo);
                bleAuthMapper.moveAuthHisData(bleAuthPo);
                delCount = bleAuthMapper.deleteAuthHisData(bleAuthPo);
                if (delCount == Constant.COMPARE_EQUAL_VALUE) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void dbUpdateRegisterBleField(BleAckPushPo bleAckPushPo, String projectId, String deviceId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));

        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .bleKeyId(bleAckPushPo.getBlekeyId())
                .deviceId(deviceId)
                .build();
        UserBleKeyPo queryUserBleKeyPo = bleKeyUserMapper.queryBleKeyInfo(userBleKeyPo);
        queryUserBleKeyPo.setBleKeyStatus(Constant.ACTIVE_STATUS);
        queryUserBleKeyPo.setUpdateBy(Constant.BLEKEY_TBOX_OP);
        queryUserBleKeyPo.setUpdateTime(now);
        int rows=bleKeyUserMapper.updateBleKeyData(queryUserBleKeyPo);

        if (rows==Constant.COMPARE_EQUAL_VALUE){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        if (queryUserBleKeyPo.getBleAuthId() != null) {
            BleAuthPo bleAuthPo = BleAuthPo.builder()
                    .projectId(queryUserBleKeyPo.getProjectId())
                    .id(queryUserBleKeyPo.getBleAuthId())
                    .status(Constant.ACTIVE_STATUS)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .updateBy(Constant.BLEKEY_TBOX_OP)
                    .updateTime(now)
                    .build();
            rows=bleAuthMapper.updateBleAuth(bleAuthPo);
            if (rows==Constant.COMPARE_EQUAL_VALUE){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
    }

    /**
     * 删除某把钥匙
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int dbDelSpecifiedBlekey(BleAckPushPo bleAckPushPo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleKeyCondition = UserBleKeyPo.builder()
                .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                .deviceId(bleAckPushPo.getDeviceId())
                .bleKeyId(bleAckPushPo.getBlekeyId())
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG).build();
        UserBleKeyPo userBleKeyPo = bleKeyUserMapper.queryBleKeyInfo(userBleKeyCondition);
        if (!Optional.ofNullable(bleAckPushPo.getCreateBy()).isPresent())
        {
            bleAckPushPo.setCreateBy(userBleKeyPo.getOwnerUserId());
        }
        if (!Optional.ofNullable(bleAckPushPo.getOwnerUserId()).isPresent())
        {
            bleAckPushPo.setCreateBy(userBleKeyPo.getOwnerUserId());
        }
        if (!Optional.ofNullable(bleAckPushPo.getUsedUserId()).isPresent())
        {
            bleAckPushPo.setCreateBy(userBleKeyPo.getOwnerUserId());
        }
        if (Optional.ofNullable(userBleKeyPo).isPresent() && Optional.ofNullable(userBleKeyPo.getBleAuthId()).isPresent()) {
            BleAuthPo bleAuthPo = BleAuthPo
                    .builder()
                    .projectId(userBleKeyPo.getProjectId())
                    .deviceId(userBleKeyPo.getDeviceId())
                    .id(userBleKeyPo.getBleAuthId())
                    .status(Constant.CANCEL_STATUS)
                    .delFlag(Constant.CANCEL_DEL_FLAG)
                    .build();
            bleAuthPo.setUpdateBy(Constant.BLEKEY_EXPIRED_OP);
            bleAuthPo.setUpdateTime(now);
            bleAuthMapper.updateBleAuth(bleAuthPo);
            bleAuthMapper.moveAuthHisData(bleAuthPo);
            int delCount = bleAuthMapper.deleteAuthHisData(bleAuthPo);
            if (delCount == Constant.COMPARE_EQUAL_VALUE) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleAuthServiceMapper.delBleAuthService(bleAuthPo);
        }
        if (Optional.ofNullable(userBleKeyPo).isPresent()) {
            userBleKeyPo.setBleKeyStatus(Constant.CANCEL_STATUS);
            userBleKeyPo.setDelFlag(Constant.CANCEL_DEL_FLAG);
            userBleKeyPo.setUpdateBy(Constant.BLEKEY_EXPIRED_OP);
            userBleKeyPo.setUpdateTime(now);
            userBleKeyPo.setBleKeyDestroyTime(now);
            int rows = bleKeyUserMapper.updateBleKeyDestroy(userBleKeyPo);
            bleKeyUserMapper.moveBlekeyHisData(userBleKeyPo);
            int delCount = bleKeyUserMapper.deleteBlekeyHisData(userBleKeyPo);
            if (delCount == Constant.COMPARE_EQUAL_VALUE) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleKeyMapMapper.deleteBleKeyServiceById(userBleKeyCondition.getProjectId(), userBleKeyCondition.getBleKeyId(),null);
            if (!StringUtil.EMPTY_STRING.equals(bleAckPushPo.getRegistrationId())) {
                bleCommonFeignService.ackBasePushToMobileMsg(bleAckPushPo, null);
            }
            VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleAckPushPo.getDeviceId());
            String userName = StringUtil.EMPTY_STRING;
            UserPhoneVo userPhoneVo = new UserPhoneVo();
            BleNotification bleNotification = new BleNotification();
            if (bleAckPushPo.getCreateBy().equals(bleAckPushPo.getOwnerUserId())) {
                userName = bleCommonFeignService.queryTspInfo(bleAckPushPo.getOwnerUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(bleAckPushPo.getUsedUserId());
                bleNotification.setCategoryId(1303211738512872662L);
                bleNotification.setTemplateId(1303211738512732579L);
                bleNotification.setUserId(Long.valueOf(bleAckPushPo.getUsedUserId()));
            } else {
                userName = bleCommonFeignService.queryTspInfo(bleAckPushPo.getUsedUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(bleAckPushPo.getOwnerUserId());
                bleNotification.setCategoryId(1303211738512872661L);
                bleNotification.setTemplateId(1303211738512732578L);
                bleNotification.setUserId(Long.valueOf(bleAckPushPo.getOwnerUserId()));
            }
            bleNotification.setDeviceId(bleAckPushPo.getDeviceId());
            bleNotification.setBleId(Long.valueOf(bleAckPushPo.getBlekeyId()));
            bleNotification.setUserName(userName);
            bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
            bleNotification.setBrand(vehicleInfoVo.getBrandName());
            bleNotification.setType(BluetoothCmdEnum.DELETE_ONE_KEY.getTypeEnum().getType());
            bleNotification.setCmd(BluetoothCmdEnum.DELETE_ONE_KEY.getCmd());
            bleNotification.setPushId(userPhoneVo.getPushRid());
            bleNotification.setExpireDate(DateUtil.formatDateToString(DateUtil.convertLongToDate(bleAckPushPo.getExpireDate()), DateUtil.TIME_MASK_DEFAULT));
            bleNotification.setStatus(Constant.INIT_STATUS);
            log.info("********************************************************");
            log.info("********************************************************");
            log.info(bleNotification.toString());
            if (Long.compare(bleAckPushPo.getExpireDate(), now.getTime()) <= Constant.COMPARE_ZERO_VALUE.intValue()) {
                userName = bleCommonFeignService.queryTspInfo(bleAckPushPo.getOwnerUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(bleAckPushPo.getUsedUserId());
                bleNotification.setCategoryId(1303211738509786376L);
                bleNotification.setTemplateId(1303211738512732601L);
                bleNotification.setUserId(Long.valueOf(bleAckPushPo.getUsedUserId()));
                bleNotification.setUserName(userName);
                bleNotification.setPushId(userPhoneVo.getPushRid());
                bleCommonFeignService.bleBaseNotification(bleNotification);

                userName = bleCommonFeignService.queryTspInfo(bleAckPushPo.getUsedUserId()).getNickname();
                userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(bleAckPushPo.getOwnerUserId());
                bleNotification.setCategoryId(1303211738509786376L);
                bleNotification.setTemplateId(1303211738512732577L);
                bleNotification.setUserId(Long.valueOf(bleAckPushPo.getOwnerUserId()));
                bleNotification.setUserName(userName);
                bleNotification.setPushId(userPhoneVo.getPushRid());
                bleCommonFeignService.bleBaseNotification(bleNotification);
            } else {
                bleCommonFeignService.bleBaseNotification(bleNotification);
            }
            return rows;
        }
        return 0;
    }


    @Transactional(rollbackFor = Exception.class)
    public int dbDelDeviceBlekey(BleAckPushPo bleAckPushPo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleKeyCondition = UserBleKeyPo.builder()
                .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                .deviceId(bleAckPushPo.getDeviceId())
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        List<UserBleKeyPo> userBleKeyPos = bleKeyUserMapper.queryBleKeyListByPrimary(userBleKeyCondition);
        userBleKeyPos.stream().forEach(elem -> {
            if (Optional.ofNullable(elem.getBleAuthId()).isPresent()) {
                BleAuthPo bleAuthPo = BleAuthPo.builder()
                        .projectId(elem.getProjectId()).id(elem.getBleAuthId())
                        .status(Constant.CANCEL_STATUS)
                        .delFlag(Constant.CANCEL_DEL_FLAG)
                        .build();
                bleAuthMapper.updateBleAuth(bleAuthPo);
                bleAuthMapper.moveAuthHisData(bleAuthPo);
                bleAuthMapper.deleteAuthHisData(bleAuthPo);
                int delCount = bleAuthServiceMapper.delBleAuthService(bleAuthPo);
                if (delCount == Constant.COMPARE_EQUAL_VALUE) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            elem.setBleKeyStatus(Constant.CANCEL_STATUS);
            elem.setDelFlag(Constant.CANCEL_DEL_FLAG);
            elem.setBleKeyDestroyTime(now);
            int rows = bleKeyUserMapper.updateBleKeyDestroy(elem);
            int count = bleKeyUserMapper.moveBlekeyHisData(elem);
            int delCount = bleKeyUserMapper.deleteBlekeyHisData(elem);
            if (delCount == Constant.COMPARE_EQUAL_VALUE) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleKeyMapMapper.deleteBleKeyServiceById(userBleKeyCondition.getProjectId(), userBleKeyCondition.getBleKeyId(),null);
        });
        return Constant.OPERATION_SUCCESS;
    }

    @Transactional(rollbackFor = Exception.class)
    public int cleanAckPushData(BleAckPushPo bleAckPushPo) {
        List<BleAckPushPo> bleAckPushPoArray = new ArrayList<>();
        bleAckPushPoArray.add(bleAckPushPo);
        bleAckPushMapper.moveBleAckPushServiceList(bleAckPushPoArray);
        bleAckPushMapper.deleteBleAckPushList(bleAckPushPoArray);
        return Constant.OPERATION_SUCCESS;
    }

    /**
     * 更新某个pin码
     *
     * @return
     */
    public int dbUpdateSpecifiedBlePin(BleAckPushPo bleAckPushPo) {
        try {
            BleCaPinPo bleCaPinPo = BleCaPinPo.builder()
                    .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                    .bleDeviceId(bleAckPushPo.getDeviceId())
                    .userTypeId(bleAckPushPo.getUserTypeId())
                    .pinCode(bleAckPushPo.getPinCode())
                    .createBy(bleAckPushPo.getCreateBy())
                    .createTime(bleAckPushPo.getCreateTime())
                    .updateTime(bleAckPushPo.getUpdateTime())
                    .updateBy(bleAckPushPo.getUpdateBy())
                    .status(Constant.ACTIVE_STATUS)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .build();
            bleCaPinMapper.deleteBlePin(bleCaPinPo);
            bleCaPinMapper.addBlePin(bleCaPinPo);
            bleAckPushMapper.deleteBleAckPushById(bleAckPushPo);
        } catch (Exception ex) {
            log.error("根据tbox反馈更新pin表发生错误,请确认后重新更新！，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_CREATE_SEND_TBOX_ERR);
        }
        return Constant.OPERATION_SUCCESS;
    }

    /**
     * 更新某个pin码
     *
     * @return
     */
    public int dbAddSpecifiedBlePin(BleCaPinPo userBleKeyPo) {
        int rows = bleCaPinMapper.ackAddBlePin(userBleKeyPo);
        return rows;
    }

    public List<BleCaPinPo> dbQueryBleDevicePin(BluetoothDownDto downDto) {
        BleCaPinPo bleCaPinPo = BleCaPinPo.builder()
                .bleDeviceId(downDto.getVin())
                .status(Constant.ACTIVE_STATUS)
                .build();
        List<BleCaPinPo> bleCaPinPoList = bleCaPinMapper.queryBlePins(bleCaPinPo);
        return bleCaPinPoList;
    }

    /**
     * 更新某个秘钥
     *
     * @return
     */
    public int dbUpdateSpecifiedBlekeySecret(BleAckPushPo bleAckPushPo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleKeyCondition = UserBleKeyPo.builder()
                .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                .deviceId(bleAckPushPo.getDeviceId())
                .bleKeyId(bleAckPushPo.getBlekeyId())
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        UserBleKeyPo userBleKeyPo = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyCondition);
        if (Optional.ofNullable(userBleKeyPo.getBleAuthId()).isPresent()) {
            BleAuthPo bleAuthPo = BleAuthPo
                    .builder()
                    .projectId(userBleKeyPo.getProjectId())
                    .id(userBleKeyPo.getBleAuthId())
                    .status(Constant.CANCEL_STATUS)
                    .build();
            bleAuthMapper.updateBleAuth(bleAuthPo);
        }
        userBleKeyPo.setBleKeyRefreshTime(now);

        int rows = bleKeyUserMapper.updateBleKeyRefreshTime(userBleKeyPo);
        return rows;
    }

    /**
     * 更新某把钥匙有效期
     *
     * @return
     */
    public int dbUpdateSpecifiedBleLimitDate(UserBleKeyPo userBleKeyPo) {
        int rows = bleKeyUserMapper.updateBleKeyLimitDate(userBleKeyPo);
        return rows;
    }

    public int dbUpdateSpecifidBleAuthDate(BleAuthPo bleAuthPo) {
        int rows = bleAuthMapper.updateAuthExpireDate(bleAuthPo);
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    public int dbUpdateSpecifidBleAuthrity(BleAckPushPo bleAckPushPo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        Long projectId = bleAckPushPo.getProjectId();
        String bleKeyId = bleAckPushPo.getBlekeyId();
        String deviceId = bleAckPushPo.getDeviceId();
        Long sn = bleAckPushPo.getSerialNum();
        Integer cmdType = bleAckPushPo.getCmdType();
        Integer cmdOrder = bleAckPushPo.getCmdOrder();
        String serviceId = bleAckPushPo.getServiceId();
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(String.valueOf(projectId))
                .deviceId(deviceId)
                .bleKeyId(bleKeyId)
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
        Stream.of(serviceId.trim().split(",")).map(Long::valueOf).forEach(elem -> {
            BleKeyServicePo bleKeyServicePo = BleKeyServicePo
                    .builder()
                    .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                    .bleId(Long.valueOf(bleAckPushPo.getBlekeyId()))
                    .serviceId(elem)
                    .version(Constant.INIT_VERSION)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .createTime(now)
                    .updateTime(now)
                    .createBy(Constant.BLEKEY_EXPIRED_OP)
                    .build();
            bleKeyMapMapper.deleteBleKeyServiceById(String.valueOf(projectId), bleKeyId,elem);
            bleKeyMapMapper.addBleKeyServiceMap(bleKeyServicePo);
            if (userBleKeyPoQuery.getBleAuthId() != null) {
                BleAuthServicePo bleAuthServicePo = BleAuthServicePo.builder()
                        .projectId(String.valueOf(projectId))
                        .authId(userBleKeyPoQuery.getBleAuthId())
                        .version(Constant.INIT_VERSION)
                        .delFlag(Constant.INIT_DEL_FLAG)
                        .serviceId(elem)
                        .build();
                bleAuthServiceMapper.delBleAuthServiceData(bleAuthServicePo);
                bleAuthServicePo.setServiceId(elem);
                bleAuthServicePo.setCreateTime(now);
                bleAuthServicePo.setUpdateTime(now);
                if (!Optional.ofNullable(bleAuthServicePo.getCreateBy()).isPresent()) {
                    bleAuthServicePo.setCreateBy(Constant.BLEKEY_EXPIRED_OP);
                }
                bleAuthServicePo.setUpdateBy(Constant.BLEKEY_EXPIRED_OP);
                bleAuthServiceMapper.addBleAuthService(bleAuthServicePo);
            }
        });
        return 0;
    }

}
