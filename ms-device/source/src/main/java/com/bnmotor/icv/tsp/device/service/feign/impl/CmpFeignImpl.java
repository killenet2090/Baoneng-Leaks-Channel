package com.bnmotor.icv.tsp.device.service.feign.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.response.feign.GetICCIDStateVo;
import com.bnmotor.icv.tsp.device.service.feign.CmpFeignService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: PushFeignImpl
 * @Description: 推送接口实现类
 * @author: huangyun1
 * @date: 2020/11/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class CmpFeignImpl {

    @Autowired
    private CmpFeignService cmpFeignService;

    /**
     * 查询认证状态
     * @param vin
     */
    public GetICCIDStateVo getICCIDState(String vin) {
        RestResponse<GetICCIDStateVo> stateResponse = cmpFeignService.getICCIDState(vin);

        if (!RespCode.SUCCESS.getValue().equals(stateResponse.getRespCode())) {
            try {
                log.error("调用cmp服务查询认证状态发生异常[{}]", JsonUtil.toJson(stateResponse));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
            throw new AdamException(stateResponse.getRespCode(), stateResponse.getRespMsg());
        }
        return stateResponse.getRespData();
    }
}
