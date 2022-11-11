package com.bnmotor.icv.tsp.common.data.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :luoyang
 * @date_time :2020/11/12 15:40
 * @desc:
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@ApiModel(value = "字典详情Vo", description = "字典详情")
public class DictDetailVo implements Serializable {


    /*
     * 选项名称
     */
    @ApiModelProperty(value = "选项名称", name = "itemName", required = true)
    private String itemName;

    /*
     * 天目数值
     */
    @ApiModelProperty(value = "选项值", name = "itemValue", required = true)
    private String itemValue;


    private static final long serialVersionUID = 1L;

}
