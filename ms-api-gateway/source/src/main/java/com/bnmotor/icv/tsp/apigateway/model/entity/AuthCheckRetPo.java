package com.bnmotor.icv.tsp.apigateway.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: AuthCheckRetPO
 * @Description: 鉴权校验结果返回实体类
 * @author: shuqi1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class AuthCheckRetPo implements Serializable {

    /**
     * 用户id
     */
    private String uid;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * vin码
     */
    private String vin;

    /**
     * 登录方式：1 app登录；2 车机登录
     */
    private Integer loginType;

}
