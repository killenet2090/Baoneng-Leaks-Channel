package com.bnmotor.icv.tsp.device.model.request.vehBind;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author huangyun1
 * @ClassName: SynchronizationSaleInfoDto
 * @Description: 同步车辆销售信息请求实体类
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 * @since 2020-07-04
 */
@Data
@ApiModel(value = "SynchronizationSaleInfoDto对象", description = "同步车辆销售信息")
public class VehicleSaleInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆识别号")
    @NotBlank(message = "[车辆识别号不能为空]")
    @Length(max = 32, message = "[车辆识别号长度超出限制]")
    private String vin;

    @ApiModelProperty(value = "经销商")
    @Length(max = 33, message = "[经销商长度超出限制]")
    private String dealer;

    @ApiModelProperty(value = "销售日期")
    private String saleDateStr;

    @ApiModelProperty(value = "发票代码/机打代码")
    @NotBlank(message = "[发票代码/机打代码不能为空]")
    @Length(max = 16, message = "[发票代码/机打代码长度超出限制]")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码/机打号码")
    @NotBlank(message = "[发票号码/机打号码不能为空]")
    @Length(max = 16, message = "[发票号码/机打号码长度超出限制]")
    private String invoiceNum;

    @ApiModelProperty(value = "开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String invoiceDateStr;

    @ApiModelProperty(value = "购买方身份证号码/组织机构代码")
    @NotBlank(message = "[购买方身份证号码/组织机构代码不能为空]")
    @Length(max = 32, message = "[购买方身份证号码/组织机构代码超出限制]")
    private String purchaserCode;

    @ApiModelProperty(value = "购买方手机号")
    @NotBlank(message = "[购买方手机号不能为空]")
    @Length(max = 16, message = "[购买方手机号超出限制]")
    private String purchaserPhone;

    @ApiModelProperty(value = "购买方名称")
    @NotBlank(message = "[购买方名称不能为空]")
    @Length(max = 32, message = "[购买方名称超出限制]")
    private String purchaser;
}
