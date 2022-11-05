package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.sdk.bluetooth.service.BluetoothDownService;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.BleAckInfoMapper;
import com.bnmotor.icv.tsp.ble.mapper.BleAuthMapper;
import com.bnmotor.icv.tsp.ble.mapper.BleKeyUserMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckInfoPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleAuthPo;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.service.BleAckInfoService;
import com.bnmotor.icv.tsp.ble.service.mq.BleKafkaPushMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * @author shuqi1
 * @ClassName: TspBleAckInfo
 * @Description: 服务实现类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-09-16
 */

@Service
@Slf4j
public class BleAckInfoServiceImpl extends ServiceImpl<BleAckInfoMapper, BleAckInfoPo> implements BleAckInfoService {
    @Resource
    private BleAckInfoMapper bleAckInfoMapper;

    @Resource
    private BleAuthMapper bleAuthMapper;

    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int moveAckInfoData() {
        LocalDateTime dt = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        List<BleAckInfoPo> bleAckInfoPosActive = bleAckInfoMapper.queryActiveBleAckInfoList();
        bleAckInfoPosActive.forEach(elem->{
            bleAckInfoMapper.moveBleAckInfoToHis(elem);
            bleAckInfoMapper.deleteBleAckInfo(elem);
        });
        List<BleAckInfoPo> bleAckInfoPosDel = bleAckInfoMapper.queryDelBleAckInfoList();
        bleAckInfoPosDel.stream().forEach(elem -> {
            UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder().deviceId(elem.getDeviceId()).bleKeyId(String.valueOf(elem.getBleKeyId())).build();
            UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
            userBleKeyPo.setBleKeyStatus(Constant.CANCEL_STATUS);
            userBleKeyPo.setDelFlag(Constant.CANCEL_DEL_FLAG);
            userBleKeyPo.setBleKeyDestroyTime(now);
            log.info(userBleKeyPo.toString());
            bleKeyUserMapper.updateBleKeyDestroy(userBleKeyPo);
            userBleKeyPo.setBleKeyDestroyTime(null);
            bleKeyUserMapper.moveBlekeyHisData(userBleKeyPo);
            int delCount =bleKeyUserMapper.deleteBlekeyHisData(userBleKeyPo);
            if (delCount==Constant.COMPARE_EQUAL_VALUE){
                log.info("-------------------------------1111111111111");
                //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
            if (userBleKeyPoQuery != null && userBleKeyPoQuery.getBleAuthId() != null) {
                BleAuthPo bleAuthPo = BleAuthPo.builder()
                        .id(userBleKeyPoQuery.getBleAuthId())
                        .deviceId(elem.getDeviceId())
                        .status(Constant.CANCEL_STATUS)
                        .delFlag(Constant.CANCEL_DEL_FLAG).build();
                bleAuthMapper.updateBleAuth(bleAuthPo);
                bleAuthMapper.moveAuthHisData(bleAuthPo);
                delCount=bleAuthMapper.deleteAuthHisData(bleAuthPo);
                if (delCount==Constant.COMPARE_EQUAL_VALUE){
                    log.info("-------------------------------2222222222222222");
                    //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
            bleAckInfoMapper.moveBleAckInfoToHis(elem);
            bleAckInfoMapper.deleteBleAckInfo(elem);
        });
        return 0;
    }

}
