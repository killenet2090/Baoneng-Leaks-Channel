package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaCommonPayload;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessage;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessageHeader;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.TBoxRespCodeEnum;
import com.bnmotor.icv.tsp.ota.handler.tbox.mapstruct.OtaMessageMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @ClassName: TBoxUtil
 * @Description:    TOBX交互工具类
 * @author: xuxiaochang1
 * @date: 2020/9/17 9:19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
public final class TBoxUtil {
    private TBoxUtil(){}

    /**
     * 构建下行OtaProtocol
     * @param businessTypeEnum
     * @param vin
     * @param deviceType
     * @return
     */
    public static OtaProtocol buildOtaDownOtaProtocol(BusinessTypeEnum businessTypeEnum, String vin, Integer deviceType, Long reqId) {
        //设置消息头
        OtaMessageHeader otaMessageHeader = new OtaMessageHeader();
        otaMessageHeader.setVin(vin);
        otaMessageHeader.setDeviceID(vin);
        otaMessageHeader.setDeviceType(Objects.nonNull(deviceType) ? deviceType : 0);
        //设置消息体
        OtaMessage otaMessage = new OtaMessage();
        otaMessage.setBusinessType(businessTypeEnum.getType());
        otaMessage.setTimestamp(Instant.now().toEpochMilli());
        otaMessage.setBusinessId(0L);

        OtaProtocol otaProtocol = new OtaProtocol();
        otaProtocol.setOtaMessageHeader(otaMessageHeader);

        otaProtocol.setBody(otaMessage);
        
        // 设置公共属性
        OtaCommonPayload otaCommonPayload = new OtaCommonPayload();
        otaCommonPayload.setReqId(reqId);
        otaProtocol.getBody().setOtaCommonPayload(otaCommonPayload);
        
        return otaProtocol;
    }
    
    public static OtaProtocol buildOtaDownOtaProtocol() {
    	
    	OtaProtocol otaProtocol = new OtaProtocol();
        otaProtocol.setOtaMessageHeader(new OtaMessageHeader()); // 设置参数
        otaProtocol.setBody(new OtaMessage()); // 设置参数
        
        // 设置公共属性
        OtaCommonPayload otaCommonPayload = new OtaCommonPayload();
        otaProtocol.getBody().setOtaCommonPayload(otaCommonPayload);
        return otaProtocol;
    }
    
    public static OtaProtocol buildOtaDownOtaProtocol(String vin) {
    	
    	OtaProtocol otaProtocol = buildOtaDownOtaProtocol();
    	otaProtocol.getOtaMessageHeader().setVin(vin);
        return otaProtocol;
    }

    /**
     * 设置公共属性
     * @param otaProtocol
     * @return
     */
    public static OtaCommonPayload buildOtaCommonPayload(OtaProtocol otaProtocol) {
    	
    	OtaMessage otaMessage = otaProtocol.getBody();
    	if (Objects.isNull(otaMessage)) {
    		otaMessage = new OtaMessage<>();
    		otaProtocol.setBody(otaMessage);
    	}
    	
    	// 设置公共属性
    	OtaCommonPayload otaCommonPayload = otaProtocol.getBody().getOtaCommonPayload();
    	if (Objects.isNull(otaCommonPayload)) {
    		otaCommonPayload = new OtaCommonPayload();
    		otaProtocol.getBody().setOtaCommonPayload(otaCommonPayload);
    	}
    	
        return otaCommonPayload;
    }
    
    public static OtaCommonPayload paddingOtaCommonPayload(OtaCommonPayload otaCommonPayload, Long taskId, Long transId) {

    	otaCommonPayload.setTaskId(taskId);
    	otaCommonPayload.setTransId(transId);
    	
        return otaCommonPayload;
    }
    
    public static OtaCommonPayload paddingOtaCommonPayload(OtaCommonPayload otaCommonPayload, Integer sourceType) {

    	otaCommonPayload.setSourceType(sourceType);
        return otaCommonPayload;
    }

    /**
     * 包装TBox下行消息， 需要下发消息到TBox的所有消息通过该方法包装完成
     *
     * 该方法屏蔽了OtaProtocol中header和body的设置及一些基本的常用属性设置细节
     * @param req
     * @param respConsumer
     * @param businessTypeEnum
     * @return
     */
    public static OtaProtocol wrapTBoxUpBusiness(OtaProtocol req, Consumer<OtaProtocol> respConsumer, BusinessTypeEnum businessTypeEnum) {
        OtaProtocol resp = new OtaProtocol();
        //设置bogy
        resp.setBody(new OtaMessage());
        resp.getBody().setOtaCommonPayload(new OtaCommonPayload());
        resp.getBody().setBusinessType(businessTypeEnum.getType());
        resp.getBody().setBusinessId(req.getBody().getBusinessId());
        resp.getBody().setTimestamp(Instant.now().toEpochMilli());
        try {
            //设置header
            resp.setOtaMessageHeader(OtaMessageMapper.INSTANCE.otaMessageHeader2OtaMessageHeader(req.getOtaMessageHeader()));
            respConsumer.accept(resp);
        }catch (Throwable e){
            log.error("处理TBxo上行消息异常.e.getMessage={}", e.getMessage(), e);
            if(Objects.isNull(resp.getBody().getOtaCommonPayload())){
                resp.getBody().setOtaCommonPayload(new OtaCommonPayload());
            }

            //包装异常消息给到TBox
            if(e instanceof TboxAdamException) {
                TboxAdamException tboxAdamException = (TboxAdamException) e;
                resp.getBody().getOtaCommonPayload().setErrorCode(tboxAdamException.getCode());
                resp.getBody().getOtaCommonPayload().setErrorMsg(tboxAdamException.getMessage());
            } /*else if(e instanceof AdamException) {
                *//*AdamException adamException = (AdamException) e;*//*
                resp.getBody().getOtaCommonPayload().setErrorCode(TBoxRespCodeEnum.SYS_ERROR.getCode());
                resp.getBody().getOtaCommonPayload().setErrorMsg(TBoxRespCodeEnum.SYS_ERROR.getDesc());
            }*/else{
                resp.getBody().getOtaCommonPayload().setErrorCode(TBoxRespCodeEnum.SYS_ERROR.getCode());
                resp.getBody().getOtaCommonPayload().setErrorMsg(TBoxRespCodeEnum.SYS_ERROR.getDesc());
            }
        }
        return resp;
    }
}
