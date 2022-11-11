package com.bnmotor.icv.tsp.ota.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: OtaMessageAttribute.java 
 * @Description: 消息路由属性注解
 * @author E.YanLonG
 * @since 2020-11-15 9:03:34
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OtaMessageAttribute {

	String[] topics() default {};

	Class<?> msgtype(); 

	boolean route() default false;
}