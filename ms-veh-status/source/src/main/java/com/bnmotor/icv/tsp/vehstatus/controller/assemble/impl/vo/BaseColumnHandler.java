package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.WindowStateEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: BaseColumnHandler
 * @Description: 基础字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaseColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //查询车窗状态
        String frontLeftWindowStatus = vehStatusMap.get(VehStatusEnum.FRONT_LEFT_WINDOW_STATUS.getColumnName());
        String frontRightWindowStatus = vehStatusMap.get(VehStatusEnum.FRONT_RIGHT_WINDOW_STATUS.getColumnName());
        String rearLeftWindowStatus = vehStatusMap.get(VehStatusEnum.REAR_LEFT_WINDOW_STATUS.getColumnName());
        String rearRightWindowStatus = vehStatusMap.get(VehStatusEnum.REAR_RIGHT_WINDOW_STATUS.getColumnName());
        if (StringUtils.isNotBlank(frontLeftWindowStatus) && StringUtils.isNotBlank(frontRightWindowStatus) &&
                StringUtils.isNotBlank(rearLeftWindowStatus) && StringUtils.isNotBlank(rearRightWindowStatus)) {
            if (WindowStateEnum.CLOSE.getValue().equals(Integer.parseInt(frontLeftWindowStatus)) &&
                    WindowStateEnum.CLOSE.getValue().equals(Integer.parseInt(frontRightWindowStatus)) &&
                    WindowStateEnum.CLOSE.getValue().equals(Integer.parseInt(rearLeftWindowStatus)) &&
                    WindowStateEnum.CLOSE.getValue().equals(Integer.parseInt(rearRightWindowStatus))) {
                vehStatusMap.put(VehStatusEnum.WINDOE_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                if (WindowStateEnum.TEN_PERCENT.getValue().equals(Integer.parseInt(frontLeftWindowStatus)) &&
                        WindowStateEnum.TEN_PERCENT.getValue().equals(Integer.parseInt(frontRightWindowStatus)) &&
                        WindowStateEnum.TEN_PERCENT.getValue().equals(Integer.parseInt(rearLeftWindowStatus)) &&
                        WindowStateEnum.TEN_PERCENT.getValue().equals(Integer.parseInt(rearRightWindowStatus))) {
                    vehStatusMap.put(VehStatusEnum.WINDOE_STATUS.getColumnName(), Constant.WINDOW_VENTILATE.toString());
                } else {
                    vehStatusMap.put(VehStatusEnum.WINDOE_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
                }
            }
        }
    }
}