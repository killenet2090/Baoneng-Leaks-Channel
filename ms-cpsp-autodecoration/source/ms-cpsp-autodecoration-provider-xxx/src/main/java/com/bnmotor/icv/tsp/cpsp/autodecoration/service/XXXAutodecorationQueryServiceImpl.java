package com.bnmotor.icv.tsp.cpsp.autodecoration.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.autodecoration.api.AutodecorationQueryService;
import com.bnmotor.icv.tsp.cpsp.autodecoration.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input.AutodecorationQueryInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.vo.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* @ClassName: XXXAutodecorationQueryServiceImpl
* @Description: 商家列表查询实现类
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.AUTODECORATION_PROVIDER_XXX, service = Constant.AUTODECORATION_QUERY_API)
public class XXXAutodecorationQueryServiceImpl implements AutodecorationQueryService, AbstractStrategy<AutodecorationQueryInput, AutodecorationQueryOutput> {

    @Override
    public AutodecorationQueryOutput call(AutodecorationQueryInput input) {
        if(StringUtils.isEmpty(input.getSorting()) || input.getSorting().equals("1")){
            if(StringUtils.isEmpty(input.getAdCode()) &&
                    (StringUtils.isEmpty(input.getLng()) && StringUtils.isEmpty(input.getLat()))){
                throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
            }
            input.setSorting("1");
        }
        AutodecorationQueryOutput output = process(input);
        return output;
    }

    @Override
    public AutodecorationQueryOutput process(AutodecorationQueryInput input) {
        //根据数据返回，组装数据进行返回
        AutodecorationQueryOutput output = packageOutput();
        return output;
    }

    private AutodecorationQueryOutput packageOutput(){
        AutodecorationQueryOutput output = new AutodecorationQueryOutput();
        Merchant merchant = new Merchant();
        merchant.setIcon("壳牌汽车美容图片url");
        merchant.setMerchantId("SJ123");
        merchant.setName("壳牌汽车美容");
        merchant.setAddress("深圳市龙华区清祥路段");
        merchant.setDistance("1.66KM");
        merchant.setRating("4.5");
        merchant.setSales("9999+");
        output.setMerchants(Arrays.asList(merchant));
        return output;
    }
}
