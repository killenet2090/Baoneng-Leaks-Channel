package com.bnmotor.icv.tsp.device.model.request.vehBind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @ClassName: VehicleBindDto
 * @Description: 车辆绑定请求DTO
 * @author: huangyun1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@EqualsAndHashCode
@ApiModel(value = "VehicleBindDto", description = "提交车辆绑定请求信息")
public class SubmitBindDto implements Serializable {
    private static final long serialVersionUID = 3356815490149109147L;

    /**
     * 发动机号
     */
    @NotEmpty(message = "发动机号不能为空！")
    @ApiModelProperty("发动机号")
    private String engineNo;

    /**
     * 车架号
     */
    @NotEmpty(message = "车架号不能为空！")
    @ApiModelProperty("车架号")
    private String vin;

    /**
     * 发票号码
     */
    @NotEmpty(message = "发票号码不能为空！")
    @ApiModelProperty("发票号码")
    private String invoiceNum;

    /**
     * 发票代码
     */
    @NotEmpty(message = "发票代码不能为空！")
    @ApiModelProperty("发票代码")
    private String invoiceCode;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 机动车销售发票照片
     */
    @ApiModelProperty("机动车销售发票照片")
    private MultipartFile invoiceFile;
}
