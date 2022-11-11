package com.bnmotor.icv.tsp.apigateway.model.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: LogInfoPO
 * @Description: 日志记录实体
 * @author: huangyun1
 * @date: 2020/5/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@Slf4j
public class LogInfoPo {
    /**
     * 请求方法
     */
    String method;
    /**
     * 请求参数
     */
    String params;
    /**
     * 请求url
     */
    String url;
    /**
     * 请求头
     */
    String header;
    /**
     * 请求结果
     */
    String result;
    /**
     * 请求ip
     */
    String ip;
    /**
     * 请求时间戳
     */
    long timestamp = System.currentTimeMillis();

    /**
     * 写入日志
     */
    @Async
    public void writeLog() {
        String lineChar = "\n";
        StringBuffer sb = new StringBuffer().append(lineChar);
        sb.append("[========================================== START ========================================]").append(lineChar);
        sb.append("URL : " + this.getUrl()).append(lineChar);
        sb.append("HTTP_METHOD : " + this.getMethod()).append(lineChar);
        sb.append("HEADER : " + this.getHeader()).append(lineChar);
        sb.append("IP : " + this.getIp()).append(lineChar);
        sb.append("REQUEST ARGS : " + this.getParams()).append(lineChar);
        sb.append(String.format("RESPONSE:%s", this.getResult())).append(lineChar);
        sb.append(String.format("SPEND TIME : %s ms", (System.currentTimeMillis() - this.getTimestamp()))).append(lineChar);
        sb.append("[========================================== END ==========================================]").append(lineChar);
        log.info(sb.toString());
    }

    /**
     * 设置日志参数
     * @param exchange
     * @return
     */
    public static LogInfoPo setLogInfo(ServerWebExchange exchange) {
        LogInfoPo logInfo = new LogInfoPo();
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String methodName = serverHttpRequest.getMethodValue().toUpperCase();
        logInfo.setMethod(methodName);
        logInfo.setHeader(serverHttpRequest.getHeaders().toString());
        logInfo.setUrl(serverHttpRequest.getURI().toString());
        logInfo.setIp(serverHttpRequest.getRemoteAddress().getHostString());
        return logInfo;
    }

    /**
     * 记录请求body
     * @param request
     * @return
     */
    public Mono<Void> recorderRequestBody(ServerHttpRequest request) {
        HttpMethod method = request.getMethod();
        HttpHeaders headers = request.getHeaders();
        Charset bodyCharset = null;

        long length = headers.getContentLength();
        MediaType contentType = headers.getContentType();
        if (length > 0 && contentType != null) {
            bodyCharset = getMediaTypeCharset(contentType);
        }

        if (bodyCharset != null) {
            return doRecordBody(request.getBody(), bodyCharset)
                    .then(Mono.defer(() -> {
                        return Mono.empty();
                    }));
        } else {
            return Mono.empty();
        }
    }

    /**
     * 获取字符集编码
     * @param mediaType
     * @return
     */
    private Charset getMediaTypeCharset(@Nullable MediaType mediaType) {
        if (mediaType != null && mediaType.getCharset() != null) {
            return mediaType.getCharset();
        } else {
            return StandardCharsets.UTF_8;
        }
    }

    /**
     * 实际写入日志参数里
     * @param body
     * @param charset
     * @return
     */
    private Mono<Void> doRecordBody(Flux<DataBuffer> body, Charset charset) {
        return DataBufferUtils.join(body).doOnNext(buffer -> {
            CharBuffer charBuffer = charset.decode(buffer.asByteBuffer());
            this.setParams(charBuffer.toString());
            DataBufferUtils.release(buffer);
        }).then();
    }
}
