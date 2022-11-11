package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.WindowStateEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: WindowColumnHandler
 * @Description: 车窗字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class WindowColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //右前窗状态
        String frontRightWindowStatusStr = vehStatusMap.get(VehStatusEnum.FRONT_RIGHT_WINDOW_STATUS.getColumnName());
        if(StringUtils.isNotBlank(frontRightWindowStatusStr)) {
            Integer frontRightWindowStatus = Integer.parseInt(frontRightWindowStatusStr);
            if(WindowStateEnum.CLOSE.getValue().equals(frontRightWindowStatus)) {
                vehStatusMap.put(VehStatusEnum.FRONT_RIGHT_WINDOW_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.FRONT_RIGHT_WINDOW_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
        //左前窗状态
        String frontLeftWindowStatusStr = vehStatusMap.get(VehStatusEnum.FRONT_LEFT_WINDOW_STATUS.getColumnName());
        if(StringUtils.isNotBlank(frontLeftWindowStatusStr)) {
            Integer frontLeftWindowStatus = Integer.parseInt(frontLeftWindowStatusStr);
            if(WindowStateEnum.CLOSE.getValue().equals(frontLeftWindowStatus)) {
                vehStatusMap.put(VehStatusEnum.FRONT_LEFT_WINDOW_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.FRONT_LEFT_WINDOW_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
        //右后窗状态
        String rearRightWindowStatusStr = vehStatusMap.get(VehStatusEnum.REAR_RIGHT_WINDOW_STATUS.getColumnName());
        if(StringUtils.isNotBlank(rearRightWindowStatusStr)) {
            Integer rearRightWindowStatus = Integer.parseInt(rearRightWindowStatusStr);
            if(WindowStateEnum.CLOSE.getValue().equals(rearRightWindowStatus)) {
                vehStatusMap.put(VehStatusEnum.REAR_RIGHT_WINDOW_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.REAR_RIGHT_WINDOW_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
        //左后窗状态
        String rearLeftWindowStatusStr = vehStatusMap.get(VehStatusEnum.REAR_LEFT_WINDOW_STATUS.getColumnName());
        if(StringUtils.isNotBlank(rearLeftWindowStatusStr)) {
            Integer rearLeftWindowStatus = Integer.parseInt(rearLeftWindowStatusStr);
            if(WindowStateEnum.CLOSE.getValue().equals(rearLeftWindowStatus)) {
                vehStatusMap.put(VehStatusEnum.REAR_LEFT_WINDOW_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.REAR_LEFT_WINDOW_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
    }
}