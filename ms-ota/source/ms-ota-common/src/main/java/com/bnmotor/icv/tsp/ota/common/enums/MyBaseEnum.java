package com.bnmotor.icv.tsp.ota.common.enums;

/**
 * @ClassName: MyBaseEnum
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/2 15:58
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MyBaseEnum<T> {

    T getValue();

    String getDesc();
}
