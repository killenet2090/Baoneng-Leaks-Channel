package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.ChargingDoorStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.ChargingGunConnectStateEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: ChargingColumnHandler
 * @Description: 充电相关字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class ChargingColumnHandler implements ColumnHandler {
    /**
     * 小时转s
     */
    private static final Integer SECONDS_TO_HOUR = 1 * 60 * 60;
    /**
     * 分钟转s
     */
    private static final Integer SECONDS_TO_MINUTE = 1 * 60;
    /**
     * 小时、分钟小于阈值时添加的字符前缀
     */
    private static final String APPEND_CHAR_LESS_THRESHOLD = "0";
    /**
     * 小时、分钟阈值
     */
    private static final Integer THRESHOLD_TIME = 10;

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //续航里程
        String distanceToEmptyStr = vehStatusMap.get(VehStatusEnum.DISTANCE_TO_EMPTY.getColumnName());
        if(StringUtils.isNotBlank(distanceToEmptyStr)) {
            String[] distanceToEmptyArray = distanceToEmptyStr.split("\\.");
            if(distanceToEmptyArray.length > 1) {
                if(Long.parseLong(distanceToEmptyArray[1]) == 0) {
                    vehStatusMap.put(VehStatusEnum.DISTANCE_TO_EMPTY.getColumnName(), distanceToEmptyArray[0]);
                }
            }
        }
        //剩余时间，SOC未达80%时，充电至80%所需时间;格式：hour:minute
        String chargingTimeForEightyStr = vehStatusMap.get(VehStatusEnum.CHARGING_TIME_FOR_EIGHTY.getColumnName());
        if(StringUtils.isNotBlank(chargingTimeForEightyStr)) {
            vehStatusMap.put(VehStatusEnum.CHARGING_TIME_FOR_EIGHTY.getColumnName(), formatTimeFromObject(chargingTimeForEightyStr));
        }
        //剩余时间 格式：hour:minute
        String chargingTimeForNinetyStr = vehStatusMap.get(VehStatusEnum.CHARGING_TIME_FOR_REMAINDER.getColumnName());
        if(StringUtils.isNotBlank(chargingTimeForNinetyStr)) {
            vehStatusMap.put(VehStatusEnum.CHARGING_TIME_FOR_REMAINDER.getColumnName(), formatTimeFromObject(chargingTimeForNinetyStr));
        }
        //SOC>=80%，充电至100%所需时间；格式：hour:minute
        String chargingTimeForFullStr = vehStatusMap.get(VehStatusEnum.CHARGING_TIME_FOR_FULL.getColumnName());
        if(StringUtils.isNotBlank(chargingTimeForFullStr)) {
            vehStatusMap.put(VehStatusEnum.CHARGING_TIME_FOR_FULL.getColumnName(), formatTimeFromObject(chargingTimeForFullStr));
        }
        //充电口盖
        String chargingDoorStateStr = vehStatusMap.get(VehStatusEnum.CHARGING_DOOR_STATE.getColumnName());
        if (StringUtils.isNotBlank(chargingDoorStateStr)) {
            if (ChargingDoorStateEnum.CLOSE.getValue().equals(chargingDoorStateStr)) {
                vehStatusMap.put(VehStatusEnum.CHARGING_DOOR_STATE.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else if (ChargingDoorStateEnum.OPEN.getValue().equals(chargingDoorStateStr)) {
                vehStatusMap.put(VehStatusEnum.CHARGING_DOOR_STATE.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            } else if (ChargingDoorStateEnum.MIDDLE.getValue().equals(chargingDoorStateStr)) {
                vehStatusMap.put(VehStatusEnum.CHARGING_DOOR_STATE.getColumnName(), Constant.CHARGING_MIDDLE_STATE.toString());
            }
        }
        //充电枪连接状态
        String chargingGunConnectStateStr = vehStatusMap.get(VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getColumnName());
        if (StringUtils.isNotBlank(chargingGunConnectStateStr)) {
            if (!ChargingGunConnectStateEnum.NO_CONNECTED.getValue().equals(chargingGunConnectStateStr)) {
                vehStatusMap.put(VehStatusEnum.CHARGING_GUN_CONNECT_STATE.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
    }

    /**
     * 将日期对象转为 hour:minute格式
     * @param dateObject
     * @return
     */
    private String formatTimeFromObject(Object dateObject) {
        int chargingTime = Integer.parseInt(dateObject.toString());
        int chargingTimeHour = chargingTime / SECONDS_TO_HOUR;
        int chargingTimeMinuteMod = chargingTime % SECONDS_TO_HOUR;
        int chargingTimeMinute = chargingTimeMinuteMod / SECONDS_TO_MINUTE;
        int secondMod = chargingTimeMinuteMod % SECONDS_TO_MINUTE;
        if(secondMod > 0) {
            chargingTimeMinute += 1;
        }
        return chargingTimeHour + Constant.REDIS_JOINER_CHAR + chargingTimeMinute;
    }
}