package com.bnmotor.icv.tsp.commons.oss.service;

import com.bnmotor.icv.tsp.commons.oss.entity.FileDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileVo;
import com.bnmotor.icv.tsp.commons.oss.entity.NfsFileDto;

import java.util.List;

/**
 * 文件上传接口
 * @author zhangjianghua
 */
public interface IFileService {
    /**
     * 上传文件
     * @param fileDto
     * @return
     */
    List<FileVo> uploadFile(FileDto fileDto ,Long uid);

    /**
     * 读取网络文件，上传至OSS
     * @param fileDto
     * @return
     */
    List<FileVo> uploadFile(NfsFileDto fileDto ,Long uid);

    /**
     * 获取文件临时访问路径
     * @param bucket
     * @param fileName
     * @return
     */
    String obtainFileEphemeralUrl(String bucket, String fileName);

    /**
     * 获取文件临时访问路径
     * @param bucket
     * @param fileName
     * @param expires
     * @return
     */
    String obtainFileEphemeralUrl(String bucket, String fileName, Integer expires);
}
