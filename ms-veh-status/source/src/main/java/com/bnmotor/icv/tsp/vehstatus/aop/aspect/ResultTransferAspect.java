package com.bnmotor.icv.tsp.vehstatus.aop.aspect;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: ResultTransferAspect
 * @Description: ResultTransfer自定义注解
 * @author: huangyun1
 * @date: 2020/12/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Aspect
@Slf4j
@Component
public class ResultTransferAspect {

    @Autowired
    private List<ColumnHandler> handlerChainList;

    @Pointcut("@annotation(com.bnmotor.icv.tsp.vehstatus.aop.annotation.ResultTransfer)")
    public void annotationPointcut() {

    }

    @AfterReturning(value="annotationPointcut()", returning="result")
    public void afterReturning(JoinPoint joinPoint, Map<String, String> result) {
        if (Objects.nonNull(result)) {
            try {
               handlerChainList.stream().forEach(handler -> {
                    handler.handleVo(result);
                });
            } catch (Exception e) {
                log.error("处理查询结果发生异常[{}]", e.getMessage());
                throw new AdamException(RespCode.UNKNOWN_ERROR);
            }
        }
    }
}
