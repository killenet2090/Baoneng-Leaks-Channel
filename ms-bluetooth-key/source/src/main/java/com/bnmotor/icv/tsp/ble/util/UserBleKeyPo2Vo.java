package com.bnmotor.icv.tsp.ble.util;

import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleAuthBleKeyVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDevicePinVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyApplyVo;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class UserBleKeyPo2Vo {
    public static BleKeyApplyVo userBleKeyPo2Vo(UserBleKeyPo userBleKeyPo, BleDevicePinVo bleDevicePinVo, BleKeyGeneratorDto bleKeyGeneratorDto){
        BleKeyApplyVo bleKeyApplyVo = BleKeyApplyVo.builder().build();
        bleKeyApplyVo.setDeviceId(userBleKeyPo.getDeviceId());
        bleKeyApplyVo.setBleKeyRefreshTime(userBleKeyPo.getBleKeyRefreshTime().getTime());
        bleKeyApplyVo.setBleKeyExpireTime(String.format("%08x", 0xFFFFFFFF));
        bleKeyApplyVo.setBleKeyEffectiveTime(userBleKeyPo.getBleKeyEffectiveTime().getTime());
        if (bleDevicePinVo != null) {
            bleKeyApplyVo.setBleConName(bleDevicePinVo.getDeviceMac());
            bleKeyApplyVo.setBleConPin(bleDevicePinVo.getPinCode());
            bleKeyApplyVo.setBleConId(Integer.parseInt(bleDevicePinVo.getUserTypeId()));
        }
        bleKeyApplyVo.setBleKeyStatus(String.valueOf(userBleKeyPo.getBleKeyStatus()));
        bleKeyApplyVo.setEncryptAppBleKey(bleKeyGeneratorDto.getEncrypAppBleKey());
        bleKeyApplyVo.setEncryptAppBleKeySign(bleKeyGeneratorDto.getAppSign());
        bleKeyApplyVo.setBleKeyId(userBleKeyPo.getBleKeyId());
        bleKeyApplyVo.setBleKeyName(userBleKeyPo.getBleKeyName());
        return bleKeyApplyVo;
    }

    public static BleAuthBleKeyVo userBleKeyPo2AuthVo(UserBleKeyPo userBleKeyPo, BleDevicePinVo bleDevicePinVo,
                                                      BleAuthPo bleAuthPo, Date now, BleKeyGeneratorDto bleKeyGeneratorDto){
        BleAuthBleKeyVo bleAuthBleKeyVo = BleAuthBleKeyVo.builder().build();
        BeanUtils.copyProperties(userBleKeyPo, bleAuthBleKeyVo);
        bleAuthBleKeyVo.setBleKeyEffectiveTime(userBleKeyPo.getBleKeyEffectiveTime().getTime());
        bleAuthBleKeyVo.setEncryptAppBleKeySign(bleKeyGeneratorDto.getAppSign());
        bleAuthBleKeyVo.setEncryptAppBleKey(bleKeyGeneratorDto.getEncrypAppBleKey());
        bleAuthBleKeyVo.setBleKeyExpireTime(String.format("%08x", 0xFFFFFFFF));
        bleAuthBleKeyVo.setBleKeyStatus(String.valueOf(userBleKeyPo.getBleKeyStatus()));
        bleAuthBleKeyVo.setBleKeyRefreshTime(now.getTime());
        bleAuthBleKeyVo.setAuthVoucher(bleKeyGeneratorDto.getAuthVoucher());
        bleAuthBleKeyVo.setAuthVoucherSign(bleKeyGeneratorDto.getAuthVoucherSign());
        bleAuthBleKeyVo.setAuthExpireTime(bleAuthPo.getAuthExpireTime());
        bleAuthBleKeyVo.setBleSkeyExpireTime(bleAuthPo.getAuthExpireTime());
        bleAuthBleKeyVo.setBleConName(bleDevicePinVo.getDeviceMac());
        bleAuthBleKeyVo.setBleConPin(bleDevicePinVo.getPinCode());
        return bleAuthBleKeyVo;
    }
}
