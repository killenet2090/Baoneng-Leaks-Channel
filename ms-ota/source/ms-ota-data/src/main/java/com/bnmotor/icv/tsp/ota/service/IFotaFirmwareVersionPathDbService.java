package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareVersionPathPo;

import java.util.List;

/**
 * @ClassName: FotaFirmwareVersionPathPo
 * @Description: 软件版本升级路径记录全量包、补丁包和差分包的升级条件信息记录从适应的版本到当前版本的升级路径 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaFirmwareVersionPathDbService extends IService<FotaFirmwareVersionPathPo> {
    /**
     *
     * @param versionIds
     * @return
     */
    List<FotaFirmwareVersionPathPo> list(List<Long> versionIds);

    /**
     *
     * @param firmwareVersionId
     * @param pkgUpload
     * @return
     */
    List<FotaFirmwareVersionPathPo> list(Long firmwareVersionId, boolean pkgUpload);

    /**
     * 根据起始版本查升级路径
     * @param originVersionId
     * @param targetVersionId
     * @return
     */
    List<FotaFirmwareVersionPathPo> findByVersionId(Long originVersionId, Long targetVersionId);

    /**
     * 物理删除
     * @param ids
     */
    void deleteBatchIdsPhysical(List<Long> ids);

    /**
     *
     * @param firmwareVersionId
     * @return
     */
    List<FotaFirmwareVersionPathPo> listByTargetVersionId(Long firmwareVersionId);
}
