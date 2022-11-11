package com.bnmotor.icv.tsp.ota.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;

/**
 * @ClassName: SpringAsyncConfigurer
 * @Description: Sring异步线程池自定义
 * @author: xuxiaochang1
 * @date: 2020/11/19 14:43
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
/*@Configuration
@EnableAsync*/
@Slf4j
public class SpringAsyncConfigurer extends AsyncConfigurerSupport {
    /*@Bean
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = 20;
        executor.setCorePoolSize(corePoolSize);
        int maxPoolSize = 50;
        executor.setMaxPoolSize(maxPoolSize);
        int queueCapacity = 1000;
        executor.setQueueCapacity(queueCapacity);
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.error("异步线程池任务已满，放弃执行!");
            }
        });
        String threadNamePrefix = "ThreadPoolTaskExecutor-";
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        int awaitTerminationSeconds = 5;
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error(String.format("执行异步任务'%s'", method), ex);
    }*/
}
