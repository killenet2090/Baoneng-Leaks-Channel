package com.bnmotor.icv.tsp.device.model.response.vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OrgLocalCacheVo
 * @Description: Org本地缓存对象
 * @author: zhangwei2
 * @date: 2020/11/16
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class OrgLocalCacheVo {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "品牌")
    private Long brandId;

    @ApiModelProperty(value = "品牌编码")
    private String brandCode;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "车系")
    private Long vehSeriesId;

    @ApiModelProperty(value = "车系编码")
    private String seriesCode;

    @ApiModelProperty(value = "车系名称")
    private String vehSeriesName;

    @ApiModelProperty(value = "车型")
    private Long vehModelId;

    @ApiModelProperty(value = "车型编码")
    private String modelCode;

    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ApiModelProperty(value = "年款 ")
    private Long yearStyle;

    @ApiModelProperty(value = "年款编码")
    private String yearCode;

    @ApiModelProperty(value = "年款名称")
    private String yearStyleName;

    @ApiModelProperty(value = "配置")
    private Long configurationId;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty(value = "配置名称")
    private String configName;
}
