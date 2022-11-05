package com.bnmotor.icv.tsp.ble.util;

import java.security.SecureRandom;

public class RandomUnit {
    public static final Integer RANDOM_ONE = 1; //生成随机数字

    public static final  Integer RANDOM_TWO = 2; //生成随机字符串

    /**
     * @param length 生成随机数的长度
     * @param randomType 生成随机数类型
     *
     * @return
     */
    public static String generateRandom(Integer length, Integer randomType){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i ++){
            switch (randomType.intValue()){
                case 1:
                    sb.append(getRandomInteger());
                    break;
                case 2:
                    sb.append(getRandomChar());
                    break;
                default:
                    sb.append(getRandomChar());

            }
        }
        return sb.toString();
    }

    public static String getRandomInteger(){
        return String.valueOf(Math.round(Math.random() * 9.0D));
    }

    public static String getRandomChar(){
        return  String.valueOf((char)(int)Math.round(Math.random() * 25.0D + 65.0D));
    }

    public static byte[] getSecureRandom(){
        byte[] values = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(values);
        return values;
    }
}
