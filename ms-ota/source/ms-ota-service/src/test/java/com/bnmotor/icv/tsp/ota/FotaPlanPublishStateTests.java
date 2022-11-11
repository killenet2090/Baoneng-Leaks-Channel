package com.bnmotor.icv.tsp.ota;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bnmotor.icv.tsp.ota.common.enums.PublishStateEnum;
import com.bnmotor.icv.tsp.ota.model.entity.FotaPlanPo;
import com.bnmotor.icv.tsp.ota.service.IFotaPlanPublishService;
import com.bnmotor.icv.tsp.ota.service.v2.IFotaPlanV2Service;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class FotaPlanPublishStateTests {

	@Autowired
	IFotaPlanPublishService fotaPlanPublishService;
	
	@Autowired
	IFotaPlanV2Service fotaPlanV2Service;
	
	@Test
	public void test() {
		Long planId = 1357578547237076994L;
		FotaPlanPo fotaPlanPo = fotaPlanV2Service.selectFotaPlan(planId);
		fotaPlanPublishService.selectPublishState(fotaPlanPo);
		
		PublishStateEnum publish = PublishStateEnum.select(fotaPlanPo.getPublishState());
		System.err.println(publish);
		
	}
}
