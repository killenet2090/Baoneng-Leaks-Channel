package com.bnmotor.icv.tsp.ota.model.resp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: FotaFirmwarePkgResp
 * @Description: 升级包信息(提供给前端展示)
包括升级包原始信息以及升级包发布信息 实体类
 * @author xuxiaochang1
 * @since 2020-06-11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@Accessors(chain = true)
@ApiModel(value="升级包信息(提供给前端展示)", description="升级包信息(提供给前端展示)")
public class FotaFirmwarePkgVo {

    @ApiModelProperty(value = "ID字符串")
    private String idStr;

    @ApiModelProperty(value = "升级包类型:0-全量包,1-补丁包,2-差分包")
    private Integer pkgType;

    @ApiModelProperty(value = " 包制作状态：0=未开始，1=制作中，2=制作成功，3=制作失败")
    private String buildPkgStatusStr;

    @ApiModelProperty(value = " 包制作状态：0=未开始，1=制作中，2=制作成功，3=制作失败")
    private Integer buildPkgStatus;
    
    @ApiModelProperty(value = " 包加密状态：0=未开始，1=加密中，2=加密成功，3=加密失败")
    private String encryptPkgStatusStr;

    @ApiModelProperty(value = " 包加密状态：0=未开始，1=加密中，2=加密成功，3=加密失败")
    private Integer encryptPkgStatus;

    @ApiModelProperty(value = "升级包类型描述:0-全量包,1-补丁包,2-差分包")
    private String pkgTypeStr;

    @ApiModelProperty(value = "升级包文件名")
    private String pkgFileName;

    @ApiModelProperty(value = "升级包文件大小")
    private String pkgFileSize;

    @ApiModelProperty(value = "固件名称名")
    private String firmwareName;

    @ApiModelProperty(value = "起始版本号")
    private String startFirmwareVersionNo;

    @ApiModelProperty(value = "目标版本号")
    private String targetFirmwareVersionNo;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态码")
    private String statusCode;

    @ApiModelProperty(value = "状态码描述")
    private String statusDesc;

    @ApiModelProperty(value = "上传日期")
    private Date uploadTime;

}
