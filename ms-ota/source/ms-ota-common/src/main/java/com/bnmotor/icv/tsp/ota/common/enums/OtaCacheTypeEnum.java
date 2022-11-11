package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;

/**
 * @ClassName: OtaCacheTypeEnum
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/2/24 11:41
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum OtaCacheTypeEnum {
    VIN_CACHE_INFO(1, "升级事务关联数据缓存", "OTA_VIN_"),
    PLAN_CACHE_INFO(2, "升级任务缓存", "OTA_PLAN_"),
    OBJECT_CACHE_INFO(3, "升级车辆缓存", "OTA_OBJECT_"),
    ;
    @Getter
    private Integer type;
    @Getter
    private String desc;
    @Getter
    private String keyPrefix;

    OtaCacheTypeEnum(Integer type, String desc, String keyPrefix){
        this.type = type;
        this.desc = desc;
        this.keyPrefix = keyPrefix;
    }
}
