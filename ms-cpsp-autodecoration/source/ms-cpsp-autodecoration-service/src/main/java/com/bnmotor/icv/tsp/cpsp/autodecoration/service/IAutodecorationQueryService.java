package com.bnmotor.icv.tsp.cpsp.autodecoration.service;

import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryDetailOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryOutput;

/**
* @ClassName: IAutodecorationQueryService
* @Description: 汽车美容查询相关接口
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface IAutodecorationQueryService {

    /**
     * 商家列表查询
     */
    AutodecorationQueryOutput queryMerchantList(String adCode,String name, String sorting,String lng,String lat);

    /**
     * 商家详情查询
     */
    AutodecorationQueryDetailOutput queryMerchantDetail(String merchantId);
}
