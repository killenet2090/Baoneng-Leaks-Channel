package com.bnmotor.icv.tsp.apigateway.servlet.filter.support;

import com.bnmotor.icv.tsp.apigateway.common.Constant;
import com.bnmotor.icv.tsp.apigateway.config.ApiGatewayProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName: FilterUtil
 * @Description: 过滤器相关util
 * @author: huangyun1
 * @date: 2020/7/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class FilterUtil {
    /**
     * 从请求头获取access_token信息
     * @param request
     * @return
     */
    public static String getAccessTokenFromHeader(ServerHttpRequest request) {
        //取出头信息
        HttpHeaders headers = request.getHeaders();
        List<String> authorizations = headers.get(Constant.AUTHORIZATION);

        if (CollectionUtils.isEmpty(authorizations)) {
            return null;
        }
        //取到accessToken令牌
        String accessToken = authorizations.get(0);
        return accessToken;
    }

    /**
     * 是否忽略鉴权
     * @param apiGatewayProperties
     * @param exchange
     * @return
     */
    public static boolean isIgnoreAuth(ApiGatewayProperties apiGatewayProperties, ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        AtomicBoolean permit = new AtomicBoolean(false);
        AntPathMatcher matcher = new AntPathMatcher();
        //如果设置了忽略鉴权||路由设置了忽略鉴权验证过滤器||配置始终放行||配置忽略鉴权 则跳过鉴权检测
        if (!apiGatewayProperties.getCheckToken() ||
                exchange.getAttribute(Constant.IGNORE_AUTH_GLOBAL_FILTER) != null ||
                apiGatewayProperties.getPermitAll().stream().filter(str -> matcher.match(str, path)).count() > 0 ||
                apiGatewayProperties.getTokenIgnores().stream().filter(str -> matcher.match(str, path)).count() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 添加参数到请求头
     * @param exchange
     * @param key
     * @param value
     * @return
     */
    public static ServerWebExchange addHeaderData(ServerWebExchange exchange, String key, String value) {
        ServerHttpRequest rebuildRequest = exchange.getRequest().mutate().headers(httpHeaders -> {
            if (!StringUtils.isEmpty(value)) {
                httpHeaders.add(key, value);
            }
        }).build();
        return exchange.mutate().request(rebuildRequest).build();
    }
}
