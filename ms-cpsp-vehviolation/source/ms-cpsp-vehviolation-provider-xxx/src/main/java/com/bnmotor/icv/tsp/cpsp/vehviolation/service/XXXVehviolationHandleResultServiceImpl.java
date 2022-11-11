package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationHandleResultService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationHandleService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHandleResultInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleResultOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
* @ClassName: XXXVehviolationHandleResultServiceImpl
* @Description: 违章办理结果查询
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = "XXX_VEHVIOLATION", service = Constant.VEHVIOLATION_HANDLE_RESULT_API)
public class XXXVehviolationHandleResultServiceImpl implements VehviolationHandleResultService, AbstractStrategy<VehviolationHandleResultInput, VehviolationHandleResultOutput> {

    @Override
    public VehviolationHandleResultOutput call(VehviolationHandleResultInput input) {
        if(StringUtils.isBlank(input.getOrderNo())){
            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
        }
        VehviolationHandleResultOutput output = process(input);
        return output;
    }

    @Override
    public VehviolationHandleResultOutput process(VehviolationHandleResultInput input) {
        //根据数据返回，组装数据进行返回
        VehviolationHandleResultOutput output = packageOutput();
        return output;
    }

    private VehviolationHandleResultOutput packageOutput(){
        VehviolationHandleResultOutput output = new VehviolationHandleResultOutput();
        output.setPayStatus("2");
        return output;
    }
}
