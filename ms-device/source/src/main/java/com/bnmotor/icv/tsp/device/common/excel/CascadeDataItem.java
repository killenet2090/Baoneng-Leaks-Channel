package com.bnmotor.icv.tsp.device.common.excel;

import lombok.Data;

/**
 * @ClassName: DataItem
 * @Description: 级联数据
 * @author: zhangwei2
 * @date: 2020/12/9
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class CascadeDataItem {
    private String parent;
    private String uniqKey;
    private String display;

    /**
     * 当前item显示值,可能会重复，因此需要通过uniqKey+"__"+display进行唯一标识
     */
    public String getName(boolean needProcess) {
        if (needProcess) {
            return uniqKey + "__" + display;
        }
        return display;
    }
}
