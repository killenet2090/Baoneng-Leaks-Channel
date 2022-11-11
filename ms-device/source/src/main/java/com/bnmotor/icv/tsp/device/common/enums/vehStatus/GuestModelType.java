package com.bnmotor.icv.tsp.device.common.enums.vehStatus;

import com.bnmotor.icv.adam.core.enums.BaseEnum;
import lombok.Getter;

/**
 * @Description 临客模式子类型
 * @Author zhangwei2
 * @Date 2020/2/24
 */
public enum GuestModelType implements BaseEnum<Integer> {
    GUSET_MODE(5, "临客模式"),
    GUEST_MODEL_ING(51, "临客模式开启中"),
    GUEST_MODEL_CLOSING(52, "临客模式关闭中"),
    GUEST_MODEL_CLOSE(53, "临客模式关闭"),
    GUEST_MODEL_FAILED(54, "临客模式设置失败"),
    GUEST_MODEL_CLOSE_FAILED(55, "取消临客模式设置失败");

    @Getter
    private Integer type;
    @Getter
    private String description;

    GuestModelType(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static GuestModelType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (GuestModelType lifecircleEnum : GuestModelType.values()) {
            if (lifecircleEnum.type.equals(type)) {
                return lifecircleEnum;
            }
        }
        return null;
    }

    /**
     * 状态机转换,用户将临客模式转换为车辆生命周期模式
     */
    public static VehLifecircleStatus transform(GuestModelType guestModel) {
        switch (guestModel) {
            case GUEST_MODEL_ING:
                return VehLifecircleStatus.GUEST_MODE_OPENING;
            case GUEST_MODEL_CLOSING:
                return VehLifecircleStatus.GUEST_MODE_CLOSING;
            case GUEST_MODEL_CLOSE:
            case GUEST_MODEL_FAILED:
                return VehLifecircleStatus.USER_MODE;
            case GUSET_MODE:
            case GUEST_MODEL_CLOSE_FAILED:
                return VehLifecircleStatus.GUEST_MODE;
        }
        return null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getValue() {
        return type;
    }
}
