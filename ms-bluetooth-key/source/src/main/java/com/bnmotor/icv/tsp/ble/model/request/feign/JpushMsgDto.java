package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
/**
 * @ClassName: JpushDTO
 * @Description: 极光推送入参实体
 * @author: shuqi1
 * @date: 2020/7/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "消息推送请求对象", description = "极光推送请求对象")
public class JpushMsgDto {
    /**
     * 业务id识,用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识
     */
    @ApiModelProperty(value = "业务id识,用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识", name = "businessId", required = true, example = "E00123222222222223")
    @NotBlank(message = "[业务id不能为空]")
    @Length(max= 36, message = "[业务id长度超出限制]")
    private String businessId;
    /**
     * 是否推送给全部用户标记位
     */
    @ApiModelProperty(value = "是否推送给全部用户标记位", name = "sendAllFlag", required = true, example = "是：sendUserId和sendTagsList无需填写，否：sendUserId和sendTagsList不能同时为空")
    @NotNull(message = "[是否推送给全部用户标记位不能为空]")
    private Boolean sendAllFlag = false;
    /**
     * 推送手机设备号
     */
    @ApiModelProperty(value = "推送手机设备号[和sendTagsList只能二选一 同时填只有一个生效]", name = "sendRegistrationIdList", required = false)
    private List<String> sendRegistrationIdList;
    /**
     * 来源终端类型 0后台管理平台 1-app 2-车机 3-tbox
     */
    @ApiModelProperty(value = "来源终端类型 0后台管理平台 1-app 2-车机 3-tbox", name = "fromType", required = true, example = "3")
    @NotNull(message = "[来源终端类型不能为空]")
    private Integer fromType;
    /**
     * 发送标题
     */
    @ApiModelProperty(value = "发送标题", name = "sendTitle", required = false, example = "标题")
    @Length(max= 1000, message = "[来源终端类型长度超出限制]")
    private String sendTitle;
    /**
     * 发送内容
     */
    @ApiModelProperty(value = "发送内容", name = "sendContent", required = true, example = "内容")
    @NotBlank(message = "[发送内容不能为空]")
    @Length(max= 2000, message = "[来源终端类型长度超出限制]")
    private String sendContent;
    /**
     * 标签列表(可以是tags或registrationId)。一次推送最多 1000 个
     */
    @ApiModelProperty(value = "发送标签列表[标签列表tags，一次推送最多 1000 个，和sendRegistrationId只能二选一 同时填只有一个生效]", name = "sendTagsList", required = false, example = "[001,002]")
    private List<String> sendTagsList;

    /**
     * 扩展数据
     */
    @ApiModelProperty(value = "扩展数据", name = "extraData", required = false, example = "{'bId':'T00002221222'}")
    private Map<String, String> extraData;

    /**
     * 发送终端类型 0后台管理平台 1远控app 2车机 3远控tbox
     */
    @ApiModelProperty(value = "发送终端类型 0后台管理平台 1远控app 2车机 3远控tbox", name = "sendType", required = true, example = "3")
    @NotNull(message = "[发送终端类型不能为空]")
    private Integer sendType = 1;
}
