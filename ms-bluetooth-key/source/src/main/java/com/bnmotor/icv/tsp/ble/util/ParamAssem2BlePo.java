package com.bnmotor.icv.tsp.ble.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.response.ble.VehicleInfoVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class ParamAssem2BlePo {
    public  static  UserBleKeyPo assemble2BleKeyPo(UserBleKeyPo userBleKeyPo, BleAuthPo bleAuthPo, UserPhoneVo userPhoneVo, VehicleInfoVo vehicleInfoVo) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleKeyPoIns = UserBleKeyPo.builder().build();
        BeanUtils.copyProperties(userBleKeyPo,userBleKeyPoIns);
        userBleKeyPoIns.setDeviceId(userBleKeyPo.getDeviceId());
        userBleKeyPoIns.setUsedUserMobileDeviceId(userBleKeyPo.getUsedUserMobileDeviceId());
        userBleKeyPoIns.setUsedUserMobileNo(userBleKeyPo.getUsedUserMobileNo());
        userBleKeyPoIns.setUsedUserId(bleAuthPo.getUserId());
        userBleKeyPoIns.setDeviceModel(vehicleInfoVo.getVehModelName());
        userBleKeyPoIns.setDeviceName(vehicleInfoVo.getDrivingLicPlate());
        userBleKeyPoIns.setDeviceModel(vehicleInfoVo.getVehModelName());
        userBleKeyPoIns.setBleKeyId(IdWorker.getIdStr());
        userBleKeyPoIns.setBleKeyStatus(Constant.INIT_STATUS);
        userBleKeyPoIns.setBleKeyRefreshTime(now);
        userBleKeyPoIns.setUsedUserMobileModel(userPhoneVo.getDeviceModel());
        userBleKeyPoIns.setUpdateBy(userBleKeyPo.getUsedUserName());
        userBleKeyPoIns.setBleKeyEffectiveTime(bleAuthPo.getAuthTime());
        userBleKeyPoIns.setBleKeyExpireTime(bleAuthPo.getAuthExpireTime());
        return userBleKeyPoIns;
    }
}
