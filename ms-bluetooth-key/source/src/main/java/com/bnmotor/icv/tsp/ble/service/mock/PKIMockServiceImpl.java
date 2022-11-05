package com.bnmotor.icv.tsp.ble.service.mock;

import com.bnmotor.icv.tsp.ble.util.JdkSignatureEcdsaUtils;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

/**
 * @ClassName: PKIMockServiceImpl
 * @Description: PKI模拟服务
 * @author: liuyiwei
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class PKIMockServiceImpl implements PKIMockService {

    @Override
    public byte[] mockPubKey() {
        KeyPair keyPair = JdkSignatureEcdsaUtils.initKey();
        byte[] pubKey = JdkSignatureEcdsaUtils.getPublicKey(keyPair);
        return pubKey;
    }
}
