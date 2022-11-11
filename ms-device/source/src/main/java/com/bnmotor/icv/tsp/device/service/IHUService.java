package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleActivateDto;
import com.bnmotor.icv.tsp.device.model.request.vehicle.VehicleActiveCallbackDto;
import com.bnmotor.icv.tsp.device.model.response.vehicle.QRCodeVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.QRCodeScanVo;
import com.bnmotor.icv.tsp.device.model.response.vehicle.VehActivateStateVo;

/**
 * @ClassName: IHUService
 * @Description: 车机服务
 * @author: huangyun1
 * @date: 2020/11/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IHUService {
    /**
     * 车辆激活
     */
    void vehicleActivate(VehicleActivateDto vehicleActivateDto);

    /**
     * 生成车辆激活二维码
     */
    byte[] generateActivateQRCode(String vin, String token);

    /**
     * 获取激活token
     */
    String getActivateToken(String vin, String deviceId);

    /**
     * 判断二维码是否已被扫码
     */
    QRCodeVo roundCheckActivateState(String vin, String deviceId, String token);

    /**
     * 手机扫描车机激活二维码操作
     */
    QRCodeScanVo scanQRCode(VehicleActivateDto vehicleActivateDto);

    /**
     * 取消激活
     * @param vehicleActivateDto
     */
    void cancelActivateHu(VehicleActivateDto vehicleActivateDto);

    /**
     * 激活回调
     * @param vin
     * @param vehicleActiveCallbackDto
     */
    void activeCallback(String vin, VehicleActiveCallbackDto vehicleActiveCallbackDto);

    /**
     * 查询车机激活状态
     * @param vin
     * @param deviceId
     * @return
     */
    VehActivateStateVo queryActiveState(String vin, String deviceId);
}
