package com.bnmotor.icv.tsp.apigateway.servlet.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: RequestInterceptorComponent
 * @Description: 重写feign请求拦截组件
 * @author: huangyun1
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Setter
public class RequestInterceptorComponent implements RequestInterceptor {
    private HttpHeaders headers;
    
    @Override
    public void apply(RequestTemplate template) {
        if(headers != null) {
            for(Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                if (!template.headers().containsKey(key)) {
                    template.header(key, entry.getValue());
                }
            }
        }
    }
}
