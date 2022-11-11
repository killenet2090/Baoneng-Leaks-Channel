package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.resp.app.TboxUpgradStatusVo;

/**
 * @ClassName: IFotaObjectService
 * @Description: OTA升级对象指需要升级的一个完整对象，在车联网中指一辆车通常拿车的vin作为升级的ID服务类
 * @author xxc
 * @since 2020-07-06
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaObjectService {
    /**
     * 获取车辆当前升级状态
     * @param vin   vin码
     * @return  参考{@link TboxUpgradStatusVo}
     */
    TboxUpgradStatusVo getFotaUpgradeStatus(String vin);

    /**
     * 获取车辆当前升级状态枚举
     * @param vin
     * @return  参考{@link Enums.TaskObjStatusTypeEnum}
     */
    Enums.TaskObjStatusTypeEnum getTaskObjStatusTypeEnum(String vin);

    /**
     * 固件添加同步更新车辆固件列表
     * @param fotaObjectPo
     * @param fotaFirmwarePo
     */
    void process4AddFotaFirmware(FotaObjectPo fotaObjectPo,  FotaFirmwarePo fotaFirmwarePo);

    /**
     * 固件删除同步更新车辆固件列表
     * @param otaObjectId
     * @param firmwareId
     */
    void process4DelFotaFirmware(Long otaObjectId,  Long firmwareId);

    /**
     *
     * @param item
     * @param fotaFirmwareDo
     */
    void process4UpdateFotaFirmware(FotaObjectPo item, FotaFirmwarePo fotaFirmwareDo);
}
