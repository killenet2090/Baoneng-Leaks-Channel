package com.bnmotor.icv.tsp.ota.model.req.web;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaStrategyReportDto.java 
 * @Description: 策略审批时上传验收报告
 * @author E.YanLonG
 * @since 2021-4-2 12:00:00
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(value="策略验收报告")
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaStrategyReportDto extends AbstractBase {

	/**
	 * 策略id
	 */
	@ApiModelProperty(value = "策略ID", example = "1353520896819613698", dataType = "String", required = true)
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long strategyId;
	
	/**
	 * 文件上传id
	 */
	@ApiModelProperty(value = "策略测试报告的文件记录id", example = "1353520896819613698")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	Long fileUploadRecordId;
	
	/**
	 * 文件访问url
	 */
	@ApiModelProperty(value = "测试报告访问链接", example = "report.report", dataType = "String")
	String url;
	
	/**
	 * 审核备注
	 */
	@ApiModelProperty(value = "策略审批的备注内容", example = "策略审批的备注内容", dataType = "String")
	String remark;
}