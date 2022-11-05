package com.bnmotor.icv.tsp.ble.model.request.ble;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: PubPriKeyDto
 * @Description: 公钥私钥对
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Builder
public class PubPriKeyDto {
    /**
     * 公钥
     */
    private byte[] appPubKey;

    /**
     * 公钥
     */
    private byte[] tboxPubKey;

    /**
     * 私钥
     */
    private byte[] cloudPriKey;
}
