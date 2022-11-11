package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.AirConditionerStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: AirConditionerColumnHandler
 * @Description: 空调字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class AirConditionerColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //空调状态
        String airConditionStatusStr = vehStatusMap.get(VehStatusEnum.AIR_CONDITION_STATUS_OFF_ON.getColumnName());
        if(StringUtils.isNotBlank(airConditionStatusStr)) {
            if(AirConditionerStateEnum.AC_OFF.getValue().equals(airConditionStatusStr)) {
                vehStatusMap.put(VehStatusEnum.AIR_CONDITION_STATUS_OFF_ON.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.AIR_CONDITION_STATUS_OFF_ON.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
    }
}