package com.bnmotor.icv.tsp.ota.model.req.v2;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.bnmotor.icv.tsp.ota.model.validate.Update;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @ClassName: FotaPlanIsEnableV2Req
 * @Description: 任务审核参数
 * @author E.YanLonG
 * @since 2020-11-30 10:29:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
@ApiModel(description = "升级任务信息V2")
public class FotaPlanApproveV2Req extends AbstractBase {

	@ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "任务ID不能为空", groups = {Update.class})
	private Long id;

	@ApiModelProperty(value = "审核状态： 1免审批，2待审批、3审批中、4已审批、5未通过", example = "1", dataType = "Integer")
	private Integer approveStatus;
}