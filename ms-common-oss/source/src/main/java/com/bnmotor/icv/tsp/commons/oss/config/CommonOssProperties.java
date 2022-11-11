package com.bnmotor.icv.tsp.commons.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: OSSProperties
 * @Description:
 * @author: zhangjianghua1
 * @date: 2020/7/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Validated
@RefreshScope
@ConfigurationProperties(prefix = "adam.oss")
public class CommonOssProperties {

    private ThreadPoolProperties threadPool = new ThreadPoolProperties();

    /**
     * bucket访问策略
     */
    private Map<String, List<BucketPolicy>> bucketPolicy;
    /**
     * 用于网络文件临时存储目录
     */
    private String tmpFilePath = "/data/oss/tmp";

    /**
     * 可上传的文件后缀
     */
    @NotEmpty(message = "可上传的文件后缀列表不能为空")
    private Set<String> acceptFileSuffix;

    /**
     * 视频文件后缀
     */
    @NotEmpty(message = "视频文件后缀列表不能为空")
    private Set<String> mediaSuffix;

    @Data
    public static class BucketPolicy{
        /**
         * 前缀
         */
        private String prefix;
        /**
         * 访问权限：
         * r, w, r/w
         */
        private String access;
    }

}
