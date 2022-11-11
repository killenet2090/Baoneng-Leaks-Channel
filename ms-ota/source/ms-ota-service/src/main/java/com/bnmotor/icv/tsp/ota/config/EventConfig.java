package com.bnmotor.icv.tsp.ota.config;

import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: EventConfig.java EventConfig
 * @Description: 应用内消息配置
 * @author E.YanLonG
 * @since 2020-11-16 11:55:50
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@EnableAsync
public class EventConfig {

	@Bean(OtaEventConstant.EVENT_DISPATCHER_EXECUTOR)
	public Executor otaEventExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(1);
		taskExecutor.setMaxPoolSize(1);
		taskExecutor.setQueueCapacity(1000);

		taskExecutor.setKeepAliveSeconds(60);
		taskExecutor.setThreadNamePrefix("ota-event-dispatcher-");
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setAwaitTerminationSeconds(60);
		return taskExecutor;
	}
	
//	@Bean(OtaEventConstant.TASK_TRANSACTION_EXECUTOR)
//	public Executor otaTransactionEventExecutor() {
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(5);
//		taskExecutor.setMaxPoolSize(5);
//		taskExecutor.setQueueCapacity(1000);
//
//		taskExecutor.setKeepAliveSeconds(60);
//		taskExecutor.setThreadNamePrefix("ota-transaction-event-exec-");
//		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
//		taskExecutor.setAwaitTerminationSeconds(60);
//		return taskExecutor;
//	}
	
	@Bean(OtaEventConstant.TASK_COMMON_EXECUTOR)
	public Executor otaRouteEventExecutor() {
		int iunt = Runtime.getRuntime().availableProcessors();
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(iunt);
		taskExecutor.setMaxPoolSize(iunt);
		taskExecutor.setQueueCapacity(1000);

		taskExecutor.setKeepAliveSeconds(60);
		taskExecutor.setThreadNamePrefix("ota-common-event-exec-");
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		taskExecutor.setAwaitTerminationSeconds(60);
		return taskExecutor;
	}
}