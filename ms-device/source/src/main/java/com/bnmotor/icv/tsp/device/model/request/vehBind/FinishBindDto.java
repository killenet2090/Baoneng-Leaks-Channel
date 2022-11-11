package com.bnmotor.icv.tsp.device.model.request.vehBind;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: VerifyAndFinishBindDto
 * @Description: 校验验证码和完成绑定请求对象
 * @author: huangyun1
 * @date: 2020/6/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "校验验证码和完成绑定请求对象", description = "校验验证码和完成绑定请求对象")
public class FinishBindDto {
    /**
     * 车辆识别号
     */
    @ApiModelProperty(value = "车辆识别号", name = "vin", required = true, example = "T09988888888")
    @NotBlank(message = "[车辆识别号不能为空]")
    @Length(max = 32, message = "[车辆识别号长度超出限制]")
    private String vin;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId")
    private Long userId;
}
