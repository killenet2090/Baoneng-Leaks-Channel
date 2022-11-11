package com.bnmotor.icv.tsp.ota.model.query;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.Page;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = " TBOX升级日志查询条件")
public class FotaUpgradeLogQuery extends Page {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "transId", example = "1316944117112332289", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private Long transId;
	
	/**
     * 任务名称
     */
    @ApiModelProperty(value = "planId", example = "1314450271405101058", dataType = "Long")
    @JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
    private Long planId;

    /**
     * 升级状态
     */
    @ApiModelProperty(value = "安装结果：1=升级完成（成功），2=升级未完成，3=升级失败", example = "1", dataType = "Integer")
    private Integer installCompleteStatus;
    
    @ApiModelProperty(value = "vin", example = "LLNC6ADB5JA047667", dataType = "String")
    private String vin;
    
    @ApiModelProperty(value = "开始时间", example = "2020-06-08 00:00:00", dataType = "String")
    private String startTime;
    
    @ApiModelProperty(value = "结束时间", example = "2020-06-08 00:00:00", dataType = "String")
    private String endTime;

}