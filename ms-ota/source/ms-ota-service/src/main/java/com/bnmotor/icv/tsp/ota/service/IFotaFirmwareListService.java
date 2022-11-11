package com.bnmotor.icv.tsp.ota.service;

/**
 * @author xxc
 * @ClassName: IFotaFirmwareListService
 * @Description: OTA升级固件清单
 * 服务类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-06
 */

public interface IFotaFirmwareListService{
    /**
     * 获取升级对象所有的固件清单列表
     *
     * @param otaObjectId
     * @return
     */
   /* List<FotaFirmwareListPo> listAllByObjectId(long otaObjectId);*/

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
    //List<FotaFirmwareListPo> getByOtaObjIdWithFirmwareId(Long otaObjectId, List<Long> firmwareIds);
}
