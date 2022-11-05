package com.bnmotor.icv.tsp.ble.service.job;

import com.bnmotor.icv.tsp.ble.common.ParamReader;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleUnbindBackPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleNotification;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleUnbindBackService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ExpiredAuthClean {
    @Resource
    private BleAuthMapper bleAuthMapper;

    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Resource
    private BleAuthServiceMapper bleAuthServiceMapper;

    @Resource
    private BleKeyMapMapper bleKeyMapMapper;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleUnbindBackService bleUnbindBackService;

    @Resource
    private BleAckPushMapper bleAckPushMapper;

    @Resource
    private ParamReader paramReader;

    @XxlJob("AuthHisDataFullSync")
    public ReturnT<String> demoJobHandler(String param) throws Exception
    {
        log.info("xxl job  start ----------------");
        LocalDateTime dt  = LocalDateTime.now();
        /**
         * 暂时关闭
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        BleAuthPo bleAuthPo = BleAuthPo.builder().authCodeExpireTime(now.getTime()).authExpireTime(now.getTime()).build();
        List<BleAuthPo> queryExpiredBleAuthCode = bleAuthMapper.queryExpiredBleAuthCode(bleAuthPo);
        if (queryExpiredBleAuthCode != null){
            queryExpiredBleAuthCode.stream().forEach(elem -> {
                cleanExpireHisData(elem);
            });
        }
        List<BleAuthPo> queryExpiredBleAuthLimit = bleAuthMapper.queryExpiredBleAuthLimit(bleAuthPo);
        if (queryExpiredBleAuthLimit!=null) {
            queryExpiredBleAuthLimit.stream().forEach(elem -> {
                cleanExpireHisData(elem);
            });
        }
        **/

        List<BleAuthPo> bleAuthTipList = bleAuthMapper.queryExpiredDayNeedTip();
        if (bleAuthTipList != null){
            bleAuthTipList.forEach(elem->{
                UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder().bleAuthId(elem.getId()).build();
                UserPhoneVo userPhoneVo = bleCommonFeignService.bleQueryUserPushIdVo(elem.getUserId(), elem.getAuthedUserMobileNo());
                UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
                if (userPhoneVo!=null){
                    VehicleInfoVo vehicleInfoVo =  bleCommonFeignService.bleGetFeignVehicleInfoVo(elem.getDeviceId());
                    String userName = bleCommonFeignService.queryTspInfo(userBleKeyPoQuery.getOwnerUserId()).getNickname();
                    BleNotification bleNotification = new BleNotification();
                    bleNotification.setDeviceId(elem.getDeviceId());
                    bleNotification.setBleId(Long.valueOf(userBleKeyPoQuery.getBleKeyId()));
                    bleNotification.setUserId(Long.valueOf(userBleKeyPoQuery.getUsedUserId()));
                    bleNotification.setUserName(userName);
                    bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
                    bleNotification.setBrand(vehicleInfoVo.getBrandName());
                    bleNotification.setType(100);
                    bleNotification.setType(2);
                    bleNotification.setPushId(userPhoneVo.getPushRid());
                    bleNotification.setStatus(Constant.INIT_STATUS);
                    bleNotification.setCategoryId(1303211738509786375L);
                    bleNotification.setTemplateId(1303211738512732576L);
                    bleCommonFeignService.bleBaseNotification(bleNotification);
                }
            });
        }

        List<UserBleKeyPo> userBleKeyPoList = bleKeyUserMapper.queryExpiredMonthNeedTip();
        if (Optional.ofNullable(userBleKeyPoList).isPresent()){
            userBleKeyPoList.forEach(elem->{
                UserPhoneVo userPhoneVo = bleCommonFeignService.bleQueryUserPushIdVo(elem.getUsedUserId(), elem.getUsedUserMobileNo());
                if (userPhoneVo!=null){
                    VehicleInfoVo vehicleInfoVo =  bleCommonFeignService.bleGetFeignVehicleInfoVo(elem.getDeviceId());
                    String userName = bleCommonFeignService.queryTspInfo(elem.getOwnerUserId()).getNickname();
                    BleNotification bleNotification = new BleNotification();
                    bleNotification.setDeviceId(elem.getDeviceId());
                    bleNotification.setBleId(Long.valueOf(elem.getBleKeyId()));
                    bleNotification.setUserId(Long.valueOf(elem.getUsedUserId()));
                    bleNotification.setUserName(userName);
                    bleNotification.setLicensePlateNumber(vehicleInfoVo.getDrivingLicPlate());
                    bleNotification.setBrand(vehicleInfoVo.getBrandName());
                    bleNotification.setType(100);
                    bleNotification.setType(3);
                    bleNotification.setPushId(userPhoneVo.getPushRid());
                    bleNotification.setStatus(Constant.INIT_STATUS);
                    bleNotification.setCategoryId(1303211738512872663L);
                    bleNotification.setTemplateId(1303211738512732581L);
                    bleCommonFeignService.bleBaseNotification(bleNotification);
                }
            });
        }
        BleUnbindBackPo build = BleUnbindBackPo.builder().status(String.valueOf(Constant.INIT_STATUS)).build();
        bleUnbindBackService.syncCallSuccessAndDel(build);
        cleanPushExpireHisData();
        return ReturnT.SUCCESS;
    }

    public  void cleanExpireHisData(BleAuthPo elem){
        LocalDateTime dt  = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        elem.setUpdateTime(now);
        elem.setStatus(Constant.CANCEL_STATUS);
        elem.setDelFlag(Constant.CANCEL_DEL_FLAG);
        elem.setUpdateBy(Constant.BLEKEY_EXPIRED_OP);
        bleAuthMapper.updateBleAuth(elem);
        bleAuthMapper.moveAuthHisData(elem);
        int delCount = bleAuthMapper.deleteAuthHisData(elem);
        if (delCount==Constant.COMPARE_EQUAL_VALUE){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        bleAuthServiceMapper.delBleAuthService(elem);
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(elem.getProjectId())
                .deviceId(elem.getDeviceId())
                .bleKeyDestroyTime(now)
                .delFlag(Constant.CANCEL_DEL_FLAG)
                .bleKeyStatus(Constant.CANCEL_STATUS)
                .bleAuthId(elem.getId())
                .build();
        bleKeyUserMapper.updateBleKeyDestroy(userBleKeyPo);
        userBleKeyPo.setBleKeyDestroyTime(null);
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfo(userBleKeyPo);
        if (userBleKeyPoQuery!=null) {
            bleKeyUserMapper.moveBlekeyHisData(userBleKeyPo);
             delCount = bleKeyUserMapper.deleteBlekeyHisData(userBleKeyPo);
            if (delCount==Constant.COMPARE_EQUAL_VALUE){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            bleKeyMapMapper.deleteBleKeyServiceById(userBleKeyPoQuery.getProjectId(), userBleKeyPoQuery.getBleKeyId(),null);
        }
    }

    public  void cleanPushExpireHisData(){
        LocalDateTime dt  = LocalDateTime.now();
        Date oriNow = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        LocalDateTime newDate = dt.minusHours(paramReader.expirePushDel);
        Date now = Date.from(newDate.toInstant(ZoneOffset.of("+8")));
        BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                .createTime(now)
                .status(40)
                .delFag(Constant.CANCEL_DEL_FLAG)
                .updateTime(oriNow)
                .build();
        bleAckPushMapper.updateOverTimeBlePushStatus(bleAckPushPo);
        List<BleAckPushPo> bleAckPushPoList = bleAckPushMapper.queryOverLimitBleAckPush(bleAckPushPo);
        if (bleAckPushPoList.size()>Constant.COMPARE_EQUAL_VALUE) {
            bleAckPushMapper.moveBleAckPushServiceList(bleAckPushPoList);
            bleAckPushMapper.deleteBleAckPushList(bleAckPushPoList);
        }

    }

}
