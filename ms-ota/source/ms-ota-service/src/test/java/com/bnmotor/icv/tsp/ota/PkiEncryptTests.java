package com.bnmotor.icv.tsp.ota;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bnmotor.icv.tsp.ota.schedule.EncryptFirmwarePackageTasks;
import com.bnmotor.icv.tsp.ota.service.pki.PkiEncryptSignatureService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PkiEncryptTests {

	@Autowired
	PkiEncryptSignatureService pkiEncryptSignatureService;
	
	@Autowired
	EncryptFirmwarePackageTasks encryptFirmwarePackageTasks;
	
	public void test() throws Exception {
		log.info("PkiEncryptTests...");
		String source = "http://minio-dev.dev.int.bnicvc.com:8001/tsp-ota/pkg/976fbf61e4718cacbdcda7e9880912c9";
		String encryptkey = "VB.00.1";
		String objectName = "pkg/976fbf61e4718cacbdcda7e9880912c9";
		
		Long firmwarePkgId = 10L;
		pkiEncryptSignatureService.selectFile4Storage(source, encryptkey, objectName, firmwarePkgId);
		log.info("PkiEncryptTests...");
		
	}
	
	public void test0() {
		String source = "http://10.210.100.17:9000/tsp-ota/pkg/976fbf61e4718cacbdcda7e9880912c9.encrypted?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20201112%2F%2Fs3%2Faws4_request&X-Amz-Date=20201112T082426Z&X-Amz-Expires=432000&X-Amz-SignedHeaders=host&X-Amz-Signature=cb81e322da57387ef94bab14a607c440cce2007cc4958993f7d498459aceca4a";
		String filename = FilenameUtils.getName(source);
		log.info("filename|{}", filename);
		filename = FilenameUtils.getBaseName(source);
		log.info("filename|{}", filename);
	}
	
	@Test
	public void test1() {
//		encryptFirmwarePackageTasks.test();
	}

}