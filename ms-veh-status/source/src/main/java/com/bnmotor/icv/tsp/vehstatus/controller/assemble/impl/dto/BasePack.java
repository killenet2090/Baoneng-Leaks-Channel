package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.dto;


import com.bnmotor.icv.tsp.vehstatus.common.enums.VehStatusEnum;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnPack;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: BasePack
 * @Description: 列封装基础实现类
 * @author: huangyun1
 * @date: 2020/7/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class BasePack implements ColumnPack {

    @Override
    public Set<String> packColumn(Set<String> columnNames, Set<String> groupNames) {
        columnNames.add(VehStatusEnum.TIMESTAMP.getColumnName());
        return columnNames;
    }

    @Override
    public void setVehStatusGroupMap(Map<String, List<String>> vehStatusGroupMap) {
    }
}
