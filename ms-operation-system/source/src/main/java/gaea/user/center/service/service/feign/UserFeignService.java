package gaea.user.center.service.service.feign;

import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.service.feign.fallback.UserFeignServiceFallbackFactory;
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
@FeignClient(value="ms-device",fallback = UserFeignServiceFallbackFactory.class)
public interface UserFeignService {

    @RequestMapping(value = "/inner/vehicle/asynQuery", method = RequestMethod.GET)
    String producerMessageToKafka(@RequestParam("uid")Long uid,
                                         @RequestParam("configIds")List<Long> configIds,
                                         @RequestParam("tagIds")List<Long> tagIds) throws AdamException;
}
