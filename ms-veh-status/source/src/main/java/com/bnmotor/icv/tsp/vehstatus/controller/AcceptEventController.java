package com.bnmotor.icv.tsp.vehstatus.controller;

import com.bnmotor.icv.adam.sdk.realtime.status.dto.RealtimeStatusDto;
import com.bnmotor.icv.adam.web.rest.BaseController;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.common.util.DateUtils;
import com.bnmotor.icv.tsp.vehstatus.config.convert.VehStatusConvertForMap;
import com.bnmotor.icv.tsp.vehstatus.model.request.SetVehStatusByMapDto;
import com.bnmotor.icv.tsp.vehstatus.model.request.SetVehStatusDto;
import com.bnmotor.icv.tsp.vehstatus.service.impl.VelStatusAcceptHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AcceptEventController
 * @Description: 接收车况变动事controller
 * @author: huangyun1
 * @date: 2020/5/21
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@RestController
@RequestMapping("/v1/acceptEvent")
@Api(value = "接收车况变动事件controller", tags = {"接收车况变动事件接口"})
public class AcceptEventController extends BaseController {

    @Autowired
    private VelStatusAcceptHandler velStatusAcceptHandler;

    @Autowired
    private VehStatusConvertForMap<String> statusNameForStatusConvertMap;
    /**
     * 设置车况信息
     * @return
     */
    @PostMapping("/status/setVehStatus")
    @ApiOperation(value = "设置车况信息")
    public ResponseEntity setVehStatus(@ApiParam(name = "setVehStatus", value = "传入json格式", required = true)
                                       @Validated  @RequestBody SetVehStatusDto setVehStatusDto) {
        RealtimeStatusDto realtimeStatusDto = new RealtimeStatusDto();
        realtimeStatusDto.setVin(setVehStatusDto.getVin());
        Map<String, Number> paramMap = new HashMap<> (3);
        if(setVehStatusDto.getColumnName() != null && !setVehStatusDto.getColumnName().isEmpty()) {
            for(int i = 0; i < setVehStatusDto.getColumnName().size(); i++) {
                String columnName = setVehStatusDto.getColumnName().get(i);
                Number columnValue = setVehStatusDto.getColumnValue().get(i);
                paramMap.put(statusNameForStatusConvertMap.get(columnName).getCode(), columnValue);
            }
        }
        realtimeStatusDto.setStatusKeyValueMap(paramMap);

        if(paramMap.containsKey(VehStatusEnum.TIMESTAMP.getCode())) {
            realtimeStatusDto.setMsgtime(paramMap.get(VehStatusEnum.TIMESTAMP.getCode()).longValue());
        }

        velStatusAcceptHandler.onMessage(realtimeStatusDto);
        return this.responseSuccess(null);
    }


    /**
     * 根据map设置车况信息
     * @return
     */
    @PostMapping("/status/setVehStatusByMap")
    @ApiOperation(value = "根据map设置车况信息")
    public ResponseEntity setVehStatusByMap(@ApiParam(name = "setVehStatusByMap", value = "传入json格式", required = true)
                                       @Validated  @RequestBody SetVehStatusByMapDto setVehStatusByMapDto) {
        RealtimeStatusDto realtimeStatusDto = new RealtimeStatusDto();
        realtimeStatusDto.setVin(setVehStatusByMapDto.getVin());
        realtimeStatusDto.setStatusKeyValueMap(setVehStatusByMapDto.getUpdateVehStatusMap());
        realtimeStatusDto.setMsgtime(DateUtils.getMillFromLocalDateTime(LocalDateTime.now()));
        velStatusAcceptHandler.onMessage(realtimeStatusDto);
        return this.responseSuccess(null);
    }

}
