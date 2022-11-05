package com.bnmotor.icv.tsp.ble.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtils {
    /**
     * 传入文本内容，返回 SHA-256 串
     *
     * @param data
     * @return
     */
    public static String  SHA256(final String data)
    {
        return SHA(data, "SHA-256");
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     *
     * @param data
     * @return
     */
    public static String SHA512(final String data)
    {
        return SHA(data, "SHA-512");
    }

    /**
     * 字符串 SHA 加密
     *
     * @param data
     * @return
     */
    private static String SHA(final String data, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (data != null && data.length() > 0) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                messageDigest.update(data.getBytes());
                byte byteBuffer[] = messageDigest.digest();
                StringBuffer strHexString = new StringBuffer();

                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

}
