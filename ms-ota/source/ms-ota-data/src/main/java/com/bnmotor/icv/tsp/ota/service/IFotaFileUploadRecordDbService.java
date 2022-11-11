package com.bnmotor.icv.tsp.ota.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFileUploadRecordPo;

/**
 * @ClassName: fotaDownloadProcessPo
 * @Description: 文件上传记录表 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaFileUploadRecordDbService extends IService<FotaFileUploadRecordPo> {
    /**
     * 查找文件上传记录
     * @param fileSha256
     * @return
     */
    FotaFileUploadRecordPo findFotaFileUploadRecordPo(String fileSha256);
}
