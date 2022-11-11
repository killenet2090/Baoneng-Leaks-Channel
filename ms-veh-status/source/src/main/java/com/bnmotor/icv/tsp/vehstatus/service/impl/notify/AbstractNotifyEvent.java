package com.bnmotor.icv.tsp.vehstatus.service.impl.notify;

import com.bnmotor.icv.tsp.vehstatus.service.chain.IHandlerChain;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @ClassName: INotifyEvent
 * @Description: 抽象通知事件实体
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public abstract class AbstractNotifyEvent extends ApplicationEvent {

    public AbstractNotifyEvent(Object source) {
        super(source);
    }

    /**
     * 通知其它业务操作
     * @param handlerChainList
     */
    public abstract void notifyOther(List<IHandlerChain> handlerChainList);

}
