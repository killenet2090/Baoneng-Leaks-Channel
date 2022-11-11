package com.bnmotor.icv.tsp.device.service.feign.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.core.utils.JsonUtil;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.JpushMsgDto;
import com.bnmotor.icv.tsp.device.service.feign.PushFeignService;
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
public class PushFeignImpl {

    @Autowired
    private PushFeignService pushFeignService;

    /**
     * 发送消息
     * @param jpushMsgDto
     */
    public void sendMessage(JpushMsgDto jpushMsgDto) {
        RestResponse pushResponse = pushFeignService.sendMessage(jpushMsgDto);

        if (!RespCode.SUCCESS.getValue().equals(pushResponse.getRespCode())) {
            try {
                log.error("调用极光推送到hu发生异常[{}]", JsonUtil.toJson(pushResponse));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
            throw new AdamException(pushResponse.getRespCode(), pushResponse.getRespMsg());
        }
    }
}
