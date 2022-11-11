package com.bnmotor.icv.tsp.ota.model.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FotaPlanVo extends BasePo {

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

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

    @ApiModelProperty(value = "计划状态:-1=草稿状态、0=创建完成待审核，1=创建,2=已发布,3=执行中,4=暂停,5=取消,6=完成")
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

    @ApiModelProperty(value = "计划终止的失败率")
    private BigDecimal planStopRate;

    @ApiModelProperty(value = "重试次数")
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

    @ApiModelProperty(value = "升级模式； 1 = 静默升级， 2 = 提示用户")
    private Integer upgradeMode;

    @ApiModelProperty(value = "数据状态：0 = 禁用，1 = 启用.")
    private Integer isEnable;

    @ApiModelProperty(value = "启用的状态：0 = 禁用，1 = 启用.")
    private Integer isEnableOfStart;

    @ApiModelProperty(value = "数据并发版本控制")
    private Integer version;

    @ApiModelProperty(value = "关联的设备树名称路径")
    @TableField(exist = false)
    private String nodeNamePath;

    @ApiModelProperty(value = "关联的车的Id")
    /*@TableField(exist = false)*/
    private String objectParentId;

}
