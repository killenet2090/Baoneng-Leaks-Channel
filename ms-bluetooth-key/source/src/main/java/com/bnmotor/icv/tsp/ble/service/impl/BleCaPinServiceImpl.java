package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.sdk.bluetooth.down.BluetoothDownDto;
import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.BleAckPushMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleCaPinPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleCaPinDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleDevicePinDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleDevicePinVo;
import com.bnmotor.icv.tsp.ble.mapper.BleCaPinMapper;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.BleCaPinService;
import com.bnmotor.icv.tsp.ble.service.BleUserService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.service.mq.BleKafkaPushMsg;
import com.bnmotor.icv.tsp.ble.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.BLE_KEY_CREATE_SEND_TBOX_ERR;

/**
 * @ClassName: BleCaPinServiceImpl
 * @Description: 蓝牙钥匙证书pin码业务DAO实现类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
public class BleCaPinServiceImpl extends ServiceImpl<BleCaPinMapper, BleCaPinPo> implements BleCaPinService {
    @Resource
    BleUserService bleUserService;

    @Resource
    BleCaPinMapper bleCaPinMapper;

    @Resource
    BleKafkaPushMsg bleKafkaPushMsg;

    @Resource
    BluetoothDownService bluetoothDownService;

    @Resource
    BleAckPushMapper bleAckPushMapper;

    @Resource
    BleCommonFeignService bleCommonFeignService;

    @Override
    /**
     * 下发证书和生成蓝牙
     */
    @Transactional(rollbackFor = Exception.class)
    public BleCaPinPo generateCaPinInfo(BleCaPinDto bleCaPinDto, String userId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo response = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        BleCaPinPo bleCaPinPo = BleCaPinPo.builder().build();
        BeanUtils.copyProperties(bleCaPinDto, bleCaPinPo);
        String pinCode = RandomUnit.generateRandom(18, 1);
        bleCaPinPo.setPinCode(pinCode);
        bleCaPinPo.setUserTypeId(Long.valueOf(bleCaPinDto.getUserType()));
        bleCaPinPo.setDelFlag(Constant.INIT_DEL_FLAG);
        bleCaPinPo.setBleDeviceId(bleCaPinDto.getDeviceId());
        bleCaPinPo.setProjectId(bleCaPinDto.getProjectId());
        bleCaPinPo.setCreateTime(now);
        bleCaPinPo.setUpdateTime(now);
        bleCaPinPo.setVersion(Constant.INIT_VERSION);
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.addOrUpdateBlePinMsg(bleCaPinPo);
            //维护消息推送表
            BleAckPushPo build = BleAckPushPo.builder()
                    .serialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()))
                    .registrationId(response.getPushRid())
                    .cmdType(bluetoothDownDto.getCmdEnum().getTypeEnum().getType())
                    .cmdOrder(bluetoothDownDto.getCmdEnum().getCmd())
                    .projectId(Long.valueOf(bleCaPinDto.getProjectId()))
                    .deviceId(bleCaPinDto.getDeviceId())
                    .userTypeId(bleCaPinPo.getUserTypeId())
                    .pinCode(pinCode)
                    .status(Constant.INIT_STATUS)
                    .version(Constant.INIT_VERSION)
                    .delFag(Constant.INIT_DEL_FLAG)
                    .ownerUserId(userId)
                    .createBy(userName)
                    .createTime(now)
                    .build();
            int result = bleAckPushMapper.addBleAckPush(build);
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_CREATE_SEND_TBOX_ERR);
        }

        return bleCaPinPo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<BleCaPinPo> generateDevicePinInfo(Optional<List<BleCaPinPo>> optionalBleCaPinPoList, BleDevicePinDto bleDevicePinDto, String userId) {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo response = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        List<BleAckPushPo> bleCaPinPoList = new ArrayList<>();
        for (int i = 1; i < Constant.CA_PIN_LIMIT; i++) {
            String pinCode = RandomUnit.generateRandom(18, 1);
            BleAckPushPo bleAckPushPo = BleAckPushPo.builder()
                    .projectId(Long.valueOf(bleDevicePinDto.getProjectId()))
                    .deviceId(bleDevicePinDto.getDeviceId()).
                            userTypeId(Long.valueOf(i))
                    .pinCode(pinCode).createBy(userName)
                    .ownerUserId(userId)
                    .createTime(now)
                    .updateBy(userName)
                    .cmdType(Constant.UPDATE_DEVICE_PIN_TYPE)
                    .cmdOrder(Constant.UPDATE_DEVICE_PIN_CMD)
                    .updateTime(now)
                    .status(Constant.INIT_STATUS)
                    .delFag(Constant.INIT_STATUS).build();
            bleCaPinPoList.add(bleAckPushPo);
        }
        try {
            //mqtt 推送
            BluetoothDownDto bluetoothDownDto = bleKafkaPushMsg.updateDeviceBlePinMsg(bleCaPinPoList);
            bleCaPinPoList.forEach(elem -> {
                elem.setSerialNum(Long.valueOf(bluetoothDownDto.getBusinessSerialNum()));
                bleAckPushMapper.addBleAckPush(elem);
            });
            bluetoothDownService.sendBluetoothDownMsg(bluetoothDownDto);
        } catch (JsonProcessingException ex) {
            log.error("申请蓝牙钥匙下发时发生错误，错误信息为[()]", ex);
            throw new AdamException(BLE_KEY_CREATE_SEND_TBOX_ERR);
        }
        if (optionalBleCaPinPoList.isPresent()) {
            return optionalBleCaPinPoList.get();
        } else {
            return new ArrayList<BleCaPinPo>();
        }
    }

    @Override
    /**
     * 根据设备ID查询蓝牙
     */
    public BleCaPinPo queryPinInfo(BleCaPinDto bleCaPinDto) {
        QueryWrapper<BleCaPinPo> queryWrapper = new QueryWrapper<BleCaPinPo>();
        queryWrapper.lambda().eq(BleCaPinPo::getUserTypeId, bleCaPinDto.getUserType());
        queryWrapper.lambda().eq(BleCaPinPo::getBleDeviceId, bleCaPinDto.getDeviceId());
        queryWrapper.lambda().eq(BleCaPinPo::getProjectId, bleCaPinDto.getProjectId());
        Optional<BleCaPinPo> tspBleCaPinPo = Optional.ofNullable(bleCaPinMapper.selectOne(queryWrapper));
        BleCaPinPo result = tspBleCaPinPo.isPresent() ? tspBleCaPinPo.get() : BleCaPinPo.builder().build();
        return result;
    }

    @Override
    public List<BleCaPinPo> queryDevicePinInfo(BleDevicePinDto bleDevicePinDto) {
        BleCaPinPo bleCaPinPo = BleCaPinPo.builder().projectId(bleDevicePinDto.getProjectId())
                .bleDeviceId(bleDevicePinDto.getDeviceId())
                .status(Constant.ACTIVE_STATUS).build();
        List<BleCaPinPo> bleCaPinPoList = bleCaPinMapper.queryBlePins(bleCaPinPo);
        return bleCaPinPoList;

    }

    @Override
    public List<BleCaPinPo> blekeyCaPinQuery(String deviceId) {
        QueryWrapper<BleCaPinPo> queryWrapper = new QueryWrapper<BleCaPinPo>();
        Map<String, Object> params = new ConcurrentHashMap<>(1);
        params.put("ble_device_id", deviceId);
        queryWrapper.allEq(params);
        List<BleCaPinPo> oneBleCaPins = bleCaPinMapper.selectList(queryWrapper);
        return oneBleCaPins;
    }

    @Override
    public Integer addCaPinInfo(BleCaPinPo BleCaPinPo) {
        int rows = bleCaPinMapper.addBlePin(BleCaPinPo);
        return rows;
    }

    @Override
    public int delCaPinInfo(BleCaPinDto bleCaPinDto) {
        return 0;
    }

    @Override
    public BleCaPinPo updateCaPinInfo(BleCaPinPo bleCaPinPo) {
        int rows = bleCaPinMapper.addBlePin(bleCaPinPo);
        if (rows > 0) {
            return bleCaPinPo;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateDevicePinInfo(List<BleCaPinPo> bleCaPinPoList) {
        for (BleCaPinPo bleCaPinPo : bleCaPinPoList) {
            int rows = bleCaPinMapper.addBlePin(bleCaPinPo);
            if (rows == 0) {
                return rows;
            }
        }
        return bleCaPinPoList.size();
    }

    @Override
    public BleDevicePinVo queryBleDevicePins(BleCaPinPo bleCaPinPo) {
        BleDevicePinVo bleDevicePinVo = bleCaPinMapper.queryBleDevicePins(bleCaPinPo);
        return bleDevicePinVo;
    }


}
