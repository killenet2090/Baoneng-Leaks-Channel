package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.route.RouteKey;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: OtaUpgradeMessage
 * @Description:    ota升级过程信息
 * @author: xuxiaochang1
 * @date: 2020/11/25 14:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class OtaUpgradeMessageBase {
    /**
     *
     */
    private Object data;

    /**
     *
     */
    @RouteKey
    private String vin;

    /**
     *
     */
    private AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum;

    /**
     * 升级消息类型：1=新版本检测结果,2=OTA升级其他流程
     */
    private Integer otaUpgradeMessageType;

    /**
     * 推送给APP其他应用所需要的消息:1=APP首页显示升级进度
     */
    private OtaDispatch4OtherMessage otaDispatch4OtherMessage;

    /**
     * 升级模式：1=工厂，2=提示交互模式，3=静默下载
     */
    private Enums.OtaUpgradeModeTypeEnum otaUpgradeModeTypeEnum;

    /**
     * 升级阶段
     */
    private Enums.OtaUpgradePhaseTypeEnum otaUpgradePhaseTypeEnum;
}
