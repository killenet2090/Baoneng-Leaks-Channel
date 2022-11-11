package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.service.IFotaFirmwarePkgDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwarePkgService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FotaFirmwarePkgServiceImpl implements IFotaFirmwarePkgService {

    @Autowired
    private IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

    @Override
    public void deleteBatchIdsPhysical(List<Long> ids) {
        fotaFirmwarePkgDbService.deleteBatchIdsPhysical(ids);
    }
}
