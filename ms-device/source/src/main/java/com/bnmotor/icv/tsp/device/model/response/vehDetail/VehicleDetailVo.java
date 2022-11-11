package com.bnmotor.icv.tsp.device.model.response.vehDetail;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.bnmotor.icv.tsp.device.model.response.tag.TagVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: VehicleDetailVo
 * @Description: 车辆详情
 * @author: zhangwei2
 * @date: 2020/7/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehicleDetailVo {
    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "车牌号")
    private String drivingLicPlate;

    @ApiModelProperty(value = "mno认证状态")
    private String certificationStatus;

    @ApiModelProperty(value = "绑定状态")
    private String bindStatus;

    @ApiModelProperty(value = "车辆状态")
    private String vehStatus;

    @ApiModelProperty(value = "车辆属性")
    private String attributes;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "车系")
    private String vehSeriesName;

    @ApiModelProperty(value = "车辆型号")
    private String vehModelName;

    @ApiModelProperty(value = "年款")
    private String yearStyleName;

    @ApiModelProperty(value = "车辆配置")
    private String configName;

    @ApiModelProperty(value = "动力类型")
    private Integer vehType;

    @ApiModelProperty(value = "动力类型")
    private String vehTypeName;

    @ApiModelProperty(value = "车辆颜色")
    private String color;

    @ApiModelProperty(value = "车辆级别")
    private String vehLevel;

    @ApiModelProperty(value = "车辆颜色")
    private String vehCategory;

    @ApiModelProperty(value = "录入时间")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime productTime;

    @ApiModelProperty(value = "合格证日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime certificateTime;

    @ApiModelProperty(value = "出厂日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime outFactoryTime;

    @ApiModelProperty(value = "销售日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime saleDate;

    @ApiModelProperty(value = "绑定日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime bindTime;

    @ApiModelProperty(value = "激活日期")
    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private LocalDateTime activeTime;

    @ApiModelProperty(value = "车辆标签")
    private List<TagVo> labels;

    @ApiModelProperty(value = "销售信息")
    private String dealer;

    @ApiModelProperty(value = "质检状态")
    private String qualityInspectStatus;
}
