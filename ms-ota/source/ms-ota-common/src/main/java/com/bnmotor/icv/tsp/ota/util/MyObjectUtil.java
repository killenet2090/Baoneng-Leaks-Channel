package com.bnmotor.icv.tsp.ota.util;

import java.util.Objects;

/**
 * @ClassName: MyObjectUtil
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/1/12 15:12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyObjectUtil {
    private MyObjectUtil(){}

    /**
     * 返回给定的值，如果给定的值为空，则返回默认值
     * @param t
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getOrDefaultT(T t, T defaultValue){
        return Objects.nonNull(t) ? t :defaultValue;
    }
}
