package com.bnmotor.icv.tsp.ota.model.req.web;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.bnmotor.icv.tsp.ota.model.validate.Save;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName: FotaStrategyReq
 * @Description: OTA升级策略
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FotaStrategyDto extends AbstractBase {

    @ApiModelProperty(value = "策略ID,编辑策略时必传", example = "1000", dataType = "String")
    @NotEmpty(message = "策略ID不能为空", groups = {Update.class})
    private String id;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", example = "1000", dataType = "String")
    private String projectId;

    /**
     * 设备树节点ID
     */
    @ApiModelProperty(value = "设备树节点ID", example = "1000", dataType = "String", required = true)
    @NotEmpty(message = "设备树节点ID不能为空", groups = {Save.class, Update.class})
    private String treeNodeId;


    @ApiModelProperty(value = "设备树路径描述", example = "宝能/GX16/GX16A/2020/高配", dataType = "String")
    private String treeNodeDesc;

    /**
     * 策略名称
     */
    @ApiModelProperty(value = "策略名称", example = "1000", dataType = "String", required = true)
    private String name;

    /**
     * 整车版本号
     */
    @ApiModelProperty(value = "整车版本号", example = "1000", dataType = "String", required = true)
    @NotEmpty(message = "整车版本号不能为空", groups = {Save.class, Update.class})
    private String entireVersionNo;

    /**
     * 回滚模式； 0 - 保守策略, 1 - 激进策略
     */
    @ApiModelProperty(value = "回滚模式； 1 - 激进策略， 0 - 保守策略", example = "1", dataType = "Integer")
    private Integer rollbackMode;

    /**
     * 预估升级时长
     */
    @ApiModelProperty(value = "预估升级时长", example = "100", dataType = "Integer", required = true)
    @NotNull(message = "预估升级时长不能为空", groups = {Save.class, Update.class})
    private Integer estimateUpgradeTime;

    /**
     * 状态:0=新建,1=审批中,2=审核通过，3=审批拒绝，4=失效
     */
    @ApiModelProperty(value = "状态:0=新建,1=审批中,2=审核通过，3=审批拒绝，4=失效", example = "1", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(value = "状态描述", example = "1", dataType = "Integer")
    private String statusDesc;

    /**
     * 状态:0=不开启,1=开启
     */
    private Integer isEnable;

    /**
     * 是否存在依赖分组
     */
    @ApiModelProperty(value = "状态:0=不存在，1=存在", example = "1", dataType = "Integer", required = true)
    @NotNull(message = "是否存在依赖分组识不能为空", groups = {Save.class, Update.class})
    private Integer dependencyGroup;

    /**
     * ecu列表信息
     */
    @ApiModelProperty(value = "ecu列表信息", example = "", dataType = "List", required = true)
    @NotNull(message = "ecu列表信息列表不能为空", groups = {Save.class, Update.class})
    private List<FotaStrategyFirmwareListDto> fotaStrategyFirmwareListDtos;

    @ApiModelProperty(value = "前置条件检查列表", example = "", dataType = "List", required = true)
    @NotNull(message = "前置条件检查列表不能为空", groups = {Save.class, Update.class})
    private List<FotaStrategyPreConditionDto> fotaStrategyPreConditionDtos;
    
    @ApiModelProperty(value = "策略审批报告的文件名称", example = "report.pdf")
    private String reportName;
    
    @ApiModelProperty(value = "策略审批报告访问地址", example = "http://oss/ota/report/report.pdf")
    private String reportUrl;
    
    @ApiModelProperty(value = "策略审批备注", example = "策略审批备注")
    private String remark;
    
    @ApiModelProperty(value = "测试报告文件id", example = "1")
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long fileRecordId;

}