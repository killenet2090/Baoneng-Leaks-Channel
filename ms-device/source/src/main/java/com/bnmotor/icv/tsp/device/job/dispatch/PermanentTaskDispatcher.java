package com.bnmotor.icv.tsp.device.job.dispatch;

import org.springframework.stereotype.Component;

/**
 * @ClassName: PermanentTaskDispatcher
 * @Description: 持续性增量任务分发器, 负责分发任务到对应的handler
 * @author: zhangwei2
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class PermanentTaskDispatcher extends AbstractTaskDispatch {
    @Override
    public void afterPropertiesSet() {
        scanAsynHandler(TaskType.PERMANENT);
    }
}
