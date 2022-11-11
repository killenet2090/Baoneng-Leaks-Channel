package com.bnmotor.icv.tsp.ota.service;

/**
 * @ClassName: ITboxHttpSevice
 * @Description: 
 * @author eyanlong2
 * @since 2020-9-7
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Deprecated
public interface ITboxHttpSevice {

	/**
	 * 获取OTA云端配置的升级固件清单列表
	 * @param vin
	 * @return
	 */
	//EcuFirmwareConfigListVo queryEcuConfigFirmwareDos(String vin);

	/**
	 *
	 * @param vin
	 * @return
	 */
	//List<FotaFirmwarePo> queryFotaFirmwarePos(String vin);

	/**
	 * 获取ecu配置列表
	 * @param req
	 * @param vin
	 * @return
	 */
	//OtaProtocol queryEcuConfigList(OtaProtocol req, String vin);
}