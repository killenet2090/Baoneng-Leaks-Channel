package com.bnmotor.icv.tsp.operation.veh.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehModelStatistics
 * @Description: 车型统计信息
 * @author: zhangwei2
 * @date: 2020/7/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehProjectStatisticsVo {
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目编码
     */
    private String code;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 型号数量
     */
    @ApiModelProperty(example = "1")
    private Integer modelNum;
}

