package com.bnmotor.icv.tsp.ble.model.request.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown=true)
@ApiModel(value = "消息中心请求对象", description = "消息中心请求对象")
public class TargetPlatforms {
    /**
     * 目标平台：
     * 1. 短信SMS 2. 手机APP 3. 车机HU
     */
    private Integer platform;
    /**
     * 目标平台消息位：
     * 11. 手机系统短信
     * 21. APP-通知栏 22. APP-弹窗 23. APP-站内信
     * 31. HU-通知栏 32. HU-弹窗 33. HU-主界面组件
     * 34. HU-宝能精灵 35. HU-TTS播报 36. HU-本地保存
     */
    private String platformInteraction;

    /**
     *
     */
    private List<Targets> targets;
}
