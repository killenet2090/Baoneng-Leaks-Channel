package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.TemplateMsgDto;
import com.bnmotor.icv.tsp.device.service.feign.fallback.SmsFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: SmsFeignService
 * @Description: 调用短信服务接口
 * @author: huangyun1
 * @date: 2020/5/13
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-sms", fallback = SmsFeignFallbackFactory.class)
@RequestMapping("/v1/sms")
public interface SmsFeignService {
    /**
     * 通过模板发送消息
     */
    @PostMapping("/message/sendByTemplate")
    RestResponse sendByTemplate(@RequestBody TemplateMsgDto templateMsgDto);

}