package com.bnmotor.icv.tsp.vehstatus.controller.assemble;


import java.util.Map;

/**
 * @ClassName: ColumnVo
 * @Description: 字段返回对象处理
 * @author: huangyun1
 * @date: 2020/11/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface ColumnHandler {


    /**
     * 字段返回对象处理
     * @param vehStatusMap
     */
    void handleVo(Map<String, String> vehStatusMap);
}