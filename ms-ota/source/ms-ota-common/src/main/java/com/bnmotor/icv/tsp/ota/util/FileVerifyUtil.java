package com.bnmotor.icv.tsp.ota.util;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.function.Function;

/**
 * @ClassName:  FileVerifyUtil
 * @Description:   文件验证工具类
 * @author: xuxiaochang1
 * @date: 2020/6/12 9:44
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class FileVerifyUtil {
    private static Logger logger = LoggerFactory.getLogger(FileVerifyUtil.class);

    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 默认缓存大小8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    private FileVerifyUtil(){}

    public static enum FileVerifyEnum{
        /*MD5("MD5", new MD5HashFun()),*/
        SHA256("SHA-256", new SHA256HashFun())
    ;
        @Getter
        private String type;
        @Getter
        private Function<InputStream, String> hashFunction;

        private FileVerifyEnum(String type, Function<InputStream, String> hashFunction){
            this.type = type;
            this.hashFunction = hashFunction;
        }

        /**
         * 获取FileVerifyEnum
         * @param type
         * @return
         */
        public static FileVerifyEnum getByType(String type){
            return EnumSet.allOf(FileVerifyEnum.class).stream().filter(item -> item.type.equals(type)).findFirst().orElse(null);
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static boolean verify(InputStream inputStream, String code/*, String type*/){
        String caculateCode = caculate(inputStream/*, type*/);
        logger.debug("input.code={}, caculateCode={}", code, caculateCode);
        return caculateCode.equals(code);
    }

    /**
     * 计算文件验证码
     * @param inputStream
     * @return
     */
    public static String caculate(InputStream inputStream/*, FileVerifyEnum fileVerifyEnum*/){
        String code = FileVerifyEnum.SHA256.hashFunction.apply(inputStream);
        logger.debug("code={}", code);
        return code;
    }

    /**
     *
     * @param inputStream
     * @return
     */
    public static String sha256(InputStream inputStream){
        return caculate(inputStream);
    }


    static class SHA256HashFun implements Function<InputStream, String>{
        @Override
        public String apply(InputStream inputStream) {
            return inputStreamHash(inputStream, "SHA-256");
        }
    }

    public static String inputStreamHash(InputStream inputStream, String hashFunName) {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest = MessageDigest.getInstance(hashFunName);
            // 使用DigestInputStream
            digestInputStream = new DigestInputStream(inputStream, messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer = new byte[bufferSize];
            while (true) {
                try {
                    if (!(digestInputStream.read(buffer) > 0)) {
                        break;
                    }
                } catch (IOException e) {
                    logger.error("读取数据流异常.e.getMessage={}", e.getMessage(), e);
                }
            }
            // 获取最终的MessageDigest
            messageDigest = digestInputStream.getMessageDigest();
            // 拿到结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } finally {
            try {
                digestInputStream.close();
            } catch (Exception e) {
                logger.error("计算hash异常", e);
            }
        }
    }

    /**
     * 二进制数组转换到字符串
     * @param byteArray
     * @return
     */
    public static String byteArrayToHex(byte[] byteArray) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = DIGITS_LOWER[b>>> 4 & 0xf];
            resultCharArray[index++] = DIGITS_LOWER[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }
}
