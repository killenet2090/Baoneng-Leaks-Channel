package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.mapper.FotaFirmwareVersionPathMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPathPo;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwareVersionPathDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionPathPo
 * @Description: 软件版本升级路径记录全量包、补丁包和差分包的升级条件信息记录从适应的版本到当前版本的升级路径 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFirmwareVersionPathDbServiceImpl extends ServiceImpl<FotaFirmwareVersionPathMapper, FotaFirmwareVersionPathPo> implements IFotaFirmwareVersionPathDbService {
    @Override
    public List<FotaFirmwareVersionPathPo> list(List<Long> versionIds){
        if(MyCollectionUtil.isEmpty(versionIds)){
            log.debug("versionIds().size=0");
            return Collections.emptyList();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("target_firmware_ver_id", versionIds);
        /*queryWrapper.in("start_firmware_ver_id", versionIds);*/
        List<FotaFirmwareVersionPathPo> list = list(queryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public List<FotaFirmwareVersionPathPo> list(Long firmwareVersionId, boolean pkgUpload) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_firmware_ver_id", firmwareVersionId);
        if(pkgUpload) {
            queryWrapper.eq("pkg_upload", Enums.ZeroOrOneEnum.ONE.getValue());
        }
        List<FotaFirmwareVersionPathPo> list = list(queryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public List<FotaFirmwareVersionPathPo> findByVersionId(Long startVersionId, Long targetVersionId) {
        QueryWrapper versionPathQueryWrapper = new QueryWrapper();
        versionPathQueryWrapper.eq("start_firmware_ver_id", startVersionId);
        versionPathQueryWrapper.eq("target_firmware_ver_id", targetVersionId);
        List<FotaFirmwareVersionPathPo> list = list(versionPathQueryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public void deleteBatchIdsPhysical(List<Long> ids) {
        if(MyCollectionUtil.isNotEmpty(ids)){
            getBaseMapper().deleteBatchIdsPhysical(ids);
        }else{
            log.warn("ids.size=0,quit");
        }
    }

    @Override
    public List<FotaFirmwareVersionPathPo> listByTargetVersionId(Long firmwareVersionId) {
        QueryWrapper<FotaFirmwareVersionPathPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("target_firmware_ver_id", firmwareVersionId);
        queryWrapper.isNotNull("firmware_pkg_id");
        List<FotaFirmwareVersionPathPo> list = list(queryWrapper);
        log.info("list().size={}", MyCollectionUtil.size(list));
        return list;
    }
}
