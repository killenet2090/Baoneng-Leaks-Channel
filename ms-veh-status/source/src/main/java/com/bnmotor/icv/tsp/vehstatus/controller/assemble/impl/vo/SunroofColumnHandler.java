package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.vo;


import com.bnmotor.icv.tsp.vehstatus.common.Constant;
import com.bnmotor.icv.tsp.vehstatus.common.enums.SunroofStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName: SunroofColumnHandler
 * @Description: 天窗字段处理类
 * @author: huangyun1
 * @date: 2020/12/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Slf4j
public class SunroofColumnHandler implements ColumnHandler {

    @Override
    public void handleVo(Map<String, String> vehStatusMap) {
        //天窗玻璃状态
        String sunroofWindowStatusStr = vehStatusMap.get(VehStatusEnum.SUNROOF_WINDOW_STATUS.getColumnName());
        if(StringUtils.isNotBlank(sunroofWindowStatusStr)) {
            Integer sunroofWindowStatus = Integer.parseInt(sunroofWindowStatusStr);
            if(SunroofStatusEnum.CLOSE.getValue().equals(sunroofWindowStatus)) {
                vehStatusMap.put(VehStatusEnum.SUNROOF_WINDOW_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.SUNROOF_WINDOW_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
        //天窗遮阳板状态
        String sunroofShadeStatusStr = vehStatusMap.get(VehStatusEnum.SUNROOF_SHADE_STATUS.getColumnName());
        if(StringUtils.isNotBlank(sunroofShadeStatusStr)) {
            Integer sunroofShadeStatus = Integer.parseInt(sunroofShadeStatusStr);
            if(SunroofStatusEnum.CLOSE.getValue().equals(sunroofShadeStatus)) {
                vehStatusMap.put(VehStatusEnum.SUNROOF_SHADE_STATUS.getColumnName(), Constant.CLOSE_OR_LOCK.toString());
            } else {
                vehStatusMap.put(VehStatusEnum.SUNROOF_SHADE_STATUS.getColumnName(), Constant.OPEN_OR_UNLOCK.toString());
            }
        }
    }
}