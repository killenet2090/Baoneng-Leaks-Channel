package com.bnmotor.icv.tsp.apigateway.servlet.filter;

import com.bnmotor.icv.tsp.apigateway.common.enums.FilterOrderEnum;
import com.bnmotor.icv.tsp.apigateway.model.entity.LogInfoPo;
import com.bnmotor.icv.tsp.apigateway.servlet.filter.support.RecorderServerHttpRequestDecorator;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @ClassName: LogRecorderGlobalFilter
 * @Description: 日志记录过滤器
 * @author: huangyun1
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class LogRecorderGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 获取当前环境
     */
    @Value("${spring.profiles.active}")
    private String profiles;
    /**
     * 打印日志环境
     */
    private static final String[] LOG_ENV_LIST = {"dev", "test"};

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //生产环境不打印日志
        //if(!Arrays.asList(LOG_ENV_LIST).contains(profiles)) {
        if(true) {
            return chain.filter(exchange);
        }
        //设置请求参数
        LogInfoPo logInfo = LogInfoPo.setLogInfo(exchange);
        //重写decoratedResponse记录日志
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = serverHttpResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(serverHttpResponse) {
            public StringBuffer sb = new StringBuffer();
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

                    Flux result = fluxBody.buffer().map(dataBuffer -> {
                        DataBuffer join = bufferFactory.join(dataBuffer);
                        byte[] content = new byte[join.readableByteCount()];
                        join.read(content);
                        DataBufferUtils.release(join);
                        String responseData = new String(content, Charsets.UTF_8);
                        sb.append(responseData);
                        return bufferFactory.wrap(content);
                    });
                    return super.writeWith(result);
                }
                return super.writeWith(body);
            }

            @Override
            public Mono<Void> setComplete() {
                logInfo.setResult(sb.toString());
                //使用线程池，后续改进
                new Thread(()->{logInfo.writeLog();}).start();
                return getDelegate().setComplete();
            }
        };

        ServerWebExchange ex = exchange.mutate()
                .request(new RecorderServerHttpRequestDecorator(exchange.getRequest()))
                .response(decoratedResponse)
                .build();
        
        return logInfo.recorderRequestBody(ex.getRequest())
                .then(chain.filter(ex))
                //使用线程池，后续改进
                .then().doAfterTerminate(new Thread(() -> {
                    decoratedResponse.setComplete();
                }));
    }

    /**
     * 优先级设置
     * @return
     */
    @Override
    public int getOrder() {
        return FilterOrderEnum.LOG_RECORD.getValue();
    }

}
