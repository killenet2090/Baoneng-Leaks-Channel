package com.bnmotor.icv.tsp.device.model.response.vehicle;

import com.bnmotor.icv.adam.core.view.BaseView;
import com.bnmotor.icv.tsp.device.model.response.View.Vehicle;
import com.bnmotor.icv.tsp.device.model.response.View.VehicleLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehicleVo
 * @Description: 车辆信息列表
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleVo {
    @JsonView(BaseView.class)
    @ApiModelProperty(value = "vehId")
    private String id;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "车辆唯一标识")
    private String vin;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "车牌号码")
    private String drivingLicPlate;

    @JsonView(Vehicle.class)
    @ApiModelProperty(value = "车辆状态")
    private String vehStatus;

    @JsonView(Vehicle.class)
    @ApiModelProperty(value = "车辆属性：对应多个标签")
    private String vehAttribute;

    @JsonView(Vehicle.class)
    @ApiModelProperty(value = "车主姓名")
    private String userName;

    @JsonView(Vehicle.class)
    @ApiModelProperty(value = "车辆认证状态")
    private String certificationStatus;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "品牌")
    private String brandName;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "车系id")
    private Long vehSeriesId;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "车系")
    private String vehSeriesName;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "车型id")
    private Long vehModelId;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "年款id")
    private Long yearStyle;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "年款")
    private String yearStyleName;

    @JsonView(BaseView.class)
    @ApiModelProperty(value = "配置id")
    private Long configurationId;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "配置")
    private String vehConfigName;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "颜色")
    private String color;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "车辆类型")
    private String vehType;

    @ApiModelProperty(value = "发布日期")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private String publishDate;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "车辆所属关系id")
    private String orgId;

    @JsonView(VehicleLevel.class)
    @ApiModelProperty(value = "车辆当前工作模式")
    private int lifeCircle;
}
