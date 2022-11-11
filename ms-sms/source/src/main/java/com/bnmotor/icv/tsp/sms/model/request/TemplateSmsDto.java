package com.bnmotor.icv.tsp.sms.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @ClassName: TemplateSmsDto
 * @Description: 短信模板发送入参实体
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "短信模板发送请求对象", description = "短信模板发送请求对象")
public class TemplateSmsDto extends AbstractSmsDto {
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
