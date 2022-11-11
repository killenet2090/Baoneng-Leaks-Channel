package com.bnmotor.icv.tsp.commons.oss.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: OssConfiguration
 * @Description:
 * @author: zhangjianghua1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@EnableConfigurationProperties(CommonOssProperties.class)
@Configuration
public class CommonOssConfiguration {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(CommonOssProperties properties) {
        ThreadPoolProperties threadPool = properties.getThreadPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPool.getMinSize());
        executor.setMaxPoolSize(threadPool.getMaxSize());
        executor.setQueueCapacity(threadPool.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPool.getKeepAliveSeconds());
        /**配置线程池中的线程的名称前缀*/
        if(null != threadPool.getThreadNamePrefix()) {
            executor.setThreadNamePrefix(threadPool.getThreadNamePrefix());
        }
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(threadPool.getAwaitTerminationSeconds());
        /**配置当队列和最大线程池都满了之后的饱和/拒绝策略：
         * ThreadPoolExecutor.AbortPolicy()：将抛出 RejectedExecutionException
         * ThreadPoolExecutor.CallerRunsPolicy()：直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务。
         * ThreadPoolExecutor.DiscardOldestPolicy()：丢弃最靠前以前的任务(无论任务已经完成还是正在运行)
         * ThreadPoolExecutor.DiscardPolicy()：默认情况下它将丢弃被拒绝的任务。
         * */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        /**初始化线程*/
        executor.initialize();

        return executor;

    }
}
