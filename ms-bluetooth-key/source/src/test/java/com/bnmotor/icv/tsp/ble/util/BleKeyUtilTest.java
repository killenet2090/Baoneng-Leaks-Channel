package com.bnmotor.icv.tsp.ble.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName: BleKeyUtilTest
 * @Description: 描述类的作用
 * @author: liuyiwei
 * @date: 2020/7/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
class BleKeyUtilTest {

    @Test
    void encryptECC() {
        try {
            byte[] bytes = BleKeyUtil.encryptECC("0fabccf3ab77a03c", "134".getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("加密测试异常", e);
        }

    }

    @Test
    void signECRSA() {
        System.out.println("signECRSA: ");
        String seed = "dd42af2ce84e7264";

        KeyPair keyPair = JdkSignatureEcdsaUtils.initKey();
        byte[] privateKey = JdkSignatureEcdsaUtils.getPrivateKey(keyPair);

        byte[] sign = JdkSignatureEcdsaUtils.sign(ShaUtils.SHA256(seed).getBytes(), privateKey, JdkSignatureEcdsaUtils.SIGN_ALGORITHM);

        System.out.println(sign.toString());
        System.out.println("signECRSA. ");
    }
}