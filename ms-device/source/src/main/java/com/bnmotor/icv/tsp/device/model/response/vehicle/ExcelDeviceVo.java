package com.bnmotor.icv.tsp.device.model.response.vehicle;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @ClassName: ExcelDeviceRow
 * @Description: excel导入设备行记录
 * @author: zhangwei2
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class ExcelDeviceVo {
    @ExcelProperty(value = {"车架号"}, index = 0)
    @ColumnWidth(value = 20)
    private String vin;

    @ExcelProperty(value = {"零件号"}, index = 1)
    @ColumnWidth(value = 15)
    private String deviceModel;

    @ExcelProperty(value = {"设备id"}, index = 2)
    @ColumnWidth(value = 20)
    private String deviceId;

    @ExcelProperty(value = {"设备类型"}, index = 3)
    @ColumnWidth(value = 15)
    private String deviceType;

    @ExcelProperty(value = {"设备名称"}, index = 4)
    @ColumnWidth(value = 15)
    private String deviceName;

    @ExcelProperty(value = {"硬件版本号"}, index = 5)
    @ColumnWidth(value = 18)
    private String hardwareVersion;

    @ExcelProperty(value = {"软件版本号"}, index = 6)
    @ColumnWidth(value = 18)
    private String softwareVersion;

    @ExcelProperty(value = {"供应商硬件版本号"}, index = 7)
    @ColumnWidth(value = 23)
    private String supplierHardwareVersion;

    @ExcelProperty(value = {"供应商软件版本号"}, index = 8)
    @ColumnWidth(value = 23)
    private String supplierSoftwareVersion;

    @ExcelProperty(value = {"boot版本号"}, index = 9)
    @ColumnWidth(value = 20)
    private String bootVersion;

    @ExcelProperty(value = {"生产序列号"}, index = 10)
    @ColumnWidth(value = 20)
    private String productSn;

    @ExcelProperty(value = {"生产时间"}, index = 11)
    @ColumnWidth(value = 20)
    private String productTime;

    @ExcelProperty(value = {"批次号"}, index = 12)
    @ColumnWidth(value = 10)
    private String batchNo;

    @ExcelProperty(value = {"sim卡号"}, index = 13)
    @ColumnWidth(value = 20)
    private String simPhone;

    @ExcelProperty(value = {"sim卡绑定时间"}, index = 14)
    @ColumnWidth(value = 20)
    private String simBindTime;

    @ExcelProperty(value = {"供应商"}, index = 15)
    @ColumnWidth(value = 20)
    private String supplierName;

    @ExcelProperty(value = {"诊断id"}, index = 16)
    @ColumnWidth(value = 20)
    private String canId;
}
