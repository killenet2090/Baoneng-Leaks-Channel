package com.bnmotor.icv.tsp.ota.service;

import com.bnmotor.icv.tsp.ota.model.req.FileUploadReq;
import com.bnmotor.icv.tsp.ota.model.req.upload.UploadDto;
import com.bnmotor.icv.tsp.ota.model.resp.FileUploadVo;
import com.bnmotor.icv.tsp.ota.model.resp.upload.UploadVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName: fotaDownloadProcessPo
 * @Description: 文件上传记录表 服务类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

public interface IFotaFileUploadRecordService {
    /**
     * ，需要验证文件是否完整有效
     * @param multipartFile
     * @param fileUploadReq
     * @return
     */
    FileUploadVo upload(MultipartFile multipartFile, FileUploadReq fileUploadReq);

    /**
     * 上传分片文件
     *
     * 步骤：
     * 1、判断数据库中是否已经有上传记录，若存在，则认为当前sha码对应的文件已经上传完成
     * 2、若不存在，则判断分块文件是否还存在未上传成功的分片集合。
     *  2.1、若存在，判断当前分块是否需要上传。当前文件上传完成，重新走2的逻辑；若继续走2的逻辑还存在未上传成功的逻辑，则退出（尝试一次文件上传成功后的检查）
     *  2、2、若不存在，尝试合并。尝试合并需要获取到竞争锁
     *
     * @param inputStream
     * @param uploadDto
     * @return
     */
    UploadVo uploadSliceFile(InputStream inputStream, UploadDto uploadDto);

    /**
     * 上传分片文件
     * @param multipartFile
     * @param uploadDto
     * @return
     */
    UploadVo uploadSliceFile(MultipartFile multipartFile, UploadDto uploadDto);

    /**
     * 合并文件
     * @param uploadDto
     * @return
     */
    UploadVo fileMerge(UploadDto uploadDto);

    /**
     * 检查文件是否上传完成:0=未完成，1=已完成
     * @param fileSha256
     * @return UploadVo
     */
    UploadVo fileCheck(String fileSha256);

    /**
     * 删除文件上传记录
     * @param fileIds
     */
    void delByFileIds(Collection<Long> fileIds);

    void delByFileIdsTest(List<Long> fileIds);
}
