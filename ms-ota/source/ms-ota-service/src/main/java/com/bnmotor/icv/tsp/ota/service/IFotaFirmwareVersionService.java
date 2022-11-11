package com.bnmotor.icv.tsp.ota.service;

/**
 * @ClassName: FotaFirmwareVersionDo
 * @Description: 软件版本,即软件坂本树上的一个节点
定义软件所生成的各个不同的版本 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Deprecated
public interface IFotaFirmwareVersionService {
    /**
     * 查询列表
     * @param projectId
     * @param firmwareId
     * @return
     */
    //List<FotaFirmwareVersionPo> list(String projectId, long firmwareId);

    /**
     * 查询列表
     * @param projectId
     * @param firmwareIds
     * @return
     */
    //List<FotaFirmwareVersionPo> list(String projectId, List<Long> firmwareIds);

    /**
     * 并发更新，防止并发错误
     * @param fotaFirmwareVersionPo
     * @param version
     * @return
     */
    //boolean updateByIdWithVersion(final FotaFirmwareVersionPo fotaFirmwareVersionPo, Integer version);

    /**
     * 根据版本号获取固件版本信息
     * @param projectId
     * @param firmwareId
     * @param firmwareVersionNo
     * @return
     */
    //FotaFirmwareVersionPo getByVersionCode(String projectId, Long firmwareId, String firmwareVersionNo);

    /**
     * 物理删除
     * @param versionIds
     */
    //void deleteBatchIdsPhysical(List<Long> versionIds);
}
