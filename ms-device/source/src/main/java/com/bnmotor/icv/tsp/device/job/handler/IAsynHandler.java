package com.bnmotor.icv.tsp.device.job.handler;

import com.bnmotor.icv.tsp.device.model.entity.VehDataAsynTaskPo;

/**
 * @ClassName: IAsynHandler
 * @Description: 同步策略接口定义
 * @author: zhangjianghua1
 * @date: 2020/11/18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IAsynHandler {
    /**
     * 处理任务
     *
     * @param taskPo 任务对象
     */
    void processData(VehDataAsynTaskPo taskPo);
}
