package com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @ClassName: Merchant
* @Description: 商家明细实体类
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Data
@NoArgsConstructor
public class Merchant {

    /**
     * 商家图片
     */
    private String icon;

    /**
     * 商家名称
     */
    private String name;

    /**
     * 商家ID
     */
    private String merchantId;

    /**
     * 评分
     */
    private String rating;

    /**
     * 地址
     */
    private String address;

    /**
     * 距离（距离为保留小数点后两位）
     */
    private String distance;

    /**
     * 销量
     */
    private String sales;

}
