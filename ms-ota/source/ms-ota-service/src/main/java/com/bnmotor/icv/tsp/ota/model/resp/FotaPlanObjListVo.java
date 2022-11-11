package com.bnmotor.icv.tsp.ota.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: FotaPlanObjListVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/11 15:37
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaPlanObjListVo {
    @ApiModelProperty("主键Id")
    private Long id;

    @ApiModelProperty("升级计划id")
    private String otaPlanId;

    @ApiModelProperty("升级对象id")
    private String otaObjectId;

    @ApiModelProperty("升级对象id")
    private String objectId;

    @ApiModelProperty("升级状态 todo")
    private Integer status;

    @ApiModelProperty("vin码")
    private String vin;

    @ApiModelProperty("当前区域")
    private String currentArea;

    @ApiModelProperty("销售区域")
    private String saleArea;

    @ApiModelProperty("原版本号")
    private String sourceVersion;

    @ApiModelProperty("目标版本号")
    private String targetVersion;

    @ApiModelProperty("当前版本号")
    private String currentVersion;

    @ApiModelProperty(value = "2",notes = "升级结果 1=升级完成,2=升级未完成,3=升级失败" )
    private Integer result;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("日志文件上传记录Id")
    private Long logFileId;

    @ApiModelProperty("日志文件下载地址")
    private String logFileUrl;
}
