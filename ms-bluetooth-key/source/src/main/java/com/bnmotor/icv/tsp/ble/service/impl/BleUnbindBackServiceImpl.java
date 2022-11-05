package com.bnmotor.icv.tsp.ble.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ble.common.RespCode;
import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import com.bnmotor.icv.tsp.ble.mapper.BleUnbindBackMapper;
import com.bnmotor.icv.tsp.ble.model.entity.BleAckPushPo;
import com.bnmotor.icv.tsp.ble.model.entity.BleUnbindBackPo;
import com.bnmotor.icv.tsp.ble.model.request.feign.BleVerifyAuthCodeDto;
import com.bnmotor.icv.tsp.ble.service.BleUnbindBackService;
import com.bnmotor.icv.tsp.ble.service.feign.BleCommonFeignService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: TspBleAuthCompe
 * @Description: 查询权限信息
 * @author shuqi1
 * @since 2020-07-20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class BleUnbindBackServiceImpl extends ServiceImpl<BleUnbindBackMapper, BleUnbindBackPo> implements BleUnbindBackService {
    @Resource
    private BleUnbindBackMapper bleUnbindBackMapper;
    @Resource
    private BleCommonFeignService bleCommonFeignService;

    @Override
    public BleUnbindBackPo queryUnbindBack(BleUnbindBackPo bleUnbindBackPo) {
        val unbindBackPo = bleUnbindBackMapper.queryUnbindBack(bleUnbindBackPo);
        if (!Optional.ofNullable(unbindBackPo).isPresent()){
            return BleUnbindBackPo.builder().build();
        }
        return unbindBackPo;
    }

    @Override
    public List<BleUnbindBackPo> queryUnbindByStausList(BleUnbindBackPo bleUnbindBackPo) {
        List<BleUnbindBackPo> bleUnbindBackPos = bleUnbindBackMapper.queryUnbindByStausList(bleUnbindBackPo);
        return bleUnbindBackPos;
    }

    @Override
    public void addBleUnbind(BleAckPushPo bleAckPushPo) {
        if (bleAckPushPo.getStatus()==1)
        {
            LocalDateTime dt  = LocalDateTime.now();
            Date now = Date.from(dt.toInstant(ZoneOffset.of("+8")));
            BleVerifyAuthCodeDto bleVerifyAuthCodeDto = BleVerifyAuthCodeDto.builder()
                    .vin(bleAckPushPo.getDeviceId())
                    .userId(bleAckPushPo.getOwnerUserId())
                    .respCode(bleAckPushPo.getStatus())
                    .build();
            String s = bleCommonFeignService.delBlueKeyCallback(bleVerifyAuthCodeDto);
            if (!s.equals(RespCode.SUCCESS.getValue())){
                BleUnbindBackPo build = BleUnbindBackPo.builder()
                        .projectId(String.valueOf(bleAckPushPo.getProjectId()))
                        .deviceId(bleAckPushPo.getDeviceId())
                        .respCode(String.valueOf(bleAckPushPo.getStatus()))
                        .userId(bleAckPushPo.getOwnerUserId())
                        .serialNum(String.valueOf(bleAckPushPo.getSerialNum()))
                        .createBy(bleAckPushPo.getCreateBy())
                        .createTime(now)
                        .build();
                bleUnbindBackMapper.addBleUnbind(build);
            }
        }
    }

    @Override
    public int updateBleUnbind(BleUnbindBackPo bleUnbindBackPo) {
        int rows = bleUnbindBackMapper.updateBleUnbind(bleUnbindBackPo);
        return rows;
    }

    @Override
    public int delBleUnbind(BleUnbindBackPo bleUnbindBackPo) {
        int rows = bleUnbindBackMapper.delBleUnbind(bleUnbindBackPo);
        return rows;
    }

    @Override
    public void syncCallSuccessAndDel(BleUnbindBackPo bleUnbindBackPo) {
        List<BleUnbindBackPo> bleUnbindBackPos = queryUnbindByStausList(bleUnbindBackPo);
        bleUnbindBackPos.forEach(elem->{
            BleVerifyAuthCodeDto bleVerifyAuthCodeDto = BleVerifyAuthCodeDto.builder()
                    .respCode(Constant.TBOX_DEL_SUCCESS)
                    .userId(bleUnbindBackPo.getUserId())
                    .vin(bleUnbindBackPo.getDeviceId())
                    .build();
            String code = bleCommonFeignService.delBlueKeyCallback(bleVerifyAuthCodeDto);
            if (code.equals(RespCode.SUCCESS.getValue())){
                delBleUnbind(elem);
            }
        });
    }
}
