package com.bnmotor.icv.tsp.ota.model.req.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: UploadDto
 * @Description:    分片信息
 * @author: xuxiaochang1
 * @date: 2020/9/15 9:59
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties
public class UploadDto implements Serializable {
    private static final long serialVersionUID = 4715477903459715457L;

    /**
     * 分片总数量
     */
    @ApiModelProperty(value = "分片总数量", example = "25", dataType = "int", required = true)
    private Integer chunkCount;

    @ApiModelProperty(value = "当前上传文件分片序号", example = "2", dataType = "int", required = true)
    private Integer partNumber;

    @ApiModelProperty(value = "上传文件的Sha256码值", example = "eeeeeeeeeeeeeewwwwwwwwwww", dataType = "string", required = true)
    private String fileSha256;

    @ApiModelProperty(value = "上传文件名称", example = "bigFile.zip", dataType = "String", required = true)
    private String fileName;

    @ApiModelProperty(value = "文件类型：0=升级包,1=测试报告，2=脚本文件,3=OTA日志文件", example = "0", dataType = "int", required = true)
    private int fileType;

    @ApiModelProperty(value = "文件大小(单位字节)", example = "10000086", dataType = "int", required = true)
    private Long fileSize;
    
    ////////////////////////////////////////////
    // V2版本增加的字段 
    ////////////////////////////////////////////
    private Integer uploadStatus;
    
    private String uploadUrl;
    
    /**
     * 上传文件/合并文件的格式
     */
    private String suffix;

    /**
     * 文件地址
     */
    private String filePath;
    
    /**
     * 存储的fileKey
     */
    private String fileKey;
    
    /**
     * 对数库中对应上传记录id
     */
    private Long fileUploadRecordId;
}
