package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionDependencePo;

import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionDependenceDo
 * @Description: 软件版本依赖 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaFirmwareVersionDependenceService {
    /**
     * 查询列表
     * @param firmwareId
     * @param firmwareVersionId
     * @return
     */
    List<FotaFirmwareVersionDependencePo> list(long firmwareId, long firmwareVersionId);

    /**
     * 查询列表
     * @param firmwareId
     * @param firmwareVersionIds
     * @return
     */
    List<FotaFirmwareVersionDependencePo> list(long firmwareId, List<Long> firmwareVersionIds);

    /**
     * 物理删除
     * @param ids
     */
    void deleteBatchIdsPhysical(List<Long> ids);
}
