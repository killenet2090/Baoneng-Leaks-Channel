package com.bnmotor.icv.tsp.apigateway.exception.handle;

import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.tsp.apigateway.common.RestResponse;
import com.bnmotor.icv.tsp.apigateway.common.RespCode;
import com.bnmotor.icv.tsp.apigateway.model.entity.LogInfoPo;
import com.bnmotor.icv.tsp.apigateway.exception.AdamException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;


/**
 * @ClassName: JsonExceptionHandler
 * @Description: json异常统一处理
 * @author: huangyun1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class JsonExceptionHandler implements ErrorWebExceptionHandler {

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 存储处理异常后的信息
     */
    private static ThreadLocal<RestResponse> exceptionHandlerResult = new ThreadLocal<>();
    /**
     * 存储处理异常后的状态码
     */
    private static ThreadLocal<Integer> exceptionStatusCode = new ThreadLocal<>();

    /**
     * 参考AbstractErrorWebExceptionHandler
     *
     * @param messageReaders
     */
    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     *
     * @param viewResolvers
     */
    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     *
     * @param messageWriters
     */
    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    /**
     * 按照异常类型进行处理
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        //返回实体
        RestResponse resultBody = null;
        //http响应状态码
        Integer httpStatus = null;
        //是否需要记录日志标志
        boolean isNeedWriteLog = false;

        if (ex instanceof AdamException) {
            AdamException se = (AdamException) ex;
            resultBody = new RestResponse(null, se.getMsg(), se.getCode());
            httpStatus = HttpStatus.OK.value();
        } else if(ex instanceof ResponseStatusException) {
            isNeedWriteLog = true;
            ResponseStatusException se = (ResponseStatusException) ex;
            httpStatus = se.getStatus().value();
            if(HttpStatus.NOT_FOUND.value() != httpStatus) {
                log.error("发生未知异常[{}]", ex);
            }
            resultBody = new RestResponse(null, ex.getMessage(), RespCode.UNKNOWN_ERROR.getCode());
        } else if(ex instanceof FeignException) {
            isNeedWriteLog = true;
            log.error("调用feign接口发生异常[{}]", ex);
            resultBody = new RestResponse(null, ex.getMessage(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getCode());
            FeignException se = (FeignException) ex;
            httpStatus = se.status();
        } else {
            isNeedWriteLog = true;
            log.error("发生未知异常", ex);
            resultBody = new RestResponse(null, ex.getMessage(), RespCode.UNKNOWN_ERROR.getCode());
        }
        //记录日志
        if(isNeedWriteLog) {
            LogInfoPo logInfo = LogInfoPo.setLogInfo(exchange);
            logInfo.recorderRequestBody(exchange.getRequest()).then().subscribe();
            try {
                logInfo.setResult(JsonUtil.toJson(resultBody));
            } catch (Exception e) {
                log.error("json转换异常信息发生异常", ex);
            }
            //后续改为线程池
            new Thread(()->{logInfo.writeLog();}).start();
        }
        //设置状态码
        if(httpStatus != null) {
            exceptionStatusCode.set(httpStatus);
        }

        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        exceptionHandlerResult.set(resultBody);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> {
                    return write(exchange, response, ex);
                });
    }

    /**
     * 参考DefaultErrorWebExceptionHandler
     *
     * @param request
     * @return
     */
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        try {
            RestResponse result = exceptionHandlerResult.get();
            return ServerResponse.status(exceptionStatusCode.get() != null ? exceptionStatusCode.get() : HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(result));
        } finally {
            exceptionHandlerResult.remove();
            exceptionStatusCode.remove();
        }
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     *
     * @param exchange
     * @param response
     * @return
     */
    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response, Throwable ex) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    /**
     * 参考AbstractErrorWebExceptionHandler
     */
    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return JsonExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return JsonExceptionHandler.this.viewResolvers;
        }

    }
}
