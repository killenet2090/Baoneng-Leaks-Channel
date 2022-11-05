package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.HexUtil;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.ble.common.ParamReader;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.enums.BleKeyStatusEnum;
import com.bnmotor.icv.tsp.ble.common.enums.BleWorkModelEnum;
import com.bnmotor.icv.tsp.ble.common.enums.UserTypeEnum;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.request.ble.*;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleNotification;
import com.bnmotor.icv.tsp.ble.model.request.feign.TokenCheck;
import com.bnmotor.icv.tsp.ble.model.request.pki.BleEncrptyDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.*;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;

/**
 * @ClassName: BleApplyServiceImpl
 * @Description: 蓝牙钥匙申请业务DAO实现类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class BleApplyServiceImpl extends ServiceImpl<BleKeyUserMapper, UserBleKeyPo> implements BleApplyService {
    /**
     * 用户蓝牙钥匙Mapper
     */
    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Resource
    private BleKeyUserHisMapper bleKeyUserHisMapper;

    @Resource
    private BleUserMapper bleUserMapper;

    @Resource
    private BleKeyMapMapper bleKeyMapMapper;

    @Resource
    private BleUserService bleUserService;

    @Resource
    private BleAuthCompeMapper bleAuthCompeMapper;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleCaPinMapper bleCaPinMapper;

    @Resource
    private BleAuthMapper bleAuthMapper;

    @Resource
    private BleAuthService bleAuthService;

    @Resource
    private BleTboxService bleTboxService;

    @Resource
    private BlePkiService blePkiService;

    @Resource
    private RedisHelper redisHelper;

    @Resource
    private ParamReader paramReader;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BleKeyApplyVo saveBleKeyAndPerms(UserBleKeyPo userBleKeyPo) {
        //生成蓝牙钥匙记录， 关联车主授权服务
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserPhoneVo response = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPo.getOwnerUserId());
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleKey(userBleKeyPo, null, false);
        userBleKeyPo.setEncryptAppBleKey(bleKeyGeneratorDto.getBleSrc());
        sendBlekeyAndRightKafka(userBleKeyPo, now, response, bleKeyGeneratorDto);
        if (userBleKeyPo.getUserType().intValue() == Constant.USER_MSTER_TYPE) {
            saveBlekeyAndRight(userBleKeyPo, now);
        }
        BleCaPinPo bleCaPinPo = BleCaPinPo
                .builder()
                .bleDeviceId(userBleKeyPo.getDeviceId())
                .userTypeId(Long.valueOf(UserTypeEnum.VEH_OWNER.getValue()))
                .build();
        BleDevicePinVo bleDevicePinVo = null;
        Optional<BleDevicePinVo> bleDevicePinVoOptional = Optional.ofNullable(bleCaPinMapper.queryBleDevicePins(bleCaPinPo));
        if (!bleDevicePinVoOptional.isEmpty()){
             bleDevicePinVo = bleDevicePinVoOptional.get();
        }
        BleKeyApplyVo bleKeyApplyVo = UserBleKeyPo2Vo.userBleKeyPo2Vo(userBleKeyPo, bleDevicePinVo, bleKeyGeneratorDto);
        bleKeyApplyVo.setByteSignSrc(bleKeyGeneratorDto.getByteSign());
        bleKeyApplyVo.setByteAppBleKey(bleKeyGeneratorDto.getByteAppBleKey());
        bleKeyApplyVo.setBleKeySrc(bleKeyGeneratorDto.getBleSrc());
        bleKeyApplyVo.setBlekeyByte(bleKeyGeneratorDto.getBlekeyByte());
        
        return bleKeyApplyVo;
    }


    @Override
    public BleAuthBleKeyVo saveAuthBleKeyPerms(String userName, VehicleInfoVo vehicleInfoVo,BleDevicePinVo bleDevicePinVo,
                                               UserBleKeyPo userBleKeyPo,BleAuthPo bleAuthPo,BleKeyGeneratorDto bleKeyGeneratorDto) {
        //生成蓝牙钥匙记录， 关联车主授权服务
        bleAuthService.saveAuthBleData(bleAuthPo, userBleKeyPo);
        UserPhoneVo response = bleCommonFeignService.bleGetFeignUserPhoneVo(bleAuthPo.getUserId());
        UserPhoneVo response_owner = bleCommonFeignService.bleGetFeignUserPhoneVo(userBleKeyPo.getOwnerUserId());
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        sendBlekeyAndRightKafka(userBleKeyPo, now, response, bleKeyGeneratorDto);
        BleNotification bleNotification = new BleNotification();
        bleNotification.setDeviceId(userBleKeyPo.getDeviceId());
        bleNotification.setBleId(Long.valueOf(userBleKeyPo.getBleKeyId()));
        bleNotification.setUserId(Long.valueOf(userBleKeyPo.getOwnerUserId()));
        bleNotification.setUserName(userName);
        bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
        bleNotification.setBrand(vehicleInfoVo.getBrandName());
        bleNotification.setType(100);
        bleNotification.setType(5);
        bleNotification.setPushId(response_owner.getPushRid());
        bleNotification.setStatus(Constant.INIT_STATUS);
        bleNotification.setCategoryId(1303211738509786374L);
        bleNotification.setTemplateId(1303211738512732575L);
        log.info("************************************************");
        log.info(bleNotification.toString());
        bleCommonFeignService.bleBaseNotification(bleNotification);
        BleCaPinPo bleCaPinPo = BleCaPinPo.builder()
                .bleDeviceId(userBleKeyPo.getDeviceId())
                .userTypeId(Long.valueOf(UserTypeEnum.VEH_OWNER.getValue()))
                .build();
        BleAuthBleKeyVo bleAuthBleKeyVo = UserBleKeyPo2Vo.userBleKeyPo2AuthVo(userBleKeyPo, bleDevicePinVo,
                bleAuthPo, now, bleKeyGeneratorDto);
        bleAuthBleKeyVo.setByteSignSrc(bleKeyGeneratorDto.getByteSign());
        bleAuthBleKeyVo.setByteAppBleKey(bleKeyGeneratorDto.getByteAppBleKey());
        bleAuthBleKeyVo.setBleKeySrc(bleKeyGeneratorDto.getBleSrc());
        bleAuthBleKeyVo.setBlekeyByte(bleKeyGeneratorDto.getBlekeyByte());
        BleAuthBleKeyVo bleAuthBleKeyVoRedis = BleAuthBleKeyVo.builder().build();
        bleAuthBleKeyVoRedis = bleAuthBleKeyVo;
        bleAuthBleKeyVoRedis.setAuthVoucher(bleKeyGeneratorDto.getAuthVoucher());
        bleAuthBleKeyVoRedis.setAuthVoucherSign(bleKeyGeneratorDto.getAuthVoucherSign());
        try {
            String authJson = JsonUtil.toJson(bleAuthBleKeyVoRedis);
            BleEncrptyDto bleEncrptyDto = BleEncrptyDto
                    .builder()
                    .algorithm(paramReader.algorithm)
                    .groupId(paramReader.groupId)
                    .content(ByteUtil.getBytes(authJson))
                    .keyIndex(Integer.parseInt(paramReader.keyId))
                    .build();
            bleCommonFeignService.pkiEncrpty(bleEncrptyDto);

            String authKey = userBleKeyPo.getDeviceId().concat("_")
                    .concat(userBleKeyPo.getUsedUserMobileDeviceId()).concat("_")
                    .concat(userBleKeyPo.getUsedUserId()).concat("_0");
            redisHelper.setStr(authKey, authJson, paramReader.expireTtl, TimeUnit.HOURS);
            String str = redisHelper.getStr(authKey);
            log.info(str);
        } catch (JsonProcessingException ex) {
            log.error("缓存授权信息到redis出错，错误信息为 {}", ex);
            throw new AdamException(BLE_KEY_AUTH_SAVE_REDIS_ERROR.getValue()
                    , BLE_KEY_AUTH_SAVE_REDIS_ERROR.getDescription());
        }
        return bleAuthBleKeyVo;
    }

    @Override
    public int updateApplyBleAuthId(UserBleKeyPo userBleKeyPo) {
        int res = bleKeyUserMapper.updateApplyBleAuthId(userBleKeyPo);
        return res;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBlekeyAndRight(UserBleKeyPo userBleKeyPo, Date now) {
        int rows = bleKeyUserMapper.addBleKeyUser(userBleKeyPo);
        if (rows <= 0) {
            log.error("车主申请阶段UserBleKeyPo在入库时发现入库行数为{}，导致申请回滚失败", rows);
            throw new AdamException(RespCode.VEOWNER_APPLY_ERROR.getValue(), RespCode.VEOWNER_APPLY_ERROR.getDescription());
        }
        List<BleAuthCompePo> bleAuthCompePos = bleAuthCompeMapper.queryBleCompe();
        bleAuthCompePos.stream().forEach(elem -> {
            BleKeyServicePo bleKeyServicePo = BleKeyServicePo.builder()
                    .bleId(Long.valueOf(userBleKeyPo.getBleKeyId()))
                    .projectId(userBleKeyPo.getProjectId())
                    .serviceId(Long.valueOf(elem.getServiceCode()))
                    .version(Constant.INIT_VERSION)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .createBy(userBleKeyPo.getCreateBy())
                    .createTime(now).build();
            bleKeyMapMapper.addBleKeyServiceMap(bleKeyServicePo);
        });
    }

    @Override
    public void sendBlekeyAndRightKafka(UserBleKeyPo userBleKeyPo, Date now, UserPhoneVo response, BleKeyGeneratorDto bleKeyGeneratorDto) {
        bleTboxService.bleApply(userBleKeyPo, now, response, bleKeyGeneratorDto);
    }


    @Override
    public UserBleKeyPo assembleCreateBleKey(String projectId, String uid, BleKeyApplyDto bleKeyApplyDto) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleKeyApplyDto.getDeviceId());
        String userName = bleUserService.getUserName(uid).getNickname();

        UserBleKeyPo userBleKeyPoIns = UserBleKeyPo.builder()
                .projectId(projectId)
                .bleKeyStatus(BleKeyStatusEnum.UNABLE.getValue())
                .ownerUserId(uid)
                .bleKeyId(IdWorker.getIdStr())
                .deviceId(bleKeyApplyDto.getDeviceId())
                .deviceName(vehicleInfoVo.getDrivingLicPlate())
                .deviceModel(vehicleInfoVo.getVehModelName())
                .usedUserName(userName)
                .usedUserMobileDeviceId(bleKeyApplyDto.getMobileDeviceId())
                .usedUserMobileModel(vehicleInfoVo.getVehModelName())
                .userType(Long.valueOf(Constant.USER_MSTER_TYPE))
                .bleWorkModel(BleWorkModelEnum.USER.getValue())
                .bleKeyEffectiveTime(now)
                .bleKeyRefreshTime(now)
                .bleKeyExpireTime(Long.MAX_VALUE)
                .usedUserId(uid)
                //.bleKeyExpireTime(String.format("%016x", 0xFFFFFFFF))
                .delFlag(Constant.INIT_DEL_FLAG)
                .version(Constant.INIT_VERSION)
                .usedUserMobileNo(bleUserService.getUserName(uid).getPhone())
                .opType(Constant.OP_BASE_ADD)
                .createBy(uid)
                .createTime(now)
                .build();
        return userBleKeyPoIns;
    }


    @Override
    public void checkVehOwnerBleKey(String projectId, String userId, String deviceId, String mobileDeviceId) {
        LambdaQueryWrapper<UserBleKeyPo> lambdaQueryWrap = Wrappers.<UserBleKeyPo>lambdaQuery();
        lambdaQueryWrap.eq(UserBleKeyPo::getUsedUserId, userId);
        lambdaQueryWrap.eq(UserBleKeyPo::getDeviceId, deviceId);
        lambdaQueryWrap.eq(UserBleKeyPo::getUsedUserMobileDeviceId, mobileDeviceId);
        lambdaQueryWrap.eq(UserBleKeyPo::getBleKeyStatus, BleKeyStatusEnum.ENABLE.getValue());
        Integer count = bleKeyUserMapper.selectCount(lambdaQueryWrap);
        if (count > 0) {
            throw new AdamException(BLE_KEY_QUERY_VEH_OWNER_EXIST);
        }
    }

    @Override
    public void queryHasApply(String projectId, String userId, BleKeyApplyDto keyApplyDto) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(keyApplyDto.getDeviceId())
                .usedUserId(userId)
                .usedUserMobileDeviceId(keyApplyDto.getMobileDeviceId())
                .delFlag(Constant.INIT_DEL_FLAG).build();
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.queryBleKeyListByPrimary(userBleKeyPo);
        if (userBleKeyPoList.size() > Constant.RECORD_EXIST) {
            throw new AdamException(BLE_KEY_QUERY_VEH_OWNER_EXIST);
        }

    }

    @Override
    public void checkVehBleKeyOverflowLimited(String projectId, String userId, BleKeyApplyDto keyApplyDto) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(keyApplyDto.getDeviceId())
                .delFlag(Constant.INIT_DEL_FLAG).build();
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.checkVehBleKeyOverflowLimited(userBleKeyPo);
        if (userBleKeyPoList.size() >= Constant.BLE_NUM_LIMIT) {
            throw new AdamException(BLE_KEY_LIMIT_NUM_CHECK_PROCESS_ERROR);
        }
        userBleKeyPo = UserBleKeyPo.builder().projectId(projectId)
                .deviceId(keyApplyDto.getDeviceId())
                .usedUserMobileDeviceId(keyApplyDto.getMobileDeviceId())
                .usedUserId(userId)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        userBleKeyPoList = bleKeyUserMapper.checkVehBleKeyOverflowLimited(userBleKeyPo);
        if (userBleKeyPoList.size() >= 1) {
            throw new AdamException(BLE_KEY_QUERY_VEH_OWNER_EXIST);
        }
    }


    @Override
    public int saveBleKeyInfo(UserBleKeyPo userBleKeyPo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        /**
         * 判断影响的行数
         */
        int rowId = bleKeyUserMapper.updateAllFieldById(userBleKeyPo);
        if (rowId < 0) {
            return rowId;
        }
        BleKeyServicePo bleKeyServicePo = BleKeyServicePo.builder()
                .bleId(Long.valueOf(userBleKeyPo.getBleKeyId()))
                .projectId(userBleKeyPo.getProjectId())
                .version(Constant.INIT_VERSION)
                .serviceId(Long.valueOf(paramReader.peojectId))
                .createBy(bleUserService.getUserName(userBleKeyPo.getOwnerUserId()).getNickname())
                .createTime(now)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        bleUserMapper.insert(bleKeyServicePo);
        return rowId;
    }


    @Override
    public UserBleKeyPo getDbBlekeyData(UserBleKeyPo userBleKeyPo) {
        UserBleKeyPo userBleKeyPoResult = bleKeyUserMapper.queryBleKeyInfo(userBleKeyPo);
        return userBleKeyPoResult;
    }

    @Override
    public List<UserBleKeyVo> getBorryDbBlekeyInfo(String projectId, String userId, String usedMobileDeviceId) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .usedUserId(userId)
                .usedUserMobileDeviceId(usedMobileDeviceId)
                .build();
        List<UserBleKeyVo> userBleKeyPoList = bleKeyUserMapper.queryBleBorrykeyInfo(userBleKeyPo);
        return userBleKeyPoList;
    }

    @Override
    public void checkInputTimeValid(BleAuthPo bleAuthPo,LocalDateTime dt) {
        try {
            /**
             * 检查用户类型是否合法，不能对车主自己授权
             */
            if (bleAuthPo.getUserType().equals(Constant.USER_MSTER_TYPE)) {
                throw new AdamException(RespCode.AUTH_USERTYPE_IMPERMISSBLE.getValue(), RespCode.AUTH_USERTYPE_IMPERMISSBLE.getDescription());
            }
            Long startTime = bleAuthPo.getAuthTime().getTime();
            Long endTime = DateUtil.convertLongToDate(bleAuthPo.getAuthExpireTime()).getTime();
            LocalDateTime localDateTime = dt.plusMinutes(-30);
            Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
            Long currentTime = now.getTime();
            if (startTime.compareTo(currentTime) < Constant.COMPARE_EQUAL_VALUE || endTime.compareTo(currentTime) < Constant.COMPARE_EQUAL_VALUE) {
                throw new AdamException(RespCode.AUTH_DATETIME_CHCKEROOR.getValue(), RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
            }
        } catch (Exception ex) {
            log.error("检查授权的时格式转换时发生异常{}", ex);
            throw new AdamException(RespCode.AUTH_DATETIME_CHCKEROOR.getValue(), RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
        }
    }

    @Override
    public void checkDeviceBleOverflowLimited(String projectId, BleAuthDto bleAuthDto) {
        UserBleKeyPo userBleKeyNumLimit = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthDto.getDeviceId())
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.checkVehBleKeyOverflowLimited(userBleKeyNumLimit);
        if (userBleKeyPoList.size() >= Constant.BLE_NUM_LIMIT) {
            throw new AdamException(BLE_KEY_LIMIT_NUM_CHECK_PROCESS_ERROR);
        }
    }
    @Override
    public List<UserBlekeyHisVo> checkDeviceBleCount(String projectId, String deviceId) {
        UserBleKeyPo userBleKeyNumLimit = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(deviceId)
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.checkVehBleKeyOverflowLimited(userBleKeyNumLimit);
        List<UserBlekeyHisVo> collect = userBleKeyPoList.stream()
                .map(elem -> {
                    UserBlekeyHisVo userBlekeyHisVo= new UserBlekeyHisVo();
                    userBlekeyHisVo.setProjectId(elem.getProjectId());
                    userBlekeyHisVo.setDeviceId(elem.getDeviceId());
                    userBlekeyHisVo.setBleKeyEffectiveTime(DateUtil.formatDateToString(elem.getBleKeyEffectiveTime(),DateUtil.TIME_MASK_DEFAULT));
                    userBlekeyHisVo.setBleKeyExpireTime(DateUtil.formatDateToString(DateUtil.convertLongToDate(elem.getBleKeyExpireTime()),DateUtil.TIME_MASK_DEFAULT));
                    userBlekeyHisVo.setBleKeyStatus(elem.getBleKeyStatus());
                    userBlekeyHisVo.setDelFlag(elem.getDelFlag());
                    userBlekeyHisVo.setBleKeyDestroyTime(DateUtil.formatDateToString(elem.getBleKeyDestroyTime(),DateUtil.TIME_MASK_DEFAULT));
                    userBlekeyHisVo.setUsedUserMobileDeviceId(elem.getUsedUserMobileDeviceId());
                    userBlekeyHisVo.setUsedUserMobileModel(elem.getUsedUserMobileModel());
                    userBlekeyHisVo.setUsedUserMobileNo(elem.getUsedUserMobileNo());
                    userBlekeyHisVo.setBleKeyId(elem.getBleKeyId());
                    return userBlekeyHisVo;
                }).collect(Collectors.toList());
        userBleKeyNumLimit.setDelFlag(null);
        List<UserBleKeyHisPo> userBleKeyHis = bleKeyUserHisMapper.queryDeviceAllBles(userBleKeyNumLimit);
        List<UserBlekeyHisVo> collectHis = userBleKeyHis.stream()
                .map(elem -> {
                    UserBlekeyHisVo userBlekeyHisVo= new UserBlekeyHisVo();
                    userBlekeyHisVo.setProjectId(elem.getProjectId());
                    userBlekeyHisVo.setDeviceId(elem.getDeviceId());
                    userBlekeyHisVo.setBleKeyEffectiveTime(DateUtil.formatDateToString(elem.getBleKeyEffectiveTime(),DateUtil.TIME_MASK_DEFAULT));
                    userBlekeyHisVo.setBleKeyExpireTime(DateUtil.formatDateToString(DateUtil.convertLongToDate(elem.getBleKeyExpireTime()),DateUtil.TIME_MASK_DEFAULT));
                    userBlekeyHisVo.setBleKeyStatus(elem.getBleKeyStatus());
                    userBlekeyHisVo.setDelFlag(elem.getDelFlag());
                    userBlekeyHisVo.setBleKeyDestroyTime(DateUtil.formatDateToString(elem.getBleKeyDestroyTime(),DateUtil.TIME_MASK_DEFAULT));
                    userBlekeyHisVo.setUsedUserMobileDeviceId(elem.getUsedUserMobileDeviceId());
                    userBlekeyHisVo.setUsedUserMobileModel(elem.getUsedUserMobileModel());
                    userBlekeyHisVo.setUsedUserMobileNo(elem.getUsedUserMobileNo());
                    userBlekeyHisVo.setBleKeyId(elem.getBleKeyId());
                    return userBlekeyHisVo;
                }).collect(Collectors.toList());
        collect.addAll(collectHis);
        return collect;
    }

    @Override
    public UserBleKeyPo queryUserAuthBlekey(BleUserAuthDelDto userAuthDelDto) {

        String peojectId = paramReader.peojectId;
        BleAuthPo bleAuthPo = BleAuthPo
                .builder()
                .projectId(peojectId)
                .deviceId(userAuthDelDto.getDeviceId())
                .userAuthId(Long.valueOf(userAuthDelDto.getUserAuthId())).build();
        BleAuthPo bleAuths = bleAuthMapper.queryExistBleAuth(bleAuthPo);
        if (!Optional.ofNullable(bleAuths).isPresent()){
            log.error("用户服务调用删除接口发现授权无记录");
        }
        UserBleKeyPo userBleKeyPa = UserBleKeyPo.builder().bleAuthId(bleAuths.getId()).build();
        UserBleKeyPo userBleKeyPo = bleKeyUserMapper.queryBleKeyInfoNoAuth(userBleKeyPa);
        if (!Optional.ofNullable(userBleKeyPo).isPresent()){
            log.error("用户服务调用删除接口发现授权无记录");
        }else {
            TokenCheck tokenCheck = new TokenCheck();
            tokenCheck.setServicePwd(userAuthDelDto.getServicePwd());
            bleCommonFeignService.verifyServicePwd(tokenCheck, userBleKeyPo.getUsedUserId());
        }
        return userBleKeyPo;
    }

    @Override
    public List<UserBleKeyVo> ownerQueryBorryDbBlekeyInfo(String projectId, String deviceId, String userId) {
        List<UserBleKeyVo> userBleKeyVoList = new ArrayList<>();
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(deviceId)
                .ownerUserId(userId)
                .build();
        userBleKeyVoList = bleKeyUserMapper.queryOwnerBleBorrykeyInfo(userBleKeyPo);
        return userBleKeyVoList;
    }

    @Override
    public List<UserBleKeyPo> queryBlekeyInfoById(UserBleKeyPo userBleKeyPo) {
        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.queryBleKeyListByPrimary(userBleKeyPo);
        return userBleKeyPoList;
    }

    @Override
    public UserBleKeyPo queryBlekeyInfo(BleKeyDelDto bleKeyDelDto, String projectId) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleKeyDelDto.getDeviceId())
                .bleKeyId(bleKeyDelDto.getBleKeyId())
                .build();
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfo(userBleKeyPo);
        return userBleKeyPoQuery;
    }

    @Override
    public Optional<UserBleKeyPo> getCancelBlekeyInfo(String projectId, String deviceId, String userId, String phoneNumber) {
        UserBleKeyPo build = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(deviceId)
                .usedUserId(userId)
                .usedUserMobileNo(phoneNumber)
                .bleKeyStatus(Constant.CANCEL_STATUS)
                .build();
        QueryWrapper<UserBleKeyPo> queryWrapper = new QueryWrapper<UserBleKeyPo>();
        Map<String, Object> params = Collections.synchronizedMap(new HashMap<String, Object>(1));
        params.put("project_id", projectId);
        params.put("device_id", deviceId);
        params.put("used_user_id", userId);
        params.put("used_user_mobile_no", phoneNumber);
        queryWrapper.allEq(params).lt("ble_key_status", Constant.CANCEL_STATUS);
        Optional<UserBleKeyPo> oneBleuthVo = Optional.ofNullable(bleKeyUserMapper.getCancelBlekeyInfo(build));
        return oneBleuthVo;
    }

    /**
     * 修改蓝蓝牙钥匙表有效期
     *
     * @param bleKeyModifyDateDto
     * @param projectId
     * @param uid
     * @return
     * @throws ParseException
     */
    @Override
    public boolean updateBleKeyExpireDate(BleKeyModifyDateDto bleKeyModifyDateDto, String projectId, String uid) throws ParseException {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .usedUserId(uid)
                .bleKeyId(bleKeyModifyDateDto.getBleKeyId())
                .bleKeyEffectiveTime(DateUtil.parseStringToDate(bleKeyModifyDateDto.getBleEffectiveTime(), DateUtil.TIME_MASK_DEFAULT))
                .bleKeyExpireTime(DateUtil.parseStringToDate(bleKeyModifyDateDto.getBleKeyExpireTime(), DateUtil.TIME_MASK_DEFAULT).getTime())
                .build();
        long result = bleKeyUserMapper.updateBleKeyExpireDate(userBleKeyPo);
        return true;
    }


    /**
     * 撤销授权
     *
     * @param bleAuthCancelDto
     * @param projectId
     * @param uid
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelBleKey(BleAuthCancelDto bleAuthCancelDto, String projectId, String uid) {
        String userName = bleUserService.getUserName(uid).getNickname();
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        int result = 0;

        BleAuthPo bleAuthCon = BleAuthPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthCancelDto.getDeviceId())
                .authedUserMobileNo(bleAuthCancelDto.getPhoneNumber())
                .id(Long.valueOf(bleAuthCancelDto.getBleAuthId()))
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        BleAuthPo bleAuthPo = bleAuthMapper.queryBleAuth(bleAuthCon);
        if (bleAuthPo != null) {
            bleAuthPo.setStatus(Constant.CANCEL_STATUS);
            bleAuthPo.setDelFlag(Constant.CANCEL_DEL_FLAG);
            bleAuthPo.setUpdateTime(now);
            bleAuthPo.setUpdateBy(userName);
            bleAuthMapper.updateBleAuth(bleAuthPo);
            bleAuthMapper.moveAuthHisData(bleAuthPo);
            int delCount=bleAuthMapper.deleteAuthHisData(bleAuthPo);
            if (delCount==Constant.COMPARE_EQUAL_VALUE){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                    .projectId(projectId)
                    .ownerUserId(uid)
                    .bleAuthId(bleAuthPo.getId())
                    .deviceId(bleAuthCancelDto.getDeviceId())
                    .usedUserMobileNo(bleAuthCancelDto.getPhoneNumber())
                    .updateTime(now)
                    .userType(bleAuthPo.getUserType())
                    .bleKeyDestroyTime(now)
                    .updateBy(userName)
                    .bleKeyStatus(Constant.CANCEL_STATUS)
                    .delFlag(Constant.CANCEL_DEL_FLAG)
                    .build();
            result = bleKeyUserMapper.updateBleKeyData(userBleKeyPo);
            bleKeyUserMapper.moveBlekeyHisData(userBleKeyPo);
            delCount =bleKeyUserMapper.deleteBlekeyHisData(userBleKeyPo);
            if (delCount==Constant.COMPARE_EQUAL_VALUE){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return result;
    }


}
