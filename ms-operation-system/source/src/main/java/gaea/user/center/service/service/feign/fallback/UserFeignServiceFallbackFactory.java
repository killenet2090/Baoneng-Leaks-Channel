package gaea.user.center.service.service.feign.fallback;

import com.bnmotor.icv.adam.core.exception.AdamException;
import feign.hystrix.FallbackFactory;
import gaea.user.center.service.common.enums.BusinessStatusEnum;
import gaea.user.center.service.model.dto.TemplateSmsDto;
import gaea.user.center.service.service.feign.PhoneFeignService;
import gaea.user.center.service.service.feign.UserFeignService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: UserFeignService
 * @Description: 用户车辆集RPC调用接口
 * @author: jiangchangyuan1
 * @date: 2020/10/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class UserFeignServiceFallbackFactory implements FallbackFactory<UserFeignService> {

    @Override
    public UserFeignService create(Throwable throwable) {
        return new UserFeignService() {
            @Override
            public String producerMessageToKafka(Long uid, List<Long> configIds, List<Long> tagIds) throws AdamException {
                throw new AdamException(BusinessStatusEnum.OTHER_SERVICE_INVOKE_ERROR.getCode(),BusinessStatusEnum.OTHER_SERVICE_INVOKE_ERROR.getDescription());
            }
        };
    }
}
