package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName:  FileUploadVo
 * @Description:    文件上传响应结果
 * @author: xuxiaochang1
 * @date: 2020/6/12 14:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@AllArgsConstructor
public class FileUploadVo implements Serializable {

    private static final long serialVersionUID = 8341449450038888923L;

    @ApiModelProperty(value = "文件记录url", example = "http://file.bnicvc.com/pgk/pkg1.apk", dataType = "string")
    private String url;

    @ApiModelProperty(value = "文件记录Id", example = "10086", dataType = "long")
    private Long fileUploadRecordId;
}
