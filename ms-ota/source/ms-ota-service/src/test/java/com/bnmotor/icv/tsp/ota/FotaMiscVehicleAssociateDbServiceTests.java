package com.bnmotor.icv.tsp.ota;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.bnmotor.icv.tsp.ota.model.compose.VehicleLabelAreaInfo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.query.FotaVehicleAssociateQuery;
import com.bnmotor.icv.tsp.ota.service.IFotaMiscVehicleAssociateDbService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FotaMiscVehicleAssociateDbServiceTests.java
 * @Description: 测试组合查询
 * @author E.YanLonG
 * @since 2021-2-18 11:07:27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class FotaMiscVehicleAssociateDbServiceTests {

	@Autowired
	IFotaMiscVehicleAssociateDbService fotaMiscVehicleAssociateDbService;

	public void before() {
		// profile=sit
		Long treeNodeId = 1356542707964047361L;
		String labelKey = "1357506773592428545";
	}
	@Test
	public void test() throws Exception {
		log.info("CheckVersionTests...");
		Long treeNodeId = 1356542707964047361L;
		List<String> regions0 = Lists.newArrayList();
		List<String> labels0 = Lists.newArrayList("1310495342958923778", "1357506773592428545");
		
		FotaVehicleAssociateQuery fotaVehicleAssociateQuery = FotaVehicleAssociateQuery.of();
		fotaVehicleAssociateQuery.setCurrent(1);
		fotaVehicleAssociateQuery.setPageSize(10);
		fotaVehicleAssociateQuery.setTreeNodeId(treeNodeId);
		
		
		List<RegionInfo> regions = regions0.stream().map(it -> RegionInfo.of().setRegionCode(it)).collect(Collectors.toList());
		List<LabelInfo> labels = labels0.stream().map(it -> LabelInfo.of().setLabelKey(it)).collect(Collectors.toList());
		fotaVehicleAssociateQuery.setRegions(regions);
		fotaVehicleAssociateQuery.setLabels(labels);
		
		PageResult<List<VehicleLabelAreaInfo>> page = fotaMiscVehicleAssociateDbService.page(fotaVehicleAssociateQuery);
		
		List<VehicleLabelAreaInfo> infos = page.getData();
		infos.forEach(it -> {
			log.info("it|{}", it);
		});
	}
	
}