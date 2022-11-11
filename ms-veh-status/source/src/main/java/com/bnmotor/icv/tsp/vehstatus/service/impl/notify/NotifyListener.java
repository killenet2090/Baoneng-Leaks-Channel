package com.bnmotor.icv.tsp.vehstatus.service.impl.notify;

import com.bnmotor.icv.tsp.vehstatus.service.chain.IHandlerChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: NotifyListener
 * @Description: 通知监听器
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class NotifyListener implements ApplicationListener<AbstractNotifyEvent> {

    @Autowired
    private List<IHandlerChain> handlerChainList;

    @Override
    public void onApplicationEvent(AbstractNotifyEvent notifyEvent) {
        notifyEvent.notifyOther(handlerChainList);
    }
}
