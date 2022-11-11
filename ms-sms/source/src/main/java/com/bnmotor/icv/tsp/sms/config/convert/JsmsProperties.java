package com.bnmotor.icv.tsp.sms.config.convert;

import lombok.Data;

/**
 * @ClassName: PushProperties
 * @Description: 推送配置类
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class JsmsProperties {
    /**
     * 发送类型 0宝能汽车
     */
    private Integer sendType;
    /**
     * 极光推送appKey 必填，例如466f7032ac604e02fb7bda89
     */
    private String appKey;

    /**
     * 极光推送密钥 必填，每个应用都对应一个masterSecret
     */
    private String masterSecret;
}
