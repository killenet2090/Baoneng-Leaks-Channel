package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.tsp.ota.service.ICommonExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName: CommonExecutor
 * @Description:   基本执行器
 * @author: xuxiaochang1
 * @date: 2020/9/8 11:41
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Slf4j
public class CommonExecutor implements ICommonExecutor {

    @Override
    public void execute(Runnable r, String msgDesc) {
        try {
            r.run();
        }catch (Exception e){
            log.error(msgDesc + "异常.msg={}",e.getMessage(), e);
        }
    }
}
