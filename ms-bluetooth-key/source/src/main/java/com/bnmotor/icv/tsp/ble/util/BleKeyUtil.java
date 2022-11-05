package com.bnmotor.icv.tsp.ble.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.tsp.ble.model.entity.UserBleKeyPo;
import com.bnmotor.icv.tsp.ble.model.request.ble.BleKeyGeneratorDto;
import com.bnmotor.icv.tsp.ble.model.request.ble.PubPriKeyDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName: BleKeyUtil
 * @Description: 蓝牙钥匙工具类
 * @author: liuyiwei
 * @date: 2020/7/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public class BleKeyUtil {

    private static final String SEP_KEY = "#";

    /**
     * 生成蓝牙钥匙
     *
     * @param userId           用户id
     * @param deviceId         车辆设备id
     * @param mobileTerminalId 用户移动终端id
     * @param random           随机数
     * @return 16字节字符
     */
    public static String generate(String userId, String deviceId, String mobileTerminalId, String random) {
        return MD5Builder.bit16(new StringBuffer(userId).append(SEP_KEY).append(deviceId).append(SEP_KEY).append(mobileTerminalId).append(SEP_KEY).append(random).toString());
    }

    /**
     * ECC 算法加密
     *
     * @param seed   加密明文
     * @param pubKey 加密公钥
     * @return
     */
    public static byte[] encryptECC(String seed, byte[] pubKey) {
        byte[] encrypt = new byte[0];
        try {
            encrypt = JdkSignatureEcdsaUtils.encrypt(seed.getBytes("utf-8"), pubKey);
        } catch (Exception e) {
            log.error("ECC加密异常", e);
        }
        return encrypt;
    }

    /**
     * 生成Long型权限表达
     *
     * @return
     */
    public static Long generateRight(List<Long> rights) {
        Double reduce = rights.stream().filter(n -> n > 0).map(n -> Math.pow(2d, n - 1d)).reduce(0d, Double::sum);

        return reduce.longValue();

    }

    /**
     * @param src 相助生成蓝牙钥匙原始16位字符串
     * @return 利用MD5对src生成16位蓝牙钥匙
     */
    public static String createBleKeySrc(String src) {
        String dest = src.concat("#").concat(IdWorker.getIdStr());
        String blekey = MD5Builder.bit16(dest);
        return blekey;
    }


    /**
     * 生成公钥私钥
     *
     * @return
     */
    public static PubPriKeyDto createPubPriKey() {
        KeyPair keyPair = JdkSignatureEcdsaUtils.initKey();
        byte[] publicKey = JdkSignatureEcdsaUtils.getPublicKey(keyPair);
        byte[] privateKey = JdkSignatureEcdsaUtils.getPrivateKey(keyPair);
        PubPriKeyDto build = PubPriKeyDto
                .builder()
                .appPubKey(publicKey)
                .tboxPubKey(publicKey)
                .cloudPriKey(privateKey)
                .build();
        return build;

    }

    public static byte[] getEncrypt(String bleKey, byte[] publicKey) {
        byte[] encrypt = new byte[0];
        try {
            encrypt = JdkSignatureEcdsaUtils.encrypt(bleKey.getBytes("utf-8"), publicKey);
        } catch (Exception ex) {
            log.error("生成加密信息出现错误，错误信息为：{[]}", ex);
        }
        return encrypt;
    }



}
