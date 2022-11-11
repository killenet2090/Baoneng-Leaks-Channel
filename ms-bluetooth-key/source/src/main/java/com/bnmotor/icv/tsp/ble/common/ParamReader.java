package com.bnmotor.icv.tsp.ble.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ParamReader {
    @Value("${ble.project-id}")
    public String peojectId;

    @Value("${ble.appliction-name}")
    public String applicationName;


    @Value("${ble.sms-template-id}")
    public String smstempalteId;

    @Value("${ble.redis-ttl}")
    public int expireTtl;

    @Value("${ble.push-del-ttl}")
    public int expirePushDel;

    @Value("${ble.algorithm}")
    public String algorithm;

    @Value("${ble.group-id}")
    public String groupId;

    @Value("${ble.key-id}")
    public String keyId;

}
