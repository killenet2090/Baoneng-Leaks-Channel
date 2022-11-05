package com.bnmotor.icv.tsp.ble.service.feign;

/**
 * 通过模板发送消息
 * @param templateMsgDto
 * @return
 */

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.TemplateMsgDto;
import com.bnmotor.icv.tsp.ble.service.feign.fallback.SmsFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "ms-sms", fallback = SmsFeignFallbackFactory.class)
@RequestMapping("/v1/sms")
public interface SmsFeignService {
    /**
     * 通过模板发送消息
     * @param templateMsgDto
     * @return
     */
    @PostMapping("/message/sendByTemplate")
    RestResponse sendByTemplate(@RequestBody TemplateMsgDto templateMsgDto);
}
