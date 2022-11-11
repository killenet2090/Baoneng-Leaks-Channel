package com.bnmotor.icv.tsp.device.model.response.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: GetDeviceInfoVo
 * @Description: 获取设备信息实体
 * @author: huangyun1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDeviceInfoVo {
    /**
     * 极光推送registrationId
     */
    @ApiModelProperty(value = "极光推送registrationId", name = "pushRid", required = true, example = "1")
    private String pushRid;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号", name = "phone", example = "15011960899")
    private String phone;
}
