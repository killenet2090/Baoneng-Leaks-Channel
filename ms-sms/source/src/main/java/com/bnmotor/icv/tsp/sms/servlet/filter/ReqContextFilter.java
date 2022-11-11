package com.bnmotor.icv.tsp.sms.servlet.filter;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.sms.common.Constant;
import com.bnmotor.icv.tsp.sms.common.ReqContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: ReqContextFilter
 * @Description: 请求头过滤器
 * @author: zhangwei2
 * @date: 2020/4/28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Configuration
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
            log.error("filter exception -> Message: [{}]", e.getMessage());
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        } finally {
            ReqContext.remove();
        }
    }
}
