package com.bnmotor.icv.tsp.ota.model.resp.v2;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(description = "任务列表V2版本展示参数")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class PlanListVo {

	Long id;
	String planName;
	String vehicleFinishVersion;
	/**
	 * 
	 */
	@ApiModelProperty(value = "审批状态", example = "免审批，待审批、审批中、已审批、未通过")
	String approveState;

	@ApiModelProperty(value = "发布状态", example = "待发布 发布中 已发布 已失效 延迟发布")
	String publishState;
	
	@ApiModelProperty(value = "任务有效期", example = "2020/12/01-2020/12/02")
	String planEffectTime;
	
	@ApiModelProperty(value = "升级方式", example = "静默下载、工厂模式、正常模式")
	String upgradeMode;
	
	@ApiModelProperty(value = "任务车辆数（个）", example = "1000")
	Integer vehicleUpgradeCount;

}