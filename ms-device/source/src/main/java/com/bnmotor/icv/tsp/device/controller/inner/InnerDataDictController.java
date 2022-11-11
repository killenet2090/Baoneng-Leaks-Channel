package com.bnmotor.icv.tsp.device.controller.inner;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.device.common.enums.veh.VehicleType;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.BindStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.CertificationStatus;
import com.bnmotor.icv.tsp.device.common.enums.vehStatus.VehicleStatus;
import com.bnmotor.icv.tsp.device.model.response.vehicle.DictVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: InnerDataDictController
 * @Description: 通用数据字典
 * @author: zhangwei2
 * @date: 2020/12/4
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@RequestMapping("/inner/dict")
@Api(value = "通用数据字典，用于查询车辆状态，认证状态，质检状态等", tags = {"车辆相关数据字典接口"})
@Slf4j
public class InnerDataDictController {
    @GetMapping("/listVehStatus")
    @ApiOperation(value = "查询车辆状态")
    public ResponseEntity listVehStatus() {
        List<VehicleStatus> vehStatus = Arrays.asList(VehicleStatus.values());
        return RestResponse.ok(vehStatus.stream().map(o -> {
            DictVo dictVo = new DictVo();
            dictVo.setKey(o.getStatus());
            dictVo.setValue(o.getDesp());
            return dictVo;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/listBindStatus")
    @ApiOperation(value = "查询车辆状态")
    public ResponseEntity listBindStatus() {
        List<BindStatus> bindStatus = Arrays.asList(BindStatus.values());
        return RestResponse.ok(bindStatus.stream().map(o -> {
            DictVo dictVo = new DictVo();
            dictVo.setKey(o.getStatus());
            dictVo.setValue(o.getDesp());
            return dictVo;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/listPowerType")
    @ApiOperation(value = "查询车辆动力类型")
    public ResponseEntity listPowerType() {
        List<VehicleType> vehicleTypes = Arrays.asList(VehicleType.values());
        return RestResponse.ok(vehicleTypes.stream().map(o -> {
            DictVo dictVo = new DictVo();
            dictVo.setKey(o.getType());
            dictVo.setValue(o.getDesp());
            return dictVo;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/listCertificateStatus")
    @ApiOperation(value = "车型车辆认证状态")
    public ResponseEntity listCertificateStatus() {
        List<CertificationStatus> certStatus = Arrays.asList(CertificationStatus.values());
        return RestResponse.ok(certStatus.stream().map(o -> {
            DictVo dictVo = new DictVo();
            dictVo.setKey(o.getStatus());
            dictVo.setValue(o.getDesp());
            return dictVo;
        }).collect(Collectors.toList()));
    }

}
