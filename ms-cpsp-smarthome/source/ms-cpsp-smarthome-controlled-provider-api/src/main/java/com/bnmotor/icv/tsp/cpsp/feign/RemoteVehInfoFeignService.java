package com.bnmotor.icv.tsp.cpsp.feign;

import com.bnmotor.icv.tsp.cpsp.pojo.input.RemoteVehInfoInput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @ClassName: RemoteVehInfoFeignService
* @Description: 车辆状态查询
* @author: liuhuaqiao1
* @date: 2021/3/9
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@FeignClient(value = "ms-veh-status")
@RequestMapping()
public interface RemoteVehInfoFeignService {

    @GetMapping(value = "/v1/vehInfo/status/get")
    ResponseEntity<String> vehInfoStatus(@RequestBody RemoteVehInfoInput vo);

}
