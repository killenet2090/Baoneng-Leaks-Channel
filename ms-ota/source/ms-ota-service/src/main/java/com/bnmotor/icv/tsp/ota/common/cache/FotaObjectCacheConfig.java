package com.bnmotor.icv.tsp.ota.common.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @ClassName: CacheRedisConfig.java 
 * @Description: 车辆全局缓存配置开关
 * @author E.YanLonG
 * @since 2021-1-22 18:48:43
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@Data
public class FotaObjectCacheConfig {

	@Value("${cache.vehicle.switch:false}")
	boolean fotaObjectCacheConfig;

}