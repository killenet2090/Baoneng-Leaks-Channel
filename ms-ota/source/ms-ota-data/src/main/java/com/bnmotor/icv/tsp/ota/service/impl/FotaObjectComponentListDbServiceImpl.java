package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaObjectComponentListMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectComponentListPo;
import com.bnmotor.icv.tsp.ota.service.IFotaObjectComponentListDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName: FotaObjectComponentListPo
 * @Description: OTA升级对象零件表,用于接收车辆零件信息同步数据 服务实现类
 * @author xuxiaochang1
 * @since 2020-12-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaObjectComponentListDbServiceImpl extends ServiceImpl<FotaObjectComponentListMapper, FotaObjectComponentListPo> implements IFotaObjectComponentListDbService {

    @Override
    public void delByFotaObjectIdPysical(Long otaObjectId) {
        baseMapper.delByFotaObjectIdPysical(otaObjectId);
    }

    @Override
    public List<FotaObjectComponentListPo> listByObjectId(Long objectId) {
       if(Objects.nonNull(objectId)){
           return Collections.emptyList();
       }
        QueryWrapper<FotaObjectComponentListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", objectId);
        queryWrapper.orderByDesc("create_time");
        List<FotaObjectComponentListPo> fotaObjectComponentListPos = list(queryWrapper);
        return fotaObjectComponentListPos;
    }

    @Override
    public void delByFirmwareIds(long objectId, Set<Long> toBeDelFirmwareIds) {
        if(Objects.nonNull(objectId) || CollectionUtils.isEmpty(toBeDelFirmwareIds)){
            log.warn("参数为空.objectId={}, toBeDelFirmwareIds={}", objectId, toBeDelFirmwareIds);
            return;
        }
        QueryWrapper<FotaObjectComponentListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", objectId);
        queryWrapper.in("firmware_id", toBeDelFirmwareIds);
        remove(queryWrapper);
    }

    @Override
    public FotaObjectComponentListPo getByDeviceId(Long objectId, Long deviceId) {
        if(Objects.nonNull(objectId) || Objects.nonNull(deviceId)){
            log.warn("参数为空.objectId={}, deviceId={}", objectId, deviceId);
            return null;
        }
        QueryWrapper<FotaObjectComponentListPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", objectId);
        queryWrapper.in("device_id", deviceId);
        List<FotaObjectComponentListPo> fotaObjectComponentListPoList = list(queryWrapper);
        return MyCollectionUtil.isNotEmpty(fotaObjectComponentListPoList) ? fotaObjectComponentListPoList.get(0) : null;
    }
}
