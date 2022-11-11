package com.bnmotor.icv.tsp.ota.event;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: OtaUpgradeOtherMessage
 * @Description:    ota升级过程信息
 * @author: xuxiaochang1
 * @date: 2020/11/25 14:11
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Builder
public class OtaUpgradeOtherMessage {
    /**
     *
     */
    private Long reqId;
    private Object t;
}
