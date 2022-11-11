package com.bnmotor.icv.tsp.ota.event;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventConstant;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventHandler;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventMonitor;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventPublisher;
import com.bnmotor.icv.tsp.ota.config.PkiConfig;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePkgPo;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwarePkgDbService;
import com.bnmotor.icv.tsp.ota.service.pki.PkiEncryptSignatureService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: PakcageBuildCompletedHandler.java PakcageBuildCompletedHandler
 * @Description: 示例处理参数
 * @author E.YanLonG
 * @since 2020-11-16 14:53:58
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@OtaMessageAttribute(topics = OtaEventConstant.UPGRADE_PACKAGE_BUILD_COMPLETE, msgtype = PackageBuildCompleted.class)
@Slf4j
public class PackageBuildCompletedHandler implements OtaEventHandler.EventHandler<PackageBuildCompleted> {

	@Autowired
	OtaEventPublisher otaEventPublisher;

	@Autowired
	IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

	@Autowired
	PkiEncryptSignatureService pkiEncryptSignatureService;
	
	@Autowired
	PkiConfig pkiConfig;

	/**
	 * 队列中大于一个任务直接失败
	 */
	@Override
	public PackageBuildCompleted onMessage(PackageBuildCompleted packageBuildCompleted) {
		log.info("PackageBuildCompletedHandler|{}", packageBuildCompleted);
		// 判断加密开关
		Integer encryptSwitch = pkiConfig.getEncryptSwitch();
		if (encryptSwitch.intValue() != 1) {
			log.info("PKI加密功能已关闭");
			return packageBuildCompleted;
		}
		
		Long packageId = packageBuildCompleted.getFirmwarePackageId();
		FotaFirmwarePkgPo fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(packageId);
		if (Objects.isNull(fotaFirmwarePkgPo)) {
			log.error("对应的记录FotaFirmwarePkgPo不存在|{}", packageBuildCompleted);
			return packageBuildCompleted;
		}

		try {
			process(packageBuildCompleted, fotaFirmwarePkgPo);
		} catch (Exception e) {
			log.error("执行加密操作失败...|{}", e.getMessage(), e);
		}

		return packageBuildCompleted;
	}

	/**
	 * 加密包任务耗时长，内存占用大，加密任务暂时设置成同一时刻只能有一个任务执行 lockKey =
	 * "tsp-ota::package-build-completed";
	 * 
	 * @param packageBuildCompleted
	 * @param fotaFirmwarePkgPo
	 */
	public void process(PackageBuildCompleted packageBuildCompleted, FotaFirmwarePkgPo fotaFirmwarePkgPo) {
		Long fotaFirmwarePkgId = fotaFirmwarePkgPo.getId();

		// 需要重新从数据库中查出
		fotaFirmwarePkgPo = fotaFirmwarePkgDbService.getById(fotaFirmwarePkgId);
		log.info("升级包加密操作=>准备进行升级包加密操作...|{}", fotaFirmwarePkgPo.getId());
		pkiEncryptSignatureService.proces(fotaFirmwarePkgPo);
		log.info("升级包加密操作=>已完成加密操作...|{}", fotaFirmwarePkgPo.getId());
	}
	
	@Override
	public void afterMessage(PackageBuildCompleted message) {
		log.info("耗时统计|{}", OtaEventMonitor.prettyPrint());
	}

	ThreadPoolExecutor executor;

	@Override
	public Executor executor() {
		executor = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.MINUTES, //
				new ArrayBlockingQueue<>(2), new ThreadFactory() {
					final AtomicLong sequence0 = new AtomicLong(0);

					@Override
					public Thread newThread(Runnable runnable) {
						Thread thread = new Thread(runnable);
						thread.setName("ota-encrypt-t" + sequence0.getAndDecrement());
						return thread;
					}
		}); //
		return executor;
	}

	@PreDestroy
	public void close() {
		if (Objects.nonNull(executor)) {
			executor.shutdown();
		}
	}

}