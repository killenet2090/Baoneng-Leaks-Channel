package com.bnmotor.icv.tsp.cpsp.vehviolation.service;

import com.bnmotor.icv.tsp.cpsp.vehviolation.model.request.VehViolationHandleDto;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationHandleOutput;
import com.bnmotor.icv.tsp.cpsp.vehviolation.pojo.output.VehviolationQueryOutput;

import java.util.List;

/**
* @ClassName: IVehviolationHandleService
* @Description: 违章办理
* @author: liuhuaqiao1
* @date: 2021/1/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
public interface IVehviolationHandleService {

    /**
     * 违章办理
     */
    VehviolationHandleOutput handleVehviolation(VehViolationHandleDto vehViolationHandleDto);
}
