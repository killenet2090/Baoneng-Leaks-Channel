package com.bnmotor.icv.tsp.device.common;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangwei2
 * @ClassName: AccessInterceptor
 * @Description: 是否登录态校验
 * @date 2020-07-03
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

            // 方法有Acl时仅检查方法的，方法没有时检查类的
            Acl acl = hm.getMethod().getAnnotation(Acl.class);
            if (acl != null) {
                return verify();
            }

            // 检查类的访问控制
            acl = hm.getBeanType().getAnnotation(Acl.class);
            if (acl != null) {
                return verify();
            }
        }

        return true;
    }

    /**
     * 校验是否登录,登录成功返回true;否则抛出业务异常
     *
     * @return 完成登录校验true
     */
    private boolean verify() {
        if (ReqContext.getUid() != null) {
            return true;
        }

        throw new AdamException(RespCode.USER_UNAUTHORIZED_ACCESS);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
