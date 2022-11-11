package com.bnmotor.icv.tsp.cpsp.controller;

import com.bnmotor.icv.adam.web.rest.RestResponse;
import com.bnmotor.icv.tsp.cpsp.domain.request.RemoteVehControlVo;
import com.bnmotor.icv.tsp.cpsp.domain.request.RemoteVehInfoVo;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehControlOutput;
import com.bnmotor.icv.tsp.cpsp.pojo.output.RemoteVehInfoOutput;
import com.bnmotor.icv.tsp.cpsp.service.IRemoteVehService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @ClassName: RemoteController
* @Description: 家控车控制器
* @author: liuhuaqiao1
* @date: 2021/3/8
* @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
*/
@RestController
@RequestMapping(value="/v1/third")
@Slf4j
public class RemoteVehController {

    @Autowired
    private IRemoteVehService remoteVehService;

    /**
     * 查询车辆状态信息
     * @param vo 车辆状态查询vo
     * @return
     */
    @GetMapping(value = "/vehInfo/status/get")
    public ResponseEntity getSceneList(RemoteVehInfoVo vo) {
        RemoteVehInfoOutput output = remoteVehService.getVehInfo(vo);
        return RestResponse.ok(output);
    }

    /**
     * 设置空调开关
     * @param vo 车辆状态控制vo
     * @return
     */
    @PostMapping(value = "/vehControl/airConditioner/setOffOn")
    public ResponseEntity getSceneDetails(@RequestBody RemoteVehControlVo vo) {
        RemoteVehControlOutput output = remoteVehService.airConditionerSetOffOn(vo);
        return RestResponse.ok(output);
    }

    /**
     * 设置空调温度
     * @param vo 车辆状态控制vo
     * @return
     */
    @PostMapping(value = "/vehControl/airConditioner/setTemperature")
    public ResponseEntity sceneExecution(@RequestBody RemoteVehControlVo vo) {
        RemoteVehControlOutput output = remoteVehService.airConditionerSetTemperature(vo);
        return RestResponse.ok(output);
    }

    /**
     * 设置车窗状态
     * @param vo 车辆状态控制vo
     * @return
     */
    @PostMapping(value = "/vehControl/window/set")
    public ResponseEntity sceneGeofenceLink(@RequestBody RemoteVehControlVo vo) {
        RemoteVehControlOutput output = remoteVehService.vehWindowSet(vo);
        return RestResponse.ok(output);
    }

}
