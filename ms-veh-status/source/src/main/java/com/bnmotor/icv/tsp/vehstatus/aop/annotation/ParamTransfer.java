package com.bnmotor.icv.tsp.vehstatus.aop.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: ParamTransfer
 * @Description: 将前端参数转换为需要的实体
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamTransfer {

    /**
     * 字段列表参数所在第几个参数
     */
    int columnsParamIdx();
    /**
     * 组列表参数所在第几个参数
     */
    int groupsParamIdx();

}
