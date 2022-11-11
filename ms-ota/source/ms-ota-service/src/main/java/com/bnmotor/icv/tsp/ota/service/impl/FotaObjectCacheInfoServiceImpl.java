package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.adam.data.redis.IRedisProvider;
import com.bnmotor.icv.tsp.ota.common.cache.FotaObjectCacheConfig;
import com.bnmotor.icv.tsp.ota.common.enums.OtaCacheTypeEnum;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaVersionCheckVerifyPo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: FotaObjectCacheInfoServiceImpl
 * @Description:    IFotaObjectCacheInfoService实现类
 * @author: xuxiaochang1
 * @date: 2020/10/19 11:48
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class FotaObjectCacheInfoServiceImpl implements IFotaObjectCacheInfoService {
    
	@Qualifier("otaRedisProvider")
	@Autowired
    private IRedisProvider redisProvider;

    @Autowired
    private IFotaObjectDbService fotaObjectDbService;

    @Autowired
    private IFotaPlanObjListDbService fotaPlanObjListDbService;

    @Autowired
    private IFotaPlanDbService fotaPlanDbService;

    @Autowired
    private IFotaVersionCheckVerifyDbService fotaVersionCheckVerifyDbService;
    
    @Autowired
    FotaObjectCacheConfig fotaObjectCacheConfig;

    private final long EXPIRE_TIME = 86400 * 30;

    @Override
    public boolean setFotaVinCacheInfo(final FotaVinCacheInfo fotaVinCacheInfo) {
        try {
            redisProvider.setObject(buildCacheInfoKey(fotaVinCacheInfo.getVin(), OtaCacheTypeEnum.VIN_CACHE_INFO), fotaVinCacheInfo, EXPIRE_TIME, TimeUnit.SECONDS);
            return true;
        } catch (IOException e) {
            log.error("设置缓存车辆升级信息异常.fotaObjectCacheInfo={}, message={}", fotaVinCacheInfo, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean setFotaPlanCacheInfo(final FotaPlanPo fotaPlanPo) {
        try {
            redisProvider.setObject(buildCacheInfoKey(fotaPlanPo.getId(), OtaCacheTypeEnum.PLAN_CACHE_INFO), fotaPlanPo, EXPIRE_TIME, TimeUnit.SECONDS);
            return true;
        } catch (IOException e) {
            log.error("设置缓存车辆升级信息异常.fotaPlanPo={}, message={}", fotaPlanPo, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean setFotaObjectCacheInfo(FotaObjectPo fotaObjectPo) {
        try {
            redisProvider.setObject(buildCacheInfoKey(fotaObjectPo.getId(), OtaCacheTypeEnum.OBJECT_CACHE_INFO), fotaObjectPo, EXPIRE_TIME, TimeUnit.SECONDS);
            return true;
        } catch (IOException e) {
            log.error("设置缓存车辆升级信息异常.fotaObjectPo={}, message={}", fotaObjectPo, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delFotaVinCacheInfo(String vin) {
        return redisProvider.delete(buildCacheInfoKey(vin, OtaCacheTypeEnum.VIN_CACHE_INFO));
    }

    @Override
    public boolean delFotaPlanCacheInfo(Long otaPlanId) {
        return redisProvider.delete(buildCacheInfoKey(otaPlanId, OtaCacheTypeEnum.PLAN_CACHE_INFO));
    }

    @Override
    public boolean delFotaObjectCacheInfo(Long otaPlanId) {
        return redisProvider.delete(buildCacheInfoKey(otaPlanId, OtaCacheTypeEnum.OBJECT_CACHE_INFO));
    }

    /**
     * 缓存开关
     * @param <T>
     * @param cacheKey
     * @param clazz
     * @return
     * @throws IOException
     */
    private <T> T getObject(String cacheKey, Class<T> clazz) throws IOException {
    	log.debug("缓存开关|{} cacheKey|{}", fotaObjectCacheConfig.isFotaObjectCacheConfig(), cacheKey);
    	return fotaObjectCacheConfig.isFotaObjectCacheConfig() ? redisProvider.getObject(cacheKey, clazz) : null;
    }
    
    @Override
    public FotaVinCacheInfo getFotaVinCacheInfo(String vin) {
        try {
            FotaVinCacheInfo fotaVinCacheInfo = getObject(buildCacheInfoKey(vin, OtaCacheTypeEnum.VIN_CACHE_INFO), FotaVinCacheInfo.class);
            if(Objects.isNull(fotaVinCacheInfo)){
                FotaObjectPo fotaObjectPo = fotaObjectDbService.findByVin(vin);
                if(Objects.isNull(fotaObjectPo)) {
                    log.warn("获取升级对象失败.vin={}", vin);
                    return null;
                }
                fotaVinCacheInfo = new FotaVinCacheInfo();
                fotaVinCacheInfo.setObjectId(fotaObjectPo.getId());
                fotaVinCacheInfo.setVin(vin);
                FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.findOneByObjectId(fotaObjectPo.getId());

                if(Objects.isNull(fotaPlanObjListPo)){
                    log.warn("升级对象不存在有效任务.vin={}", vin);
                    setFotaVinCacheInfo(fotaVinCacheInfo);
                    return fotaVinCacheInfo;
                }
                FotaPlanPo fotaPlanPo = fotaPlanDbService.getById(fotaPlanObjListPo.getOtaPlanId());
                if(Objects.isNull(fotaPlanPo)){
                    log.warn("升级对象不存在有效任务.vin={}", vin);
                    setFotaVinCacheInfo(fotaVinCacheInfo);
                    return fotaVinCacheInfo;
                }
                fotaVinCacheInfo.setOtaPlanId(fotaPlanPo.getId());
                fotaVinCacheInfo.setOtaPlanObjectId(fotaPlanObjListPo.getId());

                FotaVersionCheckVerifyPo fotaVersionCheckVerifyPo = fotaVersionCheckVerifyDbService.findCurFotaVersionCheckVerifyPo(fotaPlanPo.getId(), fotaPlanObjListPo.getId());
                if(Objects.isNull(fotaVersionCheckVerifyPo)){
                    log.warn("升级事务不存在.vin={}", vin);
                    setFotaVinCacheInfo(fotaVinCacheInfo);
                    return fotaVinCacheInfo;
                }
                fotaVinCacheInfo.setTransId(fotaVersionCheckVerifyPo.getId());
                fotaVinCacheInfo.setCheckReqId(fotaVersionCheckVerifyPo.getCheckReqId());
                setFotaVinCacheInfo(fotaVinCacheInfo);
                return fotaVinCacheInfo;
            }
            log.info("从缓存获取升级vin码对象:fotaVinCacheInfo={}", fotaVinCacheInfo);
            return fotaVinCacheInfo;
        } catch (Exception e) {
            log.error("获取缓存车辆升级信息异常.vin={}, message={}", vin, e.getMessage(), e);
        }
        log.warn("获取缓存数据为空.vin={}", vin);
        return null;
    }

    @Override
    public FotaPlanPo getFotaPlanCacheInfo(Long otaPlanId) {
        try {
            FotaPlanPo fotaPlanPo = getObject(buildCacheInfoKey(otaPlanId, OtaCacheTypeEnum.PLAN_CACHE_INFO), FotaPlanPo.class);
            if(Objects.isNull(fotaPlanPo)) {
                fotaPlanPo = fotaPlanDbService.getById(otaPlanId);
                if(Objects.isNull(fotaPlanPo)) {
                    log.warn("获取升级任务失败.otaPlanId={}", otaPlanId);
                    return null;
                }
                setFotaPlanCacheInfo(fotaPlanPo);
            }
            //log.info("从缓存获取任务对象:fotaPlanPo={}", fotaPlanPo);
            return fotaPlanPo;
        } catch (IOException e) {
            log.error("获取缓存升级任务信息异常.otaPlanId={}, message={}", otaPlanId, e.getMessage(), e);
        }
        log.warn("获取缓存升级任务信息为空.otaPlanId={}", otaPlanId);
        return null;
    }

    @Override
    public FotaObjectPo getFotaObjectCacheInfo(Long otaObjectId) {
        try {
            FotaObjectPo fotaObjectPo = getObject(buildCacheInfoKey(otaObjectId, OtaCacheTypeEnum.OBJECT_CACHE_INFO), FotaObjectPo.class);
            if(Objects.isNull(fotaObjectPo)) {
                fotaObjectPo = fotaObjectDbService.getById(otaObjectId);
                if (Objects.isNull(fotaObjectPo)) {
                    log.warn("升级任务不存在.otaObjectId={}", otaObjectId);
                    return null;
                }
                setFotaObjectCacheInfo(fotaObjectPo);
            }
            //log.info("从缓存获取升级车辆对象:fotaObjectPo={}", fotaObjectPo);
            return fotaObjectPo;
        } catch (IOException e) {
            log.error("获取缓存升级对象信息异常.otaObjectId={}, message={}", otaObjectId, e.getMessage(), e);
        }
        log.warn("获取缓存升级对象信息为空.otaObjectId={}", otaObjectId);
        return null;
    }

    @Override
    public void delFotaCacheInfo(Object object, OtaCacheTypeEnum otaCacheTypeEnum) {
        redisProvider.delete(buildCacheInfoKey(object, otaCacheTypeEnum));
    }

    /**
     * 构建缓存key
     * @param object
     * @param otaCacheTypeEnum
     * @return
     */
    private String buildCacheInfoKey(Object object, OtaCacheTypeEnum otaCacheTypeEnum){
        MyAssertUtil.notNull(otaCacheTypeEnum, "枚举值异常");
        MyAssertUtil.notNull(object, "key值对象不能为空");
        String key = otaCacheTypeEnum.getKeyPrefix() + object;
        log.info("key={}", key);
        return key;
    }

    /**
     * 缓存类型枚举
     *//*
    private enum OtaCacheTypeEnum{
        VIN_CACHE_INFO(1, "升级事务关联数据缓存", "OTA_VIN_"),
        PLAN_CACHE_INFO(2, "升级任务缓存", "OTA_PLAN_"),
        OBJECT_CACHE_INFO(3, "升级车辆缓存", "OTA_OBJECT_"),
        ;
        @Getter
        private Integer type;
        @Getter
        private String desc;
        @Getter
        private String keyPrefix;

        OtaCacheTypeEnum(Integer type, String desc, String keyPrefix){
            this.type = type;
            this.desc = desc;
            this.keyPrefix = keyPrefix;
        }
    }*/
}
