package com.bnmotor.icv.tsp.sms.service.strategy;

import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.model.SMSPayload;
import cn.jsms.api.template.TempSMSResult;
import com.alibaba.fastjson.JSON;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.sms.common.Constant;
import com.bnmotor.icv.tsp.sms.common.enums.*;
import com.bnmotor.icv.tsp.sms.config.ApplicationPropertiesConfig;
import com.bnmotor.icv.tsp.sms.config.convert.JsmsClientForMap;
import com.bnmotor.icv.tsp.sms.config.convert.TemplateProperties;
import com.bnmotor.icv.tsp.sms.mapper.SmsInfoDetailMapper;
import com.bnmotor.icv.tsp.sms.mapper.SmsInfoMapper;
import com.bnmotor.icv.tsp.sms.mapper.SmsRecordMapper;
import com.bnmotor.icv.tsp.sms.model.entity.SmsInfoDetailPo;
import com.bnmotor.icv.tsp.sms.model.entity.SmsInfoPo;
import com.bnmotor.icv.tsp.sms.model.entity.SmsRecordPo;
import com.bnmotor.icv.tsp.sms.model.request.TemplateSmsDto;
import com.bnmotor.icv.tsp.sms.service.AbstractSendSmsStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @ClassName: JsmsMsgStrategyImpl
 * @Description: 极光发送短信
 * @author: huangyun1
 * @date: 2020/5/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
@RefreshScope
public class JsmsStrategyImpl extends AbstractSendSmsStrategy<SendSMSResult, TemplateSmsDto> {
    @Autowired
    private JsmsClientForMap<Integer> jsmsClientForMap;
    @Resource
    private SmsRecordMapper smsRecordMapper;
    @Resource
    private SmsInfoMapper smsInfoMapper;
    @Resource
    private SmsInfoDetailMapper smsInfoDetailMapper;
    @Autowired
    private ApplicationPropertiesConfig applicationPropertiesConfig;
    @Autowired
    TransactionTemplate transactionTemplate;

    @Value("${tsp.sms.enabled:true}")
    private boolean enabledSms;


