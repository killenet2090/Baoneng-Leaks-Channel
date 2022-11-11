package com.bnmotor.icv.tsp.ota.model.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: FotaVinCacheInfo
 * @Description:  车辆升级缓存相关信息
 * @author: xuxiaochang1
 * @date: 2020/10/19 11:01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaVinCacheInfo implements Serializable {
    private static final long serialVersionUID = -1197598050971610405L;
    /**
     * 车辆Vin码
     */
    private String vin;

    /**
     * 车辆对象Id
     */
    private Long objectId;

    /**
     * 车辆任务Id
     */
    private Long otaPlanId;

    /**
     * 车辆任务升级关系数据Id
     */
    private Long otaPlanObjectId;

    /**
     * 车辆当前所处事务Id
     */
    private Long transId;

    /**
     * 版本检查请求Id
     */
    private Long checkReqId;
}
