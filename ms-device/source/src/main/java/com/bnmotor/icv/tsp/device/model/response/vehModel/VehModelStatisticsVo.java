package com.bnmotor.icv.tsp.device.model.response.vehModel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @ClassName: VehModelStatistics
 * @Description: 车型统计信息
 * @author: zhangwei2
 * @date: 2020/7/12
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class VehModelStatisticsVo {
    @ExcelProperty(value = "型号id", index = 0)
    @ColumnWidth(15)
    private String id;

    @ExcelProperty(value = "品牌", index = 1)
    @ColumnWidth(25)
    private String brandName;

    @ExcelProperty(value = "车系", index = 2)
    @ColumnWidth(25)
    private String vehSeriesName;


    @ExcelProperty(value = "车型", index = 3)
    @ColumnWidth(25)
    private String modelName;

    @ExcelProperty(value = "型号编码", index = 4)
    @ColumnWidth(25)
    private String modelCode;

    @ExcelProperty(value = "型号数量", index = 5)
    @ColumnWidth(15)
    private Integer modelNum;
}
