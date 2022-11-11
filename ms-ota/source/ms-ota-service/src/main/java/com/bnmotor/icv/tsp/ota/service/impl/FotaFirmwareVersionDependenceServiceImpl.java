package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionDependencePo;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareVersionDependenceService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionDependenceDo
 * @Description: 软件版本依赖 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFirmwareVersionDependenceServiceImpl implements IFotaFirmwareVersionDependenceService {
    @Override
    public List<FotaFirmwareVersionDependencePo> list(long firmwareId, long firmwareVersionId){
        /*QueryWrapper dependenceQueryWrapper = new QueryWrapper();
        dependenceQueryWrapper.eq("firmware_id", firmwareId);
        dependenceQueryWrapper.in("firmware_version_id", firmwareVersionId);
        List<FotaFirmwareVersionDependencePo> fotaFirmwareVersionDependencePos = list(dependenceQueryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(fotaFirmwareVersionDependencePos));*/
        return Lists.newArrayList();
    }

    @Override
    public List<FotaFirmwareVersionDependencePo> list(long firmwareId, List<Long> firmwareVersionIds){
        /*if(MyCollectionUtil.isEmpty(firmwareVersionIds)){
            log.info("firmwareVersionIds().size=0");
            return Collections.emptyList();
        }
        QueryWrapper dependenceQueryWrapper = new QueryWrapper();
        dependenceQueryWrapper.in("firmware_version_id", firmwareVersionIds);
        List<FotaFirmwareVersionDependencePo> fotaFirmwareVersionDependencePos = list(dependenceQueryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(fotaFirmwareVersionDependencePos));
        return fotaFirmwareVersionDependencePos;*/

        return Lists.newArrayList();
    }

    @Override
    public void deleteBatchIdsPhysical(List<Long> ids) {
        /*if(MyCollectionUtil.isNotEmpty(ids)){
            getBaseMapper().deleteBatchIdsPhysical(ids);
        }else{
            log.warn("ids.size=0,quit");
        }*/
    }
}
