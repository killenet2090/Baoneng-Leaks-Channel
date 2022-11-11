package com.bnmotor.icv.tsp.ota.service.pki;

import com.bnmotor.icv.adam.data.redis.lock.annotation.RedisLock;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.common.enums.EncryptStateEnum;
import com.bnmotor.icv.tsp.ota.common.enums.Enums;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventMonitor;
import com.bnmotor.icv.tsp.ota.config.PkiConfig;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFileUploadRecordPo;
import com.bnmotor.icv.tsp.ota.model.entity.FotaFirmwarePkgPo;
import com.bnmotor.icv.tsp.ota.model.req.feign.SignOtaFileDto;
import com.bnmotor.icv.tsp.ota.service.*;
import com.bnmotor.icv.tsp.ota.service.feign.TspPkiFeighService;
import com.bnmotor.icv.tsp.ota.service.pki.kit.ByteUtils;
import com.bnmotor.icv.tsp.ota.service.pki.kit.EncryptOtaFileData;
import com.bnmotor.icv.tsp.ota.service.pki.kit.PackageUpgradeEncryptKit;
import com.bnmotor.icv.tsp.ota.service.pki.kit.SecretKeygenerator;
import com.bnmotor.icv.tsp.ota.util.FileVerifyUtil;
import com.bnmotor.icv.tsp.ota.util.MinIOUtil;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @ClassName: PkiEncryptSignatureService.java PkiEncryptSignatureService
 * @Description: OTA系统信息安全技术要求V1.3-20200921.pdf
					OTA Server 端对已加密的数据（升级包的密文）进行 Hash，得到加密包 Hash 值；
					 
 * @author E.YanLonG
 * @since 2020-11-11 16:47:48
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class PkiEncryptSignatureService {

	@Autowired
	PkiConfig pkiConfig;
	
	@Autowired
	TspPkiFeighService tspPkiFeighService;
	
	@Autowired
	MinIOUtil minIOUtil;
	
	@Autowired
    OssService oSSService;
	
	@Autowired
	IFotaFileUploadRecordService fotaFileUploadRecordService;

	@Autowired
	IFotaFileUploadRecordDbService fotaFileUploadRecordDbService;
	
	@Autowired
	IFotaFirmwarePkgService fotaFirmwarePkgService;

	@Autowired
	IFotaFirmwarePkgDbService fotaFirmwarePkgDbService;
	 
	/**
	 * 下载升级包到本地提示：通过开放URL下载
	 * @param source
	 * @param objectName
	 * @return
	 * @throws Exception
	 */
	public File downloadFile4Storage(String source, String objectName) throws Exception {
		URL url = new URL(source);
		String filename = FilenameUtils.getName(objectName);
		File distPath = new File(pkiConfig.getTmpDirectory(), filename); // 保存在本地的文件名
		log.info("下载文件位置|{}", distPath.getAbsolutePath());
		FileUtils.copyURLToFile(url, distPath);
		return distPath;
	}
	
	/**
	 * 下载升级包到本地目录
	 * 提示：通过filekey下载
	 * 
	 * @param source
	 * @param fileKey
	 * @return
	 * @throws Exception
	 */
	public File downloadFile4Storage0(String source, String fileKey) throws Exception {
		
		String filename = FilenameUtils.getName(fileKey);
		File file = new File(pkiConfig.getTmpDirectory(), filename); // 保存在本地的文件名
		log.info("下载文件filekey|{} 位置|{}", fileKey, file.getAbsolutePath());
		String bucketName = pkiConfig.getBucketName();
		oSSService.downloadFile(bucketName, file, fileKey);
		return file ;
	}
	
	/**
	 * 不能删除目录，可能有其它加密任务
	 * @param file
	 * @throws Exception
	 */
	public void cleanFile4Storage(File file) throws Exception {
//		boolean isDirectory = file.isDirectory();
//		if (isDirectory) {
//			FileUtils.deleteDirectory(file);
//		}
		FileUtils.deleteQuietly(file);
	}
	
	public void selectFile4Storage(String source, String otaFileVersion, String objectName, Long fotaFirmwarePkgId) throws Exception {
		
		//  从外部存储下载文件
		File file = null;
		OtaEventMonitor.stick("1. 下载待加密文件");
		try {
			file = downloadFile4Storage0(source, objectName);
		} catch (Exception e) {
			log.info("通过filekey下载文件失败, 尝试通过openurl下载 filekey|{}", objectName);
			file = downloadFile4Storage(source, objectName);
		}
		OtaEventMonitor.stop();
		MyAssertUtil.notNull(file, OTARespCodeEnum.FOTA_PACKAGE_DOWNLOAD_FAILED);
		
		String otaFileInputPath = file.getAbsolutePath();
		
		OtaEventMonitor.stick("2. 签名待加密文件");
		// 签名用的Hash
		byte[] signSha256 = PackageUpgradeEncryptKit.signFileHash(otaFileInputPath);
		if (signSha256 == null) {
			log.error("ota文件摘要失败|{}", otaFileInputPath);
			return;
		}

		log.info("hash:" + ByteUtils.Bytes2hex(signSha256));

		// 签名文件
//		AlgorithmTypeEnum signAlg = AlgorithmTypeEnum.SHA256WithRSA;
		String appName = pkiConfig.getAppName();
		String releasePkgSign = signMessageRemote(signSha256, appName);
		OtaEventMonitor.stop();
		
		OtaEventMonitor.stick("3. 加密文件");
		// 加密文件
		EncryptOtaFileData encryptOtaFileData = PackageUpgradeEncryptKit.encryptOtaFile(otaFileInputPath, otaFileVersion, pkiConfig.getTmpDirectory(), releasePkgSign);
//		encryptOtaFileData.setSignDataStr(releasePkgSign);
		OtaEventMonitor.stop();
		
		// 加密密钥
//		encryptOtaFileData.setKeyScrect(otaFileVersion);
//		String encryptSecret = encryptOtaFileData.getKeyScrect();
		
		Integer state = encryptOtaFileData.getState();
		MyAssertUtil.isTrue(EncryptOtaFileData.STATUS_SUCCESS.intValue() == state.intValue(), OTARespCodeEnum.FOTA_PACKAGE_ENCRYPT_FAILED);
		
		// 本地加密后的文件 .data
//		String distPath = encryptOtaFileData.getEncryptOtaFileDataPath(); // 本地路径
		
		// 3.1 重写加密文件
		OtaEventMonitor.stick("4. 重写加密文件");
		String encryptedFilePath = encryptOtaFileData.getEncryptOtaFileDataPath();
		long encryptedFileLength = encryptOtaFileData.getEncryptOtaFileDataLength();
		byte[] versionData = encryptOtaFileData.getVersionData();
		byte[] signedData = encryptOtaFileData.getSignData();
//		String outputFilePath = pkiConfig.getTmpDirectory() + file.getName() + ".encrypted";
		String outputFilePath = outputFilePath(file.getName());
		File outputFile = new File(outputFilePath);
		PackageUpgradeEncryptKit.makeEncryptFile(encryptedFilePath, encryptedFileLength, versionData, signedData, outputFilePath);
		OtaEventMonitor.stop();
		
		OtaEventMonitor.stick("5. 计算加密文件Hash");
		// 计算加密后的文件的sha256
		AtomicLong fileSize0 = new AtomicLong(0);
		String sha256 = sha256Encrypted(outputFilePath, fileSize0);
		long fileSize = fileSize0.get();
		OtaEventMonitor.stop();
		
		// 上传加密后的文件
		OtaEventMonitor.stick("6. 上传加密文件到MinIO");
		String bucketName = pkiConfig.getBucketName();
		String encryptedObjectName = encryptedObjectName(objectName);
		String accessUrl = uploadFile4Storage(outputFilePath, bucketName, encryptedObjectName);
		OtaEventMonitor.stop();
		
		// 删除本地文件
		cleanFile4Storage(file);
		cleanFile4Storage(outputFile);
		
		// 增加文件上传记录，增加前判断之前是否有上传过
		Long uploadFileId = uploadFileUploadRecord(fileSize, encryptedObjectName, accessUrl, sha256);
		log.info("增加上传文件记录|{}", uploadFileId);
		
		// 更新表tb_fota_firmware_pkg记录
		String encryptSecret = otaFileVersion;
		updatefotaFirmwarePkg(fotaFirmwarePkgId, releasePkgSign, encryptSecret, accessUrl, uploadFileId, fileSize, sha256);
		log.info("更新升级包记录|{}", fotaFirmwarePkgId);
	}
	
	public String outputFilePath(String fileKey) {
		String filename = FilenameUtils.getName(fileKey) + pkiConfig.getEncryptSuffix();
		File file = new File(pkiConfig.getTmpDirectory(), filename);
		return file.getAbsolutePath();
//		return outputFilePath;
	}
	
	public String encryptedObjectName(String objectName) {
//		String encryptedObjectName = objectName + pkiConfig.getEncryptSuffix();
		return deta(objectName, pkiConfig.getEncryptSuffix());
	}
	
	/**
	 * 7b45f8acdcfd5d7bf0d5cbd22b71a00cf8172b3b060d85c37fa0c0696a543570-1608208983.encrypted
	 * @param objectName
	 * @param suffix
	 * @return
	 */
	public String deta(String objectName, String suffix) {
		long deta = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		return String.format("%s%d%s", objectName, deta, suffix);
	}
	
	public String sha256Encrypted(String source, AtomicLong filesize) throws Exception {
		File file = new File(source);
		//byte[] bytes = FileUtils.readFileToByteArray(file);
		FileInputStream fis = new FileInputStream(file);
		filesize.set(fis.available());
		String sha256 = FileVerifyUtil.sha256(fis);
		try {
			if(null != fis) {
				fis.close();
			}
		}catch(Exception e){
			log.error("关闭文件流异常", e);
		}
		return sha256;
	}
	
	/**
	 * 增加表记录tb_fota_file_upload_record
	 * @param fileSize
	 * @param fileKey
	 * @param fileAddress
	 * @param sha256
	 * @return
	 */
	public Long uploadFileUploadRecord(Long fileSize, String fileKey, String fileAddress, String sha256) {
		FotaFileUploadRecordPo fotaFileUploadRecordPo = new FotaFileUploadRecordPo();
		Date encryptedOperateDate = new Date();
		fotaFileUploadRecordPo.setFileSize(fileSize);
		fotaFileUploadRecordPo.setFileName(fileKey);
		fotaFileUploadRecordPo.setDelFlag(Enums.DelFlagEnum.RESERVED.getFlag());
		fotaFileUploadRecordPo.setUploadBeginDt(encryptedOperateDate);
		fotaFileUploadRecordPo.setUploadEndDt(encryptedOperateDate);
		fotaFileUploadRecordPo.setFileAddress(fileAddress);
		fotaFileUploadRecordPo.setFileKey(fileKey);
		fotaFileUploadRecordPo.setFileType(0); // 文件用途类型:0 安装包文件

        //计算sha值
		fotaFileUploadRecordPo.setFileSha(sha256);
		fotaFileUploadRecordPo.setStatus("0");
		fotaFileUploadRecordPo.setVersion(0);
		fotaFileUploadRecordPo.setCreateBy("sys");
		fotaFileUploadRecordDbService.saveOrUpdate(fotaFileUploadRecordPo);
		return fotaFileUploadRecordPo.getId();
	}
	
	/**
	 * 加密后更新表tb_fota_firmware_pkg记录
	 * @param releasePkgSign 签名
	 * @param encryptSecret 密钥
	 * @param pkgFileDownloadUrl 加密后的升级包下载地址
	 * @return 更新结果
	 */
	public boolean updatefotaFirmwarePkg(Long fotaFirmwarePkgId, String releasePkgSign, String encryptSecret, String pkgFileDownloadUrl, Long encryptUploadFileId, Long releasePkgFileSize, String releasePkgShaCode) {
		String releasePkgSignAlg = PackageUpgradeEncryptKit.SHA256WithRSA;
		String releasePkgEncryptAlg = PackageUpgradeEncryptKit.TRANSFORMATION;
		
		FotaFirmwarePkgPo fotaFirmwarePkgPo = new FotaFirmwarePkgPo();
		fotaFirmwarePkgPo.setId(fotaFirmwarePkgId);
		fotaFirmwarePkgPo.setEncryptUploadFileId(encryptUploadFileId);
		fotaFirmwarePkgPo.setEncryptPkgStatus(EncryptStateEnum.TYPE_FINISH.getState()); // 加密结果
		fotaFirmwarePkgPo.setReleasePkgSign(releasePkgSign); // 签名
		fotaFirmwarePkgPo.setReleasePkgSignAlg(releasePkgSignAlg); // 签名算法
		fotaFirmwarePkgPo.setReleasePkgEncryptAlg(releasePkgEncryptAlg); // 加密算法
		fotaFirmwarePkgPo.setReleasePkgEncryptSecret(encryptSecret); // 加密密钥
		fotaFirmwarePkgPo.setReleasePkgFileDownloadUrl(pkgFileDownloadUrl); // 加密后的下载地址
		fotaFirmwarePkgPo.setUpdateTime(LocalDateTime.now());
		fotaFirmwarePkgPo.setCreateBy("sys");
		fotaFirmwarePkgPo.setUpdateBy("sys");
		fotaFirmwarePkgPo.setReleasePkgFileSize(releasePkgFileSize);
		fotaFirmwarePkgPo.setReleasePkgShaCode(releasePkgShaCode);
		fotaFirmwarePkgPo.setReleasePkgCdnDownloadUrl(pkgFileDownloadUrl);
		log.info("fotaFirmwarePkgPo|{}", fotaFirmwarePkgPo);
		return fotaFirmwarePkgDbService.updateById(fotaFirmwarePkgPo);
	}
	
	public String uploadFile4Storage(String distPath, String bucketName, String objectName) throws IOException {
		oSSService.uploadFile(distPath, objectName);
		String encryptedFileUrl = oSSService.getObjectUrl(bucketName, objectName);
		log.info("加密后的文件下载地址|{}", encryptedFileUrl);
		return encryptedFileUrl;
	}
	
	public String signMessageRemote(byte[] hash, String applicationName) {
		SignOtaFileDto signDto = new SignOtaFileDto();
		signDto.setApplicationName(applicationName);
		signDto.setHash(hash);
		
		String sign = null;
		RestResponse<String> restResponse = tspPkiFeighService.signMessage(signDto);
		if ("00000".equalsIgnoreCase(restResponse.getRespCode())) {
			sign = restResponse.getRespData();
		}
		
		MyAssertUtil.notNull(sign, OTARespCodeEnum.FOTA_PACKAGE_SIGN_FAILED);
		
		return sign;
	}
	
	/**
	 * 业务调用入口
	 * @param fotaFirmwarePkgPo
	 */
	@RedisLock(lockName = "tsp-ota::package-encrypt", waitTime = 120, timeUnit = TimeUnit.SECONDS)
	public void proces(FotaFirmwarePkgPo fotaFirmwarePkgPo) {
		log.info("开始处理记录FotaFirmwarePkgPo.id|{}", fotaFirmwarePkgPo.getId());

		int buildState = fotaFirmwarePkgPo.getEncryptPkgStatus();
		List<Integer> stateList = Lists.newArrayList(EncryptStateEnum.TYPE_BUILDING.getState(), EncryptStateEnum.TYPE_FINISH.getState());
		if (stateList.contains(buildState)) {
			log.info("对应的加密动作在处理中或已完成...firmwarePkgId|{}", fotaFirmwarePkgPo.getId());
			return;
		}

//		Long uploadFileId = fotaFirmwarePkgPo.getUploadFileId();
//		if (Objects.isNull(uploadFileId)) {
//			log.info("对应的文件上传记录不存在...");
//			return;
//		}
		
		Long buildFileUploadId = fotaFirmwarePkgPo.getBuildUploadFileId();
		if (Objects.isNull(buildFileUploadId)) {
			log.info("对应的构建文件上传记录不存在...");
			return;
		}

		// 查询上传记录获取fileKey（可能根据accessUrl下载不到升级包）
		FotaFileUploadRecordPo fotaFileUploadRecordPo = selectFotaFileUploadRecordPo(buildFileUploadId);
		if (Objects.isNull(fotaFileUploadRecordPo)) {
			log.info("对应的构建文件上传记录不存在...buildFileUploadId|{}", buildFileUploadId);
		}
		MyAssertUtil.notNull(fotaFileUploadRecordPo, OTARespCodeEnum.FOTA_PACKAGE_UPLOAD_FILE_NO_EXISTS);
		
//		String source = fotaFileUploadRecordPo.getFileAddress(); // 可能是升级包之前的原始包
		String fileKey = fotaFileUploadRecordPo.getFileKey();
		String source = fotaFileUploadRecordPo.getFileAddress(); // 下载的openurl

		String otaFileVersion = SecretKeygenerator.keygen();
		try {
			Long fotaFirmwarePkgId = fotaFirmwarePkgPo.getId();
			selectFile4Storage(source, otaFileVersion, fileKey, fotaFirmwarePkgId);
		} catch (Exception e) {
			log.error("加密升级包失败|{}", e.getMessage(), e);
		}
	}

	public FotaFileUploadRecordPo selectFotaFileUploadRecordPo(Long uploadFileId) {
		return fotaFileUploadRecordDbService.getById(uploadFileId);
	}
	
}