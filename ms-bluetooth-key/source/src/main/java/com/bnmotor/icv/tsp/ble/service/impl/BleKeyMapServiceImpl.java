package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.utils.DateUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.*;
import com.bnmotor.icv.tsp.ble.model.entity.*;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyModifyAuthDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyModifyDateDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyModifyNameDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleModDevNameDto;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyAuthVo;
import com.bnmotor.icv.tsp.ble.model.response.ble.BleKeyModifyVo;
import com.bnmotor.icv.tsp.ble.model.response.user.UserPhoneVo;
import com.bnmotor.icv.tsp.ble.service.*;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import com.bnmotor.icv.tsp.ble.util.BleKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.*;
import static com.bnmotor.icv.tsp.ble.common.enums.BizExceptionEnum.BLE_KEY_MODIFY_WITH_CHECK_PROCESS_ERROR;

/**
 * @ClassName: BleReviseServiceImpl
 * @Description: 蓝牙钥匙修改业务DAO实现类
 * @author: shuqi1
 * @date: 2020/6/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Service
public class BleKeyMapServiceImpl extends ServiceImpl<BleKeyMapMapper, BleKeyServicePo> implements BleKeyMapService {
    @Resource
    private BleUserService bleUserService;

    @Resource
    private BleKeyMapMapper bleKeyMapMapper;

    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Resource
    private BleKeyUserMapper bleKeyUserMapper;

    @Resource
    private BleAuthCompeService bleAuthCompeService;

    @Resource
    private BleTboxService bleTboxService;

    @Override
    public BleKeyModifyAuthVo reviseBleKeyAuth(BleKeyModifyAuthDto bleKeyModifyAuthDto, String projectId,String userId) {
        LocalDateTime dt  = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserPhoneVo userVo = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);

        String collect = bleKeyModifyAuthDto.getAuthList().stream().map(String::valueOf).collect(Collectors.joining(","));
        UserBleKeyPo userBleKeyCondition = UserBleKeyPo.builder()
                .projectId(projectId)
                .bleKeyId(bleKeyModifyAuthDto.getBleKeyId())
                .ownerUserId(userId)
                .bleKeyStatus(Constant.ACTIVE_STATUS)
                .delFlag(Constant.INIT_DEL_FLAG).build();
        UserBleKeyPo userBleKeyPo = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyCondition);
        BleKeyModifyAuthVo bleKeyModifyAuthVo = new BleKeyModifyAuthVo();
        /**
         * 删除权限映射表
         */

        //查询权限
        Long bleRight = BleKeyUtil.generateRight(bleKeyModifyAuthDto.getAuthList());
        log.info(bleRight.toString());
        //mqtt 推送
        bleTboxService.reviseBleKeyAuth(userBleKeyPo,now,userVo.getPushRid(),collect,bleRight);

        Map<Long, String> map = bleAuthCompeService.queryLimitCompe(bleKeyModifyAuthDto.getAuthList());
        bleKeyModifyAuthVo.setBleKeyId(bleKeyModifyAuthDto.getBleKeyId());
        bleKeyModifyAuthVo.setAuthList(map);
        bleKeyModifyAuthVo.setDeviceId(userBleKeyPo.getDeviceId());
        return bleKeyModifyAuthVo;
    }

    @Override
    public BleKeyModifyVo queryReviseBleKeyInfo(UserBleKeyPo userBleKeyPo) {
        UserBleKeyPo modifyQuery = bleKeyMapMapper.queryBleKeyInfo(userBleKeyPo);
        BleKeyModifyVo build =  BleKeyModifyVo.builder().build();
        BeanUtils.copyProperties(modifyQuery,build);
        build.setUserId(modifyQuery.getOwnerUserId());
        build.setUserName(bleUserService.getUserName(modifyQuery.getOwnerUserId()).getNickname());
        build.setMobileDeviceId(modifyQuery.getUsedUserMobileDeviceId());
        build.setBleSkeyExpireTime(modifyQuery.getBleKeyExpireTime());
        return build;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserBleKeyPo updateDbExpireDate(BleKeyModifyDateDto bleKeyModifyDateDto, String projectId, String  userId) throws ParseException {
        String userName = bleUserService.getUserName(userId).getNickname();
        UserPhoneVo response = bleCommonFeignService.bleGetFeignUserPhoneVo(userId);
        LocalDateTime dt  = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder()
                .projectId(projectId)
                .deviceId(bleKeyModifyDateDto.getDeviceId())
                .bleKeyId(bleKeyModifyDateDto.getBleKeyId())
                .build();
        userBleKeyPo = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
        userBleKeyPo.setBleKeyEffectiveTime(DateUtil.parseStringToDate(bleKeyModifyDateDto.getBleEffectiveTime(),DateUtil.TIME_MASK_DEFAULT));
        userBleKeyPo.setBleKeyExpireTime(DateUtil.parseStringToDate(bleKeyModifyDateDto.getBleKeyExpireTime(),DateUtil.TIME_MASK_DEFAULT).getTime());
        bleTboxService.updateBleExpireDate(userBleKeyPo,now,response.getPushRid(),userId,userName);
        return userBleKeyPo;
    }

    @Override
    public boolean updateDbdeviceName(BleModDevNameDto bleModDevNameDto, String projectId, String  userId)  {
        LocalDateTime dt  = LocalDateTime.now();
        Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
        String userName = bleUserService.getUserName(userId).getNickname();
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder().projectId(projectId)
                .deviceId(bleModDevNameDto.getDeviceId())
                .deviceName(bleModDevNameDto.getDeviceName())
                .updateTime(now)
                .updateBy(userName)
                .build();
        int count = bleKeyUserMapper.updateBleNameByBleId(userBleKeyPo);
        if(count==0){
            throw new AdamException(BLE_KEY_DEVICENAME_UPDATE_PROCESS_ERROR);
        }
        return true;
    }

    @Override
    public BleKeyModifyAuthVo updateDbBleKeyName(BleKeyModifyNameDto bleKeyModifyNameDto, String projectId, String  userId)  {
        String userName = bleUserService.getUserName(userId).getNickname();
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder().projectId(projectId)
                .deviceId(bleKeyModifyNameDto.getDeviceId())
                .bleKeyId(bleKeyModifyNameDto.getBleKeyId())
                .bleKeyName(bleKeyModifyNameDto.getBleKeyName())
                .updateBy(userName)
                .build();
        int count = bleKeyUserMapper.updateBleNameByBleId(userBleKeyPo);
        if(count==0){
            throw new AdamException(BLE_KEY_USERNAME_UPDATE_PROCESS_ERROR);
        }
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);

        BleKeyModifyAuthVo userBleKeyVo = new BleKeyModifyAuthVo();
        userBleKeyVo.setDeviceId(userBleKeyPoQuery.getDeviceId());
        userBleKeyVo.setBleKeyId(userBleKeyPoQuery.getBleKeyId());
        userBleKeyVo.setBleKeyName(userBleKeyPoQuery.getBleKeyName());
        userBleKeyVo.setAuthList(null);
        return userBleKeyVo;
    }
    @Override
    public void checkCanModify(String deviceId,String bleKeyId, String projectId, String uid) {
        UserBleKeyPo userBleKeyPo = UserBleKeyPo.builder().projectId(projectId).bleKeyId(bleKeyId).build();
        if (!"".equals(deviceId)){
            userBleKeyPo.setDeviceId(deviceId);
        }
        UserBleKeyPo userBleKeyPoQuery = bleKeyUserMapper.queryBleKeyInfoByPrimary(userBleKeyPo);
        if (userBleKeyPoQuery==null){
            throw new AdamException(BLE_KEY_MODIFY_WITH_CHECK_PROCESS_ERROR);
        }
        if (userBleKeyPoQuery.getBleKeyStatus().equals(Constant.INIT_STATUS)){
            throw new AdamException(RespCode.AUTH_NOT_ALLOW_MODIFY.getValue(), RespCode.AUTH_NOT_ALLOW_MODIFY.getDescription());
        }
    }

}
