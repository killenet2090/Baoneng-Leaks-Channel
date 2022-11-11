package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationHotSpotQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationHotSpotQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHotSpotQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.HotSpotVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* @ClassName: XXXVehviolationHotSpotQueryServiceImpl
* @Description: 违章热点信息查询
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = "XXX_VEHVIOLATION", service = Constant.VEHVIOLATION_HOTSPOT_QUERY_API)
public class XXXVehviolationHotSpotQueryServiceImpl implements VehviolationHotSpotQueryService, AbstractStrategy<VehviolationHotSpotQueryInput, VehviolationHotSpotQueryOutput> {

    @Override
    public VehviolationHotSpotQueryOutput call(VehviolationHotSpotQueryInput input) {
        if(StringUtils.isEmpty(input.getLng()) || StringUtils.isEmpty(input.getLat())){
            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
        }
        VehviolationHotSpotQueryOutput output = process(input);
        return output;
    }

    @Override
    public VehviolationHotSpotQueryOutput process(VehviolationHotSpotQueryInput input) {
        //根据数据返回，组装数据进行返回
        VehviolationHotSpotQueryOutput output = packageOutput(input);
        return output;
    }

    private VehviolationHotSpotQueryOutput packageOutput(VehviolationHotSpotQueryInput input){
        VehviolationHotSpotQueryOutput output = new VehviolationHotSpotQueryOutput();
        HotSpotVo vo = new HotSpotVo();
        vo.setIllegalCount("103");
        vo.setLat(input.getLat());
        vo.setLng(input.getLng());
        vo.setLocation("深圳龙华区宝科园");
        vo.setType(input.getType());
        output.setRecords(Arrays.asList(vo));
        return output;
    }
}
