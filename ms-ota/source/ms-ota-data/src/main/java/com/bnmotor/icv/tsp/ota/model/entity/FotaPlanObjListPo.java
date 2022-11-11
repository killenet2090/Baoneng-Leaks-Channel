/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.time.LocalDateTime;

/**
 * <pre>
 * 	<b>表名</b>：tb_fota_plan_obj_list
 *  OTA升级计划对象清单
 * 定义一次升级中包含哪些需要升级的车辆，其中一些通用字段在ModelDO和BaseDO里面，该对象基本上只用于数据保存使用。
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_plan_obj_list")
@ApiModel(value = "FotaPlanObjListPo对象", description = "OTA升级计划对象清单定义一次升级中包含哪些需要升级的车辆")
public class FotaPlanObjListPo extends BasePo {

    public FotaPlanObjListPo() {
        status = Enums.TaskObjStatusTypeEnum.CREATED.getType();
        result = Enums.UpgradeResultTypeEnum.UPGRADE_UNDEFALT.getType();
    }

    @ApiModelProperty("主键Id")
    private Long id;

    @ApiModelProperty("升级计划id")
    private Long otaPlanId;

    @ApiModelProperty("升级对象id")
    private Long otaObjectId;

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

    @ApiModelProperty("升级通知通知状态")
    private Integer notifyStatus;

    @ApiModelProperty("升级通知尝试次数")
    private Integer notifyTryNum;

    @ApiModelProperty("升级通知推送时间")
    private LocalDateTime nofityTime;

    @ApiModelProperty("升级通知推送响应回调时间")
    private LocalDateTime notifyCallbackTime;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
