package com.bnmotor.icv.tsp.device.job.dispatch;

import com.bnmotor.icv.tsp.device.job.handler.IAsynHandler;
import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import com.bnmotor.icv.tsp.device.service.mq.producer.dataaysn.DataType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AbstractTaskDispatch
 * @Description: 抽象任务分发器
 * @author: zhangwei2
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public abstract class AbstractTaskDispatch implements ITaskDispatch {
    /**
     * 通过实现接口注入
     */
    private ApplicationContext applicationContext;
    /**
     * 用于存储车辆数据处理handler
     */
    protected Map<Integer, IAsynHandler> vehicleHandlers = new HashMap<>();
    /**
     * 用于存储设备数据处理handler
     */
    protected Map<Integer, IAsynHandler> deviceHandlers = new HashMap<>();
    /**
     * 标签处理器
     */
    protected Map<Integer, IAsynHandler> tagHandlers = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @param taskType 任务类型
     */
    public void scanAsynHandler(TaskType taskType) {
        Map<String, IAsynHandler> beanMap = applicationContext.getBeansOfType(IAsynHandler.class);
        //遍历该接口的所有实现，将其放入map中
        for (IAsynHandler asynHandler : beanMap.values()) {
            boolean b = asynHandler.getClass().isAnnotationPresent(Handler.class);
            if (b) {
                Handler handler = asynHandler.getClass().getAnnotation(Handler.class);
                if (handler == null) {
                    continue;
                }

                TaskType tempTaskType = handler.taskType();
                if (tempTaskType != taskType) {
                    continue;
                }

                if (DataType.VEHICLE == handler.dataType()) {
                    vehicleHandlers.put(handler.queryType().getType(), asynHandler);
                }
                if (DataType.VEH_DEVICE_MODEL == handler.dataType()) {
                    deviceHandlers.put(handler.queryType().getType(), asynHandler);
                }
                if (DataType.TAG == handler.dataType()) {
                    tagHandlers.put(handler.queryType().getType(), asynHandler);
                }
            }
        }
    }

    /**
     * 任务分发
     *
     * @param taskPo 任务
     */
    @Override
    public void dispatch(VehDataAsynTaskPo taskPo) {
        DataType dataType = DataType.valueOf(taskPo.getDataType());

        QueryType queryType = QueryType.valueOf(taskPo.getQueryType());
        if (dataType == null || queryType == null) {
            return;
        }

        switch (dataType) {
            case VEHICLE:
                IAsynHandler asynHandler = vehicleHandlers.get(queryType.getType());
                if (asynHandler == null) {
                    return;
                }
                asynHandler.processData(taskPo);
                break;
            case VEH_DEVICE_MODEL:
                IAsynHandler deviceHandler = deviceHandlers.get(queryType.getType());
                if (deviceHandler == null) {
                    return;
                }
                deviceHandler.processData(taskPo);
                break;
            case TAG:
                IAsynHandler tagHandler = tagHandlers.get(queryType.getType());
                if (tagHandler == null) {
                    return;
                }
                tagHandler.processData(taskPo);
            default:
        }
    }
}
