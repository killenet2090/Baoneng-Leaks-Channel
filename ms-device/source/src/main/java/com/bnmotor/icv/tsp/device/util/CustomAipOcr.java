package com.bnmotor.icv.tsp.device.util;

import com.baidu.aip.http.AipRequest;
import com.baidu.aip.ocr.AipOcr;
import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @ClassName: CustomAipOcr
 * @Description: 定制化aipOcr
 * @author: huangyun1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class CustomAipOcr extends AipOcr {
    private final static String VEHICLE_INVOICE_URI = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_invoice";

    public CustomAipOcr(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
    }

    public JSONObject vehicleInvoice(byte[] image, HashMap<String, String> options) {
        AipRequest request = new AipRequest();
        this.preOperation(request);
        String base64Content = Base64Util.encode(image);
        request.addBody("image", base64Content);
        if (options != null) {
            request.addBody(options);
        }

        request.setUri(VEHICLE_INVOICE_URI);
        this.postOperation(request);
        return this.requestServer(request);
    }
}
