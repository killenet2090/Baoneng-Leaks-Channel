package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.mapper.FotaFirmwarePkgMapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePkgPo;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwarePkgDbService;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: FotaFirmwarePkgPo
 * @Description: 升级包信息
包括升级包原始信息以及升级包发布信息 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFirmwarePkgDbServiceImpl extends ServiceImpl<FotaFirmwarePkgMapper, FotaFirmwarePkgPo> implements IFotaFirmwarePkgDbService {

    @Override
    public void deleteBatchIdsPhysical(List<Long> ids) {
        if(MyCollectionUtil.isNotEmpty(ids)){
            getBaseMapper().deleteBatchIdsPhysical(ids);
        }else{
            log.warn("ids.size=0,quit");
        }
    }

    @Override
    public List<FotaFirmwarePkgPo> listByIdsWithStatus(List<Long> pkgIds, int type) {
        QueryWrapper<FotaFirmwarePkgPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", pkgIds);
        queryWrapper.ne("build_pkg_status", Enums.BuildStatusEnum.TYPE_FINISH.getType());
        List<FotaFirmwarePkgPo> list = list(queryWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return list;
    }

    @Override
    public List<FotaFirmwarePkgPo> listByIdsWithEncryptStatus(List<Long> pkgIds) {
        QueryWrapper<FotaFirmwarePkgPo> queryPkgWrapper = new QueryWrapper<>();
        queryPkgWrapper.in("id", pkgIds);
        queryPkgWrapper.ne("encrypt_pkg_status", Enums.EncryptStatusEnum.TYPE_FINISH.getType());
        List<FotaFirmwarePkgPo> list = list(queryPkgWrapper);
        log.info("list.size={}", MyCollectionUtil.size(list));
        return list;
    }
}
