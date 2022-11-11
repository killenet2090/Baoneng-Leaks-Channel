package com.bnmotor.icv.tsp.device.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bnmotor.icv.adam.data.mysql.entity.BasePo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author zhangwei2
 * @ClassName: VehicleSaleInfoPo
 * @Description: 车辆销售信息 实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
@TableName("tb_vehicle_sale_info")
@ApiModel(value = "VehicleSaleInfoDo对象", description = "车辆销售信息")
public class VehicleSaleInfoPo extends BasePo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "项目代码")
    private String projectId;

    @ApiModelProperty(value = "车架号")
    private String vin;

    @ApiModelProperty(value = "经销商")
    private String dealer;

    @ApiModelProperty(value = "销售日期")
    private LocalDateTime saleDate;

    @ApiModelProperty(value = "发票代码/机打代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码/机打号码")
    private String invoiceNum;

    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime invoiceDate;

    @ApiModelProperty(value = "购买方身份证号码/组织机构代码")
    private String purchaserCode;

    @ApiModelProperty(value = "购买方手机号")
    private String purchaserPhone;

    @ApiModelProperty(value = "购买方名称")
    private String purchaser;

    @ApiModelProperty(value = "版本号")
    private Integer version;
}
