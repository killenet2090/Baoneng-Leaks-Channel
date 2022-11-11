package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.SeatHeatStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.SeatVentStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: SeatColumnHandler
 * @Description: 座椅字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class SeatColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //主驾驶加热
        String seatHeatDriverStr = vehStatusMap.get(VehStatusEnum.SEAT_HEAT_STATUS_DRIVER.getColumnName());
        if(StringUtils.isNotBlank(seatHeatDriverStr)) {
            if(SeatHeatStateEnum.CLOSE.getValue().equals(seatHeatDriverStr)) {
                vehStatusMap.put(VehStatusEnum.SEAT_HEAT_STATUS_DRIVER.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.SEAT_HEAT_STATUS_DRIVER.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }

        //副驾驶加热
        String seatHeatPasseStr = vehStatusMap.get(VehStatusEnum.SEAT_HEAT_STATUS_PASSENAGER.getColumnName());
        if(StringUtils.isNotBlank(seatHeatPasseStr)) {
            if(SeatHeatStateEnum.CLOSE.getValue().equals(seatHeatPasseStr)) {
                vehStatusMap.put(VehStatusEnum.SEAT_HEAT_STATUS_PASSENAGER.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.SEAT_HEAT_STATUS_PASSENAGER.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }

        //主驾驶通风
        String drvSeatVent = vehStatusMap.get(VehStatusEnum.SHVM_DRV_SEAT_VENT.getColumnName());
        if(StringUtils.isNotBlank(drvSeatVent)) {
            if(SeatVentStateEnum.CLOSE.getValue().equals(drvSeatVent)) {
                vehStatusMap.put(VehStatusEnum.SHVM_DRV_SEAT_VENT.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.SHVM_DRV_SEAT_VENT.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }

        //副驾驶通风
        String pasSeatVent = vehStatusMap.get(VehStatusEnum.SHVM_PAS_SEAT_VENT.getColumnName());
        if(StringUtils.isNotBlank(pasSeatVent)) {
            if(SeatVentStateEnum.CLOSE.getValue().equals(pasSeatVent)) {
                vehStatusMap.put(VehStatusEnum.SHVM_PAS_SEAT_VENT.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.SHVM_PAS_SEAT_VENT.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
    }
}