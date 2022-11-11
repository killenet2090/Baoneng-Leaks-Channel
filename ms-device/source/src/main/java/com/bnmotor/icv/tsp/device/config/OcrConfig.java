package com.bnmotor.icv.tsp.device.config;

import com.bnmotor.icv.tsp.device.util.CustomAipOcr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName: OcrConfig
 * @Description: ocr配置
 * @author: huangyun1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@RefreshScope
public class OcrConfig {

    @Value("${device.ocr.baidu-api.app-id}")
    private String appId;
    @Value("${device.ocr.baidu-api.api-key}")
    private String apiKey;
    @Value("${device.ocr.baidu-api.secret-key}")
    private String secretKey;
    @Value("${device.ocr.baidu-api.connect-timeout}")
    private Integer connectTimeout;
    @Value("${device.ocr.baidu-api.socket-timeout}")
    private Integer socketTimeout;

    /**
     * 获取ocr配置实例
     * @return ClientConfig
     */
    @Bean
    public CustomAipOcr customAipOcr() {
        // 初始化一个AipOcr
        CustomAipOcr baiDuClient = new CustomAipOcr(appId, apiKey, secretKey);

        // 可选：设置网络连接参数
        baiDuClient.setConnectionTimeoutInMillis(connectTimeout);
        baiDuClient.setSocketTimeoutInMillis(socketTimeout);

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");
        return baiDuClient;
    }
}
