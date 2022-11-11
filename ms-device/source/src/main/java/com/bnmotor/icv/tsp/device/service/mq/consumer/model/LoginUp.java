package com.bnmotor.icv.tsp.device.service.mq.consumer.model;

import lombok.Data;

/**
 * @ClassName: LoginUp
 * @Description: 车机登录上行数据
 * @author: zhangwei2
 * @date: 2020/11/26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class LoginUp {
    /**
     * 车架号
     */
    private String vin;
    /**
     * 登录状态 1-登录;2-登出
     */
    private Integer loginStatus;
}
