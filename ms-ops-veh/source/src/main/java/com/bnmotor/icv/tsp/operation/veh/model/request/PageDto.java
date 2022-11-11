package com.bnmotor.icv.tsp.operation.veh.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: PageDto
 * @Description: 分页查询基类
 * @author: zhangwei2
 * @date: 2020/6/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class PageDto implements Serializable {
    @NotNull(message = "当前页数不能为空")
    @ApiModelProperty(value = "当前页数", name = "current", example = "1")
    private Integer current = 1;

    @NotNull(message = "当前页面大小不能为空")
    @ApiModelProperty(value = "页面大小", name = "pageSize", example = "10")
    private Integer pageSize = 10;
}

