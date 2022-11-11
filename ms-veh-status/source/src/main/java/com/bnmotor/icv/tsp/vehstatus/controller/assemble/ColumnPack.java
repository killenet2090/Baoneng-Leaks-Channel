package com.bnmotor.icv.tsp.vehstatus.controller.assemble;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: ColumnPack
 * @Description: 类包装接口
 * @author: huangyun1
 * @date: 2020/7/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ColumnPack {
    /**
     * 对列名封装
     * @param columnNames
     * @param groupNames
     * @return
     */
    public Set<String> packColumn(Set<String> columnNames, Set<String> groupNames);

    /**
     * 设置分组map
     * @param vehStatusGroupMap
     */
    public void setVehStatusGroupMap(Map<String, List<String>> vehStatusGroupMap);

}
