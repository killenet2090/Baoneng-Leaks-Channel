package com.bnmotor.icv.tsp.ota.model.resp.feign;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehicleVo
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/16 14:29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class VehicleVo {
    @ApiModelProperty(value = "vehId")
    private String id;

    @ApiModelProperty(value = "车辆唯一标识")
    private String vin;

    @ApiModelProperty(value = "车牌号码")
    private String drivingLicPlate;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "车辆状态")
    private String vehStatus;

    @ApiModelProperty(value = "车辆属性：对应多个标签")
    private String vehAttribute;

    @ApiModelProperty(value = "车主姓名")
    private String userName;

    @ApiModelProperty(value = "车辆认证状态")
    private String certificationStatus;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "车系")
    private String vehSeriesName;

    @ApiModelProperty(value = "年款")
    private String yearStyleName;

    @ApiModelProperty(value = "配置")
    private String vehConfigName;

    @ApiModelProperty(value = "颜色")
    private String color;

    @ApiModelProperty(value = "车辆类型")
    private String vehType;

    @ApiModelProperty(value = "发布日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private String publishDate;

    @ApiModelProperty(value = "车辆所属关系id")
    private String orgId;
}
