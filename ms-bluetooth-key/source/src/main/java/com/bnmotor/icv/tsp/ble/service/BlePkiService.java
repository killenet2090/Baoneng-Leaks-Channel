package com.bnmotor.icv.tsp.ble.service;

import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;

import java.util.Date;
import java.util.Map;

/**
 * 生成加密对象和签名数据
 */
public interface BlePkiService {
    /**
     * 创建在线蓝牙信息和离线蓝牙信息
     * @param tspBleKeyPo
     * @param authCode
     * @param offLine
     * @return
     */
    BleKeyGeneratorDto createBleKey(UserBleKeyPo tspBleKeyPo, String authCode, Boolean offLine);

    /**
     * 创建退换蓝牙信息
     * @param bleKeyPo
     * @return
     */
    public BleKeyGeneratorDto createBleReplaceSign(Date now,UserBleKeyPo bleKeyPo) ;

    /**
     * 创建加密的蓝牙钥匙信息
     * @param tspBleKeyPo
     * @return
     */
    Map<String,String> createEncrypBleKey(UserBleKeyPo tspBleKeyPo);

    /**
     *
     * @param tboxContent
     * @return
     */
    BleKeyGeneratorDto createTboxSignBase(byte[]  tboxContent);


    /**
     * 更新蓝牙钥匙有效期签名
     * @param tboxContent
     * @return
     */
    BleKeyGeneratorDto createBleUpdateExpireSign(byte[]  tboxContent);
}
