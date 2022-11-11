package com.bnmotor.icv.tsp.ble.common;

import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: ReqContext
 * @Description: 请求线程上下文，线程安全的
 * @author  shuqi1
 * @date: 2020/10/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class ReqContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) {
        try {
            String uidStr = req.getHeader(Constant.UID);
            if (!StringUtils.isEmpty(uidStr)) {
                ReqContext.setUid(Long.valueOf(uidStr));
            }

            chain.doFilter(req, resp);
        } catch (Exception e) {
            log.error("filter exception -> Message: {}", e.getMessage());
        } finally {
            ReqContext.remove();
        }
    }
}
