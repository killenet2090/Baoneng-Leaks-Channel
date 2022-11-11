package com.bnmotor.icv.tsp.vehstatus.service.impl.notify;

import com.bnmotor.icv.tsp.vehstatus.model.request.NotifyEventDto;
import com.bnmotor.icv.tsp.vehstatus.service.chain.IHandlerChain;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName: NotifyEvent
 * @Description: 通知事件实体
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public class NotifyEvent extends AbstractNotifyEvent {

    public NotifyEvent(NotifyEventDto source) {
        super(source);
    }

    /**
     * 通知其它业务操作
     */
    @Override
    public void notifyOther(List<IHandlerChain> handlerChainList) {
        Object params = this.getSource();
        if (!(params instanceof NotifyEventDto)) {
            if (log.isDebugEnabled()) {
                log.debug("通知入参有误[{}]", params);
            }
            return;
        }
        NotifyEventDto notifyEventDto = (NotifyEventDto)params;
        handlerChainList.stream().forEach(iHandlerChain -> {
            iHandlerChain.handler(notifyEventDto);
        });
    }

}
