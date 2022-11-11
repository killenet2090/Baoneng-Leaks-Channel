package com.bnmotor.icv.tsp.device.common.excel;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: CascadeData
 * @Description: 级联数据
 * @author: zhangwei2
 * @date: 2020/12/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class CascadeData {
    /**
     * 处理父类
     */
    private boolean processParent;
    /**
     * 处理子类
     */
    private boolean processSelf;

    public CascadeData(boolean processParent, boolean processSelf) {
        this.processParent = processParent;
        this.processSelf = processSelf;
    }

    /**
     * 数据条目
     */
    private List<CascadeDataItem> cascadeDataItems;
}
