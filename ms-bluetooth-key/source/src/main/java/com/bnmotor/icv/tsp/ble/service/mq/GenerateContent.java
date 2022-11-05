package com.bnmotor.icv.tsp.ble.service.mq;

import com.bnmotor.icv.adam.core.utils.HexUtil;
import com.bnmotor.icv.adam.sdk.bluetooth.common.ACKContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.common.BluetoothContentDto;
import com.bnmotor.icv.adam.sdk.bluetooth.common.PSHContentDto;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleCaPinPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.service.BlePkiService;
import com.bnmotor.icv.tsp.ble.util.ByteUtil;
import com.bnmotor.icv.tsp.ble.util.RandomUnit;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

/**
 * @ClassName: GenerateContent
 * @Description: 封装读写kafka数据包
 * @author: shuqi1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class GenerateContent {

    @Resource
    private BlePkiService blePkiService;

    public  PSHContentDto generatePSHContentDto(byte[] src,byte[] sign) {

        BluetoothContentDto contentDto = new BluetoothContentDto();
        String magicHexstr = Hex.toHexString(src);
        contentDto.setContentHexStr(magicHexstr);
        contentDto.setContentId(1);

        List<BluetoothContentDto> contentDtos = new ArrayList<>();
        contentDtos.add(contentDto);

        contentDto = new BluetoothContentDto();
        String signHexstr = Hex.toHexString(sign);
        contentDto.setContentHexStr(signHexstr);
        contentDto.setContentId(2);
        contentDtos.add(contentDto);

        PSHContentDto pshContentDto = new PSHContentDto();
        pshContentDto.setContentDtos(contentDtos);

        return pshContentDto;
    }

    public  ACKContentDto generateACKContentDto(int status,byte[] src,byte[] sign) {
        ACKContentDto ackContentDto = new ACKContentDto();
        List<BluetoothContentDto> contentDtos = new ArrayList<>();

        BluetoothContentDto contentDto = new BluetoothContentDto();
        String magicHexstr = Hex.toHexString(src);
        contentDto.setContentHexStr(magicHexstr);
        contentDto.setContentId(1);
        contentDtos.add(contentDto);

        contentDto = new BluetoothContentDto();
        String signHexstr = Hex.toHexString(sign);
        contentDto.setContentHexStr(signHexstr);
        contentDto.setContentId(2);
        contentDtos.add(contentDto);

        ackContentDto.setContentDtos(contentDtos);
        ackContentDto.setStatus(status);
        return ackContentDto;
    }

    /**
     * 生成蓝牙钥匙申请记录
     *
     * @param userBleKeyPo
     * @param bleKeyGeneratorDto
     * @return
     */
    public  PSHContentDto createBleApplyDto(UserBleKeyPo userBleKeyPo, BleKeyGeneratorDto bleKeyGeneratorDto) {
        ByteBuffer allocate = ByteBuffer.allocate(54);
        byte[] result = bleKeyGeneratorDto.getTboxByteSrc();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(HexUtil.byte2HexStr(result));
        allocate.flip();
        allocate.clear();
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.clear();

        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }


    /**
     * 更新蓝牙钥匙F
     *
     * @param bleKeyGeneratorDto
     * @return
     */
    public  PSHContentDto replaceBleApplyDto(BleKeyGeneratorDto bleKeyGeneratorDto) {
        byte[] result = bleKeyGeneratorDto.getTboxByteSrc();
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        ByteBuffer allocateSign = ByteBuffer.allocate(signLength+2);
        allocateSign.put(ByteUtil.getBytes(signLength));
        allocateSign.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocateSign.array();
        allocateSign.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }


    /**
     * 删除某把蓝牙钥匙
     *
     * @param userBleKeyPo
     * @param userBleKeyPo
     * @return
     */
    public  PSHContentDto delBleOneBytes(UserBleKeyPo userBleKeyPo) {
        ByteBuffer allocate = ByteBuffer.allocate(12);
        int timestamp = Instant.now().atZone(ZoneId.of("+8")).getSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp);
        allocate.put(timeBytes);
        byte[] bleBytes = ByteUtil.getBytes(Long.valueOf(userBleKeyPo.getBleKeyId()));
        allocate.put(bleBytes);
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createTboxSignBase(allocate.array());
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        ByteBuffer tboxToSign = ByteBuffer.allocate(signLength+2);
        tboxToSign.put(ByteUtil.getBytes(signLength));
        tboxToSign.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = tboxToSign.array();
        tboxToSign.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }

    /**
     * 根据车辆ID删除改车下所有蓝牙钥匙
     *
     * @return
     */
    public  PSHContentDto delBleAllBytes() {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createTboxSignBase(result);
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        ByteBuffer alloToSign = ByteBuffer.allocate(signLength+2);
        alloToSign.put(ByteUtil.getBytes(signLength));
        alloToSign.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = alloToSign.array();
        alloToSign.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }

    /**
     * 更新蓝牙钥匙有效期
     *
     * @param userBleKeyPo
     * @return
     */
    public  PSHContentDto createUpdateBleExpireTimeDto(UserBleKeyPo userBleKeyPo) {
        ByteBuffer allocate = ByteBuffer.allocate(276);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);

        byte[] bleBytes = ByteUtil.getBytes(Long.valueOf(userBleKeyPo.getBleKeyId()));
        allocate.put(bleBytes);

        Long startSecond = userBleKeyPo.getBleKeyEffectiveTime().getTime() / 1000;
        allocate.put(ByteUtil.getBytes(startSecond.intValue()));
        if (userBleKeyPo.getBleKeyExpireTime().compareTo(Long.MAX_VALUE)== Constant.COMPARE_EQUAL_VALUE||
                userBleKeyPo.getBleKeyExpireTime().compareTo(9999999999000L)== Constant.COMPARE_EQUAL_VALUE) {
            byte[] signByte = new byte[4];
            Arrays.fill(signByte, (byte) 15);
            allocate.put(signByte);
        } else {
            Long endSecond = userBleKeyPo.getBleKeyExpireTime() / 1000;
            allocate.put(ByteUtil.getBytes(endSecond.intValue()));
        }
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(result);
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }

    /**
     * 更新蓝牙钥匙权限
     *
     * @param userBleKeyPo
     * @param authRight
     * @return
     */
    public  PSHContentDto createUpdateBleRightDto(UserBleKeyPo userBleKeyPo, Long authRight) {
        ByteBuffer allocate = ByteBuffer.allocate(20);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);

        byte[] bleBytes = ByteUtil.getBytes(Long.valueOf(userBleKeyPo.getBleKeyId()));
        allocate.put(bleBytes);

        allocate.put(ByteUtil.getBytes(authRight));
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createTboxSignBase(result);

        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }


    /**
     * 更新车辆下所有pin码
     *
     * @param bleCaPinPo
     * @return
     */
    public  PSHContentDto createPinInfoDto(BleCaPinPo bleCaPinPo) {
        ByteBuffer allocate = ByteBuffer.allocate(13);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);
        int userType = bleCaPinPo.getUserTypeId().intValue();
        allocate.put((byte) userType);
        allocate.put(ByteUtil.getBytes(Long.valueOf(bleCaPinPo.getPinCode())));
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createTboxSignBase(result);
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        ByteBuffer allocateSign = ByteBuffer.allocate(signLength+2);
        allocateSign.put(ByteUtil.getBytes(signLength));
        allocateSign.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocateSign.array();
        allocateSign.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }

    /**
     * 更新车辆下所有pin码
     *
     * @param bleCaPinPoList
     * @return
     */
    public  PSHContentDto createDevicePinInfoDto(List<BleAckPushPo> bleCaPinPoList) {
        ByteBuffer allocate = ByteBuffer.allocate(94);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);

        bleCaPinPoList.stream().forEach(elem -> {
            int userType = elem.getUserTypeId().intValue();
            allocate.put((byte) userType);
            allocate.put(ByteUtil.getBytes(Long.valueOf(elem.getPinCode())));
        });
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(allocate.array());

        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        ByteBuffer allocateSign = ByteBuffer.allocate(signLength+2);
        allocateSign.put(ByteUtil.getBytes(signLength));
        allocateSign.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocateSign.array();
        allocateSign.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }

    /**
     * 更新车辆下某个pin码
     *
     * @param bleCaPinPo
     * @return
     */
    public  PSHContentDto updateOnePinInfoDto(BleCaPinPo bleCaPinPo, String tboxsign) {
        ByteBuffer allocate = ByteBuffer.allocate(269);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);

        int userType = bleCaPinPo.getUserTypeId().intValue();
        allocate.put((byte) userType);
        allocate.put(bleCaPinPo.getPinCode().getBytes());

        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(allocate.array());

        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }

    /**
     * 查询指定蓝牙钥匙
     *
     * @param bleId
     * @return
     */
    public  PSHContentDto querySpecifiedBleKeytDto(Long bleId) {
        ByteBuffer allocate = ByteBuffer.allocate(268);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);

        byte[] bleBytes = ByteUtil.getBytes(bleId);
        allocate.put(bleBytes);

        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(allocate.array());

        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }


    /**
     * 查询车辆下所有蓝牙钥匙
     *
     * @return
     */
    public  PSHContentDto queryAllBleKeytDto() {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createTboxSignBase(result);

        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.clear();
        PSHContentDto pshContentDto = generatePSHContentDto(result,resultSign);
        return pshContentDto;
    }


    /**
     * 查询车辆下所有蓝牙钥匙
     *
     * @return
     */
    public  ACKContentDto ackStatusDto(int status) {
        ByteBuffer allocate = ByteBuffer.allocate(4);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);
        byte[] result = allocate.array();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(result);
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] signResult = allocate.array();
        allocate.flip();
        ACKContentDto ackContentDto = generateACKContentDto(status,result,signResult);
        return ackContentDto;
    }

    /**
     * 查询车辆下所有蓝牙钥匙
     *
     * @return
     */
    public  ACKContentDto ackUpdateSpecifiedBlePin(byte[] userType) {
        ByteBuffer allocate = ByteBuffer.allocate(13);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);
        allocate.put(userType);
        String pinCode = RandomUnit.generateRandom(18, 1);
        byte[] pinByte = ByteUtil.getBytes(Long.valueOf(pinCode));
        allocate.put(pinByte);
        byte[] result = allocate.array();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(result);
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocate.array();
        allocate.flip();
        ACKContentDto ackContentDto = generateACKContentDto(1,result,resultSign);
        return ackContentDto;
    }

    /**
     * 查询车辆下所有蓝牙钥匙
     *
     * @return
     */
    public ACKContentDto ackUpdateDeviceBlePin(int status,List<BleCaPinPo> bleCaPinPoList) {
        ByteBuffer allocate = ByteBuffer.allocate(94);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeBytes = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeBytes);
        for (BleCaPinPo bleCaPinPo : bleCaPinPoList) {
            byte userTypeByte = (byte) bleCaPinPo.getUserTypeId().intValue();
            allocate.put(userTypeByte);
            byte[] pinByte = ByteUtil.getBytes(Long.valueOf(bleCaPinPo.getPinCode()));
            allocate.put(pinByte);
        }
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        BleKeyGeneratorDto bleKeyGeneratorDto = blePkiService.createBleUpdateExpireSign(allocate.array());
        short signLength = (short)(bleKeyGeneratorDto.getTboxSign().length);
        ByteBuffer allocateSign = ByteBuffer.allocate(signLength+2);
        allocateSign.put(ByteUtil.getBytes(signLength));
        allocateSign.put(bleKeyGeneratorDto.getTboxSign());
        byte[] resultSign = allocateSign.array();
        allocateSign.clear();
        ACKContentDto ackContentDto = generateACKContentDto(1,result,resultSign);
        return ackContentDto;
    }


    public  ACKContentDto ackReceiveStatus(int status) {
        ByteBuffer allocate = ByteBuffer.allocate(5);
        Long timestamp = Instant.now().atZone(ZoneId.of("+8")).toEpochSecond();
        byte[] timeByte = ByteUtil.getBytes(timestamp.intValue());
        allocate.put(timeByte);
        byte[] signByte = new byte[1];
        Arrays.fill(signByte, (byte) status);
        allocate.put(signByte);
        byte[] result = allocate.array();
        allocate.flip();
        allocate.clear();
        byte[] tboxSign = blePkiService.createTboxSignBase(result).getTboxSign();
        short signLength = (short)(tboxSign.length);
        allocate = ByteBuffer.allocate(signLength+2);
        allocate.put(ByteUtil.getBytes(signLength));
        allocate.put(tboxSign);
        byte[] resultSign = allocate.array();
        ACKContentDto ackContentDto = generateACKContentDto(1,result,resultSign);
        return ackContentDto;
    }
}
