package com.bnmotor.icv.tsp.ota.model.req.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UploadCachedInfo
 * @Description:    分片信息
 * @author: xuxiaochang1
 * @date: 2020/9/15 9:59
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties
public class UploadCachedInfo implements Serializable {
    private static final long serialVersionUID = 4715477903459715457L;

    /**
     * 分片总数量
     */
    @ApiModelProperty(value = "分片总数量", example = "25", dataType = "int", required = true)
    private Integer chunkCount;

    /**
     * 当前上传文件分片序号
     */
    @ApiModelProperty(value = "当前上传文件分片序号", example = "2", dataType = "int", required = true)
    private Integer partNumber;

    /**
     * 上传文件的Sha256码值
     */
    @ApiModelProperty(value = "上传文件的Sha256码值", example = "eeeeeeeeeeeeeewwwwwwwwwww", dataType = "string", required = true)
    private String fileSha256;

    /**
     * 上传文件名称
     */
    @ApiModelProperty(value = "上传文件名称", example = "bigFile.zip", dataType = "String", required = true)
    private String fileName;

    @ApiModelProperty(value = "文件类型：0=升级包,1=测试报告，2=脚本文件,3=OTA日志文件", example = "0", dataType = "int", required = true)
    private int fileType;

    /**
     * 上传状态 0.上传完成 1.已上传部分
     */
    private Integer uploadStatus;

    /**
     * 文件大小(单位字节)
     */
    @ApiModelProperty(value = "文件大小(单位字节)", example = "10000086", dataType = "long")
    private Long fileSize;

    @ApiModelProperty(value = "是否需要验证：0=不需要，1=需要", example = "0", dataType = "String")
    private int needVerify;

    @ApiModelProperty(value = "验证码", example = "12393939932", dataType = "String")
    private String verfiyCode;

    @ApiModelProperty(value = "验证类型：md5=MD5,sha1=SHA1", example = "MD5", dataType = "String")
    private String verifyType;

    @ApiModelProperty(value = "文件记录url", example = "http://file.bnicvc.com/pgk/pkg1.apk", dataType = "string")
    private String url;

    @ApiModelProperty(value = "文件记录Id", example = "10086", dataType = "long")
    private long fileUploadRecordId;

    /**
     * 文件上传完成后的身份证标识
     */
    private String fileKey;

    /**
     * 创建用户
     */
    private String createBy;

    /**
     * 更新用户
     */
    private String updateBy;
}
