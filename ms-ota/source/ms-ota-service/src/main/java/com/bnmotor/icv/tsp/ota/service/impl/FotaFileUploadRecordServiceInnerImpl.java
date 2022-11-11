package com.bnmotor.icv.tsp.ota.service.impl;

import com.bnmotor.icv.adam.data.redis.lock.annotation.RedisLock;
import com.bnmotor.icv.tsp.ota.model.resp.upload.UploadVo;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordInnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @ClassName: FotaFileUploadRecordServiceInnerImpl
 * @Description: 文件上传记录表 服务实现类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Service
@Slf4j
public class FotaFileUploadRecordServiceInnerImpl implements IFotaFileUploadRecordInnerService {

    @Override
    @RedisLock(waitTime = 5, timeUnit = TimeUnit.MILLISECONDS)
    public UploadVo composeFile(String redisLockName, Supplier<UploadVo> supplier){
        log.info("进入合并文件操作.redisLockName={}", redisLockName);
        return supplier.get();
    }
}
