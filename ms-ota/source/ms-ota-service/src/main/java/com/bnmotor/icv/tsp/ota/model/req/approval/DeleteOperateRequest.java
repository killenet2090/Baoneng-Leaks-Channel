package com.bnmotor.icv.tsp.ota.model.req.approval;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: DeleteOperateRequest.java
 * @Description: 任务或者策略删除前需要判断 是否存在审批记录，如果存在审批记录，则不允许删除
 * @author E.YanLonG
 * @since 2021-4-20 17:59:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class DeleteOperateRequest {

	@ApiModelProperty(name = "primaryKey", value = "业务主键", notes = "策略id或者任务id")
	Long primaryKey;

	/**
	 * 审批类型 1策略审批 2任务审批
	 */
	@ApiModelProperty(name = "approvalType", value = "审批类型：1策略审批 2任务审批", notes = "审批类型：1策略审批 2任务审批")
	Integer approvalType = 2;

}