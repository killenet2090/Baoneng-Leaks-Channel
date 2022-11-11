package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectComponentListPo;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: FotaObjectComponentListPo
 * @Description: OTA升级对象零件表,用于接收车辆零件信息同步数据 服务类
 * @author xuxiaochang1
 * @since 2020-12-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaObjectComponentListDbService extends IService<FotaObjectComponentListPo> {
    /**
     * 根据升级车辆对象删除
     * @param otaObjectId
     */
    void delByFotaObjectIdPysical(Long otaObjectId);

    /**
     *
     * @param id
     * @return
     */
    List<FotaObjectComponentListPo> listByObjectId(Long id);

    /**
     * 逻辑删除固件信息
     * @param objectId
     * @param toBeDelFirmwareIds
     */
    void delByFirmwareIds(long objectId, Set<Long> toBeDelFirmwareIds);

    /**
     *
     * @param objectId
     * @param item
     * @return
     */
    FotaObjectComponentListPo getByDeviceId(Long objectId, Long item);
}
