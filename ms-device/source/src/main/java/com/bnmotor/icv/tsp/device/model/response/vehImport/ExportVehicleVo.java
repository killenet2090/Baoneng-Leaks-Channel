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
public class ExportVehicleVo {
    @ExcelProperty(value = {"车机号"}, index = 0)
    @ColumnWidth(value = 20)
    private String vin;

    @ExcelProperty(value = {"发动机号"}, index = 1)
    @ColumnWidth(value = 20)
    private String engineNo;

    @ExcelProperty(value = {"型号"}, index = 2)
    @ColumnWidth(value = 20)
    private String vehModelName;

    @ExcelProperty(value = {"年款"}, index = 3)
    @ColumnWidth(value = 15)
    private String yearStyleName;

    @ExcelProperty(value = {"配置"}, index = 4)
    @ColumnWidth(value = 20)
    private String configName;

    @ExcelProperty(value = {"颜色"}, index = 5)
    @ColumnWidth(value = 10)
    private String colorName;

    @ExcelProperty(value = {"批次号"}, index = 6)
    @ColumnWidth(value = 10)
    private String batchNo;

    @ExcelProperty(value = {"生产商"}, index = 7)
    @ColumnWidth(value = 20)
    private String manufacturer;

    @ExcelProperty(value = {"生产日期"}, index = 8)
    @ColumnWidth(value = 20)
    private String productTime;

    @ExcelProperty(value = {"下线日期"}, index = 9)
    @ColumnWidth(value = 20)
    private String downlineDate;

    @ExcelProperty(value = {"合格证日期"}, index = 10)
    @ColumnWidth(value = 20)
    private String certificateDate;

    @ExcelProperty(value = {"出厂日期"}, index = 11)
    @ColumnWidth(value = 20)
    private String outFactoryTime;
}
