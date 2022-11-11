package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessage;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessageHeader;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.service.OtaDownCommandService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @ClassName: TBoxDownHandler
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/7/27 11:37
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class TBoxDownHandler {

    @Autowired
    private OtaDownCommandService otaDownCommandService;

    /**
     * 往汇聚平台发送消息
     * @param downOtaProtocol
     * @return
     */
    public boolean send(OtaProtocol downOtaProtocol) {
        try {
            log.info("往汇聚平台发送消息--开始:消息类型={},下行参数={}", downOtaProtocol.getOtaMessageHeader().getMsgBusinessType(), downOtaProtocol.toString());
            otaDownCommandService.senControlCmd(downOtaProtocol);
            log.info("往汇聚平台发送消息--结束");
            return true;
        } catch (JsonProcessingException e) {
            log.info("往汇聚平台发送消息--异常:消息类型={},下行参数={}", downOtaProtocol.getOtaMessageHeader().getMsgBusinessType(), downOtaProtocol.toString());
        }
        return false;
    }

    /**
     * 往汇聚平台发送消息（app客户端/ota服务器端主动发送）
     *
     * @param respOtaMessage
     * @return 是否发送成功
     */
    public boolean send(OtaMessage respOtaMessage, String vin, Integer deviceType) {
        OtaProtocol downOtaProtocol = null;
        try {
            //TODO
            log.info("往汇聚平台发送消息[up parameters]respOtaMessage={}", respOtaMessage.toString());

            OtaMessageHeader otaMessageHeader = new OtaMessageHeader();
            otaMessageHeader.setDeviceID(vin);
            otaMessageHeader.setVin(vin);
            otaMessageHeader.setDeviceType(deviceType);
            //TODO
            downOtaProtocol = new OtaProtocol();
            downOtaProtocol.setOtaMessageHeader(otaMessageHeader);
            downOtaProtocol.setBody(respOtaMessage);
            log.info("往汇聚平台发送消息[down parameters]---start.downOtaProtocol={}", downOtaProtocol.toString());
            otaDownCommandService.senControlCmd(downOtaProtocol);
            log.info("往汇聚平台发送消息[down parameters]---end");
            return true;
        } catch (JsonProcessingException e) {
            log.error("往汇聚平台发送消息[down parameters]downOtaProtocol={}", Objects.toString(downOtaProtocol, ""), e);
        }
        return false;
    }


}
