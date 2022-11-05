package com.bnmotor.icv.tsp.ble.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Slf4j
public class RSAUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(RSAUtil.class);
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小（如果秘钥是1024bit,解密最大块是128,如果秘钥是2048bit,解密最大块是256）
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static byte[] getPrivateKey() throws Exception {
        //InputStream in = RSAUtil.class.getClassLoader().getResourceAsStream(prikeypath);
        ClassPathResource classPathResource = new ClassPathResource("keys/pri.key");
        InputStream in =  classPathResource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String readLine = null;

        while((readLine = br.readLine()) != null) {
            sb.append(readLine);
        }
        in.close();
        return Base64.decodeBase64(sb.toString());
    }
    public static PrivateKey getRsaPrivateKey() throws Exception {
        //InputStream in = RSAUtil.class.getClassLoader().getResourceAsStream(prikeypath);
        ClassPathResource classPathResource = new ClassPathResource("keys/pri.key");
        InputStream in =  classPathResource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String readLine = null;

        while((readLine = br.readLine()) != null) {
                sb.append(readLine);
        }
        in.close();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(sb.toString()));
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (Exception e) {
            LOGGER.error("获取私钥异常",e);
            throw e;
        }
    }

    public static byte[] getPublicKey() throws Exception {
        //InputStream in = RSAUtil.class.getClassLoader().getResourceAsStream(keypath);
        ClassPathResource classPathResource = new ClassPathResource("keys/pub.key");
        InputStream in =  classPathResource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String readLine = null;
        while((readLine = br.readLine()) != null) {
                sb.append(readLine);
        }
        in.close();
        return Base64.decodeBase64(sb.toString());
    }

    public static PublicKey getRsaPublicKey() throws Exception {
        //InputStream in = RSAUtil.class.getClassLoader().getResourceAsStream(keypath);
        ClassPathResource classPathResource = new ClassPathResource("keys/pub.key");
        InputStream in =  classPathResource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String readLine = null;
        while((readLine = br.readLine()) != null) {
            sb.append(readLine);
        }
        in.close();
        byte[] bytes =Base64.decodeBase64(sb.toString());
        System.out.println(bytes.length);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509EncodedKeySpec ks = new X509EncodedKeySpec(Base64.decodeBase64(sb.toString()));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(ks);
        return publicKey;
    }

    public static String dencryptRsa(String str) throws Exception{
        //64位解码加密后的字符串
        byte[] encryptedData = Base64.decodeBase64(str.getBytes("UTF-8"));
        PrivateKey privateKey = getRsaPrivateKey();
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String outStr = new String(decryptedData,"UTF-8");
        return outStr;
    }

    public static String dencrypt(String str,PrivateKey privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] encryptedData = Base64.decodeBase64(str.getBytes("UTF-8"));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        String outStr = new String(decryptedData,"UTF-8");
        return outStr;
    }
    public static String encryptRsa(String data) throws Exception{
        PublicKey pubKey = getRsaPublicKey();
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] dataByte = data.getBytes("UTF-8");
        int inputLen = dataByte.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();

        String outStr = Base64.encodeBase64String(encryptedData);
        return outStr;
    }

    public static String encrypt(String data, PublicKey pubKey) throws Exception{
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] dataByte = data.getBytes("UTF-8");
        int inputLen = dataByte.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();

        String outStr = Base64.encodeBase64String(encryptedData);
        return outStr;
    }

    public static void main(String[] args) throws Exception {
        String data = "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH\n" +
                "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH\n" +
                "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
        System.out.println("----------------------------------------------------------------------");
        //使用公钥加密,第二个参数classpath下的公钥文件
        data = encryptRsa(data);
        System.out.println("加密后的数据:"+data);
        System.out.println("加密后的数据:"+data.length());
        System.out.println("加密后的数据:"+Base64.decodeBase64(data));
        System.out.println("加密后的数据:"+Base64.decodeBase64(data).length);
        //使用私钥解密,第二个参数classpath下的私钥文件
        data = dencryptRsa(data);
        System.out.println("解密后的数据:"+data);
    }
}
