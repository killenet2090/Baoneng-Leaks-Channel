package com.bnmotor.icv.tsp.ota.handler.tbox;

import com.bnmotor.icv.tsp.ota.common.enums.TBoxRespCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.bnmotor.icv.adam.sdk.ota.domain.OtaCommonPayload;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessage;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessageHeader;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.TboxAdamException;
import com.bnmotor.icv.tsp.ota.common.enums.BusinessTypeEnum;
import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import com.bnmotor.icv.tsp.ota.common.event.OtaEventMonitor;
import com.bnmotor.icv.tsp.ota.service.IFotaConfigService;
import com.bnmotor.icv.tsp.ota.service.IFotaVersionCheckService;
import com.bnmotor.icv.tsp.ota.service.IFotaVersionCheckVerifyService;
import com.bnmotor.icv.tsp.ota.util.MyAssertUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: TBoxUpHandler
 * @Description: 上行逻辑处理类
 * @author: xuxiaochang1
 * @date: 2020/7/27 9:25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@OtaMessageAttribute(topics = { "tsp-ota-up-1", "tsp-ota-up" }, msgtype = OtaProtocol.class)
@Slf4j
public class TBoxUpHandler implements KafkaHandlerManager.KafkaHandler<OtaProtocol> {
	
	@Autowired
	IFotaVersionCheckService fotaVersionCheckService;
	
	@Autowired
	IFotaConfigService fotaConfigService;

    @Autowired
    private IFotaVersionCheckVerifyService fotaVersionCheckVerifyService;
    
    @Autowired
    private TBoxDownHandler tBoxDownHandler;

    @Override
    public void onMessage(OtaProtocol otaProtocol) {
        log.debug("---------handling start---", otaProtocol.toString());
        log.debug("从汇聚平台接受到消息otaMessage={}", otaProtocol.toString());
        handle(otaProtocol);
        log.debug("---------handling end---", otaProtocol.toString());
    }

    /**
     * 处理上行消息实际业务逻辑
     * @param otaProtocol
     */
    private void handle(OtaProtocol otaProtocol) {
        OtaMessageHeader otaMessageHeader = otaProtocol.getOtaMessageHeader();
        MyAssertUtil.notNull(otaMessageHeader, OTARespCodeEnum.UP_TBOX_PRAMAETER_HEADER_ERROR);
        
        OtaMessage otaMessage = otaProtocol.getBody();
        MyAssertUtil.notNull(otaMessage, OTARespCodeEnum.UP_TBOX_PRAMAETER_BODY_ERROR);
        
        Integer businessType = otaMessage.getBusinessType();
        BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.getByType(businessType);
        MyAssertUtil.notNull(businessTypeEnum, OTARespCodeEnum.UP_BUSINESS_TYPE_INVALID);

        OtaCommonPayload otaCommonPayload = otaProtocol.getBody().getOtaCommonPayload();
        MyAssertUtil.notNull(otaCommonPayload, OTARespCodeEnum.UP_TBOX_PRAMAETER_BODY_ERROR);

        log.info("[消息类型]={}", businessTypeEnum.getDesc());

        //获取一个计时器
        StopWatch sw = OtaEventMonitor.stick(businessTypeEnum.getDesc());
        //单独处理任务失效操作
        if(TBoxRespCodeEnum.OTA_RESP_CODE_BUSINESS_ERROR_PLAN_INVALID.getCode().equals(otaCommonPayload.getErrorCode())){
            fotaVersionCheckVerifyService.handle4PlanInvalid(otaProtocol);
        }else{
            if(businessType == BusinessTypeEnum.OTA_UP_CONF_VERSION_FROM_OTA.getType()){
                //获取云端配置列表信息
                OtaProtocol resp = fotaConfigService.queryEcuConfigList(otaProtocol);
                tBoxDownHandler.send(resp);
            }else if(businessType == BusinessTypeEnum.OTA_UP_PLAN_IF_VALID.getType()){
                //判断任务有效性
                OtaProtocol resp = fotaVersionCheckVerifyService.judgeFotaPlanValid(otaProtocol);
                tBoxDownHandler.send(resp);
            }else if(businessType == BusinessTypeEnum.OTA_UP_VERSION_CHECK.getType()){
                OtaProtocol resp;
                try {
                    resp = fotaVersionCheckService.checkVersion(otaProtocol);
                }catch(Throwable e){
                    log.error("版本检查处理异常.msg={}", e.getMessage(), e);
                    resp = fotaVersionCheckService.failCheckVersion(otaProtocol, (TboxAdamException)e);
                }
                tBoxDownHandler.send(resp);
            }else if(businessType == BusinessTypeEnum.OTA_UP_ECU_INFO_UPLOAD.getType()){
                //ECU信息上报到云端
                fotaVersionCheckService.updateEcuConfig(otaProtocol);
            }else if (businessType == BusinessTypeEnum.OTA_UP_DOWNLOAD_VERIFY_REQ.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaCommonPayload(), "[升级确认请求（下载）参数不能为空]");
                fotaVersionCheckVerifyService.downloadVerifyReq(otaProtocol);

            } else if (businessType == BusinessTypeEnum.OTA_UP_DOWNLOAD_VERIFY_RESULT.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaUpUpgradeVerifyResult(), "[升级确认结果（下载）参数不能为空]");
                fotaVersionCheckVerifyService.downloadVerifyResult(otaProtocol);

            } else if (businessType == BusinessTypeEnum.OTA_UP_DOWNLOAD_PROCESS.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaUpDownloadProcess(), "[升级下载进度汇报参数不能为空]");
                fotaVersionCheckVerifyService.downloadProcessReq(otaProtocol);

            } else if (businessType == BusinessTypeEnum.OTA_UP_INSTALLED_VERIFY_REQ.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaCommonPayload(), "[升级安装确认请求参数不能为空]");
                fotaVersionCheckVerifyService.installedVerifyReq(otaProtocol);

            } else if (businessType == BusinessTypeEnum.OTA_UP_INSTALLED_VERIFY_RESULT.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaUpInstallVerifyResult(), "[升级安装确认结果参数不能为空]");
                fotaVersionCheckVerifyService.installedVerifyResult(otaProtocol);

            } else if (businessType == BusinessTypeEnum.OTA_UP_INSTALLED_PROCESS.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaUpInstallProcess(), "[升级安装进度请求参数不能为空]");
                fotaVersionCheckVerifyService.installedProcessReq(otaProtocol);

            } else if (businessType == BusinessTypeEnum.OTA_UP_UPGRADE_RESULT.getType()) {
                MyAssertUtil.notNull(otaProtocol.getBody().getOtaUpUpgradeResult(), "[升级安装结果不能为空]");
                fotaVersionCheckVerifyService.upgradeResultReq(otaProtocol);
            }else{
                log.warn("未知的消息类型.otaProtocol={}", otaProtocol.toString());
            }
        }
        //计算消费时间
        log.info("消息处理耗费时间={}毫秒", sw.getTotalTimeMillis());
    }

}