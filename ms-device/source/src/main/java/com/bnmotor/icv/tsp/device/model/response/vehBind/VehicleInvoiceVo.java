package com.bnmotor.icv.tsp.device.model.response.vehBind;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: VehicleInvoicePo
 * @Description: 机动车销售发票信息 实体类
 * @author huangyun1
 * @since 2020-09-25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
@ApiModel(value = "机动车销售发票返回对象", description = "机动车销售发票返回对象")
public class VehicleInvoiceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发票代码/机打代码
     */
    @ApiModelProperty(value = "发票代码/机打代码")
    private String invoiceCode;

    /**
     * 发票号码/机打号码
     */
    @ApiModelProperty(value = "发票号码/机打号码")
    private String invoiceNum;

    /**
     * 开票日期
     */
    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime invoiceDate;

    /**
     * 车架号码
     */
    @ApiModelProperty(value = "车架号码")
    private String vin;

    /**
     * 发动机号码
     */
    @ApiModelProperty(value = "发动机号码")
    private String engineNo;

    /**
     * 购买方身名称
     */
    @ApiModelProperty(value = "购买方名称")
    private String purchaser;

    /**
     * 购买方名称及身份证号码/组织机构代码
     */
    @ApiModelProperty(value = "购买方名称及身份证号码/组织机构代码")
    private String purchaserCode;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

}
