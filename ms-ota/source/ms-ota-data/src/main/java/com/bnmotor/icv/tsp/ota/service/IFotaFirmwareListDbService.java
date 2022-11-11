package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareListPo;

import java.util.Collection;
import java.util.List;

/**
 * @author xxc
 * @ClassName: IFotaFirmwareListService
 * @Description: OTA升级固件清单
 * 服务类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-06
 */

public interface IFotaFirmwareListDbService extends IService<FotaFirmwareListPo> {
    /**
     * 获取升级对象所有的固件清单列表
     *
     * @param otaObjectId
     * @return
     */
    List<FotaFirmwareListPo> listAllByObjectId(long otaObjectId);

    /**
     * 从升级对象Id初始化
     * 1、根据升级对象Id查找设备树Id
     * 2、根据设备树节点获取挂载固件清单列表
     * 3、铺平固件清单列表到表(tb_fota_firmware_list表中)
     *
     * @param objectId 升级对象Id(tb_fota_object表Id)
     */
    void initWithObjectId(Long objectId);

    /**
     * 查询OTA升级固件清单
     * @param otaObjectId
     * @param firmwareIds
     * @return
     */
    List<FotaFirmwareListPo> getByOtaObjIdWithFirmwareId(Long otaObjectId, Collection<Long> firmwareIds);

    /**
     * 删除车辆固件列表数据
     * @param otaObjectId
     * @param firmwareId
     * @return
     */
    boolean remove(Long otaObjectId, Long firmwareId);

    /**
     *
     * @param otaObjectId
     * @param firmwareListIds
     */
    void delByOtaObjIdWithFirmwareId(Long otaObjectId, Collection<Long> firmwareListIds);

    /**
     * 根据对象Id和固件Id识别车辆固件Id
     * @param objectId
     * @param firmwareId
     * @return FotaFirmwareListPo
     */
    FotaFirmwareListPo findOne(Long objectId, Long firmwareId);
}
