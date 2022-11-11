package com.bnmotor.icv.tsp.common.data.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @ClassName: GeoDo
 * @Description: 地理位置信息表 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_geo")
@ApiModel(value="GeoDo对象", description="地理位置信息表")
public class GeoPo extends BasePo {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long parentId;

    @ApiModelProperty(value = "地理位置—类型ID")
    private Long typeId;

    @ApiModelProperty(value = "地理位置—类型名称")
    private String type;

    @ApiModelProperty(value = "地理位置_名称")
    private String name;

    @ApiModelProperty(value = "地理位置_编码")
    private String code;

    @ApiModelProperty(value = "地理位置_简称")
    private String abbreviation;

    @ApiModelProperty(value = "地理位置_拼音")
    private String pinyin;

    @ApiModelProperty(value = "地理位置_首字母")
    private String firstLetter;

    @ApiModelProperty(value = "地理位置_区域中心点_经度")
    private BigDecimal lng;

    @ApiModelProperty(value = "地理位置_区域中心点_纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "版本号")
    private Integer version;

}
