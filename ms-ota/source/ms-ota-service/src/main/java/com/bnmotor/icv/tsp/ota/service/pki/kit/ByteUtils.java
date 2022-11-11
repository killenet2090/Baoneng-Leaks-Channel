package com.bnmotor.icv.tsp.ota.service.pki.kit;

/**
 * @ClassName: ByteUtils.java ByteUtils
 * @Description: 供应商提供的方法
 * @author E.YanLonG
 * @since 2020-11-16 9:32:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
	import java.io.IOException;
	import java.util.Arrays;

	public class ByteUtils {
	  public static byte[] int2bytes(int i) {
	    byte[] abyte0 = new byte[4];
	    abyte0[3] = (byte)(0xFF & i);
	    abyte0[2] = (byte)((0xFF00 & i) >> 8);
	    abyte0[1] = (byte)((0xFF0000 & i) >> 16);
	    abyte0[0] = (byte)((0xFF000000 & i) >> 24);
	    return abyte0;
	  }
	  
	  public static byte int2OneByte(int num) {
	    return (byte)(num & 0xFF);
	  }
	  
	  public static byte[] int2Twobytes(int i) {
	    byte[] abyte0 = int2bytes(i);
	    byte[] result = new byte[2];
	    if (abyte0 != null && abyte0.length == 4) {
	      result[0] = abyte0[2];
	      result[1] = abyte0[3];
	      return result;
	    } 
	    return null;
	  }
	  
	  public static int bytes2int(byte[] b) {
	    int res = 0;
	    res <<= 8;
	    res |= b[0] & 0xFF;
	    res <<= 8;
	    res |= b[1] & 0xFF;
	    res <<= 8;
	    res |= b[2] & 0xFF;
	    res <<= 8;
	    res |= b[3] & 0xFF;
	    return res;
	  }
	  
	  public static String getBinaryStrFromByteArr(byte[] bArr) {
	    String result = "";
	    for (byte b : bArr)
	      result = result + getBinaryStrFromByte(b); 
	    return result;
	  }
	  
	  public static String getBinaryStrFromByte(byte b) {
	    String result = "";
	    byte a = b;
	    for (int i = 0; i < 8; i++) {
	      result = (a % 2) + result;
	      a = (byte)(a / 2);
	    } 
	    return result;
	  }
	  
	  public static String byte2hex(byte b) {
	    char[] Digit = { 
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	        'A', 'B', 'C', 'D', 'E', 'F' };
	    char[] c = new char[2];
	    c[0] = Digit[b >>> 4 & 0xF];
	    c[1] = Digit[b & 0xF];
	    String s = new String(c);
	    return s;
	  }
	  
	  public static String Bytes2hex(byte[] b) {
	    StringBuilder ret = new StringBuilder();
	    for (int i = 0; i < b.length; i++) {
	      String hex = Integer.toHexString(b[i] & 0xFF);
	      if (hex.length() == 1)
	        hex = '0' + hex; 
	      ret.append(hex.toLowerCase());
	    } 
	    return ret.toString();
	  }
	  
	  public static byte[] hex2bytes(String hex) {
	    if (hex.length() % 2 != 0)
	      hex = "0" + hex; 
	    int len = hex.length() / 2;
	    byte[] result = new byte[len];
	    char[] achar = hex.toCharArray();
	    for (int i = 0; i < len; i++) {
	      String item = new String(achar, i * 2, 2);
	      result[i] = (byte)Integer.parseInt(item, 16);
	    } 
	    return result;
	  }
	  
	  public static byte[] join(byte[] arr1, byte[] arr2) {
	    byte[] result = new byte[arr1.length + arr2.length];
	    System.arraycopy(arr1, 0, result, 0, arr1.length);
	    System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
	    return result;
	  }
	  
	  public static byte[] join(byte[]... arr) throws IOException {
	    int len = 0;
	    for (int i = 0; i < arr.length; i++)
	      len += (arr[i]).length; 
	    byte[] result = new byte[len];
	    int startPos = 0;
	    for (int j = 0; j < arr.length; j++) {
	      System.arraycopy(arr[j], 0, result, startPos, (arr[j]).length);
	      startPos += (arr[j]).length;
	      arr[j] = null;
	    } 
	    return result;
	  }
	  
	  public static byte[] long2Bytes(long num) {
	    byte[] byteNum = new byte[8];
	    for (int ix = 0; ix < 8; ix++) {
	      int offset = 64 - (ix + 1) * 8;
	      byteNum[ix] = (byte)(int)(num >> offset & 0xFFL);
	    } 
	    return byteNum;
	  }
	  
	  public static long bytes2Long(byte[] byteNum) {
	    long num = 0L;
	    for (int ix = 0; ix < 8; ix++) {
	      num <<= 8L;
	      num |= (byteNum[ix] & 0xFF);
	    } 
	    return num;
	  }
	  
	  public static byte[] pkcs7Padding(byte[] source, int blockSize) {
	    int dataLen = source.length;
	    int mod = dataLen % blockSize;
	    int paddingLen = blockSize - mod;
	    byte paddingByte = (byte)(paddingLen & 0xFF);
	    byte[] plaintext = new byte[dataLen + paddingLen];
	    System.arraycopy(source, 0, plaintext, 0, source.length);
	    Arrays.fill(plaintext, dataLen, plaintext.length, paddingByte);
	    return plaintext;
	  }
	  
	}
