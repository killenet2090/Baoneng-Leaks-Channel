package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationHandleService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.VioDetail;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.VioSummary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;

/**
* @ClassName: XXXVehviolationHandleServiceImpl
* @Description: 违章办理
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = "XXX_VEHVIOLATION", service = Constant.VEHVIOLATION_HANDLE_API)
public class XXXVehviolationHandleServiceImpl implements VehviolationHandleService, AbstractStrategy<VehviolationHandleInput, VehviolationHandleOutput> {

    @Override
    public VehviolationHandleOutput call(VehviolationHandleInput input) {
        if(StringUtils.isBlank(input.getVin()) || CollectionUtils.isEmpty(input.getViolationIds())){
            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
        }
        VehviolationHandleOutput output = process(input);
        return output;
    }

    @Override
    public VehviolationHandleOutput process(VehviolationHandleInput input) {
        //根据数据返回，组装数据进行返回
        VehviolationHandleOutput output = packageOutput();
        return output;
    }

    private VehviolationHandleOutput packageOutput(){
        VehviolationHandleOutput output = new VehviolationHandleOutput();
        output.setOrderNo("9527");
        output.setPayUrl("https://test-cxypay.cx580.com:3000/oauth?pay_id=384EA5D244EA44C88AB416E77B8DD85E");
        return output;
    }
}
