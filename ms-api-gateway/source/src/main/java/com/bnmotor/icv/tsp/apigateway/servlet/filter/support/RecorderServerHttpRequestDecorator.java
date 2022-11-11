package com.bnmotor.icv.tsp.apigateway.servlet.filter.support;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: RecorderServerHttpRequestDecorator
 * @Description: 请求装饰者
 * @author: huangyun1
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class RecorderServerHttpRequestDecorator extends ServerHttpRequestDecorator {
    private final List<DataBuffer> dataBuffers = new LinkedList<>();
    private boolean bufferCached = false;
    private Mono<Void> progress = null;

    public RecorderServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @Override
    public Flux<DataBuffer> getBody() {
        synchronized (dataBuffers) {
            if (bufferCached) {
                return copy();
            }
            if (progress == null) {
                progress = cache();
            }

            return progress.thenMany(Flux.defer(this::copy));
        }
    }

    private Flux<DataBuffer> copy() {
        return Flux.fromIterable(dataBuffers)
                .map(buf -> buf.factory().wrap(buf.asByteBuffer()));
    }

    private Mono<Void> cache() {
        return super.getBody()
                .map(dataBuffers::add)
                .then(Mono.defer(()-> {
                    bufferCached = true;
                    progress = null;

                    return Mono.empty();
                }));
    }
}
