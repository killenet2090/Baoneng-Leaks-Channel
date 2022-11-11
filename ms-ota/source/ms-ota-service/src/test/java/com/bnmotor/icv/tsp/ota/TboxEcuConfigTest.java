package com.bnmotor.icv.tsp.ota;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuConfigVo;
import com.bnmotor.icv.tsp.ota.model.resp.tbox.EcuFirmwareConfigListVo;
import com.bnmotor.icv.tsp.ota.service.IFotaConfigService;

import lombok.extern.slf4j.Slf4j;


/**
 * @ClassName: TboxEcuConfigTest
 * @Description: 查询指定车辆所有可升级ECU固件清单
 * @author eyanlong2
 * @since 2020-9-8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TboxEcuConfigTest {

	@Autowired
	IFotaConfigService fotaConfigService;
	
	@Test
	public void test() {
		log.info("TboxEcuConfigTest...");

		String vincode = "LLNC6ADB5JA047666"; // 车辆vin
		EcuFirmwareConfigListVo ecuFirmwareConfigListVo = fotaConfigService.queryEcuConfigFirmwareDos(vincode);
		Optional.ofNullable(ecuFirmwareConfigListVo).ifPresent(this::consume);
	}
	
	public void consume(EcuFirmwareConfigListVo ecuFirmwareConfigListVo ) {
		ecuFirmwareConfigListVo.getEcus().forEach(this::print);
	}
	
	public void print(EcuConfigVo ecuConfigVo) {
		log.info("result |{}", ecuConfigVo);
	}

}
