package com.bnmotor.icv.tsp.ota.model.cache;

import lombok.Data;

/**
 * @ClassName: FotaPlanObjectInfo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/15 14:27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaPlanObjectInfo {
    /**
     * 升级计划Id
     */
    private Long otaPlanId;
    /**
     * 升级计划车辆对象记录Id
     */
    private Long otaPlanObjId;
}
