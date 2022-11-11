package com.bnmotor.icv.tsp.apigateway.servlet.filter;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.apigateway.common.Constant;
import com.bnmotor.icv.tsp.apigateway.common.RespCode;
import com.bnmotor.icv.tsp.apigateway.common.RestResponse;
import com.bnmotor.icv.tsp.apigateway.common.enums.FilterOrderEnum;
import com.bnmotor.icv.tsp.apigateway.config.ApiGatewayProperties;
import com.bnmotor.icv.tsp.apigateway.model.entity.AuthCheckRetPo;
import com.bnmotor.icv.tsp.apigateway.service.AuthFeignService;
import com.bnmotor.icv.tsp.apigateway.servlet.filter.support.FilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @ClassName: 获取用户信息
 * @Description: 全局过滤器
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class GetUserInfoGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    AuthFeignService authFeignService;
    @Autowired
    ApiGatewayProperties apiGatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        try {
            //如果设置了忽略鉴权||路由设置了忽略鉴权验证过滤器||配置始终放行||配置忽略鉴权 则跳过鉴权检测
            if (FilterUtil.isIgnoreAuth(apiGatewayProperties, exchange)) {
                //获取用户基础信息
                ServerHttpRequest request = exchange.getRequest();
                String token = FilterUtil.getAccessTokenFromHeader(request);
                if (!StringUtils.isEmpty(token)) {
                    RestResponse<AuthCheckRetPo> result = authFeignService.check(path, token);
                    if (log.isDebugEnabled()) {
                        log.debug("获取用户基础信息返回[{}]", JsonUtil.toJson(result));
                    }
                    ServerWebExchange build = setUserInfoToHeader(result, exchange);
                    if (build != null) {
                        return chain.filter(build);
                    }
                }
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            return Mono.error(new Exception(e));
        }
    }

    /**
     * 将用户基础信息设置到请求头
     * @param result
     * @param exchange
     * @return
     */
    private ServerWebExchange setUserInfoToHeader(RestResponse<AuthCheckRetPo> result, ServerWebExchange exchange) {
        //处理返回数据为空
        if (result != null) {
            //处理成功返回
            if (RespCode.SUCCESS.getCode().equals(result.getRespCode())) {
                //向headers添加userId参数
                ServerHttpRequest rebuildRequest = exchange.getRequest().mutate().headers(httpHeaders -> {
                    httpHeaders.add(Constant.UID, result.getRespData().getUid());
                    if (!StringUtils.isEmpty(result.getRespData().getDeviceId())) {
                        httpHeaders.add(Constant.DEVICE_ID, result.getRespData().getDeviceId());
                    }
                    if(!StringUtils.isEmpty(result.getRespData().getVin())) {
                        httpHeaders.add(Constant.VIN, result.getRespData().getVin());
                    }
                }).build();
                return exchange.mutate().request(rebuildRequest).build();
            }
        }
        return null;
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.GET_USER_INFO.getValue();
    }
}
