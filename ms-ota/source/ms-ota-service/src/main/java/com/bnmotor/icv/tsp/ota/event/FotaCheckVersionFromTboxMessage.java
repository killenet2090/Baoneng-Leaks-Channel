package com.bnmotor.icv.tsp.ota.event;

import lombok.Data;

/**
 * @ClassName: FotaVersionCheckFromTboxMessage
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/3/26 10:18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class FotaCheckVersionFromTboxMessage extends OtaMessageBase{
    Long otaPlanObjId;
}
