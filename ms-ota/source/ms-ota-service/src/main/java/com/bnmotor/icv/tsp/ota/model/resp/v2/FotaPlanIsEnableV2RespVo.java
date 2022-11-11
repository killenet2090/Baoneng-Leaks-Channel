package com.bnmotor.icv.tsp.ota.model.resp.v2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaPlanIsEnableV2RespVo.java 
 * @Description: 开关切换时，返回发布状态
 * @author E.YanLonG
 * @since 2021-2-3 19:58:49
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaPlanIsEnableV2RespVo {

	@ApiModelProperty(value = "操作结果 true成功 false失败", example = "true", dataType = "boolean")
	boolean result; // 1成功 0失败

	@ApiModelProperty(value = "发布状态: 1待发布 2发布中 3已发布 4延迟发布 5已失效 6暂停发布", example = "1", dataType = "Integer")
	Integer publishState;

	@ApiModelProperty(value = "发布状态描述: 待发布 发布中 已发布 延迟发布 已失效 暂停发布", example = "待发布", dataType = "String")
	String publishStateDesc;
}