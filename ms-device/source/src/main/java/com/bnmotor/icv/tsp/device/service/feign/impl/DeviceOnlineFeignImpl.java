package com.bnmotor.icv.tsp.device.service.feign.impl;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.BusinessExceptionEnums;
import com.bnmotor.icv.tsp.device.service.feign.DeviceOnlineFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @ClassName: DeviceOnlineFeignImpl
 * @Description: 车辆在线服务接口实现类
 * @author: huangyun1
 * @date: 2020/11/19
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Service
@Slf4j
public class DeviceOnlineFeignImpl {
    @Autowired
    private DeviceOnlineFeignService deviceOnlineFeignService;

    /**
     * 判断车辆是否在线
     */
    public Boolean checkOnline(String vin) {
        ResponseEntity<RestResponse<Boolean>> onlineRet = deviceOnlineFeignService.onlineStatus(vin);
        RestResponse<Boolean> response = onlineRet.getBody();
        if (response == null) {
            throw new AdamException(BusinessExceptionEnums.CALL_DEVICE_ONLINE_SER_ERROR);
        }

        if (HttpStatus.OK.equals(onlineRet.getStatusCode()) && RespCode.SUCCESS.getValue().equals(response.getRespCode())) {
            //todo 离线唤醒
            return response.getRespData() == null || response.getRespData();
        } else {
            throw new AdamException(BusinessExceptionEnums.CALL_DEVICE_ONLINE_SER_ERROR);
        }
    }

}
