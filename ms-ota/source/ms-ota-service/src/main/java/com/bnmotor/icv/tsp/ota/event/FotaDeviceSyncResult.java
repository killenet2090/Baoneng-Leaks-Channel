package com.bnmotor.icv.tsp.ota.event;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: FotaDeviceSyncResult
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/26 17:53
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaDeviceSyncResult {
    private FotaDeviceSyncMessage fotaDeviceSyncMessage;
    private Integer total = 0;
    private Integer success  = 0;
    private Integer noNeed  = 0;
    private List<FotaDeviceSyncDetail> errors;

    @Data
    public static class FotaDeviceSyncDetail{
        private Long objectId;
        private String vin;
        private String errorMsg;
    }
}
