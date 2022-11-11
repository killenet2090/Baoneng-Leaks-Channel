package com.bnmotor.icv.tsp.ota.model.req.v2;

import lombok.Data;

/**
 * @ClassName: ExcelDataVO.java 
 * @Description: Excel上传车辆关联任务
 * @author E.YanLonG
 * @since 2020-12-29 9:41:45
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ExcelDataVo {

	/**
	 * 车辆vin号
	 */
	String vin;
	
	/**
	 * 车辆objectId
	 */
	Long objectId;

}