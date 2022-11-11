package com.bnmotor.icv.tsp.ota.model.resp;

import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "TBOX升级日志信息(前端展示)", description = "TBOX升级日志信息(前端展示)")
public class FotaUpgradeLogVo extends BasePo {

	@ApiModelProperty(value = "车辆vin")
	String vin;

	@ApiModelProperty(value = "升级计划id")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long planId;
	
	@ApiModelProperty(value = "任务名称")
	String planName;

	@ApiModelProperty(value = "transId")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long transId;

	@ApiModelProperty(value = "下载TBOX升级日志的文件名")
	String filename;

	@ApiModelProperty(value = "transId对应的升级安装结果 -1 状态 显示- 表示未知 1=升级完成（成功），2=升级未完成，3=升级失败")
	Integer installCompleteStatus;
	
	@ApiModelProperty(value = "transId对应的升级状态")
	String processState;

	@ApiModelProperty(value = "升级日志是否已上传 0 未上传 1 已上传")
	Integer existsLog;

}