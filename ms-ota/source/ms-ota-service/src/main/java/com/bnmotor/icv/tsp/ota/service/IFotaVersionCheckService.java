package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.RebuildFlagEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;

import java.util.List;

/**
 * @ClassName: iFotaVersionCheckSelector.java 
 * @Description: 版本检查新旧版本切换
 * @author E.YanLonG
 * @since 2021-1-13 11:27:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IFotaVersionCheckService {

	OtaProtocol checkVersion(OtaProtocol otaProtocol);
	
	OtaProtocol failCheckVersion(OtaProtocol otaProtocol, TboxAdamException e);
	
	void register(RebuildFlagEnum rebuildFlag, IFotaVersionCheckStrategy fotaVersionCheckStrategy);
	
	List<FotaFirmwarePo> queryFotaFirmwarePos(String vin);

	/**
	 * 从版本检查的结果记录中解析出检查对象
	 * @param vin
	 * @return
	 */
	OtaProtocol newVersionFromOta(String vin);

	/**
	 * 更新ecu信息
	 * @param otaProtocol
	 */
    void updateEcuConfig(OtaProtocol otaProtocol);
}