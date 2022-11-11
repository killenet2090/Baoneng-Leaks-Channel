package com.bnmotor.icv.tsp.device.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: DelBlueKeyCallbackDto
 * @Description: 注销蓝牙钥匙回调入参实体
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "注销蓝牙钥匙回调请求对象", description = "注销蓝牙钥匙回调请求对象")
public class DelBlueKeyCallbackDto {
    /**
     * 车辆识别号vin
     */
    @NotBlank(message = "车辆识别号vin不能为空")
    @ApiModelProperty(value = "车辆识别号vin",required = true,dataType = "String", example = "1288728448789463041")
    @Length(min = 17,max = 17)
    private String vin;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id",required = true,dataType = "Long", example = "1288728448789463041")
    @NotNull(message = "[用户Id不能为空]")
    private Long userId;

    /**
     * 响应状态码
     */
    @NotNull(message = "响应状态码0x01 - 执行成功" +
            "0x02 - 钥匙已存在\n" +
            "0x03 - 钥匙未找到\n" +
            "0x04 - 数量已满\n" +
            "0x05 - 钥匙保存失败\n" +
            "0x06 - MCU通讯失败\n" +
            "0x07 - 错误的命令")
    @ApiModelProperty(value = "响应状态码",required = true, dataType = "int", example = "00000")
    private  Integer respCode;
}
