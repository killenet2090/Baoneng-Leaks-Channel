package com.bnmotor.icv.tsp.vehstatus.common.util;

import com.bnmotor.icv.adam.core.enums.BaseEnum;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: EnumUtils
 * @Description: 枚举工具类
 * @author: huangyun1
 * @date: 2020/7/30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class EnumUtils {
    /**
     * 存放枚举map  key=className value={enum.code, enum}
     */
    private static final Map<String, Map> ENUM_MAP =  new ConcurrentHashMap<>();

    /**
     * 根据code获取描述
     * @param targetType
     * @param code
     * @return
     */
    public static String getDescByCode(Class<? extends BaseEnum> targetType, Object code) {
        if (code == null || StringUtils.isBlank(code.toString())) {
            return new String();
        }
        Map<String, BaseEnum> itemMap = ENUM_MAP.get(targetType.getSimpleName());
        if (itemMap == null) {
            itemMap = Maps.newHashMap();
            BaseEnum[] enums = targetType.getEnumConstants();
            for(BaseEnum e : enums) {
                itemMap.put(String.valueOf(e.getValue()), e);
            }
            ENUM_MAP.putIfAbsent(targetType.getSimpleName(), itemMap);
        }
        BaseEnum result = itemMap.get(String.valueOf(code));
        if (result == null) {
            return null;
            //throw new IllegalArgumentException("No element matches" + code);
        }
        return result.getDescription();
    }

    /**
     * 根据code获取枚举
     * @param targetType
     * @param code
     * @param <T>
     * @return
     */
    public static <T extends BaseEnum> T getEnumByCode(Class<T> targetType, Object code) {
        if (code == null || StringUtils.isBlank(code.toString())) {
            return null;
        }
        Map<String, T> itemMap = ENUM_MAP.get(targetType.getSimpleName());
        if (itemMap == null) {
            itemMap = Maps.newHashMap();
            T[] enums = targetType.getEnumConstants();
            for(T e : enums) {
                itemMap.put(String.valueOf(e.getValue()), e);
            }
            ENUM_MAP.putIfAbsent(targetType.getSimpleName(), itemMap);
        }
        T result = itemMap.get(String.valueOf(code));
        if (result == null) {
            throw new IllegalArgumentException("No element matches" + code);
        }
        return result;
    }

}
