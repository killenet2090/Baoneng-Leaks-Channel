package com.bnmotor.icv.tsp.ble.service;

import com.bnmotor.icv.adam.sdk.bluetooth.common.ACKContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.BluetoothTypeEnum;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import java.util.*;

public interface BleSyncKafkaService {

    /**
     * 根据业务命令去查询钥匙ID
     * @param vin
     * @param businessSerialNum
     * @param typeEnum
     * @param ackCmd
     * @return
     */
    BleAckPushPo CombineMsgToPush(String vin, Integer businessSerialNum, BluetoothTypeEnum typeEnum, int ackCmd);

    /**
     * 根据业务命令去查询多条钥匙ID
     * @param vin
     * @param businessSerialNum
     * @param typeEnum
     * @param ackCmd
     * @return
     */
    List<BleAckPushPo> CombineMsgsToPush(String vin, Integer businessSerialNum, BluetoothTypeEnum typeEnum, int ackCmd);


    /**
     * 注册蓝牙钥匙回应
     * @param contentDto
     * @param bleAckPushPo
     * @return
     */
    boolean ackUpdateRegisterBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);

    /**
     * 查询指定蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackQuerySpecifiedBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);

    /**
     * 查询指定车辆蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackQueryDeviceIdAllBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);

    /**
     * 删除指定蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackDelSpecifiedBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);
    /**
     * 更新某指定的pin码
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackUpdateSpecifiedBlePin(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);
    /**
     * 更新车下所有钥匙
     *
     * @param bleAckPushPos
     * @return
     */
    boolean updateDeviceIdAllBlePin(ACKContentDto contentDto, List<BleAckPushPo> bleAckPushPos);

    /**
     * 删除车辆下所有蓝牙钥匙
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackDelAllBlekey(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);

    /**
     * 更新蓝牙钥匙有效期，并通知给APP
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackUpdateSpecifiedBlekeySecret(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);
    /**
     * 更新蓝牙钥匙有效期，并通知给APP
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackUpdateSpecifiedBleLimitDate(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);
    /**
     * 修改某把钥匙权限
     *
     * @param bleAckPushPo
     * @return
     */
    boolean ackUpdateSpecifidBleAuthrity(ACKContentDto contentDto,BleAckPushPo bleAckPushPo);

    /**
     * 构建更新某把蓝牙钥匙
     *
     * @param downDto
     * @return
     */
    boolean buildUpdateSpecifiedBlePin(BluetoothDownDto downDto);

    /**
     * 构建某辆车下的所有蓝牙钥匙
     *
     * @param downDto
     * @return
     */
    boolean buildCreateDeviceIdAllBlePin(BluetoothDownDto downDto);

    /**
     * 回复蓝牙钥匙上报
     *
     * @param downDto
     * @return
     */
    boolean buildUpReportInfo(BluetoothDownDto downDto);

    /**
     * 车辆蓝牙钥匙上报，用于和数据库比较
     *
     * @param downDto
     * @return
     */
    boolean buildBleKeyReport(BluetoothDownDto downDto);

    /**
     * tbox端主动注销的蓝牙钥匙上报
     *
     * @param downDto
     * @return
     */
    boolean buildCancelBleKeyId(BluetoothDownDto downDto);

    /**
     * tbox端上报钥匙状态回复结果
     *
     * @param downDto
     * @return
     */
    boolean buildConnectionStatus(BluetoothDownDto downDto);

    /**
     * 离线注销
     * @param downDto
     * @return
     */
    boolean buildBleKeyDel(BluetoothDownDto downDto);

    /**
     * 离线激活
     * @param downDto
     * @return
     */
    boolean buildBleKeyOffLineActive(BluetoothDownDto downDto);
}
