package com.bnmotor.icv.tsp.vehstatus.config.convert;

import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import lombok.Setter;

import java.util.HashMap;

/**
 * @ClassName: VehStatusConvertForMap
 * @Description: 车况转换map
 * @author: huangyun1
 * @data: 2020-06-15
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Setter
public class VehStatusConvertForMap<K> extends HashMap<K, VehStatusEnum>
{

    private static VehStatusEnum convert;

    @Override
    public VehStatusEnum get(Object key)
    {
        return super.getOrDefault(key, convert);
    }
}
