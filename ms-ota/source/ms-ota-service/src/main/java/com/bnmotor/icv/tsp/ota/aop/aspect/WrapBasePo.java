package com.bnmotor.icv.tsp.ota.aop.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName:  WrapBasePo
 * @Description:    客户端提交参数对象是否需要包装createBy/updateBy属性注解
 * @author: xuxiaochang1
 * @date: 2020/6/11 13:30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WrapBasePo {
}
