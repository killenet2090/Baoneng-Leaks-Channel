package com.bnmotor.icv.tsp.device.model.request.feign;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: VehLoginDto
 * @Description: 车机登录请求实体
 * @author: huangyun1
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@ApiModel(value = "VehLoginDto", description = "车机登录")
@Data
public class VehLoginDto {

    @ApiModelProperty(value = "车架号", name = "vin", required = true, example = "xsssda16546546")
    private String vin;

    @ApiModelProperty(value = "用户id", name = "userId", required = true, example = "xsssda16546546")
    private String uid;

    @ApiModelProperty(value = "登录模式", name = "mode", required = true, example = "1:脸部；2: 声纹; 3: 扫码")
    private Integer modeType;
}
