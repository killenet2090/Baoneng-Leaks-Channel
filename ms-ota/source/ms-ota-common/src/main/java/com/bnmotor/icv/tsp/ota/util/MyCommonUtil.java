package com.bnmotor.icv.tsp.ota.util;

import com.google.common.base.Stopwatch;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: MyCommonUtil
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/15 16:22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class MyCommonUtil {
    private static Logger logger = LoggerFactory.getLogger(MyCodecUtil.class);

    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 默认缓存大小8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    public static String sha256(File file) {
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        MessageDigest messageDigest = null;
        int read;
        try(FileInputStream in = new FileInputStream(file)){
            messageDigest = MessageDigest.getInstance("SHA-256");
            while ((read = in.read(buffer, 0, DEFAULT_BUFFER_SIZE)) > -1) {
                messageDigest.update(buffer, 0, read);
            }
        }catch(Exception e){
            logger.info("计算文件hash出错：{}:{}",e,e.getMessage());
            return "";
        }

        byte[] data = messageDigest.digest();
        int len = data.length;
        //len*2
        char[] out = new char[len << 1];
        // two characters from the hex value.
        for (int i = 0, j = 0; i < len; i++) {
            // 高位
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            // 低位
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }


    public static String fileMD5(InputStream inputStream, String hashFunName) throws IOException {
        // 缓冲区大小（这个可以抽出一个参数）
        int bufferSize = 256 * 1024;
        /*FileInputStream fileInputStream = null;*/
        DigestInputStream digestInputStream = null;
        try {
            // 拿到一个MD5转换器（同样，这里可以换成SHA1）
            MessageDigest messageDigest =MessageDigest.getInstance(hashFunName);
            // 使用DigestInputStream
            /*fileInputStream = new FileInputStream(inputStream);*/
            digestInputStream = new DigestInputStream(inputStream,messageDigest);
            // read的过程中进行MD5处理，直到读完文件
            byte[] buffer =new byte[bufferSize];
            while (digestInputStream.read(buffer) > 0);
            // 获取最终的MessageDigest
            messageDigest= digestInputStream.getMessageDigest();
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
            }
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }



    public static void main(String[] args) throws IOException, InterruptedException {
        DecimalFormat df = new DecimalFormat("“0.00") ;
        long totalMem = Runtime.getRuntime().totalMemory();
        System.out.println(df.format(totalMem/1000000F)+"MB");


        File file = new File("C:\\Users\\xuxiaochang1.BAONENGMOTOR\\Downloads\\CentOS-7-x86_64-Minimal-2003.iso");
        Stopwatch sw = Stopwatch.createStarted();
        String sha256 = FileVerifyUtil.caculate(new FileInputStream(file));
        totalMem = Runtime.getRuntime().totalMemory();
        System.out.println(df.format(totalMem/1000000F)+"MB");
        sw.stop();
        System.out.println("sha256:" + sha256 + ",花销时间:" + sw.elapsed());
        sw.start();
        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String sha2561 = Hashing.sha256().hashBytes(bytes).toString();
        totalMem = Runtime.getRuntime().totalMemory();
        System.out.println(df.format(totalMem/1000000F)+"MB");
        sw.stop();

        System.out.println("sha256:" + sha2561 + ",花销时间:" + sw.elapsed());

        TimeUnit.SECONDS.sleep(4000);
    }
}

