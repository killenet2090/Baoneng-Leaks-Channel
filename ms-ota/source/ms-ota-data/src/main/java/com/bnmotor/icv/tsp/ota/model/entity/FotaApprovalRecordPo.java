package com.bnmotor.icv.tsp.ota.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaApprovalRecordPo.java 
 * @Description: 审批记录
 * @author E.YanLonG
 * @since 2021-3-24 16:29:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_fota_approval_record")
@ApiModel(value = "FotaApprovalRecordPo对象", description = "OTA 策略、升级任务审批流程记录")
public class FotaApprovalRecordPo extends BasePo {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(name = "id", value = "id")
	private Long id;
	
	@ApiModelProperty(name = "businessKey", value = "审批业务唯一键业务方生成", notes = "业务方生成唯一键")
	String businessKey;
	
	@ApiModelProperty(name = "definitionKey", value = "审批流程定义Key", notes = "审批流程定义Key")
	String definitionKey;
	
	@ApiModelProperty(name = "formTitle", value = "表单名称", notes = "表单名称")
	String formTitle;
	
	@ApiModelProperty(name = "link", value = "页面自定义参数", notes ="页面自定义参数")
	String vars;
	
	@ApiModelProperty(name = "approvalType", value = "审批类型1策略审批 2任务审批", notes ="审批类型1策略审批 2任务审批")
	Integer approvalType;
	
	@ApiModelProperty(name = "processInstanceId", value = "流程实例id", notes ="审批平台返回的流程实例id")
	String processInstanceId;
	
	@ApiModelProperty(name = "processInstanceId", value = "流程实例id", notes ="审批平台返回的流程实例id")
	Long otaObjectKey;
	
	@ApiModelProperty(name = "otaObjectBody", value = "表单实体", notes ="表单实体 JSON格式")
	String otaObjectBody;
	
	// 之前定义：审批状态：0待审批 1审批中，2通过 3驳回 4拒绝
	@ApiModelProperty(name = "status", value = "审批状态：1免审批 2待审批 3审批中 4审批通过 5未通过 6驳回", notes ="审批状态：1免审批 2待审批 3审批中 4审批通过 5未通过 6驳回")
	Integer status;
	
	@ApiModelProperty(name = "description", value = "备注描述", notes ="备注描述")
	String description;
	
	@ApiModelProperty(value = "数据版本，后台使用")
	Integer version;

}