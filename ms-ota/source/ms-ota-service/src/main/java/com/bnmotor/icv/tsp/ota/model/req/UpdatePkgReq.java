package com.bnmotor.icv.tsp.ota.model.req;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName:  UpdatePkgReq
 * @Description:  新增升级包操作请求参数
 * @author: xuxiaochang1
 * @date: 2020/6/12 10:41
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ApiModel(description = "新增升级包操作请求参数")
public class UpdatePkgReq  extends BasePo {

    @ApiModelProperty(value = "固件版本记录Id字符串", example = "1273956055825604609", dataType = "string", required = true)
    @NotNull
    private String firmwareVersionIdStr;

    @ApiModelProperty(value = "原始包文件上传记录Id", example = "12349493", dataType = "String", required = true)
    @NotEmpty
    private String fileId;

    @ApiModelProperty(value = "升级包类型", example = "0=全量,1=补丁,2=差分", dataType = "Integer")
    @JsonIgnore
    private Integer pkgType;

    @ApiModelProperty(value = "验证码", example = "0039329-2303032-3093", dataType = "String")
    @NotEmpty
    private String verifyCode;

    @ApiModelProperty(value = "验证码类型", example = "SHA256", dataType = "String")
    @NotEmpty
    private String verifyType;

    @ApiModelProperty(value = "测试报告文件url地址", example = "http://file.bnicvc.com/file/report.docx", dataType = "String")
    private String originalReportFilePath;

    @ApiModelProperty(value = "差分脚本url(针对差分类型必传)", example = "http://file.bnicvc.com/file/difScript.sh", dataType = "String")
    private String difScriptUrl;

    @ApiModelProperty(value = "差分脚本url文件上传记录Id", example = "12349493", dataType = "String", required = true)
    @NotEmpty
    private String difFileId;

    @ApiModelProperty(value = "差分升级起始版本号Id集合(针对差分类型必传)", example = "V1.0.9", dataType = "String")
    private List<String> targetFirmwareVersonIds;

    @ApiModelProperty(value = "预计刷写时长(单位为妙)", example = "30", dataType = "int")
    @NotNull
    private int estimateTime;

    @ApiModelProperty(value = "供应商信息", example = "博世", dataType = "String")
    private String supplierInfo;

    @ApiModelProperty(value = "发布信息", example = "最新升级包，升了保证你受不了", dataType = "String")
    private String releaseDescription;
}
