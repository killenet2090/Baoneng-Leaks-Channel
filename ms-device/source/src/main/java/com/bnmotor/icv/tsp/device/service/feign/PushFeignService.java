package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.device.service.feign.fallback.PushFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @ClassName: PushFeignService
 * @Description: 调用消息推送服务接口
 * @author: zhangwei2
 * @date: 2020/11/17
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-push", fallback = PushFeignFallbackFactory.class)
@RequestMapping("/v1/jpush")
public interface PushFeignService {
    /**
     * 发送消息
     */
    @PostMapping("/message/send")
    RestResponse sendMessage(@RequestBody JpushMsgDto jpushMsgDTO);
}