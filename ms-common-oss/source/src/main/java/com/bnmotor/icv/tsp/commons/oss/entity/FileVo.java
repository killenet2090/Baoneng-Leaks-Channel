package com.bnmotor.icv.tsp.commons.oss.entity;

import lombok.Data;

@Data
public class FileVo {
    /**
     * 文件相对路径
     */
    private String relativePath;

    /**
     * 视频缩略图
     */
    private String thumbnailPath = "";
}
