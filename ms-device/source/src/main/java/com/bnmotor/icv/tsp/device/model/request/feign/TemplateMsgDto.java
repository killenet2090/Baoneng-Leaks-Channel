package com.bnmotor.icv.tsp.device.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @ClassName: TemplateMsgDto
 * @Description: 短信模板发送入参实体
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "消息推送请求对象", description = "极光推送请求对象")
public class TemplateMsgDto {
    /**
     * 业务id识,用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识
     */
    @ApiModelProperty(value = "业务id识,用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识", name = "businessId", required = true, example = "E00123222222222223")
    @NotBlank(message = "[业务id不能为空]")
    @Length(max = 36, message = "[业务id长度超出限制]")
    private String businessId;

    /**
     * 来源终端类型 0后台管理平台 1-app 2-车机 3-tbox
     */
    @ApiModelProperty(value = "来源终端类型 0后台管理平台 1-app 2-车机 3-tbox", name = "fromType", required = true, example = "3")
    @NotNull(message = "[来源终端类型不能为空]")
    private Integer fromType;

    /**
     * 发送渠道 0无意义 1极光短信
     */
    @ApiModelProperty(value = "发送渠道 0无意义 1极光短信", name = "sendChannel", required = true, example = "1")
    @NotNull(message = "[发送渠道不能为空]")
    private Integer sendChannel = 1;

    /**
     * 发送手机号
     */
    @ApiModelProperty(value = "发送手机号", name = "sendPhone", required = false, example = "15011960899")
    @NotBlank(message = "[发送手机号不能为空]")
    @Length(max = 11, message = "[发送手机号长度超出限制]")
    private String sendPhone;

    /**
     * 映射模板id 1-用户注册验证码 2-发送锁车/解车验证码 3-发送熄火后锁定 4-告知用户车辆已锁定
     */
    @ApiModelProperty(value = "映射模板id 1-用户注册验证码 2-发送锁车/解车验证码 3-发送熄火后锁定 4-告知用户车辆已锁定", name = "mappingTemplateId", required = true, example = "1")
    @NotNull(message = "[映射模板id不能为空]")
    private Integer mappingTemplateId;

    /**
     * 扩展数据
     */
    @ApiModelProperty(value = "扩展数据", name = "extraData", required = false, example = "{'bId':'T00002221222'}")
    private Map<String, String> extraData;
}
