package com.bnmotor.icv.tsp.ble.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @ClassName: SecurityUtils
 * @Description: 安全加解密类, 提供1-sha256进行密码加密；2-对称加密手机邮箱的功能
 * @author: shuqi
 * @date: 2020/5/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class SecurityUtils {
    /**
     * 密钥长度: 128, 192 or 256
     */
    private static final int KEY_SIZE = 256;

    /**
     * 加密/解密算法名称
     */
    private static final String ALGORITHM = "AES";

    /**
     * 随机数生成器（RNG）算法名称
     */
    private static final String RNG_ALGORITHM = "SHA1PRNG";

    /**
     * AES加密/解密用的原始密码
     */
    private static final String SECRET_KET = "tspUser@2020";

    /**
     * 利用java原生的类实现SHA256加密
     *
     * @param str 原始报文
     * @return 加密后的报文
     */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }


    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return 转换后的16进制字符串
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 生成密钥对象
     */
    private static SecretKey generateKey(byte[] key) throws Exception {
        // 创建安全随机数生成器
        SecureRandom random = SecureRandom.getInstance(RNG_ALGORITHM);
        // 设置 密钥key的字节数组 作为安全随机数生成器的种子
        random.setSeed(key);

        // 创建 AES算法生成器
        KeyGenerator gen = KeyGenerator.getInstance(ALGORITHM);
        // 初始化算法生成器
        gen.init(KEY_SIZE, random);

        // 生成 AES密钥对象, 也可以直接创建密钥对象: return new SecretKeySpec(key, ALGORITHM);
        return gen.generateKey();
    }

    /**
     * 数据加密: 明文 -> 密文
     */
    public static String aesEncrypt(String originContent) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(SECRET_KET.getBytes());
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器（加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        // 加密数据, 返回密文
        byte[] cipherBytes = cipher.doFinal(originContent.getBytes());
        return String.valueOf(cipherBytes);
    }

    /**
     * 数据解密: 密文 -> 明文
     */
    public static String aesDecrypt(String secretContent) throws Exception {
        // 生成密钥对象
        SecretKey secKey = generateKey(SECRET_KET.getBytes());
        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化密码器（解密模型）
        cipher.init(Cipher.DECRYPT_MODE, secKey);
        // 解密数据, 返回明文
        byte[] plainBytes = cipher.doFinal(secretContent.getBytes());
        return String.valueOf(plainBytes);
    }
}
