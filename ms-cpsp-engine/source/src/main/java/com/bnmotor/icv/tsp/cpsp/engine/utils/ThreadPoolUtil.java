package com.bnmotor.icv.tsp.cpsp.engine.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: ThreadPoolUtils
 * @Description: 线程池工具类
 * @author: liuhuaqiao1
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Component
public class ThreadPoolUtil {

    // 整个应用程序只创建一个线程池
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * 执行Runnable任务
     *
     * @param command
     */
    public static void execute(Runnable command) {
        if (threadPool.isShutdown()) {
            // 如果线程池关闭，那么就再创建一个线程池
            threadPool = Executors.newCachedThreadPool();
            execute(command);
        } else {
            threadPool.execute(command);
        }
    }

    /**
     * 关闭线程池
     */
    public static void shutdown() {
        try {
            if (!threadPool.isShutdown()) {
                log.info("关闭线程池:{}", threadPool);
                threadPool.shutdown();
            }
        } catch (Exception e) {
            if (!threadPool.isTerminated()) {
                threadPool.shutdownNow();
            }
        } finally {
            try {
                if (threadPool != null && !threadPool.isShutdown()) {
                    threadPool.shutdown();
                }
            } catch (Exception e) {
                log.error("线程池关闭异常：{}", e);
            }
        }
    }
}
