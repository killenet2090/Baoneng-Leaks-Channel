package com.bnmotor.icv.tsp.ota.config;

import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class PkiConfig {
	
	String bucketName;
	/**
	 * 当前目录
 	 */
	String tmpDirectory;
	/**
	 * 加密文件后缀名
	 */
	String encryptSuffix;

	/**
	 * ms-ota-service
 	 */
	String appName;
	
	/**
	 * 加密开关
	 */
	@Value("${pki.encrypt.switch}")
	Integer encryptSwitch;
	
	public PkiConfig() {
		bucketName = CommonConstant.BUCKET_OTA_NAME;
		tmpDirectory = "otatmp/";
		encryptSuffix = ".encrypted";
		appName = "tsp-ota";
	}
	
}