package com.bnmotor.icv.tsp.device.common.excel;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * @ClassName: PoiCascadeData
 * @Description: 导出级联excel模板
 * @author: zhangwei2
 * @date: 2020/11/8
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class SheetData {
    /**
     * excel sheet名称
     */
    private String sheetName;
    /**
     * excel 表头
     */
    private List<String> headers;

    /**
     * 级联关系
     */
    private List<CascadeRelation> relations;

    /**
     * 判断是否存在级联数据
     *
     * @return true存在级联数据
     */
    public boolean isCascade() {
        return CollectionUtils.isNotEmpty(relations);
    }
}
