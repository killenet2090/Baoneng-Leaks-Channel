package com.bnmotor.icv.tsp.device.job.dispatch;

import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

/**
 * @ClassName: ITaskDispatch
 * @Description: 任务分发器
 * @author: zhangjianghua1
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface ITaskDispatch extends InitializingBean, ApplicationContextAware {
    /**
     * 任务分发器
     */
    void dispatch(VehDataAsynTaskPo taskPo);
}
