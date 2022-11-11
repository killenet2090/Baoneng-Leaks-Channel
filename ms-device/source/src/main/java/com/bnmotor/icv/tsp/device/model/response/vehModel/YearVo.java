package com.bnmotor.icv.tsp.device.model.response.vehModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: YearVo
 * @Description: 年款响应实体
 * @author: zhangwei2
 * @date: 2020/11/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class YearVo {
    @ApiModelProperty("年款id")
    private String id;

    @ApiModelProperty(value = "年款名称")
    private String yearName;

    @ApiModelProperty(value = "车辆配置")
    private List<VehConfigVo> configs;
}
