package com.bnmotor.icv.tsp.ota.model.resp.feign;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehicleOrgRelationVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/1/11 15:54
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class VehicleOrgRelationVo {
    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "品牌")
    private Long brandId;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "车系")
    private Long vehSeriesId;

    @ApiModelProperty(value = "车系名称")
    private String vehSeriesName;

    @ApiModelProperty(value = "车型")
    private Long vehModelId;

    @ApiModelProperty(value = "车型名称")
    private String vehModelName;

    @ApiModelProperty(value = "年款 ")
    private Long yearStyle;

    @ApiModelProperty(value = "年款名称")
    private String yearStyleName;

    @ApiModelProperty(value = "配置")
    private Long configurationId;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "车辆类型")
    private Integer vehType;
}
