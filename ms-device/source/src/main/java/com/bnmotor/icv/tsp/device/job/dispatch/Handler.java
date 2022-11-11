package com.bnmotor.icv.tsp.device.job.dispatch;

import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
    /**
     * 任务类型:一次性增量任务;持续性全量任务
     */
    TaskType taskType() default TaskType.ONCE;

    /**
     * 查询类型:全量查询；根据标签查询；根据车型查询；根据配置查询
     */
    QueryType queryType() default QueryType.ALL;

    /**
     * 数据类型:车辆数据；车辆标签数据；零部件数据
     */
    DataType dataType() default DataType.VEHICLE;
}
