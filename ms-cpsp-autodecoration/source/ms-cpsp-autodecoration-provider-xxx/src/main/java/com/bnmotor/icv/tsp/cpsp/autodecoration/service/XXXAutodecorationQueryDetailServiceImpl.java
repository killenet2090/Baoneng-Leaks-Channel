package com.bnmotor.icv.tsp.cpsp.autodecoration.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.autodecoration.api.AutodecorationQueryService;
import com.bnmotor.icv.tsp.cpsp.autodecoration.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.input.AutodecorationQueryDetailInput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.output.AutodecorationQueryDetailOutput;
import com.bnmotor.icv.tsp.cpsp.autodecoration.pojo.vo.ServicePrice;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @ClassName: XXXAutodecorationQueryDetailServiceImpl
* @Description: 商家详情查询实现类
* @author: liuhuaqiao1
* @date: 2021/1/14
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = Constant.AUTODECORATION_PROVIDER_XXX, service = Constant.AUTODECORATION_QUERY_DETAIL_API)
public class XXXAutodecorationQueryDetailServiceImpl implements AutodecorationQueryService, AbstractStrategy<AutodecorationQueryDetailInput, AutodecorationQueryDetailOutput> {

    @Override
    public AutodecorationQueryDetailOutput call(AutodecorationQueryDetailInput input) {
        if(StringUtils.isBlank(input.getMerchantId())){
            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
        }
        AutodecorationQueryDetailOutput output = process(input);
        return output;
    }

    @Override
    public AutodecorationQueryDetailOutput process(AutodecorationQueryDetailInput input) {
        //根据数据返回，组装数据进行返回
        AutodecorationQueryDetailOutput output = packageOutput();
        return output;
    }

    private AutodecorationQueryDetailOutput packageOutput(){
        AutodecorationQueryDetailOutput output = new AutodecorationQueryDetailOutput();
        List<String> icons = new ArrayList<>();
        icons.add("壳牌汽车美容图片url");
        List<ServicePrice> services = new ArrayList<>();
        ServicePrice project = new ServicePrice();
        project.setService("精致洗车");
        project.setPrice("168元");
        services.add(project);
        output.setIcons(icons);
        output.setName("壳牌汽车美容");
        output.setAddress("深圳市龙华区清祥路段");
        output.setDistance("1.66KM");
        output.setRating("4.5");
        output.setSales("9999+");
        output.setBusinessHours("09:00-18:00");
        output.setServices(services);
        output.setTelephone("17688999620");
        output.setLng("12.34567");
        output.setLat("45.34567");
        return output;
    }
}
