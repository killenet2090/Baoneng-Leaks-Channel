package com.bnmotor.icv.tsp.ota;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bnmotor.icv.tsp.ota.service.impl.FotaPlanServiceImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class FotaPlanServiceTests {

	@Autowired
	FotaPlanServiceImpl fotaPlanServiceImpl;

	@Test
	public void test() {
		long firewmareId = 1305681773657075714L;
		long versionId = 1305681944432357377L;
		boolean flag = fotaPlanServiceImpl.existPlanWithFirmware(firewmareId, versionId);
		log.info("flag|{}", flag);
	}

}