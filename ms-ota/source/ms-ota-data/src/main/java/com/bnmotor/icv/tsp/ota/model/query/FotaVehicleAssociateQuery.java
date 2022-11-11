package com.bnmotor.icv.tsp.ota.model.query;

import java.util.List;

import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;
import com.bnmotor.icv.tsp.ota.model.compose.RegionInfo;
import com.bnmotor.icv.tsp.ota.model.compose.VehicleInfo;
import com.bnmotor.icv.tsp.ota.model.req.Page;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaVehicleAssociateQuery.java 
 * @Description: 
 * @author E.YanLonG
 * @since 2021-2-18 10:21:01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
@ApiModel(value = "车辆关联查询对象", description = "车辆关联查询对象")
public class FotaVehicleAssociateQuery extends Page {
	
	Long treeNodeId;
	List<VehicleInfo> vehicles;
	List<RegionInfo> regions;
	List<LabelInfo> Labels;

}