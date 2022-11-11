package com.bnmotor.icv.tsp.device.common.excel;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: Sheet
 * @Description: sheet对象封装
 * @author: zhangwei2
 * @date: 2020/9/24
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class Sheet<T> {
    /**
     * sheetNo
     */
    private Integer sheetNo;

    /**
     * sheet名称
     */
    private String sheetName;

    /**
     * 数据类型
     */
    private T type;

    /**
     * 数据
     */
    private List<T> data;
}
