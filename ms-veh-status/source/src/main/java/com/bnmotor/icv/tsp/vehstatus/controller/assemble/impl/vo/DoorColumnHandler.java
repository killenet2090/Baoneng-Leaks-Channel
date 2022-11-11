package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.BackDoorLockStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: DoorColumnHandler
 * @Description: 车门字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class DoorColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //四门锁状态
        String doorLockStatusStr = vehStatusMap.get(VehStatusEnum.DOOR_LOCK_STATUS.getColumnName());
        if (StringUtils.isNotBlank(doorLockStatusStr)) {
            vehStatusMap.put(VehStatusEnum.DOOR_LOCK_STATUS.getColumnName(), String.valueOf(Integer.parseInt(doorLockStatusStr) ^ 1));
        }
        //后备箱锁状态
        String trunkLockStatusStr = vehStatusMap.get(VehStatusEnum.TRUNK_SWITCH_STATUS.getColumnName());
        if (StringUtils.isNotBlank(trunkLockStatusStr)) {
            if(BackDoorLockStateEnum.CINCHED.getValue().equals(trunkLockStatusStr)) {
                vehStatusMap.put(VehStatusEnum.TRUNK_SWITCH_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.TRUNK_SWITCH_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
    }
}