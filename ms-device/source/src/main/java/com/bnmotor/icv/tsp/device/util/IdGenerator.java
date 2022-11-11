package com.bnmotor.icv.tsp.device.util;

import java.util.UUID;

/**
 * @ClassName: IdGenerator
 * @Description: id生成器
 * @author: zhangwei2
 * @date: 2021/1/12
 * @Copyright: 2021 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class IdGenerator {
    public static String getDeviceIdByUUId() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        return System.currentTimeMillis() / 1000 + String.format("%010d", hashCodeV);
    }
}
