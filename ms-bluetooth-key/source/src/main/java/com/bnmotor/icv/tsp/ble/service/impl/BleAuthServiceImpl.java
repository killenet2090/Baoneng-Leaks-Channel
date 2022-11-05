package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.core.utils.HexUtil;
import com.bnmotor.icv.adam.core.utils.StringUtil;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.request.ble.*;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleNotification;
import com.bnmotor.icv.tsp.ble.model.response.ble.*;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserVo;
import com.bnmotor.icv.tsp.ble.service.*;
import com.bnmotor.icv.tsp.ble.service.feign.*;
import com.bnmotor.icv.tsp.ble.util.BleKeyUtil;
import com.bnmotor.icv.tsp.ble.util.ParamAssem2BlePo;
import com.bnmotor.icv.tsp.ble.util.RandomUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import javax.annotation.Resource;
import java.text.ParseException;
import java.time.*;
import java.util.*;
import static com.bnmotor.icv.tsp.ble.common.RespCode.*;
import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;

/**
 * @ClassName: BleAuthServiceImpl
 * @Description: 蓝牙钥匙授权业务DAO实现类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class BleAuthServiceImpl extends ServiceImpl<BleAuthMapper, BleAuthPo> implements BleAuthService {
    @Autowired
    private BleApplyService bleApplyService;

    @Resource
    private BleAuthMapper bleAuthMapper;

    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Resource
    private BleUserService bleUserService;

    @Resource
    private BleAuthMapService bleAuthMapService;

    @Resource
    private BleCaPinMapper bleCaPinMapper;

    @Resource
    private BleAuthServiceMapper bleAuthServiceMapper;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleKeyMapMapper bleKeyMapMapper;

    @Resource
    private BlePkiService blePkiService;

    @Override
    public List<BleKeyServicePo> assembleBleKeyServicePo(BleAuthPo bleAuthPo, UserBleKeyPo userBleKeyPo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthServicePo bleAuthServicePo = BleAuthServicePo.builder()
                .projectId(bleAuthPo.getProjectId())
                .authId(bleAuthPo.getId())
                .delFlag(Constant.INIT_DEL_FLAG).build();
        List<BleAuthServicePo> bleAuthServices = bleAuthServiceMapper.queryBleAuthService(bleAuthServicePo);
        List<BleKeyServicePo> bleKeyServicePoList = new ArrayList<>();
        bleAuthServices.stream().forEach(elem -> {
            BleKeyServicePo build = BleKeyServicePo.builder().projectId(elem.getProjectId())
                    .serviceId(elem.getServiceId())
                    .bleId(Long.valueOf(userBleKeyPo.getBleKeyId()))
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .version(Constant.INIT_VERSION)
                    .createTime(now)
                    .createBy(userBleKeyPo.getCreateBy())
                    .build();
            bleKeyServicePoList.add(build);
        });

        return bleKeyServicePoList;
    }

    /**
     * 生成授权码函数
     *
     * @return
     */
    @Override
    public String generateAuthCode() {
        String random_code = RandomUnit.generateRandom(6, 1);
        return random_code;
    }

    @Override
    public UserVo queryTspUserVo(String phoneNumber, String uid) {
        UserVo userVo = bleCommonFeignService.queryTspUserVo(uid, phoneNumber);
        return userVo;
    }

    @Override
    public void getDbHasAuthCondition(LocalDateTime dt, Date nowParam, String projectId, UserVo userVo, BleAuthDto bleAuthDto) {
        if (bleAuthDto.getUserType().equals(Constant.USER_MSTER_TYPE)) {
            throw new  AdamException(RespCode.AUTH_USERTYPE_IMPERMISSBLE.getValue(),
                    RespCode.AUTH_USERTYPE_IMPERMISSBLE.getDescription());
        }
        try {
            Long startTime = DateUtil.parseStringToDate(bleAuthDto.getBleKeyEffectiveTime(),
                    DateUtil.TIME_MASK_DEFAULT).getTime();
            Long endTime = DateUtil.parseStringToDate(bleAuthDto.getBleKeyExpireTime(),
                    DateUtil.TIME_MASK_DEFAULT).getTime();
            LocalDateTime localDateTime = dt.plusMinutes(-30);
            Date now = Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
            Long currentTime = now.getTime();
            if (startTime.compareTo(currentTime) < 0 || endTime.compareTo(currentTime) < 0) {
                throw new AdamException(RespCode.AUTH_DATETIME_CHCKEROOR.getValue(),
                        RespCode.AUTH_DATETIME_CHCKEROOR.getDescription());
            }
        }catch (ParseException ex){
            log.error("借车人开通蓝牙钥匙时，传入的时间格式有错误,错误信息为=[{}]",ex);
            throw new AdamException(RespCode.DATETIME_FORMATE_ERROR.getValue(),
                    RespCode.DATETIME_FORMATE_ERROR.getDescription());
        }
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthDto.getDeviceId())
                .authedUserMobileNo(bleAuthDto.getPhoneNumber())
                .delFlag(Constant.INIT_DEL_FLAG)
                .userId(String.valueOf(userVo.getUid()))
                .isAuthConfirmed(Constant.WAS_CONFIRMED)
                .build();
        Optional<BleAuthPo> bleAuthPoQuery = Optional.ofNullable(bleAuthMapper.queryExistBleAuth(bleAuthPo));
        if(bleAuthPoQuery.isPresent()){
            throw new AdamException(RespCode.AUTH_DUPLICATE_ERROR.getValue(),
                    RespCode.AUTH_DUPLICATE_ERROR.getDescription());
        }
    }


    @Override
    public void queryUserNameLimit(String projectId, String uid, BleAuthDto bleAuthDto, UserPhoneVo userPhoneVo) {
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthDto.getDeviceId())
                .authedUserName(bleAuthDto.getUsedUserName())
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        BleAuthPo bleAuthPoQuery = bleAuthMapper.queryUserNameLimit(bleAuthPo);

        if (bleAuthPoQuery != null) {
            throw new AdamException(BLE_KEY_USERNAME_NUM_CHECK_PROCESS_ERROR);
        }
    }


    @Override
    public void getDbHasConfirmCondition(String projectId, String uid, BleAuthConfirmDto bleAuthConfirmDto) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthConfirmDto.getDeviceId())
                .authedUserMobileNo(bleAuthConfirmDto.getPhoneNumber())
                .delFlag(Constant.INIT_DEL_FLAG)
                .userId(uid)
                .isAuthConfirmed(Constant.WAS_CONFIRMED)
                .authExpireTime(now.getTime())
                .authedMobileDeviceId(bleAuthConfirmDto.getUsedMobileDeviceId())
                .build();
        Optional<BleAuthPo> bleAuthPoQuery = Optional.ofNullable(bleAuthMapper.queryExistBleAuth(bleAuthPo));
        if (bleAuthPoQuery.isPresent()) {
            throw new AdamException(RespCode.AUTH_CONFIRM_DUPLICATE_ERROR.getValue(), RespCode.AUTH_CONFIRM_DUPLICATE_ERROR.getDescription());
        }
    }


    /**
     * 车主授权第一阶段，授权码生成并存库
     * @param projectId
     * @param uid
     * @param bleAuthConfirmDto
     * @return
     */
    public BleAuthPo queryHasBlekeyAuthData(String projectId,String uid,BleAuthConfirmDto bleAuthConfirmDto){
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .userId(uid)
                .deviceId(bleAuthConfirmDto.getDeviceId())
                .authedUserMobileNo(bleAuthConfirmDto.getPhoneNumber())
                .build();
        BleAuthPo result = bleAuthMapper.queryExistBleAuth(bleAuthPo);
        return result;
    }
    /**
     * 车主授权第一阶段，授权码生成并存库
     *
     * @param projectId
     * @param bleAuthDto
     * @param authCode
     * @return
     * @throws ParseException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BleAuthBleKeyVo saveBleAuthRight(LocalDateTime dt, Date now, String projectId, BleAuthDto bleAuthDto,
                                            VehicleInfoVo vehicleInfoVo, UserPhoneVo userPhoneVo, UserVo userVo,
                                            String authCode, String uid) {
        BleCaPinPo bleCaPinPo = BleCaPinPo
                .builder()
                .bleDeviceId(bleAuthDto.getDeviceId())
                .userTypeId(Long.valueOf(bleAuthDto.getUserType()))
                .build();
        BleDevicePinVo bleDevicePinVo = bleCaPinMapper.queryBleDevicePins(bleCaPinPo);
        if (!Optional.ofNullable(bleDevicePinVo).isPresent()) {
            throw new AdamException(BLE_KEY_PIN_CODE_NOT_EXIST);
        }
        try {
            UserBleKeyPo userBleKeyPo = assembleBleKey(projectId, bleAuthDto, vehicleInfoVo, userPhoneVo, userVo, 0L,now);
            BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleKey(userBleKeyPo, authCode, false);
            BleAuthPo bleAuthPo = assembleBleAuth(dt, now, projectId, bleAuthDto, vehicleInfoVo, userPhoneVo, userVo, authCode, bleKeyGeneratorDto);

            bleAuthDto.getAuthList().stream().forEach(elem->{
                BleAuthServicePo bleAuthServicePo = BleAuthServicePo.builder()
                        .projectId(projectId)
                        .authId(bleAuthPo.getId())
                        .delFlag(Constant.INIT_DEL_FLAG)
                        .version(Constant.INIT_VERSION)
                        .serviceId(elem)
                        .createBy(String.valueOf(userVo.getUid()))
                        .createTime(now)
                        .build();
                bleAuthServiceMapper.addBleAuthService(bleAuthServicePo);
                BleKeyServicePo bleKeyServicePo = BleKeyServicePo.builder()
                        .projectId(projectId)
                        .bleId(Long.valueOf(userBleKeyPo.getBleKeyId()))
                        .serviceId(elem)
                        .createBy(String.valueOf(userVo.getUid()))
                        .createTime(now)
                        .delFlag(Constant.INIT_DEL_FLAG)
                        .version(Constant.INIT_VERSION)
                        .build();
                bleKeyMapMapper.addBleKeyServiceMap(bleKeyServicePo);
            });
            userBleKeyPo.setBleAuthId(bleAuthPo.getId());
            userBleKeyPo.setEncryptAppBleKey(bleKeyGeneratorDto.getBleSrc());
            bleApplyService.updateApplyBleAuthId(userBleKeyPo);
            String userName = bleUserService.getUserName(uid).getNickname();
            BleAuthBleKeyVo bleAuthBleKeyVo = bleApplyService.saveAuthBleKeyPerms(userName,vehicleInfoVo,bleDevicePinVo,userBleKeyPo,bleAuthPo,bleKeyGeneratorDto);
            return bleAuthBleKeyVo;

        }catch (Exception ex){
            log.error("授权入库出现异常，异常信息为=[{}]",ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new AdamException(BLE_KEY_AUTH_SAVE_ERROR);
        }
    }


    @Override
    public BleAuthBleKeyVo createNewBle(String projectId, String uid, BleAuthConfirmDto bleAuthConfirmDto) {
        String userName = bleUserService.getUserName(uid).getNickname();
        VehicleInfoVo vehicleInfoVo = bleCommonFeignService.bleGetFeignVehicleInfoVo(bleAuthConfirmDto.getDeviceId());
        UserVo tspUserVo = queryTspUserVo(bleAuthConfirmDto.getPhoneNumber(), uid);

        BleAuthPo bleAuthPo = queryHasBlekeyAuthData(projectId, uid, bleAuthConfirmDto);
        UserPhoneVo userPhoneVo = bleCommonFeignService.bleQueryUserPushIdVo(uid, bleAuthConfirmDto.getPhoneNumber());
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(bleAuthPo.getProjectId())
                .deviceId(bleAuthPo.getDeviceId())
                .bleAuthId(bleAuthPo.getId())
                .build();
        UserBleKeyPo queryBlekey = bleApplyService.getDbBlekeyData(userBleKeyPo);
        userBleKeyPo.setUsedUserMobileDeviceId(bleAuthConfirmDto.getUsedMobileDeviceId());
        userBleKeyPo.setUsedUserMobileModel(userPhoneVo.getDeviceModel());
        String authCode = generateAuthCode();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleKey(userBleKeyPo, null, false);

        BleCaPinPo bleCaPinPo = BleCaPinPo.builder()
                .bleDeviceId(bleAuthConfirmDto.getDeviceId())
                .userTypeId(Long.valueOf(queryBlekey.getUserType()))
                .build();
        BleDevicePinVo bleDevicePinVo = bleCaPinMapper.queryBleDevicePins(bleCaPinPo);
        if (!Optional.ofNullable(bleDevicePinVo).isPresent()) {
            throw new AdamException(BLE_KEY_PIN_CODE_NOT_EXIST);
        }
        BleAuthBleKeyVo bleAuthBleKeyVo = bleApplyService.saveAuthBleKeyPerms(userName, vehicleInfoVo,bleDevicePinVo,queryBlekey,bleAuthPo,bleKeyGeneratorDto);
        return bleAuthBleKeyVo;
    }

    @Override
    public BleAuthPo assembleBleAuth(LocalDateTime dt,Date now ,String projectId, BleAuthDto bleAuthDto,
                                     VehicleInfoVo vehicleInfoVo,UserPhoneVo userPhoneVo, UserVo userVo,
                                     String authCode,BleKeyGeneratorDto bleKeyGeneratorDto) {
        log.info("接收到授权信息为=[{}]",bleAuthDto.toString());
        BleAuthPo bleAuthPo = BleAuthPo.builder().build();
        bleAuthPo.setDeviceId(bleAuthDto.getDeviceId());
        bleAuthPo.setAuthedUserMobileNo(bleAuthDto.getPhoneNumber());
        //bleAuthPo.setAuthedMobileDeviceId(userPhoneVo.getDeviceId());
        try {
            bleAuthPo.setAuthTime(DateUtil.parseStringToDate(bleAuthDto.getBleKeyEffectiveTime(),
                    DateUtil.TIME_MASK_DEFAULT));
        } catch (Exception ex) {
            log.error("输入的授权生效时间转换出错，按照规则取当前默认时间", ex);
            bleAuthPo.setAuthTime(now);
        }
        bleAuthPo.setStatus(Constant.INIT_STATUS);
        bleAuthPo.setAuthedUserName(bleAuthDto.getUsedUserName());
        try {
            bleAuthPo.setAuthExpireTime(DateUtil.parseStringToDate(bleAuthDto.getBleKeyExpireTime(),
                    DateUtil.TIME_MASK_DEFAULT).getTime());
        } catch (ParseException ex) {
            log.error("授权时转换有效期发生错误=", ex);
            throw new AdamException(RespCode.BLE_EXPIRE_TIME_ERROR);
        }
        bleAuthPo.setCreateBy(String.valueOf(userVo.getUid()));
        bleAuthPo.setCreateTime(now);
        bleAuthPo.setVersion(Constant.INIT_VERSION);
        bleAuthPo.setDelFlag(Constant.INIT_DEL_FLAG);
        bleAuthPo.setUserId(String.valueOf(userVo.getUid()));
        bleAuthPo.setProjectId(projectId);
        bleAuthPo.setDeviceName(vehicleInfoVo.getDrivingLicPlate());
        bleAuthPo.setAuthCode(authCode);
        bleAuthPo.setUserType(Long.valueOf(bleAuthDto.getUserType()));
        bleAuthPo.setAuthedUserName(bleAuthDto.getUsedUserName());
        LocalDateTime localDateTime = dt.plusHours(Constant.AUTH_EXPIRE_SPAN);
        bleAuthPo.setUserAuthId(Long.valueOf(bleAuthDto.getAuthId()));
        bleAuthPo.setAuthVoucherExpireTime(DateUtil.formatDateToString(
                Date.from(localDateTime.toInstant(ZoneOffset.of("+8"))), DateUtil.TIME_MASK_DEFAULT));
        bleAuthPo.setIsAuthConfirmed(Constant.WAS_CONFIRMED);
        bleAuthPo.setAuthedMobileDeviceId(bleAuthDto.getUsedMobileDeviceId());
        bleAuthPo.setAuthConfirmedTime(now);
        bleAuthPo.setAuthConfirmedUserId(String.valueOf(userVo.getUid()));
        bleAuthPo.setAuthVoucher(bleKeyGeneratorDto.getAuthVoucher());
        bleAuthPo.setAuthVoucherSign(bleKeyGeneratorDto.getAuthVoucherSign());
        bleAuthPo.setEncryptAppBleKey(bleKeyGeneratorDto.getEncrypAppBleKey());
        bleAuthPo.setEncryptAppBleKeySign(HexUtil.byte2HexStr(bleKeyGeneratorDto.getAppSign()));
        int rowId = bleAuthMapper.insert(bleAuthPo);
        return bleAuthPo;
    }

    @Override
    public void assemblePushMessageBleAuth(String projectId, BleAuthDto bleAuthDto, UserPhoneVo userPhoneVo,
                                           VehicleInfoVo vehicleInfoVo, String ownerUid, Long uid, String userName) {
        if (ownerUid.trim().equals(String.valueOf(uid).trim())) {
            throw new AdamException(RespCode.AUTH_SELF_IMPERMISSBLE.getValue(),
                    RespCode.AUTH_SELF_IMPERMISSBLE.getDescription());
        }
        UserBleKeyPo ownerBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthDto.getDeviceId())
                .usedUserMobileDeviceId(userPhoneVo.getDeviceId())
                .usedUserId(String.valueOf(uid))
                .usedUserMobileNo(bleAuthDto.getPhoneNumber())
                .userType(Long.valueOf(Constant.USER_MSTER_TYPE))
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        UserBleKeyPo ownerBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(ownerBleKeyPo);
        if (ownerBleKeyPoQuery != null) {
            throw new AdamException(RespCode.AUTH_USERTYPE_IMPERMISSBLE.getValue(),
                    RespCode.AUTH_USERTYPE_IMPERMISSBLE.getDescription());
        }
        BleNotification bleNotification = new BleNotification();
        bleNotification.setDeviceId(bleAuthDto.getDeviceId());
        bleNotification.setUserId(uid);
        bleNotification.setUserName(userName);
        bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
        bleNotification.setBrand(vehicleInfoVo.getBrandName());
        bleNotification.setType(100);
        bleNotification.setType(1);
        bleNotification.setPushId(userPhoneVo.getPushRid());
        bleNotification.setStatus(Constant.INIT_STATUS);
        bleNotification.setCategoryId(1303211738509786373L);
        bleNotification.setTemplateId(1303211738512732574L);
        log.info("************************************************");
        log.info(bleNotification.toString());
        bleCommonFeignService.bleBaseNotification(bleNotification);
    }

    @Override
    public UserBleKeyPo assembleBleKey(String projectId, BleAuthDto bleAuthDto, VehicleInfoVo vehicleInfoVo,
                                       UserPhoneVo userPhoneVo, UserVo userVo, Long authId,Date now) {
        try {
            UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                    .projectId(projectId)
                    .deviceId(bleAuthDto.getDeviceId())
                    .bleKeyId(IdWorker.getIdStr())
                    .ownerUserId(bleAuthDto.getOwnerId())
                    .bleKeyExpireTime(DateUtil.parseStringToDate(bleAuthDto.getBleKeyExpireTime(),
                            DateUtil.TIME_MASK_DEFAULT).getTime())
                    .version(Constant.INIT_VERSION)
                    .delFlag(Constant.INIT_DEL_FLAG)
                    .bleWorkModel(Constant.INIT_KEY_MODEL)
                    .createBy(String.valueOf(userVo.getUid()))
                    .deviceModel(vehicleInfoVo.getVehModelName())
                    .deviceName(vehicleInfoVo.getDrivingLicPlate())
                    .userType(Long.valueOf(bleAuthDto.getUserType()))
                    .usedUserMobileNo(bleAuthDto.getPhoneNumber())
                    .usedUserMobileDeviceId(bleAuthDto.getUsedMobileDeviceId())
                    .usedUserName(bleAuthDto.getUsedUserName())
                    .bleKeyName(bleAuthDto.getBleKeyName())
                    .usedUserImgUrl(userVo.getHeadImgUrl())
                    .bleAuthId(authId)
                    .usedUserId(String.valueOf(userVo.getUid()))
                    .bleKeyRefreshTime(now)
                    .usedUserMobileModel(userPhoneVo.getDeviceModel())
                    .bleKeyStatus(Constant.INIT_STATUS)
                    .createTime(now)
                    .opType(Constant.OP_BASE_ADD)
                    .createBy(String.valueOf(userVo.getUid()))
                    .build();

            userBleKeyPo.setBleKeyEffectiveTime(DateUtil.parseStringToDate(bleAuthDto.getBleKeyEffectiveTime(),
                    DateUtil.TIME_MASK_DEFAULT));
            boolean save = bleApplyService.save(userBleKeyPo);
            if (save) {
                return userBleKeyPo;
            }
            return null;
        } catch (ParseException ex) {
            log.error("授权信息中钥匙生效时间转换出错=", ex);
            return null;
        }
    }


    /**
     * 授权第二阶段， 借车人查询没有确认的记录
     *
     * @param projectId   项目ID
     * @param uid         用户ID
     * @param phoneNumber 手机号码
     * @return 没有授权记录实体类
     */
    @Override
    public List<BleAuthingVo> blekeyAuthorisingQueryService(String projectId, String uid, String phoneNumber) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .status(Constant.INIT_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .authedUserMobileNo(phoneNumber)
                .isAuthConfirmed(Constant.INIT_CONFIRME)
                .authExpireTime(now.getTime())
                .build();
        List<BleAuthPo> bleAuthPoList = bleAuthMapper.queryBleNoAuth(bleAuthPo);
        if (bleAuthPoList == null) {
            throw new AdamException(AUTH_COMPE_QUERY_ERROR);
        }
        List<BleAuthingVo> bleAuthingVoList = new ArrayList<>();
        bleAuthPoList.stream().forEach(
                elem -> {
                    BleAuthingVo bleAuthingVo = new BleAuthingVo();
                    BeanUtils.copyProperties(elem, bleAuthingVo);
                    bleAuthingVo.setUserId(uid);
                    bleAuthingVo.setUserName(elem.getCreateBy());
                    bleAuthingVo.setDeviceName(elem.getDeviceName());
                    bleAuthingVo.setAuthTime(elem.getAuthTime().getTime());
                    bleAuthingVo.setAuthExpireTime(elem.getAuthExpireTime());
                    //待优化
                    bleAuthingVoList.add(bleAuthingVo);
                }
        );
        return bleAuthingVoList;
    }


    /**
     * 授权第三阶段（确认阶段），根据出入的参数得到授权表实体类
     *
     * @param bleAuthConfirmDto
     * @param projectId
     * @param uid
     * @param userPhoneVo
     * @return
     */
    @Override
    public BleAuthPo assembleTspBleAuthPo(BleAuthConfirmDto bleAuthConfirmDto, String projectId, String uid, String userName, UserPhoneVo userPhoneVo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .authCode(bleAuthConfirmDto.getAuthCode())
                .authedUserMobileNo(bleAuthConfirmDto.getPhoneNumber())
                .status(Constant.INIT_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG)
                .deviceId(bleAuthConfirmDto.getDeviceId())
                .build();
        BleAuthPo bleAuthPoQuery = bleAuthMapper.queryBleAuthToActive(bleAuthPo);
        bleAuthPoQuery.setAuthConfirmedTime(now);
        bleAuthPoQuery.setUserId(uid);
        bleAuthPoQuery.setAuthConfirmedUserId(uid);
        bleAuthPoQuery.setIsAuthConfirmed(Constant.WAS_CONFIRMED);
        bleAuthPoQuery.setAuthedUserMobileNo(bleAuthConfirmDto.getPhoneNumber());
        bleAuthPoQuery.setAuthedMobileDeviceId(bleAuthConfirmDto.getUsedMobileDeviceId());
        bleAuthPoQuery.setStatus(Constant.INIT_STATUS);
        bleAuthPoQuery.setUpdateBy(userName);
        bleAuthPoQuery.setUpdateTime(now);

        return bleAuthPoQuery;
    }


    /**
     * 授权第三阶段（确认阶段）,检查授权码是否合法
     *
     * @param bleAuthConfirmDto
     * @param projectId
     * @param uid
     * @return
     */
    @Override
    public void checkAuthCodeInfo(BleAuthConfirmDto bleAuthConfirmDto, String projectId, String uid) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .authCode(bleAuthConfirmDto.getAuthCode())
                .deviceId(bleAuthConfirmDto.getDeviceId())
                .authedUserMobileNo(bleAuthConfirmDto.getPhoneNumber())
                .status(Constant.INIT_STATUS)
                .userType(Long.valueOf(Constant.USER_MSTER_TYPE))
                .build();
        BleAuthPo bleAuthPoQuery = bleAuthMapper.queryBleAuth(bleAuthPo);
        if (bleAuthPoQuery == null || StringUtil.EMPTY_STRING.equals(bleAuthPoQuery.getDeviceId())) {
            throw new AdamException(RespCode.USER_SMS_CEHCK_CODE_INPUT_ALREADY.getValue(), RespCode.USER_SMS_CEHCK_CODE_INPUT_ALREADY.getDescription());
        }
        if (bleAuthPoQuery.getIsAuthConfirmed().equals(Constant.WAS_CONFIRMED)) {
            log.info("车辆已经激活，请勿再次激活-车辆ID{}", bleAuthConfirmDto.getDeviceId());
            throw new AdamException(RespCode.USER_SMS_CEHCK_CODE_INPUT_ALREADY);
        }
        Long sub = bleAuthPoQuery.getAuthExpireTime() - now.getTime();
        if (sub < Constant.COMPARE_ZERO_VALUE) {
            throw new AdamException(RespCode.AUTH_HAS_ALREAD_ACTIVE);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BleKeyServicePo> saveAuthBleData(BleAuthPo bleAuthPo, UserBleKeyPo bleKeyInfoPo) {
        List<BleKeyServicePo> bleKeyServicePos = assembleBleKeyServicePo(bleAuthPo, bleKeyInfoPo);
        int saveCount = bleApplyService.saveBleKeyInfo(bleKeyInfoPo);

        if (saveCount == Constant.COMPARE_EQUAL_VALUE) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("授权阶段保存蓝牙钥匙数据数目为{}，数据体为{}", saveCount, bleKeyInfoPo.toString());
            throw new AdamException(AUTH_APPLY_ERROR);
        }
        int rows = bleAuthMapper.confirmUpdateBleAuth(bleAuthPo);

        if (rows <= 0) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("授权阶段确认操作更新蓝牙钥匙数据成功行数为{}，数据体为{}", rows, bleKeyInfoPo.toString());
            throw new AdamException(AUTH_APPLY_ERROR);
        }
        bleKeyMapMapper.deleteBleKeyServiceById(bleKeyInfoPo.getProjectId(), bleKeyInfoPo.getBleKeyId(),null);
        bleKeyServicePos.stream().forEach(elem -> {
            bleKeyMapMapper.addBleKeyServiceMap(elem);
        });
        return bleKeyServicePos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeOfflineAuthBleKey(UserBleKeyPo bleKeyInfoParam){
        try{
            LocalDateTime dt = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            bleKeyInfoParam.setBleKeyStatus(Constant.ACTIVE_STATUS);
            bleKeyInfoParam.setUpdateTime(now);
            BleAuthPo bleAuthPo = BleAuthPo.builder()
                    .id(bleKeyInfoParam.getBleAuthId())
                    .status(Constant.ACTIVE_STATUS)
                    .updateTime(now)
                    .build();
            bleKeyUserMapper.updateBleKeyStatus(bleKeyInfoParam);
            bleAuthMapper.updateBleAuth(bleAuthPo);
        }catch (Exception ex){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("离线激活出错，出错信息为[{}]，数据体为[{}]", ex);
            throw new AdamException(AUTH_ACTIVE_OFFLINE_ERROR);
        }
    }


    @Override
    public int cancelAuth(BleAuthPo bleAuthPo, String projectId, String uid) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        bleAuthPo.setDelFlag(Constant.CANCEL_DEL_FLAG);
        bleAuthPo.setStatus(Constant.CANCEL_STATUS);
        bleAuthPo.setUpdateBy(bleUserService.getUserName(uid).getNickname());
        bleAuthPo.setUpdateTime(now);
        int result = bleAuthMapper.cancelAuth(bleAuthPo);
        return result;
    }

    @Override
    public BleAuthPo cancelCheck(BleAuthCancelDto bleAuthCancelDto, String projectId, String uid) {
        BleAuthPo bleAuthPo = BleAuthPo.builder()
                .projectId(projectId)
                .deviceId(bleAuthCancelDto.getDeviceId())
                .authedUserMobileNo(bleAuthCancelDto.getPhoneNumber())
                .id(Long.valueOf(bleAuthCancelDto.getBleAuthId()))
                .delFlag(Constant.INIT_DEL_FLAG)
                .build();
        BleAuthPo QueryBleAuthPo = bleAuthMapper.cancelBleKeyCheck(bleAuthPo);
        return QueryBleAuthPo;
    }

}
