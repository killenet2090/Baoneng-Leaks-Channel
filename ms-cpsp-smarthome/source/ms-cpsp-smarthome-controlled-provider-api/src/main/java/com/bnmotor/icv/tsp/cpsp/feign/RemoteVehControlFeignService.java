package com.bnmotor.icv.tsp.cpsp.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehControlInput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @ClassName: RemoteVehControlFeignService
* @Description: 车辆远程控制
* @author: liuhuaqiao1
* @date: 2021/3/9
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@FeignClient(value = "ms-remote-control")
@RequestMapping()
public interface RemoteVehControlFeignService {

    @PostMapping(value = "/v1/vehControl/airConditioner/setOffOn")
    RestResponse vehAirConditionerSetOffOn(@RequestBody RemoteVehControlInput vo);

    @PostMapping(value = "/v1/vehControl/airConditioner/setTemperature")
    RestResponse vehAirConditionerSetTemp(@RequestBody RemoteVehControlInput vo);

    @PostMapping(value = "/v1/vehControl/window/set")
    RestResponse vehWindowSet(@RequestBody RemoteVehControlInput vo);

}
