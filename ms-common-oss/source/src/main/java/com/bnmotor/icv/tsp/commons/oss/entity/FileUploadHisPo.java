package com.bnmotor.icv.tsp.commons.oss.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/*
 * @Author:luoyang
 * @Date: 16:28 2020/9/28
 * @Description: 文件上传历史记录持久化对象
 */
@Data
@Builder
public class FileUploadHisPo {

    /*
    * 主键 id
    */
    private Long id;

    /*
     *上传用户id
     */
    private Long userId;

    /*
     *文件大小  单位 （byte）
     */
    private Long fileSize;

    /*
     *源文件名称
     */
    private String originName;

    /*
     *文件名称 （重新生成的文件名称）
     */
    private String fileName;

    /*
     *文件来源 1 本地文件 2 网络文件
     */
    private Short fileSource;

    /*
     *文件所属桶
     */
    private String bucket;
    
    /*
     *文件相对路径 （服务器返回前端的相对路径）
     */
    private String relativePath;
    
    /*
     *文件后缀名
     */
    private String fileSuffix;
    
    /*
     *文件归属组
     */
    private String fileGroup;

    /*
    * 文件上传的时间
     */
    private String uploadTime;



}
