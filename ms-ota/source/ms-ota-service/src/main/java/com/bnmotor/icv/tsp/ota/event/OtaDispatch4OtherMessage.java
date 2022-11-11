package com.bnmotor.icv.tsp.ota.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: OtaDispatch4OtherMessage
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/1/13 15:03
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class OtaDispatch4OtherMessage {
    /*{
        "businessId":xxxx,
            "businessType":4,
            "fromType":3,
            "version":1,
            "timestamp":1610095668686,
            "vin":"SVT57214543000020",
            "jsonPayload":"{"columnName":["otaUpgradeFlag"],"columnValue":[1]}"
    }*/

    private Long businessId;
    private Integer businessType = 4;
    private Integer fromType = 3;
    private Integer version = 1;
    private Long timestamp;
    private String vin;
    private String jsonPayload;

    @Data
    @AllArgsConstructor
    private static class JsonPayload{
        private String[] columnName;
        private Object[] columnValue;
    }

    /**
     * 升级开始阶段信息
     * @return
     */
    public static JsonPayload start4UpgradeProcess(){
        return new JsonPayload(DEFAULT_COLUMNNAME_FOR_UPGRADE_PROCESS, DEFAULT_COLUMVALUE_FOR_UPGRADE_PROCESS_START);
    }

    /**
     * 升级结束阶段信息
     * @return
     */
    public static JsonPayload end4UpgradeProcess(){
        return new JsonPayload(DEFAULT_COLUMNNAME_FOR_UPGRADE_PROCESS, DEFAULT_COLUMNNAME_FOR_UPGRADE_PROCESS_2);
    }


    private static String[] DEFAULT_COLUMNNAME_FOR_UPGRADE_PROCESS = {"otaUpgradeFlag"};
    private static Object[] DEFAULT_COLUMVALUE_FOR_UPGRADE_PROCESS_START = {1};
    private static Object[] DEFAULT_COLUMNNAME_FOR_UPGRADE_PROCESS_2 = {2};
}
