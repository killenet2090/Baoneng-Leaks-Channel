package com.bnmotor.icv.tsp.common.data.model.request;

import com.bnmotor.icv.tsp.common.data.common.validation.group.AddGroup;
import com.bnmotor.icv.tsp.common.data.common.validation.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @ClassName: GeoReqVo
 * @Description: 地理位置信息新增/修改 实体类
 * @author zhangjianghua1
 * @since 2020-07-10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value="Geo请求对象", description="地理位置信息表")
public class GeoReqVo{

    @NotNull(message = "父ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Long parentId;

    @NotNull(message = "地理位置—类型ID不能为空", groups = {AddGroup.class})
    @ApiModelProperty(value = "地理位置—类型ID")
    private Long typeId;

    @NotBlank(message = "地理位置_名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "地理位置_名称")
    private String name;

    @NotBlank(message = "地理位置_编码不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "地理位置_编码")
    private String code;

    @NotBlank(message = "地理位置_简称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "地理位置_简称")
    private String abbreviation;

    @NotBlank(message = "地理位置_拼音不能为空", groups = {AddGroup.class})
    @ApiModelProperty(value = "地理位置_拼音")
    private String pinyin;

    @NotBlank(message = "地理位置_首字母不能为空", groups = {AddGroup.class})
    @Size(min = 1, max = 1, message = "地理位置_首字母长度不符合", groups = {AddGroup.class})
    @ApiModelProperty(value = "地理位置_首字母")
    private String firstLetter;

    @NotNull(message = "地理位置_区域中心点_经度不能为空", groups = {AddGroup.class})
    @ApiModelProperty(value = "地理位置_区域中心点_经度")
    private BigDecimal lng;

    @NotNull(message = "地理位置_区域中心点_纬度不能为空", groups = {AddGroup.class})
    @ApiModelProperty(value = "地理位置_区域中心点_纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @NotBlank(message = "创建人不能为空", groups = AddGroup.class)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @NotBlank(message = "修改人不能为空", groups = UpdateGroup.class)
    @ApiModelProperty(value = "修改人")
    private String updateBy;

}
