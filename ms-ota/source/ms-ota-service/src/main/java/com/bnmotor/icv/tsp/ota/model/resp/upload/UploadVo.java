package com.bnmotor.icv.tsp.ota.model.resp.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: UploadVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/10/29 16:16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class UploadVo implements Serializable {
    private static final long serialVersionUID = 6350498735629137708L;

    @ApiModelProperty(value = "响应结果：-1=待分片, 1=部分上传，0=上传成功，2=文件上传失败，3=文件合并中，4=等待文件合并", example = "10086", dataType = "long")
    private Integer result;

    @ApiModelProperty(value = "需要上传的分片标识列表：result=1时,不为空", example = "10086", dataType = "list")
    private List<Integer> partNums;

    @ApiModelProperty(value = "文件记录url:result=0时不为空", example = "http://file.bnicvc.com/pgk/pkg1.apk", dataType = "string")
    private String url;

    @ApiModelProperty(value = "文件记录Id:result=0时不为空", example = "10086", dataType = "long")
    private Long fileUploadRecordId;

    @ApiModelProperty(value = "是否需要合并:0=不需要，1=需要", example = "1", dataType = "int")
    private Integer needMerge;

    @ApiModelProperty(value = "文件名称", example = "分片文件上传需要合并时，系统回传参数", dataType = "string")
    private String fileName;

    @ApiModelProperty(value = "文件大小", example = "1000L", dataType = "long")
    private Long fileSize;
}
