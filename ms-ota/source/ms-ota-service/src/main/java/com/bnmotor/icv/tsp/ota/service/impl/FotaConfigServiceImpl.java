package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.down.OtaDownConfigCheckResponse;
import com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.HttpMessageMapper;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.OtaMessageMapper;
import com.bnmotor.icv.tsp.ota.model.cache.FotaVinCacheInfo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwareListPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuConfigVo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuFirmwareConfigListVo;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @ClassName: FotaEcuServiceImpl.java
 * @Description: 查询服务器配置
 * @author E.YanLonG
 * @since 2021-1-13 12:05:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class FotaConfigServiceImpl implements IFotaConfigService {

	@Autowired
	IFotaObjectCacheInfoService fotaObjectCacheInfoService;

	@Autowired
	IFotaFirmwareListDbService fotaFirmwareListDbService;

	@Autowired
	IFotaFirmwareDbService fotaFirmwareDbService;

	@Autowired
	IFotaStrategyFirmwareListDbService fotaStrategyFirmwareListDbService;

	@Override
	public EcuFirmwareConfigListVo queryEcuConfigFirmwareDos(String vin) {
		FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
		MyAssertUtil.notNull(fotaVinCacheInfo, OTARespCodeEnum.FOTA_OBJECT_NOT_EXIST);
		FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
		MyAssertUtil.notNull(fotaObjectPo, OTARespCodeEnum.FOTA_OBJECT_NOT_EXIST);

		List<FotaFirmwarePo> firmwareList = queryFotaFirmwarePosInner(fotaObjectPo);
		EcuFirmwareConfigListVo ecuFirmwareConfigListVo = new EcuFirmwareConfigListVo();

		List<EcuConfigVo> ecus = HttpMessageMapper.INSTANCE.fotaFirmwarePo2EcuConfig(firmwareList);
		paddingResponseId(ecus);

		ecuFirmwareConfigListVo.setEcus(ecus);
		ecuFirmwareConfigListVo.setConfVersion(fotaObjectPo.getConfVersion());

		return ecuFirmwareConfigListVo;
	}

	/**
	 * 临时处理的方法 响应repsonseId
	 * 
	 * @param ecus
	 */
	private void paddingResponseId(List<EcuConfigVo> ecus) {

		ecus.forEach(it -> {
			if (StringUtils.isBlank(it.getEcuResponseId())) {
				String ecuResponseId = EcuHexUtil.increase(it.getEcuDid(), 8);
				it.setEcuResponseId(ecuResponseId);
			}
		});
	}

	@ Override
	public List<FotaFirmwarePo> queryFotaFirmwarePosInner(FotaObjectPo fotaObjectPo) {
		List<FotaFirmwareListPo> fotaFirmwareListPos = fotaFirmwareListDbService.listAllByObjectId(fotaObjectPo.getId());
		if (MyCollectionUtil.isEmpty(fotaFirmwareListPos)) {
			log.warn("升级对象固件清单列表未空.", fotaObjectPo);
			return Collections.emptyList();
		}

		List<FotaFirmwarePo> firmwareList = fotaFirmwareDbService.listByIds(MyCollectionUtil.map2NewList(fotaFirmwareListPos, item -> item.getFirmwareId()));
		if (MyCollectionUtil.isEmpty(firmwareList)) {
			log.warn("升级对象固件清单列表为空.", fotaObjectPo);
			return Collections.emptyList();
		}
		return firmwareList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateEcuConfig(OtaProtocol otaProtocol) {
		String vin = otaProtocol.getOtaMessageHeader().getVin();
		FotaVinCacheInfo fotaVinCacheInfo = fotaObjectCacheInfoService.getFotaVinCacheInfo(vin);
		FotaObjectPo fotaObjectPo = fotaObjectCacheInfoService.getFotaObjectCacheInfo(fotaVinCacheInfo.getObjectId());
		Long otObjectId = fotaObjectPo.getId();
		otaProtocol.getBody().getOtaUpVersionCheck().getEcuModules().forEach(item ->{
			FotaFirmwarePo fotaFirmwarePo = fotaFirmwareDbService.getByFrimwareCode(item.getFirmwareCode());
			if(Objects.isNull(fotaFirmwarePo)){
				log.warn("illegal firmware code.firmwareCode={}", item.getFirmwareCode());
			}
			FotaFirmwareListPo fotaFirmwareListPo = fotaFirmwareListDbService.findOne(otObjectId, fotaFirmwarePo.getId());
			if(Objects.isNull(fotaFirmwareListPo)){
				log.warn("illegal firmwareListPo. ");
				return;
			}
			FotaFirmwareListPo entity = new FotaFirmwareListPo();
			entity.setId(fotaFirmwareListPo.getId());
			//更新当前最新版本
			entity.setRunningFirmwareVersion(item.getFirmwareVersion());
			CommonUtil.wrapBasePo4Update(entity);
			fotaFirmwareListDbService.updateById(entity);
		});
	}

	@Override
	public OtaProtocol queryEcuConfigList(OtaProtocol otaProtocol) {
		// 定义获取配置的实现
		Consumer<OtaProtocol> respConsumer = resp -> {
			EcuFirmwareConfigListVo ecuFirmwareConfigListVo = queryEcuConfigFirmwareDos(otaProtocol.getOtaMessageHeader().getVin());
			OtaDownConfigCheckResponse downConfigCheckResponse = OtaMessageMapper.INSTANCE.eEcuFirmwareConfigListVo2OtaDownConfigCheckResponse(ecuFirmwareConfigListVo);
			downConfigCheckResponse.setConfVersion(ecuFirmwareConfigListVo.getConfVersion());
			resp.getBody().setOtaDownConfigCheckResponse(downConfigCheckResponse);
		};
		return TBoxUtil.wrapTBoxUpBusiness(otaProtocol, respConsumer, BusinessTypeEnum.OTA_DOWN_CONF_VERSION_FROM_OTA);
	}
}