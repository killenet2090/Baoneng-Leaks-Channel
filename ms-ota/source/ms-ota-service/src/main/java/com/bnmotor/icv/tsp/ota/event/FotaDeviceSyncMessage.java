package com.bnmotor.icv.tsp.ota.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: FotaDeviceSyncMessage
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/26 17:28
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@AllArgsConstructor
public class FotaDeviceSyncMessage {
    private Long treeNodeId;
    private Long firmwareId;
    /*private boolean save;*/
    private Integer action;
}

