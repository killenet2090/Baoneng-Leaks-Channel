package com.bnmotor.icv.tsp.device.common.enums.dataaysn;

/**
 * @ClassName: ImportTaskType
 * @Description: 车辆数据导入任务类型
 * @author: zhangjianghua1
 * @date: 2020/11/11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum ControlType {
    PAUSE_CHECK(1, "暂停任务"),
    RESUME_CHECK(2, "恢复任务"),
    DELETE(3, "删除任务"),
    IMPORT(4, "导入");

    private final Integer type;
    private final String desp;

    ControlType(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static ControlType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (ControlType taskType : ControlType.values()) {
            if (taskType.type.equals(type)) {
                return taskType;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesp() {
        return desp;
    }
}
