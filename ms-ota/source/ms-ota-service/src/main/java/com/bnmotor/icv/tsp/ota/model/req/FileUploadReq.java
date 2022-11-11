package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName:
 * @Description:    文件上传参数
 * @author: xuxiaochang1
 * @date: 2020/6/13 14:57
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FileUploadReq extends BasePo {
    @ApiModelProperty(value = "是否需要验证：0=不需要，1=需要", example = "0", dataType = "String")
    private int needVerify;

    @ApiModelProperty(value = "验证码", example = "12393939932", dataType = "String")
    private String verfiyCode;

    @ApiModelProperty(value = "验证类型：md5=MD5,sha1=SHA1", example = "MD5", dataType = "String")
    private String verifyType;

    @ApiModelProperty(value = "文件类型：0=升级包,1=测试报告，2=脚本文件", example = "0", dataType = "int", required = true)
    private int fileType;

    @ApiModelProperty(value = "文件名称", example = "test.abc", dataType = "String", required = false)
    private String fileName;
}
