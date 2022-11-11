package com.bnmotor.icv.tsp.ota.controller.approval;

/**
 * @ClassName: BusinessKeyGenerator.java
 * @Description: 工具类 生成businessKey
 * @author E.YanLonG
 * @since 2021-3-22 16:48:01
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class BusinessKeyGenerator {

	static final String KEY_PREFIX = "OTA_";

	public static String businessKey() {
		return String.format("%s%d", KEY_PREFIX, System.currentTimeMillis());
	}
	
}