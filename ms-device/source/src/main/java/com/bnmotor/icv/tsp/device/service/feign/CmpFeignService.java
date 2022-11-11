package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.response.feign.CmpSimVo;
import com.bnmotor.icv.tsp.device.model.response.feign.GetICCIDStateVo;
import com.bnmotor.icv.tsp.device.model.response.feign.SimVo;
import com.bnmotor.icv.tsp.device.service.feign.fallback.CmpFeignFallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName: CmpFeignService
 * @Description: sim卡管理服务接口
 * @author: huangyun1
 * @date: 2021/01/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@FeignClient(name = "ms-cmp", fallback = CmpFeignFallbackFactory.class)
public interface CmpFeignService {
    /**
     * 查询认证状态
     */
    @GetMapping("/inner/authentication/iccid/getState")
    @ApiOperation(value = "查询认证状态")
    RestResponse<GetICCIDStateVo> getICCIDState(@RequestParam("vin") String vin);

    /**
     * 根据iccid查询物联网卡信息
     */
    @GetMapping("/inner/sim/getSimByIccid")
    RestResponse<CmpSimVo> getSimByIccid(@RequestParam("iccid") String iccid);
}
