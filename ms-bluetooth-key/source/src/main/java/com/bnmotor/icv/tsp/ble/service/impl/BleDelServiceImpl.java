package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.cmd.BluetoothCmdEnum;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleDeviceDelDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleUserKeyDelDto;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleNotification;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenCheck;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDeviceDelVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyDelVo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyDelDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleDelService;
import com.bnmotor.icv.tsp.ble.service.BleTboxService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BleDelServiceImpl extends ServiceImpl<BleKeyUserMapper, UserBleKeyPo> implements BleDelService {
    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Resource
    private BleAuthMapper bleAuthMapper;

    @Resource
    private BleKeyMapMapper bleKeyMapMapper;

    @Resource
    private BleAuthServiceMapper bleAuthServiceMapper;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleTboxService bleTboxService;

    /**
     * 判断数据库是否已经存在
     *
     * @param bleKeyDelDto，projectId
     * @return true表示已经存在  false表示不存在
     */
    @Override
    public boolean bleKeyIsExistInDb(BleKeyDelDto bleKeyDelDto, String projectId,String userId) {
        try {
            LocalDateTime dt = LocalDateTime.now();
            Long endTime = DateUtil.parseStringToDate(bleKeyDelDto.getBleKeyDestroyTime(), DateUtil.TIME_MASK_DEFAULT).getTime();
            LocalDateTime localDateTime = dt.plusMinutes(-30);
            Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
            Long currentTime = now.getTime();
            if (endTime.compareTo(currentTime) < 0) {
                throw new AdamException(com.bnmotor.icv.tsp.ble.common.RespCode.AUTH_DATETIME_CHCKEROOR.getValue(),
                        com.bnmotor.icv.tsp.ble.common.RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
            }
        }catch (ParseException ex){
            throw new AdamException(com.bnmotor.icv.tsp.ble.common.RespCode.AUTH_DATETIME_CHCKEROOR.getValue(),
                    com.bnmotor.icv.tsp.ble.common.RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
        }
        UserBleKeyPo bleKeyDelPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleKeyDelDto.getDeviceId())
                .bleKeyId(bleKeyDelDto.getBleKeyId())
                //.usedUserId(userId)
                //.bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        UserBleKeyPo bleKeyDel = bleKeyUserMapper.queryBleKeyInfoByPrimary(bleKeyDelPo);
        if (bleKeyDel == null) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 根据车辆判断数据库是否已经存在
     * @param devieId，projectId
     * @return true表示已经存在  false表示不存在
     */
    @Override
    public boolean deviceIsExistInDb(String devieId, String projectId) {
        UserBleKeyPo bleKeyDelPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(devieId)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        List<UserBleKeyPo> bleKeyDel = bleKeyUserMapper.queryBleKeyListByPrimary(bleKeyDelPo);
        if (bleKeyDel.size()== 0) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 判断数据库是否已经存在
     *
     * @param bleUserKeyDelDto，projectId
     * @return true表示已经存在  false表示不存在
     */
    @Override
    public List<UserBleKeyPo>  queryUserKeysFromDb(BleUserKeyDelDto bleUserKeyDelDto, String projectId, String userId) {
        if (bleUserKeyDelDto.getDelType()==Constant.OP_BASE_DEL) {
            TokenCheck tokenCheck = new TokenCheck();
            tokenCheck.setServicePwd(bleUserKeyDelDto.getServicePwd());
            bleCommonFeignService.verifyServicePwd(tokenCheck, userId);
        }
        UserBleKeyPo bleKeyDelPo = UserBleKeyPo
                .builder()
                .projectId(projectId)
                .deviceId(bleUserKeyDelDto.getDeviceId())
                .ownerUserId(userId)
                .usedUserId(bleUserKeyDelDto.usedUserId)
                //.bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        List<UserBleKeyPo> bleKeyDelList = bleKeyUserMapper.queryBleKeyListByPrimary(bleKeyDelPo);
        return bleKeyDelList;
    }
    @Override
    public BleKeyDelVo AssembleDelBleVo(BleKeyDelDto bleKeyDelDto){
        Date now = new Date();
        BleKeyDelVo bleKeyDelVo = BleKeyDelVo.builder()
                .deviceId(bleKeyDelDto.getDeviceId())
                .bleKeyDestroyTime(now.getTime())
                .bleKeyStatus(Constant.CANCEL_STATUS).build();
        return bleKeyDelVo;
    }

    @Override
    public BleDeviceDelVo AssembleDelBleDeviceVo(BleDeviceDelDto bleDeviceDelDto){
        LocalDateTime dt  = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleDeviceDelVo bleDeviceDelVo = BleDeviceDelVo.builder()
                .deviceId(bleDeviceDelDto.getDeviceId())
                .bleKeyNum(Constant.COMPARE_ZERO_VALUE.intValue()).build();
        try{
            bleDeviceDelVo.setBleKeyDestroyTime(DateUtil.parseStringToDate(bleDeviceDelDto.getBleKeyDestroyTime(),DateUtil.TIME_MASK_DEFAULT).getTime());
        }catch (ParseException ex){
            log.error("注销车辆下所有蓝牙钥匙时，传入的注销时间转换异常",ex);
            bleDeviceDelVo.setBleKeyDestroyTime(now.getTime());
        }
        return bleDeviceDelVo;
    }

    @Override
    public void mobileMessagePush(UserBleKeyPo userBleKeyPo, VehicleInfoVo vehicleInfoVo, String userId) {
        String userName= StringUtil.EMPTY_STRING;
        UserPhoneVo userPhoneVo = new UserPhoneVo();
        BleNotification bleNotification = new BleNotification();
        if (userId.trim().equals(userBleKeyPo.getOwnerUserId().trim())) {
            userName = bleCommonFeignService.queryTspInfo(userBleKeyPo.getOwnerUserId()).getNickname();
            userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPo.getUsedUserId());
            bleNotification.setCategoryId(1303211738512872662L);
            bleNotification.setTemplateId(1303211738512732579L);
            bleNotification.setUserId(Long.valueOf(userBleKeyPo.getUsedUserId()));
        }else {
            userName = bleCommonFeignService.queryTspInfo(userBleKeyPo.getUsedUserId()).getNickname();
            userPhoneVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPo.getOwnerUserId());
            bleNotification.setCategoryId(1303211738512872661L);
            bleNotification.setTemplateId(1303211738512732578L);
            bleNotification.setUserId(Long.valueOf(userBleKeyPo.getOwnerUserId()));
        }
        bleNotification.setDeviceId(userBleKeyPo.getDeviceId());
        bleNotification.setBleId(Long.valueOf(userBleKeyPo.getBleKeyId()));
        bleNotification.setUserName(userName);
        bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
        bleNotification.setBrand(vehicleInfoVo.getBrandName());
        bleNotification.setType(BluetoothCmdEnum.DELETE_ONE_KEY.getTypeEnum().getType());
        bleNotification.setCmd(BluetoothCmdEnum.DELETE_ONE_KEY.getCmd());
        bleNotification.setPushId(userPhoneVo.getPushRid());
        bleNotification.setExpireDate(DateUtil.formatDateToString(DateUtil.convertLongToDate(userBleKeyPo.getBleKeyExpireTime()),DateUtil.TIME_MASK_DEFAULT));
        bleNotification.setStatus(Constant.INIT_STATUS);
        log.info("********************************************************");
        log.info("********************************************************");
        log.info(bleNotification.toString());
        bleCommonFeignService.bleBaseNotification(bleNotification);
    }

    /**
     * 根据蓝牙钥匙相关参数，从数据库里查询蓝牙钥匙实体
     * @param projectId
     * @param bleDeviceDelDto
     * @return
     */
    @Override
    public List<UserBleKeyPo> queryInvalidBleKey(String projectId,BleDeviceDelDto  bleDeviceDelDto) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleDeviceDelDto.getDeviceId())
                .build();
        List<UserBleKeyPo> bleKeyInvalidList = bleKeyUserMapper.queryBleKeyListByPrimary(userBleKeyPo);

        return bleKeyInvalidList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BleKeyDelVo delBleKey(UserBleKeyPo userBleKeyPoQuery,String uid,String userName) {
        LocalDateTime dt  = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(uid);
        bleTboxService.delBleKey(userBleKeyPoQuery,now,userVo.getPushRid(),uid,userName);
        BleKeyDelVo bleKeyDelVo = BleKeyDelVo.builder()
                .deviceId(userBleKeyPoQuery.getDeviceId())
                .bleKeyDestroyTime(now.getTime())
                .bleKeyStatus(Constant.INIT_STATUS)
                .build();
        return bleKeyDelVo;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BleKeyDelVo delMoveBleKey(UserBleKeyPo userBleKeyPo,VehicleInfoVo vehicleInfoVo,String projectId, String userId,String userName) {
        try {
            LocalDateTime dt  = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            if (userBleKeyPo.getBleAuthId()!= null){
                BleAuthPo bleAuthPo = BleAuthPo.builder()
                        .projectId(projectId)
                        .deviceId(userBleKeyPo.getDeviceId())
                        .id(userBleKeyPo.getBleAuthId())
                        .build();
                BleAuthPo bleAuthPoDel = bleAuthMapper.queryBleAuth(bleAuthPo);
                if (bleAuthPoDel != null) {
                    UserPhoneVo userVo = bleCommonFeignService.bleQueryUserPushIdVo(userId,bleAuthPoDel.getAuthedUserMobileNo());
                    if (userVo != null) {
                        bleAuthPoDel.setDelFlag(Constant.CANCEL_DEL_FLAG);
                        bleAuthPoDel.setUpdateBy(userName);
                        bleAuthPoDel.setUpdateTime(now);
                        bleAuthMapper.updateBleAuth(bleAuthPoDel);
                        bleAuthMapper.moveAuthHisData(bleAuthPoDel);
                        int delCount=bleAuthMapper.deleteAuthHisData(bleAuthPoDel);
                        if (delCount==Constant.COMPARE_EQUAL_VALUE){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        bleAuthServiceMapper.delBleAuthService(bleAuthPoDel);

                    }
                }
            }
            userBleKeyPo.setBleKeyDestroyTime(now);
            userBleKeyPo.setUpdateTime(now);
            userBleKeyPo.setUpdateBy(userName);
            userBleKeyPo.setDelFlag(Constant.CANCEL_DEL_FLAG);
            bleKeyUserMapper.updateBleKeyDestroy(userBleKeyPo);
            userBleKeyPo.setBleKeyDestroyTime(null);
            bleKeyUserMapper.moveBlekeyHisData(userBleKeyPo);
            int delCount=bleKeyUserMapper.deleteBlekeyHisData(userBleKeyPo);
            if (delCount==Constant.COMPARE_EQUAL_VALUE){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleKeyMapMapper.deleteBleKeyServiceById(userBleKeyPo.getProjectId(),userBleKeyPo.getBleKeyId(),null);
            BleKeyDelVo bleKeyDelVo = BleKeyDelVo.builder()
                    .deviceId(userBleKeyPo.getDeviceId())
                    .bleKeyDestroyTime(now.getTime())
                    .bleKeyStatus(Constant.INIT_STATUS)
                    .build();
            return bleKeyDelVo;

        } catch (Exception ex) {
            log.error("注销没有激活的蓝牙钥匙发生异常，详情参考：{}", ex.fillInStackTrace());
            throw new AdamException(RespCode.USER_PARAM_TYPE_ERROR, "注销没有激活的蓝牙钥匙发生异常，请稍后再试！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delDeviceAllBleKey(BleDeviceDelDto bleDeviceDelDto, String projectId, String userId,String userName) {
        try {
            LocalDateTime dt = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);

            UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                    .projectId(projectId)
                    .deviceId(bleDeviceDelDto.getDeviceId())
                    .ownerUserId(userId)
                    .build();
            bleTboxService.delDeviceBle(userBleKeyPo, now, userVo.getPushRid());
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 1;
        }
        return 0;
    }
}
