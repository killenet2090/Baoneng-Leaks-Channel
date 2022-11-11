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
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value="GeoDo对象", description="地理位置信息表")
public class GeoVo{

    private static final long serialVersionUID = 1L;

    @JsonView(MgtView.class)
    private String id;
    @JsonView(MgtView.class)
    private String parentId;

    @ApiModelProperty(value = "地理位置—类型ID")
    @JsonView(MgtView.class)
    private String typeId;

    @ApiModelProperty(value = "地理位置—类型名称")
    @JsonView(MgtView.class)
    private String type;

    @ApiModelProperty(value = "地理位置_名称")
    @JsonView(BaseView.class)
    private String name;

    @ApiModelProperty(value = "地理位置_编码")
    @JsonView(BaseView.class)
    private String code;

    @ApiModelProperty(value = "地理位置_简称")
    @JsonView(BaseView.class)
    private String abbreviation;

    @ApiModelProperty(value = "地理位置_拼音")
    private String pinyin;

    @ApiModelProperty(value = "地理位置_首字母")
    @JsonView(BaseView.class)
    private String firstLetter;

    @ApiModelProperty(value = "地理位置_区域中心点_经度")
    @JsonView(BaseView.class)
    private BigDecimal lng;

    @ApiModelProperty(value = "地理位置_区域中心点_纬度")
    @JsonView(BaseView.class)
    private BigDecimal lat;

    @ApiModelProperty(value = "地理位置_子集")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonView(BaseView.class)
    private List<GeoVo> children = new ArrayList<>();

    @ApiModelProperty(value = "地理位置_层级code数组(从上到下)")
    @JsonView(MgtView.class)
    private String[] path;

}
