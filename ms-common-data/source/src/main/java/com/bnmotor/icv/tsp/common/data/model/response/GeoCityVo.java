package com.bnmotor.icv.tsp.common.data.model.response;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.bnmotor.icv.tsp.common.data.model.response.view.MgtView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: GeoCityVo
* @Description: 城市平铺信息 实体类
* @author: liuhuaqiao1
* @date: 2021/1/21
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@ApiModel(value="GeoCityVo对象", description="地理位置信息表")
public class GeoCityVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "地理位置_省市")
    @JsonView(BaseView.class)
    private String province;

    @ApiModelProperty(value = "地理位置_市")
    @JsonView(BaseView.class)
    private String city;

    @ApiModelProperty(value = "地理位置_区县")
    @JsonView(BaseView.class)
    private String district;

    @ApiModelProperty(value = "地理位置_编码")
    @JsonView(BaseView.class)
    private String adCode;

}
