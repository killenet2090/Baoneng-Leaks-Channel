package com.bnmotor.icv.tsp.device.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ReqContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) {
        try {
            String uidStr = req.getHeader(Constant.UID);
            if (StringUtils.isEmpty(uidStr)) {
                uidStr = req.getHeader(Constant.USER_ID);
            }

            if (!StringUtils.isEmpty(uidStr)) {
                ReqContext.setUid(Long.valueOf(uidStr));
            }

            chain.doFilter(req, resp);
        } catch (Exception e) {
            log.error("filter exception -> Message: [{}]", e.getMessage());
        } finally {
            ReqContext.remove();
        }
    }
}
