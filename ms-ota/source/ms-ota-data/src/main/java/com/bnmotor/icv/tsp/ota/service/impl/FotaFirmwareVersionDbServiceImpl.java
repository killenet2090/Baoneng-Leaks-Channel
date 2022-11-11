package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.mapper.FotaFirmwareVersionMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPo;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareVersionDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: FotaFirmwareVersionDo
 * @Description: 软件版本,即软件坂本树上的一个节点
定义软件所生成的各个不同的版本 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFirmwareVersionDbServiceImpl extends ServiceImpl<FotaFirmwareVersionMapper, FotaFirmwareVersionPo> implements IFotaFirmwareVersionDbService {

    @Override
    public List<FotaFirmwareVersionPo> list(String projectId, long firmwareId){
        QueryWrapper versionQueryWrapper = new QueryWrapper();
        versionQueryWrapper.eq("firmware_id", firmwareId);
        List<FotaFirmwareVersionPo> list = list(versionQueryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public List<FotaFirmwareVersionPo> list(String projectId, List<Long> firmwareIds) {
        if(MyCollectionUtil.isEmpty(firmwareIds)){
            log.info("firmwareIds().size=0");
            return Collections.emptyList();
        }
        QueryWrapper versionQueryWrapper = new QueryWrapper();
        /*versionQueryWrapper.eq("project_id", projectId);*/
        versionQueryWrapper.in("firmware_id", firmwareIds);
        List<FotaFirmwareVersionPo> list = list(versionQueryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public boolean updateByIdWithVersion(FotaFirmwareVersionPo fotaFirmwareVersionPo, Integer version) {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", fotaFirmwareVersionPo.getId());
        updateWrapper.eq("version", version);
        return update(fotaFirmwareVersionPo, updateWrapper);
    }

    @Override
    public FotaFirmwareVersionPo getByVersionCode(String projectId, Long firmwareId, String firmwareVersionNo) {
        QueryWrapper versionQueryWrapper = new QueryWrapper();
        /*versionQueryWrapper.eq("project_id", projectId);*/
        versionQueryWrapper.eq("firmware_id", firmwareId);
        versionQueryWrapper.eq("firmware_version_no", firmwareVersionNo);
        FotaFirmwareVersionPo fotaFirmwareVersionPo = getOne(versionQueryWrapper);
        log.info("fotaFirmwareVersionDo={}", Objects.nonNull(fotaFirmwareVersionPo) ? fotaFirmwareVersionPo.toString() : "");
        return fotaFirmwareVersionPo;
    }

    @Override
    public void deleteBatchIdsPhysical(List<Long> ids) {
        if(MyCollectionUtil.isNotEmpty(ids)){
            getBaseMapper().deleteBatchIdsPhysical(ids);
        }else{
            log.warn("ids.size=0,quit");
        }
    }
}
