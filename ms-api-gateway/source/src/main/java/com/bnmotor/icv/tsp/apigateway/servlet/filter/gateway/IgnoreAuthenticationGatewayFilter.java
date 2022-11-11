package com.bnmotor.icv.tsp.apigateway.servlet.filter.gateway;

import com.bnmotor.icv.tsp.apigateway.common.Constant;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: IgnoreAuthenticationGlobalFilter
 * @Description: 关闭全局鉴权过滤器
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class IgnoreAuthenticationGatewayFilter extends AbstractGatewayFilterFactory<IgnoreAuthenticationGatewayFilter.Config> {
    public IgnoreAuthenticationGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return this::filter;
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(Constant.IGNORE_AUTH_GLOBAL_FILTER, true);
        return chain.filter(exchange);
    }

    public static class Config {
    }

    @Override
    public String name() {
        return "IgnoreAuthenticationGatewayFilter";
    }
}
