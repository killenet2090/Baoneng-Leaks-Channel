package com.bnmotor.icv.tsp.ble.model.request.ble;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: TspBleCaPinDto
 * @Description: 蓝牙钥匙证书pin码入参实体类
 * @author: shuqi1
 * @date: 2020/6/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(description = "BleCaPinDto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BleCaPinDto implements Serializable{
    @ApiModelProperty(value = "项目ID")
    @Length(min = 1,max = 19)
    private String projectId;

    @NotBlank(message = "车辆设备ID不能为空")
    @ApiModelProperty(value = "车辆设备ID", required = true, dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String  deviceId;

    @ApiModelProperty(value = "用户类型",required = true, dataType = "String", example = "1")
    @NotNull(message = "用户类型不能为空")
    private Integer  userType;
}
