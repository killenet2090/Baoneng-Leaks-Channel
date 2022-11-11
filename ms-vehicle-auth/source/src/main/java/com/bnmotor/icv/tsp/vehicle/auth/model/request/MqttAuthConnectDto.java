package com.bnmotor.icv.tsp.vehicle.auth.model.request;

import lombok.Data;

/**
 * @ClassName: TBoxAuthDto
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author: wuhao1
 * @date: 2020/12/22
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class MqttAuthConnectDto {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 客户端clientId
     */
    private String clientId;

    /**
     * 客户端IP地址
     */
    private String ipAddr;

    /**
     * 接入协议
     */
    private String accessProtocol;

    /**
     * 明文密码
     */
    private String clearTextPassword;

    /**
     * 客户端端口
     */
    private Integer port;

    /**
     * TSL证书公用名
     */
    private String tslCertificateOfficialName;

    /**
     * TSL证书subject
     */
    private String tslCertificateSubject;
}
