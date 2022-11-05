package com.bnmotor.icv.tsp.ble.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.ble.service.feign.fallback.PushFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "ms-push",fallback = PushFeignFallbackFactory.class)
@RequestMapping("/v1/jpush")
public interface PushFeignService {
     /**
     * 通過極光推送接口發送消息
     * @param jpushMsgDto
     * @return
     */
    @PostMapping("/message/send")
    RestResponse sendMessage(@RequestBody JpushMsgDto jpushMsgDto);

    @PostMapping("/notification/send")
    RestResponse sendNotification(@RequestBody JpushMsgDto jpushMsgDto);
}
