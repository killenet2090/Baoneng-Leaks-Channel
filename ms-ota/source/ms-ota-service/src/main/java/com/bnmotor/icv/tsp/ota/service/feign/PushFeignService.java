package com.bnmotor.icv.tsp.ota.service.feign;

import com.bnmotor.icv.tsp.ota.model.req.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.ota.service.feign.fallback.PushFeignFallbackFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: JpushMsgService
 * @Description:
 * @author: handong1
 * @date: 2020/8/10 18:55
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(value = "ms-push",fallback = PushFeignFallbackFactory.class)
@RequestMapping("/v1/jpush")
public interface PushFeignService {
    @PostMapping("/message/send")
    @ApiOperation(value = "发送消息")
    ResponseEntity sendMessage(@ApiParam(name = "jpushMsgDTO", value = "传入json格式", required = true)
                               @Validated @RequestBody JpushMsgDto jpushMsgDTO);
}
