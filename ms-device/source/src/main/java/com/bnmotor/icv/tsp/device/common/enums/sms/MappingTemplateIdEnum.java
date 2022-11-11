package com.bnmotor.icv.tsp.device.common.enums.sms;

import com.bnmotor.icv.adam.core.enums.BaseEnum;

/**
 * @ClassName: MappingTemplateIdEnum
 * @Description: 映射模板id枚举
 * @author: huangyun1
 * @date: 2020/8/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum MappingTemplateIdEnum implements BaseEnum<Integer> {
    /**
     * 车辆绑定成功提醒
     */
    BIND_SUCCESS_MSG(300020001, "车辆绑定成功提醒"),
    /**
     * 通知下载app进行车辆绑定激活提醒
     */
    NOTIFY_DOWNLOAD_APP_MSG(300030001, "通知下载app进行车辆绑定激活提醒"),
    /**
     * 车辆绑定验证码
     */
    VEHICLE_BIND_AUTH_CODE(500010001, "车辆绑定验证码"),
    ;
    /**
     * 编码
     */
    private final Integer code;

    private final String desc;

    MappingTemplateIdEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}