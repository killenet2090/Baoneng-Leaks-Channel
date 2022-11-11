package com.bnmotor.icv.tsp.vehstatus.aop.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: ResultTransfer
 * @Description: 将结果转换为前端需要的实体
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultTransfer {

}
