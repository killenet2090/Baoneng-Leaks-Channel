package com.bnmotor.icv.tsp.ota.service;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public interface OssService {
    /**
     * 文件上传
     * @param filePath
     * @param fileKey
     * @throws IOException
     */
    void uploadFile(String filePath, String fileKey) throws IOException;

    /**
     * 文件下载
     *
     *
     * @param bucketName
     * @param file
     * @param fileKey
     * @throws IOException
     */
    void downloadFile(String bucketName, File file, String fileKey);

    /**
     * 文件url
     * @param ducket
     * @param fileKey
     * @return
     */
    String getObjectUrl(String ducket, String fileKey);
}
