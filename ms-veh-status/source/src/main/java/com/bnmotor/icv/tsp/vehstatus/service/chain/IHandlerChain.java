package com.bnmotor.icv.tsp.vehstatus.service.chain;


import com.bnmotor.icv.tsp.vehstatus.model.request.NotifyEventDto;

/**
 * @ClassName: IHandlerChain
 * @Description: 处理抽象类
 * @author: huangyun1
 * @date: 2020/12/08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IHandlerChain {


    /**
     * 处理器
     * @param notifyEventDto
     */
    void handler(NotifyEventDto notifyEventDto);
}