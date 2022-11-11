package com.bnmotor.icv.tsp.device.service.feign;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.model.request.feign.BleDeviceDelDto;
import com.bnmotor.icv.tsp.device.model.request.feign.UpdateVehInfoBluDto;
import com.bnmotor.icv.tsp.device.service.feign.fallback.BluetoothKeyFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @ClassName: BluetoothKeyFeignService
 * @Description: 基于feign调用蓝牙钥匙服务
 * @author: huangyun1
 * @date: 2020/10/20
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@FeignClient(value = "ms-bluetooth-key", fallback = BluetoothKeyFeignServiceFallbackFactory.class)
public interface BluetoothKeyFeignService {

    /**
     * 注销所有蓝牙
     * @param uid
     * @param projectId
     * @param bleDeviceDelDto
     * @return
     */
    @PostMapping(value = "/v1/ble/mgt/all/deregister", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResponse delDeviceBlekeyApp(@RequestHeader("uid") String uid, @RequestHeader("projectId") String projectId, @Validated @RequestBody BleDeviceDelDto bleDeviceDelDto);

    @PostMapping(value = "/v1/ble/mgt/devname/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    RestResponse update(@RequestBody UpdateVehInfoBluDto updateVehInfoBluDto);
}
