package com.bnmotor.icv.tsp.device.model.request.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: UpdateUserIdentificationDto
 * @Description: 修改用户身份信息请求DTO
 * @author: huangyun1
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@ApiModel("修改用户身份信息")
@Data
public class UpdateUserIdentificationDto implements Serializable {

    @ApiModelProperty(value = "用户userId")
    private Long userId;

    @ApiModelProperty(value = "证件类型: 0 - 身份证, 1 - 港澳通行证,2 - 护照")
    private Integer idType;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "证件号码")
    private String idCardNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "证件有效期开始时间")
    private Date idCardValidFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "证件有效期到期时间")
    private Date idCardValidTo;

    @ApiModelProperty(value = "证件照片正面URL")
    private String idCardImage1Url;

    @ApiModelProperty(value = "证件照片反面URL")
    private String idCardImage2Url;

}
