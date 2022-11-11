package com.bnmotor.icv.tsp.vehstatus.aop.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: ValidRemoteHandle
 * @Description: 校验是否有效远控操作
 * @author: huangyun1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidRemoteHandle {
    /**
     * vin名称
     */
    String VIN_NAME = "vin";
    /**
     * 参数名称
     * @return
     */
    String argName() default VIN_NAME;

    ArgType argType() default ArgType.VIN;


    /**
     * 参数类型
     */
    enum ArgType {
        /**
         * vin
         */
        VIN(VIN_NAME);

        private String value;

        ArgType(String value) {
            this.value = value;
        }
    }
}
