package gaea.user.center.service.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import feign.hystrix.FallbackFactory;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.model.dto.TemplateSmsDto;
import gaea.user.center.service.service.feign.PhoneFeignService;

import java.util.List;

/**
 * @ClassName: PhoneFeignServiceFallbackFactory
 * @Description: feign调用第三方手机发送短信接口
 * @author: jiangchangyuan1
 * @date: 2020/12/30
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class PhoneFeignServiceFallbackFactory implements FallbackFactory<PhoneFeignService> {
    @Override
    public PhoneFeignService create(Throwable throwable) {
        return new PhoneFeignService() {
            @Override
            public String sendPhone(TemplateSmsDto TemplateSmsDto) throws AdamException {
                throw new AdamException(BusinessStatusEnum.OTHER_SERVICE_INVOKE_ERROR.getCode(),BusinessStatusEnum.OTHER_SERVICE_INVOKE_ERROR.getDescription());
            }
        };
    }
}
