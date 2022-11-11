package com.bnmotor.icv.tsp.ota.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MinIOConfig
 * @Description: MinIO文件存储相关信息自动配置
 * @author: xuxiaochang1
 * @date: 2020/6/5 9:43
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
public class MinIoConfig {

    @Value("${adam.tbox.pkg.download.domain}")
    @Getter
    private String tboxPkgDownloadDomain;
}
