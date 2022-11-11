package com.bnmotor.icv.tsp.ota.common.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RouteThreadConfig.java 
 * @Description: 线程组工厂
 * @author E.YanLonG
 * @since 2020-12-18 16:37:32
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@Slf4j
public class RouteThreadConfig {

	@Bean(name = "RouteThreadFactory")
	public RouteThreadFactory test() {
		int threadnum = Runtime.getRuntime().availableProcessors();
		RouteThreadFactory routeThreadFactory = new RouteThreadFactory(threadnum);
		return routeThreadFactory;
	}

}