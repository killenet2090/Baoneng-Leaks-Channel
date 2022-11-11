package com.bnmotor.icv.tsp.ota.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName: PackageBuildCompleted.java 
 * @Description: PKI加密消息
 * @author E.YanLonG
 * @since 2021-1-18 10:40:30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class PackageBuildCompleted {
	Long firmwareId;
	Long firmwareVerId;
	Long firmwarePackageId;
	boolean succeed;
	
}