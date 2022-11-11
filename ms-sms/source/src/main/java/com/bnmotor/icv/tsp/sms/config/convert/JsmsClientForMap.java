package com.bnmotor.icv.tsp.sms.config.convert;

import cn.jsms.api.JSMSClient;
import lombok.Setter;

import java.util.HashMap;

/**
 * @ClassName: JsmsClientForMap
 * @Description: JsmsClient-map
 * @author: huangyun1
 * @data: 2020-06-15
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Setter
public class JsmsClientForMap<K> extends HashMap<K, JSMSClient> {

    private static JSMSClient convert;

    @Override
    public JSMSClient get(Object key)
    {
        return super.getOrDefault(key, convert);
    }
}
