package com.bnmotor.icv.tsp.apigateway.servlet.filter;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.apigateway.common.Constant;
import com.bnmotor.icv.tsp.apigateway.common.RespCode;
import com.bnmotor.icv.tsp.apigateway.common.RestResponse;
import com.bnmotor.icv.tsp.apigateway.common.enums.BusinessResultEnum;
import com.bnmotor.icv.tsp.apigateway.common.enums.FilterOrderEnum;
import com.bnmotor.icv.tsp.apigateway.config.ApiGatewayProperties;
import com.bnmotor.icv.tsp.apigateway.exception.AdamException;
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
 * @ClassName: TokenAuthenticationGlobalFilter
 * @Description: 全局鉴权过滤器
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class TokenAuthenticationGlobalFilter implements GlobalFilter, Ordered {
    @Resource
    AuthFeignService authFeignService;
    @Autowired
    ApiGatewayProperties apiGatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        try {
            //如果设置了忽略鉴权||路由设置了鉴权验证过滤器||配置始终放行||配置忽略鉴权 则跳过鉴权检测
            if (FilterUtil.isIgnoreAuth(apiGatewayProperties, exchange)) {
                return chain.filter(exchange);
            }
            //鉴权校验
            ServerHttpRequest request = exchange.getRequest();
            String token = FilterUtil.getAccessTokenFromHeader(request);
            if (StringUtils.isEmpty(token)) {
                return Mono.error(new AdamException(BusinessResultEnum.AUTHORIZED_EMPTY));
            }

            RestResponse<AuthCheckRetPo> result = authFeignService.check(path, token);
            if (log.isDebugEnabled()) {
                log.debug("鉴权返回信息[{}]", JsonUtil.toJson(result));
            }
            //处理返回数据为空
            if (result == null) {
                return Mono.error(new AdamException(RespCode.SERVER_DATA_ERROR.getValue(), RespCode.SERVER_DATA_ERROR.getCode()));
            }
            //处理成功返回
            if (RespCode.SUCCESS.getCode().equals(result.getRespCode())) {
                //向headers添加userId参数
                ServerHttpRequest rebuildRequest = exchange.getRequest().mutate().headers(httpHeaders->{
                    httpHeaders.add(Constant.UID, result.getRespData().getUid());
                    if(!StringUtils.isEmpty(result.getRespData().getDeviceId())) {
                        httpHeaders.add(Constant.DEVICE_ID, result.getRespData().getDeviceId());
                    }
                    if(!StringUtils.isEmpty(result.getRespData().getVin())) {
                        httpHeaders.add(Constant.VIN, result.getRespData().getVin());
                    }
                }).build();
                ServerWebExchange build = exchange.mutate().request(rebuildRequest).build();
                return chain.filter(build);
            }
            //拒绝访问
            return Mono.error(new AdamException(result.getRespCode(), result.getRespMsg()));
        } catch (Exception e) {
            return Mono.error(new Exception(e));
        }
    }

    @Override
    public int getOrder() {
        return FilterOrderEnum.TOKEN_AUTHENTICATION.getValue();
    }
}
