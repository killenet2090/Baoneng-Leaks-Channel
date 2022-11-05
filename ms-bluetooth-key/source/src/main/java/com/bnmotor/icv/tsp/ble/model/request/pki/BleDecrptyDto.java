package com.bnmotor.icv.tsp.ble.model.request.pki;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: BleDecrptyDto
 * @Description: 对称解密接口
 * @author shuqi1
 * @since 2020-11-1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Builder
public class BleDecrptyDto {
       /**
        * 解密算法
        */
       private String  algorithm;
       /**
        * 解密内容
        */
       private byte[]  cipherText;
       /**
        * 蓝牙ID
        */
       private Integer keyIndex;
}
