package com.bnmotor.icv.tsp.ble.util;

import com.bnmotor.icv.adam.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class MD5Builder {
    private static final Logger logger = LoggerFactory.getLogger(MD5Builder.class);
    // 用来将字节转换成 16 进制表示的字符
    static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /** */
    /**
     * 对文件全文生成MD5摘要
     *
     * @param file 要加密的文件
     * @return MD5摘要码
     */
    public static String getMD5(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            logger.info("MD5摘要长度：" + md.getDigestLength());
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            logger.info("开始生成摘要");
            long s = System.currentTimeMillis();
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            logger.info("摘要生成成功,总用时: " + (System.currentTimeMillis() - s)
                    + "ms");
            byte[] b = md.digest();
            return byteToHexStringSingle(b);//byteToHexString(b);
            // 16位加密
            // return buf.toString().substring(8, 24);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /** */
    /**
     * 对一段String生成MD5加密信息
     *
     * @param message 要加密的String
     * @return 生成的MD5信息
     */
    public static String getMD5(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            logger.info("MD5摘要长度：" + md.getDigestLength());
            byte[] b = md.digest(message.getBytes("utf-8"));
            return byteToHexStringSingle(b);//byteToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    /** */
    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     * @param tmp    要转换的byte[]
     * @return 十六进制字符串表示形式
     */
    private static String byteToHexString(byte[] tmp) {
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        return s;
    }

    /**
     * 独立把byte[]数组转换成十六进制字符串表示形式
     *
     * @param byteArray
     * @return
     * @author Bill
     * @create 2010-2-24 下午03:26:53
     * @since
     */
    public static String byteToHexStringSingle(byte[] byteArray) {
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

    /**
     * 将字符转为16位字节码  TODO 算法
     *
     * @param origin
     * @return
     */
    public static String bit16(String origin) {
        String result = "";
        Optional<String> md5Value = Optional.ofNullable(getMD5(origin));
        if (md5Value.isPresent()) {
            result = md5Value.get().substring(8, 24);
            return result;
        }else {
            return StringUtil.EMPTY_STRING;
        }

    }

}
