package com.bnmotor.icv.tsp.device.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.tag.TagUsedCountDto;
import com.bnmotor.icv.tsp.device.model.response.tag.TagVo;
import com.bnmotor.icv.tsp.device.service.feign.TagFeighService;
import feign.hystrix.FallbackFactory;

import java.util.List;

/**
 * @ClassName: OperationTagsServiceFallbackFactory
 * @Description: 标签服务调用
 * @author: zhangwei2
 * @date: 2020/9/15
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class TagFeighFallbackFactory implements FallbackFactory<TagFeighService> {

    @Override
    public TagFeighService create(Throwable throwable) {
        return new TagFeighService() {
            @Override
            public RestResponse<List<TagVo>> listTags(Long businessId) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }

            @Override
            public RestResponse<Void> countTag(TagUsedCountDto countDto) {
                return new RestResponse(null, RespCode.OTHER_SERVICE_INVOKE_ERROR.getDescription(), RespCode.OTHER_SERVICE_INVOKE_ERROR.getValue());
            }
        };
    }
}
