package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.annotation.ThirdPartySvcAnno;
import com.bnmotor.icv.tsp.cpsp.engine.route.adapter.strategy.AbstractStrategy;
import com.bnmotor.icv.tsp.cpsp.vehviolation.api.VehviolationQueryService;
import com.bnmotor.icv.tsp.cpsp.vehviolation.common.constants.Constant;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.input.VehviolationQueryInput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.VioDetail;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.vo.VioSummary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* @ClassName: VehviolationQueryServiceImpl
* @Description: 违章查询
* @author: liuhuaqiao1
* @date: 2021/1/7
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@Slf4j
@Service
@ThirdPartySvcAnno(provider = "XXX_VEHVIOLATION", service = Constant.VEHVIOLATION_QUERY_API)
public class XXXVehviolationQueryServiceImpl implements VehviolationQueryService, AbstractStrategy<VehviolationQueryInput, VehviolationQueryOutput> {

    @Override
    public VehviolationQueryOutput call(VehviolationQueryInput input) {
        if(StringUtils.isBlank(input.getVin())){
            throw new AdamException(RespCode.REQUEST_PARAMETER_MISSING);
        }
        VehviolationQueryOutput output = process(input);
        return output;
    }

    @Override
    public VehviolationQueryOutput process(VehviolationQueryInput input) {
        //根据数据返回，组装数据进行返回
        VehviolationQueryOutput output = packageOutput();
        return output;
    }

    private VehviolationQueryOutput packageOutput(){
        VehviolationQueryOutput output = new VehviolationQueryOutput();
        VioSummary vioSummary = new VioSummary();
        vioSummary.setTotalDegree("3");
        vioSummary.setTotalFine("500");
        vioSummary.setVioCount("2");
        output.setSummary(vioSummary);
        VioDetail vioDetail = new VioDetail();
        vioDetail.setCanProcess("1");
        vioDetail.setDegree("6");
        vioDetail.setFine("200");
        vioDetail.setLocation("深圳市龙华区清祥路段");
        vioDetail.setReason("机动车违反规定停放，临时停车，驾驶人未在规定时间驶离，妨碍其他车辆、行人通行。");
        vioDetail.setStatus("0");
        vioDetail.setTime("2020-11-18 15:20:00");
        vioDetail.setViolationId("123");
        output.setVioDetails(Arrays.asList(vioDetail));
        return output;
    }
}
