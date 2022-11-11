package com.bnmotor.icv.tsp.device.model.response.vehImport;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @ClassName: VehicleExcelDto
 * @Description: 车辆excel信息
 * @author: zhangwei2
 * @date: 2020/7/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ExportFailedVo {
    @ExcelProperty(value = {"车机号"}, index = 0)
    @ColumnWidth(value = 20)
    private String vin;

    @ExcelProperty(value = {"失败原因"}, index = 1)
    @ColumnWidth(value = 20)
    private String reason;
}
