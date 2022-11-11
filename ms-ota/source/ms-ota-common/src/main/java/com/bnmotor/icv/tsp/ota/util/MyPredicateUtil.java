package com.bnmotor.icv.tsp.ota.util;

import org.springframework.util.CollectionUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @ClassName: MyPredicateUtil
 * @Description:    判断工具类增强
 * @author: xuxiaochang1
 * @date: 2020/6/19 9:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyPredicateUtil extends CollectionUtils {
    private MyPredicateUtil(){}


    /**
     * 判断为真则执行
     * @param t
     * @param predicate
     * @param consumer
     * @param <T>
     */
    public static <T> void ifTest(T t, Predicate<T> predicate, Consumer<T> consumer){
        if(predicate.test(t)){
            consumer.accept(t);
        }
    }

    /**
     * 判断为真则执行consumer, 否则执行elseConsumer
     * @param t
     * @param predicate
     * @param consumer
     * @param elseConsumer
     * @param <T>
     */
    public static <T> void ifTest(T t, Predicate<T> predicate, Consumer<T> consumer, Consumer<T> elseConsumer){
        if(predicate.test(t)){
            consumer.accept(t);
        }else{
            elseConsumer.accept(t);
        }
    }
}
