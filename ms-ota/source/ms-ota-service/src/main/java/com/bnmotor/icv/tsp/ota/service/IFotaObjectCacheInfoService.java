package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.common.enums.OtaCacheTypeEnum;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;

/**
 * @ClassName: IFotaObjectCacheInfoService
 * @Description:    升级对象缓存信息接口
 * @author: xuxiaochang1
 * @date: 2020/10/19 11:40
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IFotaObjectCacheInfoService {
    /**
     * 写缓存数据
     * @param fotaVinCacheInfo
     * @return
     */
    boolean setFotaVinCacheInfo(FotaVinCacheInfo fotaVinCacheInfo);

    //boolean updateFotaObjectCacheInfo(Supplier<FotaObjectPo> fotaObjectPoSupplier, Supplier<FotaPlanObjectInfo> fotaPlanObjectInfoSupplier, Supplier<Long> transIdSupplier);

    /**
     * 设置任务计划缓存数据
     * @param fotaPlanPo
     * @return
     */
    boolean setFotaPlanCacheInfo(FotaPlanPo fotaPlanPo);

    /**
     * 设置升级对象缓存数据
     * @param fotaObjectPo
     * @return
     */
    boolean setFotaObjectCacheInfo(FotaObjectPo fotaObjectPo);

    /**
     * 清除缓存
     * @param vin
     * @return
     */
    boolean delFotaVinCacheInfo(String vin);

    /**
     * 删除任务计划缓存数据
     * @param otaPlanId
     * @return
     */
    boolean delFotaPlanCacheInfo(Long otaPlanId);

    /**
     * 删除升级对象缓存数据
     * @param otaPlanId
     * @return
     */
    boolean delFotaObjectCacheInfo(Long otaPlanId);

    /**
     * 获取车辆升级对象缓存数据
     * @param vin
     * @return
     */
    FotaVinCacheInfo getFotaVinCacheInfo(String vin);

    /**
     * 获取任务计划缓存数据
     * @param otaPlanId
     * @return
     */
    FotaPlanPo getFotaPlanCacheInfo(Long otaPlanId);

    /**
     * 获取升级对象缓存数据
     * @param otaObjectId
     * @return
     */
    FotaObjectPo getFotaObjectCacheInfo(Long otaObjectId);

    /**
     * 删除缓存
     * @param object
     * @param otaCacheTypeEnum
     */
    void delFotaCacheInfo(Object object, OtaCacheTypeEnum otaCacheTypeEnum);
}
