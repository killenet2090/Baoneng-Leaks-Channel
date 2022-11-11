package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ota.model.resp.feign.MessageCommonReqVo;
import com.bnmotor.icv.tsp.ota.model.resp.feign.MessageSendReqVo;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.MessageCenterFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: MessageCenterFeignService
 * @Description: 调用消息中心服务接口
 * @author: xuxiaochang1
 * @date: 2020/9/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-message-center", fallback = MessageCenterFeignFallbackFactory.class)
public interface MessageCenterFeignService {

    @PostMapping("/api/v1/message/singleSend")
    RestResponse<String> singleSend(@RequestParam(name="commonVO") MessageCommonReqVo commonVO, @RequestBody MessageSendReqVo reqVO/*, HttpServletRequest request*/);
}