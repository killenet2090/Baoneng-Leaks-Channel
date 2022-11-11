package com.bnmotor.icv.tsp.ota.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bnmotor.icv.tsp.ota.mapper.FotaMiscVehicleAssociateMapper;
import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.VehicleLabelAreaInfo;
import com.bnmotor.icv.tsp.ota.model.page.PageResult;
import com.bnmotor.icv.tsp.ota.model.page.PageResult.Position;
import com.bnmotor.icv.tsp.ota.model.query.FotaVehicleAssociateQuery;

/**
 * @ClassName: FotaMiscVehicleAssociateDbService.java 
 * @Description: 组合查询
 * @author E.YanLonG
 * @since 2021-2-18 11:01:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Repository
public class IFotaMiscVehicleAssociateDbService {

	@Autowired
	FotaMiscVehicleAssociateMapper fotaMiscVehicleAssociateMapper;
	
	public PageResult<List<VehicleLabelAreaInfo>> page(FotaVehicleAssociateQuery query) {
		
		Integer total = fotaMiscVehicleAssociateMapper.associateCount(query);
		
		Integer current = query.getCurrent();
		Integer pageSize = query.getPageSize();
		PageResult<List<VehicleLabelAreaInfo>> pageResult = new PageResult();
//		pageResult.setIndex(current).setSize(size).setTotal(total);
		pageResult.setCurrent(current);
		pageResult.setPageSize(pageSize);
		
		Position position = pageResult.position(current, pageSize);
		List<VehicleLabelAreaInfo> vehicles = fotaMiscVehicleAssociateMapper.test1Page(query, position);
		pageResult.setTotalItem(total);
		pageResult.setData(vehicles);
		return pageResult;
	}
	
	public Integer totalPages(PageResult<List<VehicleLabelAreaInfo>> pageResult) {
		
		int totalRecords = pageResult.getTotalItem();
		int pagesize = pageResult.getPageSize();
		int pages =  totalRecords / pagesize + (totalRecords % pagesize > 0 ? 1 : 0);
		
		return pages;
	}
	
	public List<LabelInfo> listLabels(List<Long> objectIds) {
		return fotaMiscVehicleAssociateMapper.listLabels(objectIds);
	}
	
}