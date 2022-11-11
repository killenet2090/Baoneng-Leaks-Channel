package com.bnmotor.icv.tsp.commons.oss.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 上传网络文件 亲求参数
 * @author zhangjianghua1
 */
@Data
public class NfsFileDto implements Serializable {
    private static final long serialVersionUID = -7350612422599623833L;

    @ApiModelProperty(value = "bucket名称, 必须", example = "在Minio服务器上创建的bucket的名称", dataType = "String")
    @NotBlank(message = "bucket名称不能为空")
    private String bucket;

    @ApiModelProperty(value = "文件分组, 必须", example = "在Minio服务器上创建的bucket的名称", dataType = "String")
    @NotBlank(message = "文件分组不能为空")
    private String group;

    @ApiModelProperty(value = "上传文件列表, 必须", example = "在Minio服务器上创建的bucket的名称", dataType = "String")
    @NotNull(message = "上传文件不能为空")
    @Size(min = 1, max = 9, message = "上传文件不能为空, 且最大数量为9")
    private List<String> files;
}
