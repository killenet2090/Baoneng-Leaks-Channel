package com.bnmotor.icv.tsp.ota.common.route;

import java.util.Objects;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: RouteSelector.java 
 * @Description: 路由选择
 * @author E.YanLonG
 * @since 2020-12-18 16:32:57
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class RouteSelector {

	final RouteMetaObject routeMetaObject = RouteMetaObject.of();
	
	/**
	 * 如果指定了路由，但没有发现路由的key，需要抛出异常
	 * @param message
	 * @param needRoute
	 * @return
	 */
	public Object select(Object message, boolean needRoute) {
		Object value = null;
		try {
			value = routeMetaObject.reverse(message);
			if (Objects.isNull(value) && needRoute ) {
				log.error("指定路由，但没有找到对应的key message|{}", message);
			}
		} catch (Exception e) {
			log.error("route key parsing error|{}", e.getMessage(), e );
		}
		return value;
	}
	
	public void register(Class<?> clazz) {
		routeMetaObject.register(clazz, RouteKey.class);
	}

}