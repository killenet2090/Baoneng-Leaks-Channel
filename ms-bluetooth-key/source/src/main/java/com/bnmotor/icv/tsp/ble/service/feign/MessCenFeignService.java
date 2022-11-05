package com.bnmotor.icv.tsp.ble.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.feign.MessageCenterDto;
import com.bnmotor.icv.tsp.ble.service.feign.fallback.MessCenFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "ms-message-center",fallback = MessCenFeignFallbackFactory.class/*,configuration = FeignConfig.class*/)

@Service
public interface MessCenFeignService {
    @PostMapping(value = "/api/v1/message/singleSend")
    RestResponse<String> singleSend(@RequestBody MessageCenterDto messageCenterDto);
}
