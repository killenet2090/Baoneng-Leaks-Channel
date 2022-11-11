package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.tag.TagUsedCountDto;
import com.bnmotor.icv.tsp.device.model.response.tag.TagVo;
import com.bnmotor.icv.tsp.device.service.feign.fallback.TagFeighFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: IUserAuthFeighService
 * @Description: 基于feign调用鉴权服务，用于实现注册后自动登录，验证码发送和验证等操作
 * @author: zhangwei2
 * @date: 2020/5/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-operation-tags", fallback = TagFeighFallbackFactory.class)
public interface TagFeighService {
    @GetMapping(value = "/inner/tag/list")
    RestResponse<List<TagVo>> listTags(@RequestParam("businessId") Long businessId);

    @PostMapping(value = "/inner/tag/count")
    RestResponse<Void> countTag(TagUsedCountDto countDto);
}
