package com.bnmotor.icv.tsp.ota.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePkgPo;
import com.bnmotor.icv.tsp.ota.service.IFotaFileUploadRecordService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwarePkgDbService;
import com.bnmotor.icv.tsp.ota.service.IFotaFirmwarePkgService;
import com.bnmotor.icv.tsp.ota.service.pki.PkiEncryptSignatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: EncryptFirmwarePackageTasks.java EncryptFirmwarePackageTasks
 * @Description: 临时的计划任务 关于加密密钥的选择，如果对应升级版本号，应该是目标版本的版本号
 * @author E.YanLonG
 * @since 2020-11-12 19:22:45
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
//@EnableScheduling
@Component
@Slf4j
public class EncryptFirmwarePackageTasks {

	@Autowired
	IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;

	@Autowired
	IFotaFirmwarePkgService fotaFirmwarePkgService;

	@Autowired
	IFotaFileUploadRecordService fotaFileUploadRecordService;

	@Autowired
	PkiEncryptSignatureService pkiEncryptSignatureService;

	/**
	 * 每小时执行一次
	 */
//	@XxlJob("otaFotaPakcageEncryptJobHandler")
	public void execute() {
		log.info("EncryptFirmwarePackageTasks加密升级包计划任务开始执行...");
		List<FotaFirmwarePkgPo> fotaFirmwarePkgPoList = queryFotaFirmwarePkgPoList(2, 0);

		log.info("待处理列表|{}", fotaFirmwarePkgPoList.size());
		fotaFirmwarePkgPoList.forEach(fotaFirmwarePkgPo -> {
			pkiEncryptSignatureService.proces(fotaFirmwarePkgPo);
		});
		log.info("EncryptFirmwarePackageTasks加密升级包计划任务结束执行...");
	}

//	@Deprecated
//	public void test() {
//		log.info("EncryptFirmwarePackageTasks加密升级包计划任务开始执行...");
//		List<FotaFirmwarePkgPo> fotaFirmwarePkgPoList = queryfotaFirmwarePkgPoList(2, 0);
//
//		log.info("待处理列表|{}", fotaFirmwarePkgPoList.size());
//
//		FotaFirmwarePkgPo first = fotaFirmwarePkgPoList.stream().findFirst().orElse(null);
//		proces(first);
//
//		log.info("EncryptFirmwarePackageTasks加密升级包计划任务结束执行...");
//	}

	public List<FotaFirmwarePkgPo> queryFotaFirmwarePkgPoList(Integer buildState, Integer encryptState) {
		QueryWrapper<FotaFirmwarePkgPo> queryWrapper = new QueryWrapper<FotaFirmwarePkgPo>();
		queryWrapper.eq("build_pkg_status", buildState);
		queryWrapper.eq("encrypt_state", encryptState);
		return fotaFirmwarePkgDbService.list(queryWrapper);
	}

}