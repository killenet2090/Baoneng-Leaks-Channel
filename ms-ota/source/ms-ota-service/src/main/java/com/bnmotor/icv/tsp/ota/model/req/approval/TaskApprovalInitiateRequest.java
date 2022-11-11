package com.bnmotor.icv.tsp.ota.model.req.approval;

import java.util.Map;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: TaskApprovalInitiateRequest.java
 * @Description: 发起审批
 * @author E.YanLonG
 * @since 2021-3-24 9:03:40
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class TaskApprovalInitiateRequest extends BasePo {

	/**
	 * 业务主键 otaPlanId或者strategyId
	 */
	@ApiModelProperty(name = "primaryKey", value = "业务主键", notes = "策略id或者任务id")
	Long primaryKey;

	/**
	 * 审批类型 1策略审批 2任务审批
	 */
	@ApiModelProperty(name = "approvalType", value = "审批类型：1策略审批 2任务审批", notes = "审批类型：1策略审批 2任务审批")
	Integer approvalType = 2;
	
	/**
	 * 详情内容的地址
	 */
//	@ApiModelProperty(name = "detailLink", value = "详情页面链接地址", notes = "用于业务系统跳转到详情内容页面")
//	String detailLink;
//	
//	/**
//	 * 修改内容的地址
//	 */
//	@ApiModelProperty(name = "modifyLink", value = "编辑页面链接地址", notes = "用于业务系统跳转到编辑内容页面")
//	String modifyLink;
	
	@ApiModelProperty(name = "vars", value = "页面自定义参数", notes = "页面自定义参数：存放跳转链接等内容")
	Map<String, String> vars;

}