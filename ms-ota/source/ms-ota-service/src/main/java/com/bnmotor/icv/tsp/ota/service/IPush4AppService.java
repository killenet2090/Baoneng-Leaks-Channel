package com.bnmotor.icv.tsp.ota.service;


import com.bnmotor.icv.tsp.ota.common.enums.AppEnums;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeMessageBase;
import com.bnmotor.icv.tsp.ota.event.OtaUpgradeNewVersionMessage;
import org.springframework.http.ResponseEntity;


/**
 * @ClassName: IPush4AppService
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/8/20 21:51
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface IPush4AppService {
    /**
     * 推送消息到App--使用异步线程转发消息
     * @param t
     * @param vin
     * @param <T>
     * @param reqId
     * @return
     */
    <T> ResponseEntity push2App(T t, String vin, Long reqId);

    /**
     * 推送到消息中心
     *
     * @param vin
     * @param messageCenterMsgTypeEnum
     * @param attachInfo
     * @return
     */
    ResponseEntity push2MessageCenter(String vin, AppEnums.MessageCenterMsgTypeEnum messageCenterMsgTypeEnum, Object attachInfo);

    /**
     * 推送到消息中心
     * @param otaUpgradeMessageBase
     * @return
     */
    ResponseEntity push2MessageCenter(OtaUpgradeMessageBase otaUpgradeMessageBase);

    /**
     * 通过Vin码获取推送Id
     * @param vin
     * @return
     */
    String getUserRegistrationId(String vin);

    /**
     * 根据vin码获取用户Id
     * @param vin
     * @return
     */
    Long getUid(String vin);

    /**
     * 推送版本检查结果到APP--存在新版本
     *
     * @param otaUpgradeNewVersionMessage
     */
    void pushNewVersionCheck2App(OtaUpgradeNewVersionMessage otaUpgradeNewVersionMessage);
    //void pushNewVersionCheck2App(OtaProtocol otaProtocol, Long transId, Supplier<FotaObjectPo> fotaObjectDoSupplier, Supplier<FotaPlanPo> fotaPlanPoSupplier, Supplier<FotaVersionCheckPo> fotaVersionCheckPoSupplier, Long reqId, boolean newVersion);

    /**
     * 推送安装请求到APP
     * @param vin
     * @param transId
     * @param taskId
     */
    void pushInstalledVerifyReq2App(String vin, Long transId, Long taskId);
}
