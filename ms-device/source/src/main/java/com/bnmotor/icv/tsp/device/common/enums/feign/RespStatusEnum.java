package com.bnmotor.icv.tsp.device.common.enums.feign;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bnmotor.icv.adam.core.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @ClassName: RespStatusEnum
 * @Description: 响应状态枚举
 * @author: huangyun1
 * @date: 2020/8/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum RespStatusEnum implements BaseEnum<Integer> {
    /**
     * 等待响应
     */
    WAITING(10, "等待响应"),
    /**
     * 执行中
     */
    RUNNING(15, "执行中"),
    /**
     * 成功
     */
    SUCESS(20, "成功"),
    /**
     * 失败
     */
    FAIL(30, "失败"),
    /**
     * 超时
     */
    TIME_OUT(99, "超时"),
    ;

    /**
     * 编码
     */
    @EnumValue
    private final Integer value;

    @JsonValue
    private final String desc;

    RespStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
