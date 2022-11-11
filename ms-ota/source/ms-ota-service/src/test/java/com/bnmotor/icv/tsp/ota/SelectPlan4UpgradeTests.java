package com.bnmotor.icv.tsp.ota;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanUpgradeService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: SelectPlan4UpgradeTests.java 
 * @Description: 测试条件查询
 * @author E.YanLonG
 * @since 2021-2-4 10:08:36
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class SelectPlan4UpgradeTests {

	@Autowired
	IFotaPlanUpgradeService fotaPlanUpgradeService;
	
	@Test
	public void test() {
		Long otaObjectId = 1293744703931469826L;
		FotaPlanPo fotaPlanPo = fotaPlanUpgradeService.selectPlan4Upgrade(otaObjectId, Lists.newArrayList());
		log.info("test|{}", fotaPlanPo);
		
	}
}
