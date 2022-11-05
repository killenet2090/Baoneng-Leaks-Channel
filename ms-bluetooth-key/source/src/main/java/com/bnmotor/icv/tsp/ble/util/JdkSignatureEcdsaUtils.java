package com.bnmotor.icv.tsp.ble.util;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

public class JdkSignatureEcdsaUtils {
    public static final String ALGORITHM = "EC";
    public static final String SIGN_ALGORITHM = "SHA384withECDSA";

    // 初始化密钥对
    public static KeyPair initKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
            generator.initialize(ecGenParameterSpec, new SecureRandom());
            generator.initialize(256);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 获取公钥
    public static byte[] getPublicKey(KeyPair keyPair) {
        byte[] bytes = keyPair.getPublic().getEncoded();
        return bytes;
    }

    // 获取公钥
    public static String getPublicKeyStr(KeyPair keyPair) {
        byte[] bytes = keyPair.getPublic().getEncoded();
        return encodeHex(bytes);
    }

    // 获取私钥
    public static byte[] getPrivateKey(KeyPair keyPair) {
        byte[] bytes = keyPair.getPrivate().getEncoded();
        return bytes;
    }

    // 获取私钥
    public static String getPrivateKeyStr(KeyPair keyPair) {
        byte[] bytes = keyPair.getPrivate().getEncoded();
        return encodeHex(bytes);
    }

    // 签名
    public static byte[] sign(byte[] data, byte[] privateKey, String signAlgorithm) {
        try {
            // 还原使用
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 1、实例化Signature
            Signature signature = Signature.getInstance(signAlgorithm);
            // 2、初始化Signature
            signature.initSign(priKey);
            // 3、更新数据
            signature.update(data);
            // 4、签名
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 验证
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign, String signAlgorithm) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            // 1、实例化Signature
            Signature signature = Signature.getInstance(signAlgorithm);
            // 2、初始化Signature
            signature.initVerify(pubKey);
            // 3、更新数据
            signature.update(data);
            // 4、签名
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] privateKey) throws Exception {

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, "SunEC");

        ECPrivateKey priKey = (ECPrivateKey) keyFactory
                .generatePrivate(pkcs8KeySpec);

        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),
                priKey.getParams());

        // 对数据解密
        // TODO Chipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        //Cipher cipher = Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
        cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

        return cipher.doFinal(data);
    }
    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] publicKey) throws Exception {
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);

        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, "SunEC");

        ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);

        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(), pubKey.getParams());

        // 对数据加密
        // TODO Chipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        //Cipher cipher = Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());

        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] cache = cipher.doFinal(data);
        return cache;
    }

    // 数据准16进制编码
    public static String encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    // 数据转16进制编码
    public static String encodeHex(final byte[] data, final boolean toLowerCase) {
        final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }

    public static void main(String[] args) throws Exception {
        String str = "宝能汽车kldfjkldjsfkldjskfjdsklfjdkasjfdkasjfkldasjfkldasjfkldasjfklasjdfkldasjfkldasjfklasjfljdsjfkdjas ";
        byte[] data = str.getBytes();
        // 初始化密钥度
        KeyPair keyPair = initKey();
        byte[] publicKey = getPublicKey(keyPair);
        byte[] privateKey = getPrivateKey(keyPair);

        System.out.println(Base64.encodeBase64String(publicKey));
        System.out.println(Base64.encodeBase64String(privateKey));

        // 签名
        byte[] sign = sign(str.getBytes(), privateKey, SIGN_ALGORITHM);
        System.out.println(Base64.encodeBase64String(sign));


        // 验证
        boolean b = verify(data, publicKey, sign, SIGN_ALGORITHM);
        System.out.println("验证:" + b);

        byte[] ble = encrypt("wwwwwuuuuuuuwwww".getBytes("UTF-8"),publicKey);
        System.out.println(Base64.encodeBase64String(ble));
    }
}
