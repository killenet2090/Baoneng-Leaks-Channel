package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.tsp.ota.common.enums.MyBaseEnum;

import java.util.EnumSet;

/**
 * @ClassName: MyEnumUtil
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/2 16:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyEnumUtil {
    private MyEnumUtil(){}

    /**
     * 获取枚举类实例
     * @param value
     * @param clz
     * @param <E>
     * @return
     */
    public static  <E extends Enum<E> & MyBaseEnum> MyBaseEnum getByValue(Integer value, Class<E> clz){
        return EnumSet.allOf(clz).stream().filter(item -> item.getValue().equals(value)).findFirst().orElse(null);
    }

    /**
     * 获取枚举类实例并检查是否为空
     * @param value
     * @param clz
     * @param <E>
     * @return
     */
    public static  <E extends Enum<E> & MyBaseEnum> MyBaseEnum getByValueWithCheck(Integer value, Class<E> clz){
        MyBaseEnum myBaseEnum = getByValue(value, clz);
        MyAssertUtil.notNull(myBaseEnum, "枚举类型未定义，请检查");
        return myBaseEnum;
    }
}
