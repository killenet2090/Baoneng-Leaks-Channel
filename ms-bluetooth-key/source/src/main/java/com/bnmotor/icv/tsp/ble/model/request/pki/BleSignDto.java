package com.bnmotor.icv.tsp.ble.model.request.pki;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: BleSignDto
 * @Description: 签名
 * @author shuqi1
 * @since 2020-11-1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class BleSignDto {
     private String algorithm;
     private String applicationName;
     private byte[] inData;
}
