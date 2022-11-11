/*
 * Copyright 2020 The JA-SIG Collaborative. All rights reserved.
 * distributed with thi file and available online at
 */
package com.bnmotor.icv.tsp.ota.model.req.v2;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.bnmotor.icv.tsp.ota.model.validate.Update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @ClassName: UpgradeObjectListV2Req.java UpgradeObjectListV2Req
 * @Description:
 * @author E.YanLonG
 * @since 2020-12-14 15:16:46
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
@ApiModel(description = "OTA升级计划对象列表 升级任务中包含需要升级的车辆")
public class UpgradeObjectListV2Req {

	@ApiModelProperty(value = "待升级车辆信息", example = "[{'vin':'OTA20200924BN0002', 'objectId':'1294207330359762946'}]", dataType = "List")
	@NotNull(message = "{UpgradeObjectListReq.upgradeTaskObjectReqList.notNull.message}", groups = { Update.class })
	private List<UpgradeTaskObjectV2Req> upgradeTaskObjectReqList;

	@ApiModelProperty(value = "策略id", example = "1338701600839294978", dataType = "Long")
	@NotNull(message = "策略id不能为空")
	Long fotaStrategyId;
	
	@ApiModelProperty(value = "任务id", example = "1338701600839294978", dataType = "Long")
	@NotNull(message = "升级计划id不能为空")
	Long fotaPlanId;
	
	@NotNull(message = "操作人员")
	String updateBy;

}