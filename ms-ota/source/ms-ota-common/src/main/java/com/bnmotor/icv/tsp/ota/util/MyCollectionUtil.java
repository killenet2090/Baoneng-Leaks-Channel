package com.bnmotor.icv.tsp.ota.util;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: MyCollectionUtil
 * @Description:    CollectionUtils工具类增强
 * @author: xuxiaochang1
 * @date: 2020/6/19 9:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyCollectionUtil extends CollectionUtils {
    private MyCollectionUtil(){}

    /**
     * 集合类不为空判断
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * Long to String
     * @param value
     * @return
     */
    public static String long2Str(Long value){
        return Objects.nonNull(value) ? Long.toString(value) : null;
    }

    /**
     * 通用包装处理
     * @param collection
     * @param consumer
     * @param <T>
     */
    public static <T> void wrapCollection(Collection<T> collection, Consumer<T> consumer){
        if(MyCollectionUtil.isNotEmpty(collection)){
            collection.forEach(item -> {
                consumer.accept(item);
            });
        }
    }

    /**
     * 转换为新的集合
     * @param list
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> map2NewList(List<T> list, Function<T, R> function){
        if(MyCollectionUtil.isNotEmpty(list)){
            return list.stream().map(function).collect(Collectors.toList());
        }
        return (List<R>)Collections.emptyList();
    }


    /**
     * 转换成新的排序列表
     * @param list
     * @param comparator
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T> List<T> newSortedList(List<T> list, Comparator<? super T> comparator, Consumer<T> consumer){
        if(MyCollectionUtil.isNotEmpty(list)){
            List<T> newList = list.stream().sorted(comparator).collect(Collectors.toList());
            if(Objects.nonNull(consumer)) {
                newList.forEach(item -> {
                    consumer.accept(item);
                });
            }
            return newList;
        }
        return Collections.emptyList();
    }

    /**
     *
     * @param list
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T> List<T> newSortedList(List<T> list, Comparator<? super T> comparator){
        return newSortedList(list, comparator, null);
    }

    /**
     *
     * @param collection
     * @param function
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> newCollection(Collection<T> collection, Function<T, R> function){
        if(MyCollectionUtil.isNotEmpty(collection)){
            return collection.stream().map(function).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 如果为空，返回一个空数据
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> safeList(List<T> list){
        if(MyCollectionUtil.isNotEmpty(list)){
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * 返回集合数量
     * @param collection
     * @return
     */
    public static int size(Collection<?> collection){
    	return isEmpty(collection) ? 0 : collection.size();
    }

    /**
     *
     * @param lists
     * @param keyFun
     * @param <T>
     * @return
     */
    public static <T, R> Map<R, T> toMap(List<T> lists, Function<T, R> keyFun){
        return lists.stream().collect(Collectors.toMap(keyFun, Function.identity()));
    }
}
