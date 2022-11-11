package com.bnmotor.icv.tsp.apigateway.servlet.filter;

import com.bnmotor.icv.tsp.apigateway.common.Constant;
import com.bnmotor.icv.tsp.apigateway.common.enums.FilterOrderEnum;
import com.bnmotor.icv.tsp.apigateway.config.ApiGatewayProperties;
import com.bnmotor.icv.tsp.apigateway.servlet.filter.support.FilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: 获取用户信息
 * @Description: 全局过滤器
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class AddCommonInfoGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    ApiGatewayProperties apiGatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            exchange = FilterUtil.addHeaderData(exchange, Constant.PROJECT_ID, apiGatewayProperties.getProjectId());
            return chain.filter(exchange);
        } catch (Exception e) {
            return Mono.error(new Exception(e));
        }
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.ADD_COMMON_INFO.getValue();
    }
}
