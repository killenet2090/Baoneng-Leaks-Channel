package com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @ClassName: ThirdPartySvcAnno
 * @Description:
 * @author liuyiwei1
 * @date 2020-08-17 9:48
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ThirdPartySvcAnno {

    String value() default "";

    /**
     * 服务提供商列表
     * @return
     */
    String provider() default "";

    /**
     * 服务接口业务类别
     * @return
     */
    String service() default "";
}
