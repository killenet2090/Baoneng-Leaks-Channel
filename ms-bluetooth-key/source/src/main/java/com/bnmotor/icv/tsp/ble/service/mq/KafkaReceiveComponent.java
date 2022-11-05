package com.bnmotor.icv.tsp.ble.service.mq;

import com.bnmotor.icv.adam.sdk.bluetooth.common.ACKContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.BluetoothTypeEnum;
import com.bnmotor.icv.adam.sdk.bluetooth.enums.cmd.BluetoothCmdEnum;
import com.bnmotor.icv.adam.sdk.bluetooth.handler.IBluetoothUpHandler;
import com.bnmotor.icv.adam.sdk.bluetooth.up.BluetoothUpDto;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName: KafkaReceiveComponent
 * @Description: 读写kafka数据
 * @author: shuqi1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Component
public class KafkaReceiveComponent implements IBluetoothUpHandler {
    @Autowired
    private ACKService ackService;

    @Resource
    private BleAckSyncdbService bleAckSyncdbService;

    @Resource
    private BleAckInfoService bleAckInfoService;

    @Resource
    private BleSyncKafkaService bleSyncKafkaService;

    @Override
    public void onMessage(BluetoothUpDto bluetoothUpDto) {
        if (bluetoothUpDto.getVin().equals("00000000000000000")){
            return;
        }
        log.info("kafka  onMessage===========================================");
        log.info(bluetoothUpDto.toString());
        BluetoothCmdEnum cmd = bluetoothUpDto.getCmdEnum();
        boolean isAck = bluetoothUpDto.isAck();
        log.info(bluetoothUpDto.getVin());
        if (isAck) {
            Integer businessSerialNum = bluetoothUpDto.getBusinessSerialNum();
            BluetoothTypeEnum typeEnum = cmd.getTypeEnum();
            int ackCmd = cmd.getCmd();
            ACKContentDto contentDto = bluetoothUpDto.getAckContentDto();
            int status = contentDto.getStatus();
            if (cmd == BluetoothCmdEnum.UPDATE_PINS){
                List<BleAckPushPo> bleAckPushPoList = bleSyncKafkaService.CombineMsgsToPush(
                        bluetoothUpDto.getVin(), businessSerialNum, typeEnum, ackCmd);
                bleSyncKafkaService.updateDeviceIdAllBlePin(contentDto,bleAckPushPoList);
                return;
            }
            BleAckPushPo bleAckPushPo = bleSyncKafkaService.CombineMsgToPush(bluetoothUpDto.getVin(),
                    businessSerialNum, typeEnum, ackCmd);
            log.info("cmd={0}",cmd);
            if (!Optional.ofNullable(bleAckPushPo).isPresent()){
                log.info("接收到tbox的反馈，根据反馈的信息查询数据的数据为null,反馈的数据为={}",bluetoothUpDto.toString());
                return;
            }
            switch (cmd) {
                case REGISTER_ONE_KEY:
                    bleSyncKafkaService.ackUpdateRegisterBlekey(contentDto,bleAckPushPo);
                    break;
                case DELETE_ONE_KEY:
                    bleSyncKafkaService.ackDelSpecifiedBlekey(contentDto,bleAckPushPo);
                    break;
                case DELETE_ALL_KEYS:
                    bleSyncKafkaService.ackDelAllBlekey(contentDto,bleAckPushPo);
                    break;
                case UPDATE_ONE_SECRET:
                    bleSyncKafkaService.ackUpdateSpecifiedBlekeySecret(contentDto,bleAckPushPo);
                    break;
                case MODIFY_DATE_KEYS:
                    bleSyncKafkaService.ackUpdateSpecifiedBleLimitDate(contentDto,bleAckPushPo);
                    break;
                case QUERY_ONE_KEY:
                    bleSyncKafkaService.ackQuerySpecifiedBlekey(contentDto,bleAckPushPo);
                    break;
                case QUERY_ALL_KEY:
                    bleSyncKafkaService.ackQueryDeviceIdAllBlekey(contentDto,bleAckPushPo);
                    break;
                case UPDATE_ONE_PIN:
                    bleSyncKafkaService.ackUpdateSpecifiedBlePin(contentDto,bleAckPushPo);
                    break;
                case MODIFY_AUTHORITY_KEYS:
                    bleSyncKafkaService.ackUpdateSpecifidBleAuthrity(contentDto,bleAckPushPo);
                    break;
                case UNKNOWN:
                    break;
                default:
                    logStatusInfo("onMessage enter default branch", contentDto.toString(), status);
                    break;
            }
            log.info("received: {}", bluetoothUpDto);
            bleAckSyncdbService.cleanAckPushData(bleAckPushPo);
        } else {
            BluetoothDownDto downDto = ackService.generateDownDto(bluetoothUpDto);
            switch (cmd) {
                case UNKNOWN:
                    break;
                case ADD_PIN_LIST:
                    bleSyncKafkaService.buildCreateDeviceIdAllBlePin(downDto);
                    break;
                case REPORT_BLUETOOTH_INFO:
                    bleSyncKafkaService.buildUpReportInfo(downDto);
                    break;
                case QUERY_LIST_REPORT:
                    bleSyncKafkaService.buildBleKeyReport(downDto);
                    break;
                case REPORT_CANCEL_NOTICE:
                    bleSyncKafkaService.buildCancelBleKeyId(downDto);
                    break;
                case CONNECTION_STATUS:
                    bleSyncKafkaService.buildConnectionStatus(downDto);
                    break;
                case OFFLINE_DELETE_NOTICE:
                    bleSyncKafkaService.buildBleKeyDel(downDto);
                    break;
                case OFFLINE_ACTIVE_NOTICE:
                    bleSyncKafkaService.buildBleKeyOffLineActive(downDto);
                default:
                    logStatusInfo("onMessage enter default branch", downDto.toString(), 1);
                    break;
            }
            log.info("received: {}", bluetoothUpDto);
            bleAckInfoService.moveAckInfoData();
        }
    }

    private void logStatusInfo(String funcName, String msg, int status) {
        if (status == 1) {
            log.info("received success resp with func {} and msg body was {}, status is {}", funcName, msg, status);
        } else {
            log.info("received fault resp with func {} and msg body was {} , status is {}", funcName, msg, status);
        }
    }

}
