package com.bnmotor.icv.tsp.device.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: QRProperties
 * @Description:
 * @author: huangyun1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RefreshScope
@Configuration
@Data
@ConfigurationProperties(prefix = "device.activate.qrcode")
public class QRProperties {
    /**
     * 有效时间
     */
    private Integer validTimeSeconds;
    /**
     * logo地址
     */
    private String logoUrl;
    /**
     * 校验url
     */
    private String checkQrcodeUrl;
    /**
     * 确认url
     */
    private String qrcodeConfirmUrl;
    /**
     * 取消url
     */
    private String qrcodeCancelUrl;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
}
