package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.adam.core.utils.CodecUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

/**
 * @ClassName: MyCodecUtil
 * @Description: 加密算法工具类
 * @author: xuxiaochang1
 * @date: 2020/7/13 9:39
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyCodecUtil extends CodecUtil {
    private static Logger logger = LoggerFactory.getLogger(MyCodecUtil.class);

    private MyCodecUtil() {
    }

    private static final String ALGORITHM_AES = "AES";
    private static final int KEY_SIZE = 128;
    private static final int CACHE_SIZE = 1024;

    static enum CodecTypeEnum {
        AES("AES"),
        ;

        @Getter
        private String code;

        private CodecTypeEnum(String code) {
            this.code = code;
        }
    }

    /**
     * <p>
     * 生成随机密钥
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static String getSecretKey() throws Exception {
        return getSecretKey(null);
    }

    /**
     * <p>
     * 生成密钥
     * </p>
     *
     * @param seed 密钥种子
     * @return
     * @throws Exception
     */
    public static String getSecretKey(String seed) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
        SecureRandom secureRandom;
        if (seed != null && !"".equals(seed)) {
            secureRandom = new SecureRandom(seed.getBytes());
        } else {
            secureRandom = new SecureRandom();
        }
        keyGenerator.init(KEY_SIZE, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return CodecUtil.encodeBase64String(secretKey.getEncoded());
    }

    /**
     * 构建Cipher
     *
     * @param key
     * @param codecTypeEnum
     * @param mode
     * @return
     * @throws Exception
     */
    private static Cipher genCipher(String key, CodecTypeEnum codecTypeEnum, int mode) throws Exception {
        Key k = toKey(CodecUtil.decodeBase64(key));
        byte[] raw = k.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, codecTypeEnum.getCode());
        Cipher cipher = Cipher.getInstance(codecTypeEnum.getCode());
        cipher.init(mode, secretKeySpec);
        return cipher;
    }

    /**
     * <p>
     * 加密
     * </p>
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String key, CodecTypeEnum codecTypeEnum) throws Exception {
        Cipher cipher = genCipher(key, codecTypeEnum, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(data);
    }

    /**
     * aes加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptAes(byte[] data, String key) throws Exception {
        return encrypt(data, key, CodecTypeEnum.AES);
    }

    /**
     * <p>
     * 文件加密
     * </p>
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void encryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);

        if (sourceFile.exists() && sourceFile.isFile()) {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            if (destFile.createNewFile()) {
                Cipher cipher = genCipher(key, CodecTypeEnum.AES, Cipher.ENCRYPT_MODE);
                try (FileInputStream in = new FileInputStream(sourceFile);
                     FileOutputStream out = new FileOutputStream(destFile);
                     CipherInputStream cin = new CipherInputStream(in, cipher);) {


                    byte[] cache = new byte[CACHE_SIZE];
                    int nRead = 0;
                    while ((nRead = cin.read(cache)) != -1) {
                        out.write(cache, 0, nRead);
                        out.flush();
                    }
                } catch (IOException e) {
                    throw e;
                }
            }
        }

    }

    /**
     * <p>
     * 解密
     * </p>
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String key, CodecTypeEnum codecTypeEnum) throws Exception {
        Cipher cipher = genCipher(key, codecTypeEnum, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }

    /**
     * aes方式解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptAes(byte[] data, String key) throws Exception {
        return decrypt(data, key, CodecTypeEnum.AES);
    }

    /**
     * <p>
     * 文件解密
     * </p>
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void decryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
        File sourceFile = new File(sourceFilePath);
        File destFile = new File(destFilePath);

        if (sourceFile.exists() && sourceFile.isFile()) {
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            if (destFile.createNewFile()) {
                Cipher cipher = genCipher(key, CodecTypeEnum.AES, Cipher.DECRYPT_MODE);

                try (FileInputStream in = new FileInputStream(sourceFile);
                     FileOutputStream out = new FileOutputStream(destFile);
                     CipherOutputStream cout = new CipherOutputStream(out, cipher);) {

                    byte[] cache = new byte[CACHE_SIZE];
                    int nRead = 0;
                    while ((nRead = in.read(cache)) != -1) {
                        cout.write(cache, 0, nRead);
                        cout.flush();
                    }
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

    /**
     * <p>
     * 转换密钥
     * </p>
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_AES);
        return secretKey;
    }

    public static void main(String[] args) throws Exception {
        // 生成RSA公钥/私钥:
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
        kpGen.initialize(2048);
        KeyPair kp = kpGen.generateKeyPair();
        PrivateKey sk = kp.getPrivate();
        PublicKey pk = kp.getPublic();

        // 待签名的消息:
        byte[] message = "Hello, I am Bob!".getBytes(StandardCharsets.UTF_8);

        // 用私钥签名:
        Signature s = Signature.getInstance("SHA1withRSA");
        s.initSign(sk);
        s.update(message);
        byte[] signed = s.sign();
        System.out.println(String.format("signature: %x", new BigInteger(1, signed)));
        //System.out.println(String.format("signature1: %s", new String(signed, "UTF-8")));

        // 用公钥验证:
        Signature v = Signature.getInstance("SHA1withRSA");
        v.initVerify(pk);
        v.update(message);
        boolean valid = v.verify(signed);
        System.out.println("valid? " + valid);
    }
}
