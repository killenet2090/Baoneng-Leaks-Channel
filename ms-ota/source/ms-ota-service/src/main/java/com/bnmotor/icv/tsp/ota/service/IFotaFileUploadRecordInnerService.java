package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.resp.upload.UploadVo;

import java.util.function.Supplier;

/**
 * @ClassName: FotaFileUploadRecordPo
 * @Description: 文件上传记录表 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaFileUploadRecordInnerService{
    /**
     * 合并文件并返回文件信息
     *
     * @param redisLockName
     * @param supplier
     * @return
     */
    UploadVo composeFile(String redisLockName, Supplier<UploadVo> supplier) /*throws MyRedisException*/;
}
