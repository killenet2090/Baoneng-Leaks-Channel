package gaea.user.center.service.service.feign;

import com.bnmotor.icv.adam.core.exception.AdamException;
import gaea.user.center.service.model.dto.TemplateSmsDto;
import gaea.user.center.service.service.feign.fallback.PhoneFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @ClassName: PhoneFeignService
 * @Description: 手机发送验证码
 * @author: jiangchangyuan1
 * @date: 2020/11/3
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value="ms-sms",fallback = PhoneFeignServiceFallbackFactory.class)
public interface PhoneFeignService {
    @RequestMapping(value = "/v1/sms/message/sendByTemplate", method = RequestMethod.POST)
    String sendPhone(@RequestBody TemplateSmsDto TemplateSmsDto) throws AdamException;
}
