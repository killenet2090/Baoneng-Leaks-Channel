package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.bnmotor.icv.tsp.ble.common.RespCode;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: BleVerifyAuthCodeDto
 * @Description: 蓝牙钥匙解绑回调接口
 * @author shuqi1
 * @since 2020-11-1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Builder
public class BleVerifyAuthCodeDto {
    private Integer respCode;
    private String  userId;
    private String  vin;
}
