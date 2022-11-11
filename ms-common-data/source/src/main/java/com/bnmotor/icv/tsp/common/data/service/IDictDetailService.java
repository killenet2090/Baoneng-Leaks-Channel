package com.bnmotor.icv.tsp.common.data.service;

import org.springframework.http.ResponseEntity;

/**
 * @author :luoyang
 * @date_time :2020/11/12 16:03
 * @desc:
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface IDictDetailService {
    ResponseEntity getDictValueByIdOrCode(String code);
}
