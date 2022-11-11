package com.bnmotor.icv.tsp.device.service.feign.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.response.feign.GetDeviceInfoVo;
import com.bnmotor.icv.tsp.device.service.feign.UserFeignService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserFeignImpl
 * @Description: 获取用户信息接口实现类
 * @author: wangxuejuan1
 * @date: 2020/10/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
@RefreshScope
public class UserFeignImpl {

    @Autowired
    private UserFeignService userFeignService;


    /**
     * 获取极光推送registrationId
     * @param vin
     */
    public String getUserRidByVin(String vin) {
        RestResponse<GetDeviceInfoVo> response = userFeignService.device(vin);
        if(RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            if(response.getRespData() == null || StringUtils.isBlank(response.getRespData().getPushRid())) {
                return null;
            } else {
                return response.getRespData().getPushRid();
            }
        } else {
            try {
                log.error("调用用户服务获取极光id发生异常[{}]", JsonUtil.toJson(response));
            } catch (JsonProcessingException e) {
                log.error("将结果转为json转换发生异常[{}]", e.getMessage());
            }
            throw new AdamException(response.getRespCode(), response.getRespMsg());
        }
    }

}
