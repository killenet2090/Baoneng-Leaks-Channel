package com.bnmotor.icv.tsp.device.service;

import com.bnmotor.icv.tsp.device.model.request.vehBind.*;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleBindVo;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleInvoiceVo;
import com.bnmotor.icv.tsp.device.model.response.vehBind.VehicleLicenseVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: TspDrivingLicensePo
 * @Description: 行驶证信息 服务类
 * @author huangyun1
 * @since 2020-09-24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IVehicleBindService {
    /**
     * 上传行驶证
     * @param file
     * @param uid
     * @return
     */
    VehicleLicenseVo uploadLicense(MultipartFile file, Long uid);

    /**
     * 上传机动车销售发票
     * @param multipartFile
     * @param uid
     * @return
     */
    VehicleInvoiceVo uploadVehicleInvoice(MultipartFile multipartFile, Long uid);
    /**
     * 识别机动车销售发票信息
     * @param invoiceFile
     * @return
     */
    VehicleInvoiceVo recognitionVehInvoice(MultipartFile invoiceFile);

    /**
     * 个人车主提交绑定操作
     * @param submitBindDto
     * @return
     */
    VehicleBindVo submitBind(SubmitBindDto submitBindDto);

    /**
     * 同步车辆销售信息
     * @param vehicleSaleInfoDto
     */
    void synchronizationSaleInfo(VehicleSaleInfoDto vehicleSaleInfoDto);

    /**
     * 车辆解绑操作
     * @param vehicleUnbindDto
     */
    void submitUnbind(VehicleUnbindDto vehicleUnbindDto);


    /**
     * 完成绑定
     * @param finishBindDto
     */
    void finishBind(FinishBindDto finishBindDto);

    /**
     * 重置车辆绑定状态
     * @param resetVehicleBindStatusDto
     */
    void resetBindStatus(ResetVehicleBindStatusDto resetVehicleBindStatusDto);
}
