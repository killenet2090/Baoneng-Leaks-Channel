package gaea.vehicle.basic.service.config;
import java.lang.annotation.*;

/**
 * @ClassName: Weblog
 * @Description: 日志注解
 * @author: xiajiankang1
 * @date: 2020/04/24
 *
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {

    String description() default "";

}

