package com.bnmotor.icv.tsp.device.job.dispatch;

/**
 * @ClassName: TaskType
 * @Description: 任务类型
 * @author: zhangwei2
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public enum TaskType {
    ONCE(1, "一次性全量任务"),
    PERMANENT(2, "永久性增量任务");

    private final Integer type;
    private final String desp;

    TaskType(int type, String desp) {
        this.type = type;
        this.desp = desp;
    }

    public static TaskType valueOf(Integer type) {
        if (type == null) {
            return null;
        }
        for (TaskType taskType : TaskType.values()) {
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
