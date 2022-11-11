package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaObjectPo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuFirmwareConfigListVo;

import java.util.List;

/**
 * @ClassName: IFotaEcuService.java 
 * @Description: 查询服务器配置
 * @author E.YanLonG
 * @since 2021-1-13 12:04:40
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IFotaConfigService {
	
    /**
     * 获取OTA云端配置的升级固件清单列表
     * @param vin
     * @return
     */
    EcuFirmwareConfigListVo queryEcuConfigFirmwareDos(String vin);

    /**
     * 获取ecu配置列表
     * @param otaProtocol
     * @return
     */
    OtaProtocol queryEcuConfigList(OtaProtocol otaProtocol);

    /**
     *
     * @param fotaObjectPo
     * @return
     */
    List<FotaFirmwarePo> queryFotaFirmwarePosInner(FotaObjectPo fotaObjectPo);

    void updateEcuConfig(OtaProtocol otaProtocol);
}