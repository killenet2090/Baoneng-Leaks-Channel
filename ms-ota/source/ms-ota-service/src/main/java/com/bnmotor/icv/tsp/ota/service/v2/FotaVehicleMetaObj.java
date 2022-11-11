package com.bnmotor.icv.tsp.ota.service.v2;

import java.util.List;

import com.bnmotor.icv.tsp.ota.model.compose.LabelInfo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: FotaVehicleMetaObj.java 
 * @Description: 车辆属性
 * @author E.YanLonG
 * @since 2020-12-28 15:00:18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class FotaVehicleMetaObj {
	Long treeNodeId;
	Long objectId;
	String vin;
	
	List<LabelInfo> labels;
	String regionName;
	String regionCode;
}