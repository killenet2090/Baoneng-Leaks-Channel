package com.bnmotor.icv.tsp.ota.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.RebuildFlagEnum;
import com.bnmotor.icv.tsp.ota.common.enums.TBoxRespCodeEnum;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxDownHandler;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanObjListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.bnmotor.icv.tsp.ota.util.TBoxUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: IFotaVersionCheckSelectorImpl.java
 * @Description: 版本切换选择器
 * @author E.YanLonG
 * @since 2021-1-13 11:28:20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class FotaVersionCheckServiceImpl implements IFotaVersionCheckService {

	final Map<RebuildFlagEnum, IFotaVersionCheckStrategy> versionCheckService = Maps.newHashMap();

	@Autowired
	IFotaObjectCacheInfoService fotaObjectCacheInfoService;

	@Autowired
	IFotaPlanObjListDbService fotaPlanObjListDbService;
	
	@Autowired
	IFotaConfigService fotaConfigService;
	
	@Autowired
	IFotaPlanUpgradeService fotaPlanUpgradeService;
	
	@Autowired
    TBoxDownHandler boxDownHandler;

	@Override
	public void register(RebuildFlagEnum rebuildFlag, IFotaVersionCheckStrategy fotaVersionCheckStrategy) {
		versionCheckService.put(rebuildFlag, fotaVersionCheckStrategy);
	}
	
//	public IFotaVersionCheckStrategy selectFotaPlanPo(String vin) {
//		FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
//		// 校验车辆是否存在
//		MyAssertUtil.notNull(fotaVinCacheInfo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
//
//		FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
//		// 校验车辆是否存在
//		MyAssertUtil.notNull(fotaObjectPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
//
//		// 检查当前车辆是否存在有效的任务
//		FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListDbService.findOneByObjectId(fotaObjectPo.getId());
//
//		// 检查是否存在有效的升级任务
//		FotaPlanPo fotaPlanPo = fotaObjectCacheInfoService.getFotaPlanCacheInfo(fotaPlanObjListPo.getOtaPlanId());
//		MyAssertUtil.notNull(fotaPlanPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
//		
//		RebuildFlagEnum rebuilFlag = RebuildFlagEnum.select(fotaPlanPo.getRebuildFlag(), RebuildFlagEnum.V1);
//		return versionCheckService.get(rebuilFlag);
//	}
	
	public IFotaVersionCheckStrategy selectFotaPlanPo(String vin) {
		FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
		// 校验车辆是否存在
		MyAssertUtil.notNull(fotaVinCacheInfo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);

		FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
		// 校验车辆是否存在
		MyAssertUtil.notNull(fotaObjectPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);

		// 检查是否存在有效的升级任务
		List<FotaPlanObjListPo> fotaPlanObjListPos = Lists.newArrayList();
        FotaPlanPo fotaPlanPo = fotaPlanUpgradeService.selectPlan4Upgrade(fotaObjectPo.getId(), fotaPlanObjListPos);
        if(Objects.isNull(fotaPlanPo)){
            log.info("获取升级任务失败.fotaObjectPo={}", fotaObjectPo.getId());
            MyAssertUtil.notNull(fotaPlanPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
        }
        
        FotaPlanObjListPo fotaPlanObjListPo = fotaPlanObjListPos.stream().findFirst().orElse(null);
		if(Objects.isNull(fotaPlanObjListPo)){
			log.warn("获取升级任务对象失败.fotaObjectDo.getId()={}", fotaObjectPo.getId());
			MyAssertUtil.notNull(fotaPlanPo, TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_NO_VERSION);
		}
		
		RebuildFlagEnum rebuilFlag = RebuildFlagEnum.select(fotaPlanPo.getRebuildFlag(), RebuildFlagEnum.V1);
		return versionCheckService.get(rebuilFlag);
	}
	
	@Override
	public OtaProtocol checkVersion(OtaProtocol otaProtocol) {
		String vin = otaProtocol.getOtaMessageHeader().getVin();
		IFotaVersionCheckStrategy fotaVersionCheckService = selectFotaPlanPo(vin);
		return fotaVersionCheckService.checkVersion(otaProtocol);
	}

	@Override
	public OtaProtocol failCheckVersion(OtaProtocol otaProtocol, TboxAdamException tboxAdamException) {
		// 默认V1版本处理
		IFotaVersionCheckStrategy fotaVersionCheckService = versionCheckService.get(RebuildFlagEnum.V1);
		return fotaVersionCheckService.failCheckVersion(otaProtocol, tboxAdamException);
	}
	
	@Override
    public List<FotaFirmwarePo> queryFotaFirmwarePos(String vin) {
        FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
        MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_OBJECT_NOT_EXIST);
        FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
        MyAssertUtil.notNull(fotaObjectPo, OTARespCodeEnum.FOTA_OBJECT_NOT_EXIST);

        return fotaConfigService.queryFotaFirmwarePosInner(fotaObjectPo);
    }

	@Override
    public OtaProtocol newVersionFromOta(String vin) {
        log.info("来自云端的升级通知");
        OtaProtocol otaProtocol = TBoxUtil.buildOtaDownOtaProtocol(com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum.OTA_DOWN_VERSION_CHECK_FROM_OTA, vin, null, IdWorker.getId());
        boxDownHandler.send(otaProtocol);
        return otaProtocol;
    }

	@Override
	public void updateEcuConfig(OtaProtocol otaProtocol) {
		fotaConfigService.updateEcuConfig(otaProtocol);
	}
}