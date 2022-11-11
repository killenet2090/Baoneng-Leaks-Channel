package gaea.user.center.service.common.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: LoginLogAnnotation
 * @Description: 登录日志自定义注解
 * @author: jiangchangyuan1
 * @date: 2020/9/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLogAnnotation {

    /**
     * 方法描述,可使用占位符获取参数:{{tel}}
     */
    String detail() default "";
}