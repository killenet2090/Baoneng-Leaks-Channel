package com.bnmotor.icv.tsp.vehstatus.controller.assemble.impl.dto;


import com.bnmotor.icv.tsp.vehstatus.controller.assemble.ColumnPack;
import com.bnmotor.icv.tsp.vehstatus.controller.assemble.PriorityOrder;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: GroupPack
 * @Description: 对组进行封装类
 * @author: huangyun1
 * @date: 2020/7/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class GroupPack implements ColumnPack, PriorityOrder {
    private final ColumnPack delegate;
    private Map<String, List<String>> vehStatusGroupMap;

    public GroupPack(ColumnPack delegate) {
        this.delegate = delegate;
    }

    @Override
    public Set<String> packColumn(Set<String> columnNames, Set<String> groupNames) {
        if(groupNames != null && !groupNames.isEmpty()) {
            if(vehStatusGroupMap != null) {
                Set<String> groupColumnNames = new LinkedHashSet<>();
                groupNames.forEach(groupName -> {
                    List<String> groupMapList = vehStatusGroupMap.get(groupName);
                    if(groupMapList != null && !groupMapList.isEmpty()) {
                        groupColumnNames.addAll(groupMapList);
                    }
                });
                columnNames.addAll(groupColumnNames);
            }
        }
        return delegate.packColumn(columnNames, groupNames);
    }

    @Override
    public void setVehStatusGroupMap(Map<String, List<String>> vehStatusGroupMap) {
        this.vehStatusGroupMap = vehStatusGroupMap;
    }
}
