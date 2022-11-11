package com.bnmotor.icv.tsp.device.model.request.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: JpushContentVo
 * @Description: 极光推送内容实体
 * @author: huangyun1
 * @date: 2020/6/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class JpushContentDto<T> {
    /**
     * 消息类型 1-设置临客;2-取消临客;3-车机激活
     */
    @ApiModelProperty(value = "消息类型 1-设置临客;2-取消临客;3-车机激活", name = "type", required = true, example = "1")
    private Integer type = 3;

    /**
     * 响应实体
     */
    @JsonProperty("data")
    @ApiModelProperty(value = "返回实体泛型", name = "data", example = "任何对象")
    private T data;
    /**
     * 响应状态码
     */
    @JsonProperty("respCode")
    private String respCode;
    /**
     * 响应返回信息
     */
    @JsonProperty("respMsg")
    private String respMsg;
}
