package com.bnmotor.icv.tsp.ota.common.enums;

import lombok.Getter;

import java.util.EnumSet;

/**
 * @ClassName:
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/6/11 14:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum DeviceTreeNodeLevelEnum {
    /**
     * 品牌
     */
    BRAND(0, "品牌", "brand"),

    /**
     * 车系
     */
    SERIES(1, "车系", "series"),

    /**
     * 车型
     */
    MODEL(2, "车型", "model"),

    /**
     * 年款
     */
    YEAR(3, "年款", "year"),

    /**
     * 配置
     */
    CONF(4, "配置", "conf"),

    /*ECU(5, "零件", "ecu"),*/
    ;
    @Getter
    private int level;
    @Getter
    private String name;
    @Getter
    private String code;

    private DeviceTreeNodeLevelEnum(int level, String name, String code){
        this.level = level;
        this.name = name;
        this.code = code;
    }

    /**
     * 获取DeviceTreeNodeLevelEnum
     * @param level
     * @return
     */
    public static DeviceTreeNodeLevelEnum getByLevel(int level){
        return EnumSet.allOf(DeviceTreeNodeLevelEnum.class).stream().filter(item -> item.level==level).findFirst().orElse(null);
    }
}
