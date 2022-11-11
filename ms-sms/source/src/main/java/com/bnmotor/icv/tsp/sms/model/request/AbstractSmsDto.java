package com.bnmotor.icv.tsp.sms.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: AbstractMsgDto
 * @Description: 抽象消息入参实体
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "抽象消息入参请求对象", description = "抽象消息入参请求对象")
public abstract class AbstractSmsDto {
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
     * 扩展数据
     */
    @JsonIgnore
    private Long fromUserId;

}
