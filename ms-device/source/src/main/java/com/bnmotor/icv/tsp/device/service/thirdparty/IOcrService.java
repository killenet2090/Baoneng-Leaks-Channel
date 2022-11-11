package com.bnmotor.icv.tsp.device.service.thirdparty;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.entity.VehicleBindInvoicePo;
import com.bnmotor.icv.tsp.device.model.entity.VehicleLicensePo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: IOcrService
 * @Description: ocr文字识别接口调用
 * @author: huangyun1
 * @date: 2020/6/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IOcrService {
    /**
     * 解析行驶证
     * @param file
     * @return
     */
    RestResponse<VehicleLicensePo> analysisVehicleLicense(MultipartFile file);

    /**
     * 解析机动车销售发票信息
     * @param file
     * @return
     */
    RestResponse<VehicleBindInvoicePo> analysisVehicleInvoice(MultipartFile file);
}
