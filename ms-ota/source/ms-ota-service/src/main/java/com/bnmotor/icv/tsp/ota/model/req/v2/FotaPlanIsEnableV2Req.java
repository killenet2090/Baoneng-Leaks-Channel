package com.bnmotor.icv.tsp.ota.model.req.v2;

import com.bnmotor.icv.tsp.ota.common.convert.LongJsonDeserializer;
import com.bnmotor.icv.tsp.ota.common.convert.LongJsonSerializer;
import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import com.bnmotor.icv.tsp.ota.model.validate.Save;
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
 * @Description: V2版本新建升级任务
 * @author E.YanLonG
 * @since 2020-11-30 10:29:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
@ApiModel(description = "任务是否启用V2")
public class FotaPlanIsEnableV2Req extends AbstractBase {

	@ApiModelProperty(value = "任务ID", example = "1328997695695290369", dataType = "Long")
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	@NotNull(message = "任务ID不能为空", groups = {Update.class})
	private Long id;

	@ApiModelProperty(value = "是否启用:1=启用;0=不启用", example = "1", dataType = "Integer")
	@NotNull(message = "开关状态不能为空", groups = {Save.class, Update.class})
	private Integer isEnable;
}