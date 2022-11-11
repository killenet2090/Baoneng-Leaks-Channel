package com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: ServiceProject
* @Description: 服务项目实体类
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
public class ServicePrice {

    /**
     * 服务项目
     */
    private String service;

    /**
     * 服务价格
     */
    private String price;

}
