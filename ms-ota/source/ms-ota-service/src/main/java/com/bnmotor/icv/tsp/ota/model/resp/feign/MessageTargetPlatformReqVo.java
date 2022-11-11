package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: MessageTargetPlatformPo
 * @Description: 目标平台 实体类
 * @author zhangjianghua1
 * @since 2020-09-04
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */

@Data
public class MessageTargetPlatformReqVo {

    /**
     * 目标平台:
            1. 短信SMS
            2. 手机APP
            3. 车机HU
     */
    private Integer platform;

    /**
     * 目标平台消息位:
            11. 手机系统短信
            21. APP-通知栏
            22. APP-弹窗
            23. APP-站内信
            31. HU-通知栏
            32. HU-弹窗
            33. HU-主界面组件
            34. HU-宝能精灵
            35. HU-TTS播报
            36. HU-本地保存
     */
    private String platformInteraction;

    /**
     * 推送目标
     */
    private List<MessageTargetReqVo> targets;

}
