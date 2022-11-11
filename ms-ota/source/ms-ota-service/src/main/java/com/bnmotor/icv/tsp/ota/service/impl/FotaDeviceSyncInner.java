package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.adam.data.redis.lock.annotation.RedisLock;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FotaDevicySyncInner
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/31 17:22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Service
public class FotaDeviceSyncInner {
    @RedisLock(waitTime = 2)
    public void handle(String lockName, Runnable r){
        r.run();
    }
}
