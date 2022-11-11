package com.bnmotor.icv.tsp.commons.oss.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ThreadPoolProperties
 * @Description: 线程池配置
 * @author: zhangjianghua1
 * @date: 2020/9/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class ThreadPoolProperties {
    private int minSize = 8;
    private int maxSize = 16;
    private int queueCapacity = 1000;
    private int keepAliveSeconds = 60;
    private int awaitTerminationSeconds = 60;
    private String threadNamePrefix = "async-task";
}
