package com.bnmotor.icv.tsp.ota.model.req.web;

import com.bnmotor.icv.tsp.ota.model.req.AbstractBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaStrategyAuditDto
 * @Description: OTA升级策略审核
 * @author xuxiaochang1
 * @since 2020-12-01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FotaStrategyAuditDto extends AbstractBase {

    @ApiModelProperty(value = "策略ID", example = "1000", dataType = "String", required = true)
    private String id;

    @ApiModelProperty(value = "状态:0=新建, 1=发起审批, 2=审核通过，3=审批拒绝, 4=策略失效 5=驳回 6=撤回", example = "1", dataType = "Integer", required = true)
    private Integer status;
}
