package com.bnmotor.icv.tsp.ota.model.resp.feign;

import lombok.Data;

/**
 * @ClassName: ActionParams
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/11/5 10:52
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class ActionParams {
    private String vin;

    private String action;
}
