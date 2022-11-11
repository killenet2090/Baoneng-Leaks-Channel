package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.dto;

import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnPack;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: WindowStatusPack
 * @Description: 车窗状态封装
 * @author: huangyun1
 * @date: 2020/7/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class WindowStatusPack implements ColumnPack {
    private final ColumnPack delegate;

    public WindowStatusPack(ColumnPack delegate) {
        this.delegate = delegate;
    }

    /**
     * 对列名封装
     * @param columnNames
     * @return
     */
    @Override
    public Set<String> packColumn(Set<String> columnNames, Set<String> groupNames) {
        if(columnNames != null && columnNames.contains(VehStatusEnum.WINDOE_STATUS.getColumnName())) {
            if(!columnNames.contains(VehStatusEnum.FRONT_LEFT_WINDOW_STATUS.getColumnName())) {
                columnNames.add(VehStatusEnum.FRONT_LEFT_WINDOW_STATUS.getColumnName());
            }
            if(!columnNames.contains(VehStatusEnum.FRONT_RIGHT_WINDOW_STATUS.getColumnName())) {
                columnNames.add(VehStatusEnum.FRONT_RIGHT_WINDOW_STATUS.getColumnName());
            }
            if(!columnNames.contains(VehStatusEnum.REAR_LEFT_WINDOW_STATUS.getColumnName())) {
                columnNames.add(VehStatusEnum.REAR_LEFT_WINDOW_STATUS.getColumnName());
            }
            if(!columnNames.contains(VehStatusEnum.REAR_RIGHT_WINDOW_STATUS.getColumnName())) {
                columnNames.add(VehStatusEnum.REAR_RIGHT_WINDOW_STATUS.getColumnName());
            }
            if(!columnNames.contains(VehStatusEnum.SUNROOF_WINDOW_STATUS.getColumnName())) {
                columnNames.add(VehStatusEnum.SUNROOF_WINDOW_STATUS.getColumnName());
            }
        }
        return delegate.packColumn(columnNames, groupNames);
    }

    @Override
    public void setVehStatusGroupMap(Map<String, List<String>> vehStatusGroupMap) {

    }
}
