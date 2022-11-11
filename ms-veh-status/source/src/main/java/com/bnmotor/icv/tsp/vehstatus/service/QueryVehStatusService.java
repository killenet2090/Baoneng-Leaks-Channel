package com.bnmotor.icv.tsp.vehstatus.service;

import java.util.Map;
import java.util.Set;

/**
 * @ClassName: QueryVehStatusService
 * @Description: 查询车况service
 * @author: huangyun1
 * @date: 2020/5/14
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public interface QueryVehStatusService {


    /**
     * 根据映射名称查询车况map
     * @param vin
     * @param mappingColumnNames
     * @return
     */
    Map<String, Object> queryVehStatusMap(String vin, String[] mappingColumnNames);

    /**
     * 根据映射名称查询车况bean
     * @param vin
     * @param mappingColumnNames
     * @return
     */
    Map<String, String> queryVehStatusBean(String vin, Set<String> mappingColumnNames);
}
