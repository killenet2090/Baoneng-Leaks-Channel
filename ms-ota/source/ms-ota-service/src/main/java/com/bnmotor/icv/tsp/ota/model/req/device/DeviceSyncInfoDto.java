package com.bnmotor.icv.tsp.ota.model.req.device;

import lombok.Data;

/**
 * @ClassName: DeviceSyncInfoDto
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/19 9:35
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class DeviceSyncInfoDto {
    private Integer type;
    private Integer businessType;
    private Integer action;
    private Object data;
}
