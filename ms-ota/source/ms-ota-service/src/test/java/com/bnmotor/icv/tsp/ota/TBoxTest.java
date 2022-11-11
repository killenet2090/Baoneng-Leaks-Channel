package com.bnmotor.icv.tsp.ota;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bnmotor.icv.adam.sdk.ota.domain.BusinessTypeEnum;
import com.bnmotor.icv.adam.sdk.ota.domain.EcuModule;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessage;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaMessageHeader;
import com.bnmotor.icv.adam.sdk.ota.domain.OtaProtocol;
import com.bnmotor.icv.adam.sdk.ota.up.OtaUpVersionCheck;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxDownHandler;
import com.bnmotor.icv.tsp.ota.handler.tbox.TBoxUpHandler;
import com.google.common.collect.Lists;


@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TBoxTest
{
    @Autowired
    private TBoxUpHandler tBoxUpHandler;

    @Autowired
    private TBoxDownHandler tBoxDownHandler;

    /*@Test
    public void query(){
        List<DeviceTreeNodePo> deviceTreeNodePoList = deviceTreeNodeService.listChildren(null);
        if(!CollectionUtils.isEmpty(deviceTreeNodePoList)){
            deviceTreeNodePoList.forEach(System.out::println);
        }
    }*/

    @Test
    public void test01() {
      
    	Long planId = 1316650926563708929L;
//    	List<UpgradeCondition> upgradeConditions = fotaVersionCheckServiceImpl.getUpgradeConditions(planId);
//    	upgradeConditions.forEach(item -> {
//    		System.err.println(item);
//    	});
    }
    
    public void send(){
        OtaProtocol otaProtocal = new OtaProtocol();
        OtaMessageHeader otaMessageHeader = new OtaMessageHeader();
        otaMessageHeader.setDeviceID("bqNo000000");
        otaMessageHeader.setMsgBusinessType(BusinessTypeEnum.VERSION_CHECK_REQ.getType());
        /*otaMessageHeader.set*/
        otaProtocal.setOtaMessageHeader(otaMessageHeader);
        OtaMessage otaMessage = new OtaMessage();
        otaMessage.setTimestamp(System.currentTimeMillis());
        otaMessage.setBusinessId(IdWorker.getId());
        otaMessage.setBusinessType(BusinessTypeEnum.VERSION_CHECK_REQ.getType());

        OtaUpVersionCheck otaUpVersionCheck = new OtaUpVersionCheck();
        List<EcuModule> ecuModules = Lists.newArrayList();
        /**
         * {
         * 	"body": {
         * 		"ecuModules": [
         *                        {
         * 				"backupFirmwareVersion": "V1.0",
         * 				"bootVersion": "V1.0",
         * 				"ecuId": "V1.0",
         * 				"ecuSeq": "V1.0",
         * 				"ecuSystemSupplier": "V1.0",
         * 				"firmwareCode": "xxc_btc_001",
         * 				"firmwareVersion": "v1.1",
         * 				"hardwareVersion": "V1.0",
         * 				"projectId": 1,
         * 				"sysName": "V1.0"
         *            },
         *                         {
         * 				"backupFirmwareVersion": "V1.0",
         * 				"bootVersion": "V1.0",
         * 				"ecuId": "V1.0",
         * 				"ecuSeq": "V1.0",
         * 				"ecuSystemSupplier": "V1.0",
         * 				"firmwareCode": "xxc_btc_002",
         * 				"firmwareVersion": "v1.1",
         * 				"hardwareVersion": "V1.0",
         * 				"projectId": 1,
         * 				"sysName": "V1.0"
         *            }
         *
         * 		],
         * 		"transId": 10086
         * 	},
         * 	"businessId": 1,
         * 	"reqType": 1,
         * 	"sysId": "",
         * 	"sysTimestamp": 0,
         * 	"timestamp": 123456789,
         * 	"vin": "bqNo000000"
         * }
         */

        EcuModule ecuModule1 = new EcuModule();

        ecuModule1.setFirmwareCode("xxc_btc_001");
        ecuModule1.setFirmwareVersion("v1.1");
        ecuModules.add(ecuModule1);
        otaUpVersionCheck.setEcuModules(ecuModules);
        otaMessage.setOtaUpVersionCheck(otaUpVersionCheck);
        otaProtocal.setBody(otaMessage);

        /*IntStream.rangeClosed(0, 200).forEach(item ->{
            tBoxDownHandler.send(otaProtocal, otaMessage);
            try {
                TimeUnit.SECONDS.sleep(3L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
    }
}