    /**
     * 记录发送信息
     * @param parameter
     * @return
     * @throws Exception
     */
    @Override
    protected SmsInfoPo saveSmsInfo(TemplateSmsDto parameter) {
        TemplateProperties templateProperties = applicationPropertiesConfig.getTemplateSettingMap()
                .get(applicationPropertiesConfig.getMappingTemplateIdPrefix() + parameter.getMappingTemplateId());

        if(templateProperties == null) {
            throw new AdamException(BusinessRetEnum.TEMPLATE_ID_ERROR);
        }

        //获取模板内容
        TempSMSResult tempSmsResult = null;
        if(enabledSms) {
            try {
                tempSmsResult = jsmsClientForMap.get(templateProperties.getUseSendType()).checkTemplate(templateProperties.getTemplateId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new AdamException(RespCode.UNKNOWN_ERROR);
            }
            if(!TemplateStatusEnum.PASS.getValue().equals(tempSmsResult.getStatus())) {
                throw new AdamException(BusinessRetEnum.TEMPLATE_ID_NO_PASS);
            }
        }
        final String msgContent = tempSmsResult != null ? tempSmsResult.getTemplate() : new String();
        SmsInfoPo msgInfo = appendJsmsRecord(parameter);

        transactionTemplate.execute(transactionStatus -> {
            try {
                //记录消息
                msgInfo.setCreateBy(Constant.DEFAULT_USER);
                smsInfoMapper.insert(msgInfo);
                //记录消息明细
                SmsInfoDetailPo detailPo = new SmsInfoDetailPo();
                detailPo.setExtraData(JSON.toJSON(parameter.getExtraData()).toString());
                detailPo.setSmsContent(msgContent);
                detailPo.setSmsInfoId(msgInfo.getId());
                detailPo.setCreateBy(msgInfo.getCreateBy());
                smsInfoDetailMapper.insert(detailPo);
                //记录发送日志
                SmsRecordPo smsRecordPo = new SmsRecordPo();
                smsRecordPo.setSmsInfoId(msgInfo.getId());
                smsRecordPo.setSendStatus(msgInfo.getSendStatus());
                smsRecordPo.setSendTime(msgInfo.getSendTime());
                smsRecordPo.setCreateBy(msgInfo.getCreateBy());
                smsRecordPo.setTemplateId(String.valueOf(templateProperties.getTemplateId()));
                smsRecordPo.setSendChannel(msgInfo.getSendChannel());
                smsRecordMapper.insert(smsRecordPo);
                msgInfo.setSmsRecordPo(smsRecordPo);
            } catch (Exception e) {
                transactionStatus.setRollbackOnly();
                log.error(e.getMessage(), e);
                throw new AdamException(RespCode.UNKNOWN_ERROR);
            }
            return true;
        });
        return msgInfo;
    }

    /**
     * 调用发送并记录结果
     * @param parameter
     * @return
     */
    @Override
    protected SendSMSResult callSendAndRecordResult(TemplateSmsDto parameter, SmsInfoPo smsInfoPo) {
        try {
            TemplateProperties templateProperties = applicationPropertiesConfig.getTemplateSettingMap()
                    .get(applicationPropertiesConfig.getMappingTemplateIdPrefix() + parameter.getMappingTemplateId());
            //发送
            SMSPayload payload = SMSPayload.newBuilder()
                    .setMobileNumber(parameter.getSendPhone())
                    .setTempId(templateProperties.getTemplateId())
                    .setTempPara(parameter.getExtraData())
                    .build();
            SendSMSResult sendRet = null;
            try {
                sendRet = jsmsClientForMap.get(templateProperties.getUseSendType()).sendTemplateSMS(payload);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            //更新发送状态
            if (sendRet != null && sendRet.getResponseCode() == HttpStatus.OK.value()) {
                smsInfoPo.getSmsRecordPo().setStatusCode(HttpStatus.OK.value());
                smsInfoPo.getSmsRecordPo().setSendStatus(SendStatusEnum.SUCESS.getValue());
                smsInfoPo.setSendStatus(SendStatusEnum.SUCESS.getValue());
            } else {
                //记录错误原因
                smsInfoPo.getSmsRecordPo().setStatusCode(sendRet != null ? sendRet.getResponseCode() : HttpStatus.INTERNAL_SERVER_ERROR.value());
                smsInfoPo.getSmsRecordPo().setSendStatus(SendStatusEnum.FAIL.getValue());
                smsInfoPo.setSendStatus(SendStatusEnum.FAIL.getValue());
            }
            smsInfoPo.setFinishTime(LocalDateTime.now());
            smsInfoPo.setUpdateBy(Constant.DEFAULT_USER);
            smsInfoPo.getSmsRecordPo().setFinishTime(smsInfoPo.getFinishTime());
            smsInfoPo.getSmsRecordPo().setUpdateBy(Constant.DEFAULT_USER);
            smsInfoPo.getSmsRecordPo().setCallbackMsgId(sendRet != null ? sendRet.getMessageId() : null);
            smsInfoMapper.updateById(smsInfoPo);
            smsRecordMapper.updateById(smsInfoPo.getSmsRecordPo());
            return sendRet;
        } catch (AdamException e) {
            throw e;
        } catch (Exception e) {
            log.error("发送短信发生异常", e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 拼装记录日志信息
     * @param parameter
     * @return
     */
    private SmsInfoPo appendJsmsRecord(TemplateSmsDto parameter) {
        SmsInfoPo msgInfo = new SmsInfoPo();
        msgInfo.setSendChannel(ChannelEnum.JSMS.getValue());
        msgInfo.setBusinessId(parameter.getBusinessId());
        msgInfo.setFromType(parameter.getFromType() != null ? parameter.getFromType() : FromTypeEnum.APP.getValue());
        msgInfo.setSendStatus(SendStatusEnum.SENDING.getValue());
        msgInfo.setSendTime(LocalDateTime.now());
        msgInfo.setSendPhone(parameter.getSendPhone());
        msgInfo.setSendChannel(parameter.getSendChannel());
        return msgInfo;
    }
}
