package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationAlertQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationAlertQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationAlertQueryOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @ClassName: VehviolationQueryServiceImpl
* @Description: 违章提醒查询
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = "XXX_VEHVIOLATION", service = Constant.VEHVIOLATION_ALERT_QUERY_API)
public class XXXVehviolationAlertQueryServiceImpl implements VehviolationAlertQueryService, AbstractStrategy<VehviolationAlertQueryInput, VehviolationAlertQueryOutput> {

    @Override
    public VehviolationAlertQueryOutput call(VehviolationAlertQueryInput input) {
        if(StringUtils.isBlank(input.getVin())){
            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
        }
        VehviolationAlertQueryOutput output = process(input);
        return output;
    }

    @Override
    public VehviolationAlertQueryOutput process(VehviolationAlertQueryInput input) {
        //根据数据返回，组装数据进行返回
        VehviolationAlertQueryOutput output = packageOutput();
        return output;
    }

    private VehviolationAlertQueryOutput packageOutput(){
        VehviolationAlertQueryOutput output = new VehviolationAlertQueryOutput();
        output.setTotalViolation(10);
        return output;
    }
}
