package com.bnmotor.icv.tsp.ble.util;

import java.nio.charset.Charset;

/**
 * @ClassName: ByteUtil
 * @Description: 日期工具类
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ByteUtil {
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }

    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "GBK");
    }

    /**
     * 转换byte数组为Char（小端）
     *
     * @return
     * @note 数组长度至少为2，按小端方式转换
     */
    public char Bytes2Char_LE(byte[] bytes) {
        if (bytes.length < 2) {
            return (char) -1;
        }
        int iRst = (bytes[0] & 0xFF);
        iRst |= (bytes[1] & 0xFF) << 8;

        return (char) iRst;
    }


    /**
     * 转换byte数组为char（大端）
     *
     * @return
     * @note 数组长度至少为2，按小端方式转换
     */
    public char Bytes2Char_BE(byte[] bytes) {
        if (bytes.length < 2) {
            return (char) -1;
        }
        int iRst = (bytes[0] << 8) & 0xFF;
        iRst |= bytes[1] & 0xFF;

        return (char) iRst;
    }
}
