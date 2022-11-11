package com.bnmotor.icv.tsp.device.common;

import com.bnmotor.icv.adam.data.mysql.DataAccessContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 添加用户UID
 */
@Configuration
@Slf4j
public class UidAddInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        DataAccessContext.setUid(ReqContext.getUid());
        return true;
    }
}
