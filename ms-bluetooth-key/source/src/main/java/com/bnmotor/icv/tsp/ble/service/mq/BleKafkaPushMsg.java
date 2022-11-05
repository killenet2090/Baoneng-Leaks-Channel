package com.bnmotor.icv.tsp.ble.service.mq;

import com.bnmotor.icv.adam.sdk.bluetooth.common.ACKContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.cmd.BluetoothCmdEnum;
import com.bnmotor.icv.adam.sdk.bluetooth.up.BluetoothUpDto;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleCaPinPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import com.bnmotor.icv.adam.sdk.bluetooth.common.PSHContentDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: BleKafkaPushMsg
 * @Description: 封装读写kafka数据包
 * @author: shuqi1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class BleKafkaPushMsg {
    @Resource
    private  GenerateContent generateContent;

    /**
     * 车辆授权
     *
     * @param userBleKeyPo
     * @return
     */
    public BluetoothDownDto createApplyMsg(UserBleKeyPo userBleKeyPo, BleKeyGeneratorDto bleKeyGeneratorDto) {
        try {

            BluetoothDownDto downDto = commonSetting(userBleKeyPo.getDeviceId());
            downDto.setCmdEnum(BluetoothCmdEnum.REGISTER_ONE_KEY);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.createBleApplyDto(userBleKeyPo,bleKeyGeneratorDto);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("注册特定蓝牙钥匙接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 蓝牙钥匙删除指定
     *
     * @param userBleKeyPo
     * @return
     */
    public BluetoothDownDto delSpecifiedBlekeyMsg(UserBleKeyPo userBleKeyPo) {
        try {

            BluetoothDownDto downDto = commonSetting(userBleKeyPo.getDeviceId());
            downDto.setCmdEnum(BluetoothCmdEnum.DELETE_ONE_KEY);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.delBleOneBytes(userBleKeyPo);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("删除特定蓝牙钥匙接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 蓝牙钥匙删除所有钥匙
     *
     * @param DeviceId
     * @return
     */
    public BluetoothDownDto delDevieAllBlekeyMsg(String DeviceId) {
        try {

            BluetoothDownDto downDto = commonSetting(DeviceId);
            downDto.setCmdEnum(BluetoothCmdEnum.DELETE_ALL_KEYS);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.delBleAllBytes();
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("删除特定车辆下蓝牙钥匙接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }


    /**
     * 拼装装蓝牙钥匙更新有效期数据包
     *
     * @return
     * @throws JsonProcessingException
     */
    public BluetoothDownDto createBleUpdateExpireMsg(UserBleKeyPo userBleKeyPo) {
        try {
            BluetoothDownDto downDto = commonSetting(userBleKeyPo.getDeviceId());
            downDto.setCmdEnum(BluetoothCmdEnum.MODIFY_DATE_KEYS);
            /**
             * 消息中蓝牙钥匙主体信息
             */
            PSHContentDto pshContentDto = generateContent.createUpdateBleExpireTimeDto(userBleKeyPo);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("拼装装蓝牙钥匙更新有效期接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 拼装装蓝牙钥匙更新权限数据包
     *
     * @return
     * @throws JsonProcessingException
     */
    public BluetoothDownDto createBleUpdateRightMsg(UserBleKeyPo userBleKeyPo, Long right) {
        try {
            BluetoothDownDto downDto = commonSetting(userBleKeyPo.getDeviceId());
            downDto.setCmdEnum(BluetoothCmdEnum.MODIFY_AUTHORITY_KEYS);

            /**
             *下发响应包
             */
            //downDto.getAckContentDto()

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.createUpdateBleRightDto(userBleKeyPo, right);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("拼装装蓝牙钥匙更新权限接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 蓝牙钥匙删除所有钥匙
     *
     * @param userBleKeyPo
     * @return
     */
    public BluetoothDownDto replaceBlekeyMsg(UserBleKeyPo userBleKeyPo,BleKeyGeneratorDto bleKeyGeneratorDto) {
        try {

            BluetoothDownDto downDto = commonSetting(userBleKeyPo.getDeviceId());
            downDto.setCmdEnum(BluetoothCmdEnum.UPDATE_ONE_SECRET);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.replaceBleApplyDto(bleKeyGeneratorDto);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("蓝牙钥匙更新特定钥匙接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 蓝牙钥匙添加/更新pin码
     *
     * @param bleCaPinPo
     * @return
     */
    public BluetoothDownDto addOrUpdateBlePinMsg(BleCaPinPo bleCaPinPo) {
        try {

            BluetoothDownDto downDto = commonSetting(bleCaPinPo.getBleDeviceId());
            downDto.setCmdEnum(BluetoothCmdEnum.UPDATE_ONE_PIN);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.createPinInfoDto(bleCaPinPo);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("蓝牙钥匙添加/更新pin码接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 蓝牙钥匙添加/更新pin码
     *
     * @param bleCaPinPoList
     * @return
     */
    public BluetoothDownDto updateDeviceBlePinMsg(List<BleAckPushPo> bleCaPinPoList) {
        try {

            BluetoothDownDto downDto = commonSetting(bleCaPinPoList.get(0).getDeviceId().toString());
            downDto.setCmdEnum(BluetoothCmdEnum.UPDATE_PINS);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.createDevicePinInfoDto(bleCaPinPoList);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("更新车辆下pin码接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 蓝牙钥匙更新指定pin码
     *
     * @param bleCaPinPo
     * @return
     */
    public BluetoothDownDto addSpecifiedBlePinMsg(BleCaPinPo bleCaPinPo, String tboxSign) {
        try {

            BluetoothDownDto downDto = commonSetting(bleCaPinPo.getBleDeviceId().toString());
            downDto.setCmdEnum(BluetoothCmdEnum.REGISTER_ONE_KEY);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.updateOnePinInfoDto(bleCaPinPo, tboxSign);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("更新特定pin码接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 拼装蓝牙钥匙查询数据包
     *
     * @return
     * @throws JsonProcessingException
     */
    public BluetoothDownDto createSpecifiedBleQueryMsg(String deviceId, Long bleId) {
        try {

            BluetoothDownDto downDto = commonSetting(deviceId);
            downDto.setCmdEnum(BluetoothCmdEnum.QUERY_ONE_KEY);

            /**
             *下发响应包
             */
            //downDto.getAckContentDto()

            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.querySpecifiedBleKeytDto(bleId);
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("拼装蓝牙钥匙查询特定蓝牙钥匙接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 拼装蓝牙钥匙查询数据包
     *
     * @return
     * @throws JsonProcessingException
     */
    public BluetoothDownDto createAllBleQueryMsg(String deviceId) {
        try {

            BluetoothDownDto downDto = commonSetting(deviceId);
            downDto.setCmdEnum(BluetoothCmdEnum.QUERY_ALL_KEY);
            /**
             * 消息中蓝牙钥匙主体信息
             */

            PSHContentDto pshContentDto = generateContent.queryAllBleKeytDto();
            downDto.setPshContentDto(pshContentDto);

            /**
             *ack
             */
            downDto.setAck(false);
            return downDto;
        } catch (Exception ex) {
            log.error("拼装蓝牙钥匙查询特定蓝牙钥匙接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }
//#####################################################################################################################
//########################信息反馈部分
//#####################################################################################################################

    /**
     * 车辆蓝牙信息上报响应
     *
     * @param vin
     * @param status
     * @return
     */
    public BluetoothDownDto bleDeviceInfoAckMsg(String vin, int status) {
        try {

            BluetoothDownDto downDto = commonSetting(vin);
            downDto.setCmdEnum(BluetoothCmdEnum.REGISTER_ONE_KEY);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            ACKContentDto ackContentDto = generateContent.ackStatusDto(status);
            /**
             *下发响应包
             */
            downDto.setAckContentDto(ackContentDto);

            /**
             *ack
             */
            downDto.setAck(true);
            return downDto;
        } catch (Exception ex) {
            log.error("车辆蓝牙信息上报响应接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }

    /**
     * 注销通知上报响应
     *
     * @param vin
     * @param status
     * @return
     */
    public BluetoothDownDto bleCancelBleAckMsg(String vin, Integer status, Integer businessSerialNum) {
        try {

            BluetoothDownDto downDto = commonSetting(vin);
            downDto.setCmdEnum(BluetoothCmdEnum.REGISTER_ONE_KEY);
            downDto.setBusinessSerialNum(businessSerialNum);

            /**
             * 消息中蓝牙钥匙主体信息
             */

            ACKContentDto ackContentDto = generateContent.ackStatusDto(status);
            ackContentDto.setStatus(status);
            /**
             *下发响应包
             */
            downDto.setAckContentDto(ackContentDto);

            /**
             *ack
             */
            downDto.setAck(true);
            return downDto;
        } catch (Exception ex) {
            log.error("注销通知上报响应接收tbox反馈时发生异常 [(0)]", ex);
        }
        return null;
    }


    private void addUpCommonData(BluetoothUpDto upDto) {
        String vin = "LLNC6ADB5JA047666";
        int businessSerialNum = abs(UUID.randomUUID().hashCode());
        int version = 1;
        long platformTime = Instant.now().toEpochMilli();

        upDto.setVin(vin);
        upDto.setBusinessSerialNum(businessSerialNum);
        upDto.setVersion(version);
        upDto.setPlatformTime(platformTime);
    }

    private int abs(int value) {
        return value >= 0 ? value : -value;
    }

    private BluetoothDownDto commonSetting(String vin) {
        BluetoothDownDto downDto = new BluetoothDownDto();

        int businessSerialNum = abs(UUID.randomUUID().hashCode());
        /**
         *下发信息对象
         */
        int version = 1;
        long platformTime = Instant.now().toEpochMilli();

        /**
         * 消息头信息
         */
        downDto.setVin(vin);
        downDto.setBusinessSerialNum(businessSerialNum);
        downDto.setVersion(version);
        downDto.setPlatformTime(platformTime);
        return downDto;
    }

}
