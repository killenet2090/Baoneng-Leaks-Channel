package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "消息中心请求对象", description = "消息中心请求对象")
public class Targets {
    /**
     * 消息接收用户ID
     */
    private String uid;
    /**
     * 消息推送通道
     * 11. 手机系统短信
     * 21. APP-极光通道
     * 31. 车机-IHU通道
     * 41. 车机-TBOX通道
     */
    private Integer channel;

    /**
     * 推送目标：
     *     如果通道是手机短信，则为手机号码；
     *     如果通道是极光通道，则为RID；
     *     如果通道是HU，则为VIN码
     */
    private String target;
}
