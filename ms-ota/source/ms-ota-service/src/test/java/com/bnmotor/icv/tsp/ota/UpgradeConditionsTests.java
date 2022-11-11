package com.bnmotor.icv.tsp.ota;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bnmotor.icv.tsp.ota.service.IFotaVersionCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试升级条件查询 bugfix: http://zentao.bnicvc.com/bug-view-758.html
 * 
 * @ClassName: UpgradeConditionsTests.java UpgradeConditionsTests
 * @Description:
 * @author E.YanLonG
 * @since 2020-10-16 17:19:14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class UpgradeConditionsTests {

	@Autowired
	IFotaVersionCheckService fotaVersionCheckService;

	@Test
	public void test01() {

		Long planId = 1316650926563708929L;
//		List<UpgradeCondition> upgradeConditions = fotaVersionCheckServiceImpl.getUpgradeConditions(planId);
//		upgradeConditions.forEach(item -> {
//			log.info("itme|{}", item);
//		});
	}

}
