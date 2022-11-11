package com.bnmotor.icv.tsp.sms.config.convert;

import lombok.Data;

/**
 * @ClassName: SmsProperties
 * @Description: 短信配置类
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class TemplateProperties {
    /**
     * 极光模板id
     */
    private Integer templateId;
    /**
     * 使用哪个账户发送短信 对应veh.event.jpushSettingMap.sendType的值 默认配1
     */
    private Integer useSendType;

}
