package com.bnmotor.icv.tsp.ota.common.route;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.digest.MurmurHash3;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RouteThreadFactory.java 
 * @Description: 支持路由的线程池
 * @author E.YanLonG
 * @since 2020-12-18 16:31:12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class RouteThreadFactory {

	final List<TaskExecutorPair> taskExecutorPairs = Lists.newArrayList();

	@Getter
	Integer size;

	public RouteThreadFactory(int size) {
		this.size = size;
		OrderedThreadFactory orderedThreadFactory = new OrderedThreadFactory();
		for (int i = 0; i < size; i++) {
			log.info("initial TaskExecutorPair{}", i);
			taskExecutorPairs.add(new TaskExecutorPair("TaskExecutorPair" + i, orderedThreadFactory));
		}
	}

	public Executor with(Object key) {
		if (Objects.isNull(key)) {
			return null;
		}
		
		int index = Math.abs(hashCode0(key)) % size;
		TaskExecutorPair taskExecutorPair = taskExecutorPairs.get(index);
		return taskExecutorPair.getExecutorService();
	}
	
	public int hashCode0(Object object) {
		String key = String.valueOf(object);
		return MurmurHash3.hash32(key.getBytes(StandardCharsets.UTF_8));
	}
	
	public void close() {
		log.info("RouteThreadFactory close...");
		taskExecutorPairs.stream().forEach(TaskExecutorPair::shutdown);
	}

	@NoArgsConstructor(staticName = "of")
	@Accessors(chain = true)
	@Data
	public static class TaskExecutorPair {
		String executorkey;
		ExecutorService executorService;
		OrderedThreadFactory orderedThreadFactory;

		public TaskExecutorPair(String executorkey, OrderedThreadFactory orderedThreadFactory) {
			this.executorkey = executorkey;
			this.orderedThreadFactory = orderedThreadFactory;
			executorService = Executors.newFixedThreadPool(1, orderedThreadFactory);
		}

		public void execute(Runnable runnable) {
			executorService.execute(runnable);
		}
		
		public void shutdown() {
			log.info("TaskExecutorPair shutdown|{}", executorkey);
			executorService.shutdown();
		}

	}
}

class OrderedThreadFactory implements ThreadFactory {

	static final AtomicInteger sequence = new AtomicInteger(0);

	String factory = "RouteThreadFactory";

	public OrderedThreadFactory() {
		this.factory = "RouteThreadFactory";
	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r, "RouteThread-t" + sequence.getAndIncrement());
	}

}