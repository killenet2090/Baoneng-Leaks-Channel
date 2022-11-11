package com.bnmotor.icv.tsp.ota.service.impl;

import com.alibaba.excel.util.FileUtils;
import com.bnmotor.icv.adam.data.oss.IOSSProvider;
import com.bnmotor.icv.adam.data.oss.model.OSSObject;
import com.bnmotor.icv.tsp.ota.common.CommonConstant;
import com.bnmotor.icv.tsp.ota.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class OssServiceImpl implements OssService {

	@Autowired
	private IOSSProvider ossProvider;

	@Override
	public void downloadFile(String bucketName, File file, String fileKey) {
		OSSObject object = ossProvider.getObject(bucketName, fileKey);
		InputStream inputStream = object.getContent();
		FileUtils.writeToFile(file, inputStream);
	}

	@Override
	public String getObjectUrl(String ducket, String fileKey) {
		return ossProvider.getObjectUrl(CommonConstant.BUCKET_OTA_NAME, fileKey);
	}

	@Override
	public void uploadFile(String filePath, String fileKey) throws IOException {

		File file = new File(filePath);
		try (FileInputStream fis = new FileInputStream(file)) {
			ossProvider.putObject(CommonConstant.BUCKET_OTA_NAME, fileKey, fis);
		} finally {
			FileUtils.delete(file);
		}
	}
}
