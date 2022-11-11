package com.bnmotor.icv.tsp.cpsp.autodecoration.service.impl;

import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input.AutodecorationQueryDetailInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryDetailOutput;
import com.bnmotor.icv.tsp.cpsp.engine.support.CPSPProxy;
import com.bnmotor.icv.tsp.cpsp.autodecoration.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.autodecoration.common.redis.RedisKey;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input.AutodecorationQueryInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.service.IAutodecorationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @ClassName: AutodecorationQueryServiceImpl
* @Description: 汽车美容商家查询实现类
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Service
public class AutodecorationQueryServiceImpl implements IAutodecorationQueryService {

    @Autowired
    private CPSPProxy cpspProxy;

    @Override
    public AutodecorationQueryOutput queryMerchantList(String adCode,String name, String sorting,String lng,String lat) {
        AutodecorationQueryInput input = AutodecorationQueryInput.builder().build();
        input.setAdCode(adCode);
        input.setName(name);
        input.setSorting(sorting);
        input.setLng(lng);
        input.setLat(lat);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.AUTODECORATION_QUERY_API);
        input.setCacheKey(String.format(RedisKey.AUTODECORATION_QUERY_SORT_KEY,adCode));
        input.setCacheClass(AutodecorationQueryOutput.class);
        AutodecorationQueryOutput output = cpspProxy.call(input);
        return output;
    }

    @Override
    public AutodecorationQueryDetailOutput queryMerchantDetail(String merchantId) {
        AutodecorationQueryDetailInput input = AutodecorationQueryDetailInput.builder().build();
        input.setMerchantId(merchantId);
        input.setServiceCode(Constant.SERVICE_CODE);
        input.setCurrentAPI(Constant.AUTODECORATION_QUERY_DETAIL_API);
        input.setCacheKey(String.format(RedisKey.AUTODECORATION_QUERY_NAME_KEY,merchantId));
        input.setCacheClass(AutodecorationQueryDetailOutput.class);
        AutodecorationQueryDetailOutput output = cpspProxy.call(input);
        return output;
    }
}
