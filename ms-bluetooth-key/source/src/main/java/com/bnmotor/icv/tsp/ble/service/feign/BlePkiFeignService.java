package com.bnmotor.icv.tsp.ble.service.feign;


import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.ble.model.request.pki.*;
import com.bnmotor.icv.tsp.ble.service.feign.fallback.DeviceFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "ms-pki",  fallback = DeviceFeignFallbackFactory.class)
@RequestMapping("/inner/security")
public interface BlePkiFeignService {
    @RequestMapping(value = "/asymmetricEncrypt", consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    RestResponse<AsymmetricVo> asymmetricEncrypt(@RequestBody BleEncryptDto bleEncryptDto);

    @RequestMapping(value = "/encrpty", consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    RestResponse<Map> encrpty(@RequestBody BleEncrptyDto bleEncrptyDto);

    @RequestMapping(value = "/decrpty", consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    RestResponse<Map> decrpty(@RequestBody BleDecrptyDto bleDecrptyDto);

    @RequestMapping(value = "/sign", consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    RestResponse<SignVo> sign(@RequestBody BleSignDto bleSignDto);

    @RequestMapping(value = "/verifySign", consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    RestResponse<VerifySignVo> verifySign(@RequestBody BleVerSignDto bleVerSignDto);

    @RequestMapping(value = "/importKey", consumes = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    RestResponse<Map> importKey(@RequestBody BleImportDto bleImportDto);
}
