package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.TBoxRespCodeEnum;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @ClassName: MyAssertUtil
 * @Description:    Assert包装，抛出业务异常
 * @author: xuxiaochang1
 * @date: 2020/6/19 16:59
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyAssertUtil extends Assert {
    private MyAssertUtil(){}


    public static void state(boolean expression, OTARespCodeEnum otaRespCodeEnum) {
        if (!expression) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void state(boolean expression, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (!expression) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void isTrue(boolean expression, OTARespCodeEnum otaRespCodeEnum) {
        if (!expression) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void isTrue(boolean expression, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (!expression) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void notEmpty(String expression, OTARespCodeEnum otaRespCodeEnum) {
        if (StringUtils.isEmpty(expression)) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void notEmpty(String expression, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (StringUtils.isEmpty(expression)) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void isNull(@Nullable Object object, OTARespCodeEnum otaRespCodeEnum) {
        if (object != null) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void isNull(@Nullable Object object, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (object != null) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void notNull(@Nullable Object object, OTARespCodeEnum otaRespCodeEnum) {
        if (object == null) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void notNull(@Nullable Object object, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (object == null) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void hasLength(@Nullable String text, OTARespCodeEnum otaRespCodeEnum) {
        if (!StringUtils.hasLength(text)) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void hasLength(@Nullable String text, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (!StringUtils.hasLength(text)) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void hasText(@Nullable String text, OTARespCodeEnum otaRespCodeEnum) {
        if (!StringUtils.hasText(text)) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void hasText(@Nullable String text, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (!StringUtils.hasText(text)) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Object[] array, OTARespCodeEnum otaRespCodeEnum) {
        if (ObjectUtils.isEmpty(array)) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Object[] array, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (ObjectUtils.isEmpty(array)) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, OTARespCodeEnum otaRespCodeEnum) {
        if (CollectionUtils.isEmpty(collection)) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (CollectionUtils.isEmpty(collection)) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, OTARespCodeEnum otaRespCodeEnum) {
        if (CollectionUtils.isEmpty(map)) {
            throw ExceptionUtil.buildAdamException(otaRespCodeEnum);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, TBoxRespCodeEnum tBoxRespCodeEnum) {
        if (CollectionUtils.isEmpty(map)) {
            throw ExceptionUtil.buildTboxAdamException(tBoxRespCodeEnum);
        }
    }
}
