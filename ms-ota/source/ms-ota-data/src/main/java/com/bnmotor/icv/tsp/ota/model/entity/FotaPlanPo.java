package com.bnmotor.icv.tsp.ota.model.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaPlanPo
 * @Description: OTA升级计划表 实体类
 * @author xxc
 * @since 2020-07-07
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_plan")
@ApiModel(value="FotaPlanPo对象", description="OTA升级计划表")
public class FotaPlanPo extends CustBasePo {

    @ApiModelProperty(value = "主键字符串")
    @TableField(exist = false)
    private String idStr;

    @ApiModelProperty(value = "项目Id")
    private String projectId;

    @ApiModelProperty(value = "计划名称")
    private String planName;

    @ApiModelProperty(value = "计划名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "计划开始时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date planStartTime;

    @ApiModelProperty(value = "计划结束时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date planEndTime;

    @ApiModelProperty(value = "计划开始时间")
    @TableField(exist = false)
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startTime;

    @ApiModelProperty(value = "计划结束时间")
    @TableField(exist = false)
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date finishTime;

    @ApiModelProperty(value = "计划状态:-1 草稿状态  0 - 未审核、1 - 审核中 2 - 已审核 3 - 已发布4 - 已失效")
    private Integer planStatus;

    @ApiModelProperty(value = "计划分批的批次大小")
    private Integer batchSize;

    @ApiModelProperty(value = "计划描述")
    private String planDesc;

    @ApiModelProperty(value = "发布者")
    private String publishBy;

    @ApiModelProperty(value = "发布日期")
    private Date publishDate;

    @ApiModelProperty(value = "计划类型：0 - 软件,1 - 整车")
    private Integer planType;

    @ApiModelProperty(value = "目标版本ID：整车的目标版本")
    private String targetVersion;

    @ApiModelProperty(value = "计划终止的失败率", hidden = true)
    private BigDecimal planStopRate;

    @ApiModelProperty(value = "重试次数", hidden = true)
    private Integer retryCount;

    @ApiModelProperty(value = "任务状态:-1 草稿状态  0 - 未审核、1 - 审核中 2 - 已审核 3 - 已发布4 - 已失效")
    @TableField(exist = false)
    private Integer taskStatus;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "新版本提示语")
    private String newVersionTips;

    @ApiModelProperty(value = "下载提示语")
    private String downloadTips;

    @ApiModelProperty(value = "免责声明")
    private String disclaimer;

    @ApiModelProperty(value = "任务提示")
    private String taskTips;

//    @ApiModelProperty(value = "升级模式； 1 = 静默升级， 2 = 提示用户")
    @ApiModelProperty(value = "升级模式：1 - 静默升级（工厂模式：下载+升级都全自动）， 2 - 提示用户（交互模式），3=静默下载 (自动下载)")
    private Integer upgradeMode;

    @ApiModelProperty(value = "数据状态：0 = 禁用，1 = 启用.")
    private Integer isEnable;

    @ApiModelProperty(value = "数据并发版本控制")
    private Integer version;

    @ApiModelProperty(value = "关联的设备树名称路径")
    @TableField(exist = false)
    private String nodeNamePath;

    @ApiModelProperty(value = "关联的车的Id")
    private String objectParentId;
    
    @ApiModelProperty(value = "升级策略Id")
    private Long fotaStrategyId;
    
    @ApiModelProperty(value = "审批状态: 1免审批，2待审批、3审批中、4已审批、5未通过")
    private Integer approveState;

//    @ApiModelProperty(value = "发布状态: -1草稿状态  0 未审核、1审核中 2已审核 3已发布 4已失效")
    @ApiModelProperty(value = "1待发布  2发布中 3已发布 4延迟发布 5已失效 6暂停发布")
    private Integer publishState;
    
    @ApiModelProperty(value = "任务模式： 0测试任务 1正式任务")
    private Integer planMode;
    
    @ApiModelProperty(value = "车辆叶子节点：1278883228948725762")    
    private Long treeNodeId;
    
    @ApiModelProperty(value = "额外信息：json串")
    String extra;
    
    @ApiModelProperty(value = "新版本升级任务标识：0版本V1 1版本V2")
    @TableField(exist = true)
    Integer rebuildFlag;
}
