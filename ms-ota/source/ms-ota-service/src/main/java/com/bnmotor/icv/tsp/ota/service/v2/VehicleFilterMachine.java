package com.bnmotor.icv.tsp.ota.service.v2;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Sets;

import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.google.common.collect.Lists;

/**
 * @ClassName: VehicleFilterMachine.java 
 * @Description: 过滤车辆
 * @author E.YanLonG
 * @since 2020-12-25 18:13:10
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class VehicleFilterMachine {

	List<Condition> conditions = Lists.newArrayList();

	List<FotaVehicleMetaObj> vehicles;

	public VehicleFilterMachine(List<FotaVehicleMetaObj> vehicles, List<String> regions, List<LabelInfo> labels, List<String> vins) {
		this.vehicles = vehicles;
		conditions.add(new LableCondition(labels));
		conditions.add(new RegionCondition(regions));
		conditions.add(new VinCondition(vins));
	}
	
	public VehicleFilterMachine(List<FotaVehicleMetaObj> vehicles, Long treeNodeId) {
		this.vehicles = vehicles;
		conditions.add(new TreeNodeCondition(treeNodeId));
	}

	public List<FotaVehicleMetaObj> filter() {
		List<FotaVehicleMetaObj> target = vehicles;
		for (Condition cond : conditions) {
			target = cond.filter(target);
		}
		return target;
	}
}

interface Condition {

	List<FotaVehicleMetaObj> filter(List<FotaVehicleMetaObj> vehicles);
}

class LableCondition implements Condition {

	List<LabelInfo> labels;
	
	LableCondition(List<LabelInfo> labels) {
		this.labels = labels;
	}
	
	@Override
	public List<FotaVehicleMetaObj> filter(List<FotaVehicleMetaObj> vehicles) {
		List<String> labels1 = labels.stream().map(LabelInfo::getLabelKey).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(labels1)) {
			return vehicles.stream().filter(vehicle -> {
				List<LabelInfo> label0 = vehicle.getLabels();
				return match(labels, label0);
			}).collect(Collectors.toList());
			
		}
		return vehicles;
		
	}
	public boolean match(List<LabelInfo> labels0, List<LabelInfo> labels1) {
		Set<String> t1 = padding(labels0);
		Set<String> t2 = padding(labels1);
		return t1.stream().filter(t -> t2.contains(t)).count() > 0; 
	}
	
	public Set<String> padding(List<LabelInfo> its) {
		Set<String> ttt = Sets.newHashSet();
		its.forEach(it -> {
			StringBuilder builder = new StringBuilder();
//			builder.append(it.getLabelGroupId()); // 一方可能为空
			builder.append(it.getLabelKey());
			ttt.add(builder.toString());
		});
		return ttt;
	}

}

class VinCondition implements Condition {

	List<String> vins;

	VinCondition(List<String> vins) {
		this.vins = vins;
	}

	@Override
	public List<FotaVehicleMetaObj> filter(List<FotaVehicleMetaObj> vehicles) {
		if (CollectionUtils.isNotEmpty(vins)) {
			return vehicles.stream().filter(it -> vins.contains(it.getVin())).collect(Collectors.toList());
		}

		return vehicles;
	}

}

class RegionCondition implements Condition {

	List<String> regions;

	RegionCondition(List<String> regions) {
		this.regions = regions;
	}

	@Override
	public List<FotaVehicleMetaObj> filter(List<FotaVehicleMetaObj> vehicles) {
		if (CollectionUtils.isNotEmpty(regions)) {
			return vehicles.stream().filter(it -> regions.contains(it.getRegionCode())).collect(Collectors.toList());
		}

		return vehicles;
	}

}

class  TreeNodeCondition implements Condition {

	Long treeNodeId;

	TreeNodeCondition(Long treeNodeId) {
		this.treeNodeId = treeNodeId;
	}

	@Override
	public List<FotaVehicleMetaObj> filter(List<FotaVehicleMetaObj> vehicles) {
		if (Objects.nonNull(treeNodeId)) {
			return vehicles.stream().filter(it -> treeNodeId.equals(it.getTreeNodeId())).collect(Collectors.toList());
		}

		return vehicles;
	}

}