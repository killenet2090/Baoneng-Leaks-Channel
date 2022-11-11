package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


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
public class VehLockColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //整车锁状态
        String carLockStatusStr = vehStatusMap.get(VehStatusEnum.CAR_LOCK.getColumnName());
        if(StringUtils.isNotBlank(carLockStatusStr)) {
            vehStatusMap.put(VehStatusEnum.CAR_LOCK.getColumnName(), String.valueOf(Integer.parseInt(carLockStatusStr) ^ 1));
        }
    }
}