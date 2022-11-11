package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: FotaPlanTaskDetailPo
 * @Description: OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级
在创建升级计划时创建升级 实体类
 * @author xxc
 * @since 2020-08-08
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_plan_task_detail")
@ApiModel(value="FotaPlanTaskDetailPo对象", description="OTA升级任务明细定义的是一辆车的一个零部件的一款软件所涉及的一个或多个软件的升级在创建升级计划时创建升级")
public class FotaPlanTaskDetailPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目Id")
    private String projectId;

    @ApiModelProperty(value = "ota任务Id")
    private Long otaPlanId;

    @ApiModelProperty(value = "升级计划对象ID")
    private Long otaPlanObjId;

    @ApiModelProperty(value = "升级对象Id")
    private Long otaPlanFirmwareId;

    @ApiModelProperty(value = "整个OTA开始时间从版本检查更新开始")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date finishTime;

    @ApiModelProperty(value = "失败时间")
    private Date failedTime;

    @ApiModelProperty(value = "升级失败原因")
    private String failedReason;

    @ApiModelProperty(value = "重试次数")
    private Integer retryCount;

    @ApiModelProperty(value = "重试时间")
    private Date retryTime;

    /**
     *
     */
    @ApiModelProperty(value = "原版本")
    private String sourceVersion;

    @ApiModelProperty(value = "升级目标版本")
    private String currentVersion;

    @ApiModelProperty(value = "安装完成后版本")
    private String installedVersion;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "数据版本控制字段")
    private Integer version;

    @ApiModelProperty(value = "升级任务状态",notes = "1=升级完成, 2=升级失败，3=升级未完成")
    private Integer status;
}
