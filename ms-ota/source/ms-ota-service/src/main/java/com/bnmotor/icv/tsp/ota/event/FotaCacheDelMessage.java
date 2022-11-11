package com.bnmotor.icv.tsp.ota.event;

import com.bnmotor.icv.tsp.ota.common.enums.OtaCacheTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: FotaCacheDelMessage
 * @Description:
 * @author: xuxiaochang1
 * @date: 2021/2/24 11:39
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@AllArgsConstructor
public class FotaCacheDelMessage {
    Object object;
    OtaCacheTypeEnum otaCacheTypeEnum;
}
