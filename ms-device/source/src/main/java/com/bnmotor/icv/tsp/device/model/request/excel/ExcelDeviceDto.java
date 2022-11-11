package com.bnmotor.icv.tsp.device.model.request.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.bnmotor.icv.tsp.device.common.excel.NameRule;
import com.bnmotor.icv.tsp.device.common.excel.StringToDateConvert;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @ClassName: ExcelDeviceRow
 * @Description: excel导入设备行记录
 * @author: zhangwei2
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Valid
@Data
public class ExcelDeviceDto {
    @NotBlank(message = "车架号不能为空")
    @Length(max = 30, message = "车架号长度非法")
    @ExcelProperty(value = {"vin", "车架号"}, index = 0)
    private String vin;

    @NotBlank(message = "生产序列号不能为空")
    @Length(max = 30, message = "生产序列号长度非法")
    @ExcelProperty(value = {"productSn", "生产序列号"}, index = 1)
    private String productSn;

    @NotBlank(message = "设备类型不能为空")
    @Length(max = 30, message = "设备类型长度非法")
    @ExcelProperty(value = {"deviceType", "设备类型"}, index = 2)
    private String deviceType;

    @NotBlank(message = "零件号不能为空")
    @Length(max = 30, message = "零件号长度非法")
    @ExcelProperty(value = {"deviceModel", "零件号"}, index = 3)
    @ColumnWidth(value = 15)
    private String deviceModel;

    @NotBlank(message = "硬件版本号不能为空")
    @Length(max = 50, message = "硬件版本号长度非法")
    @ExcelProperty(value = {"hardwareVersion", "硬件版本号"}, index = 4)
    private String hardwareVersion;

    @NotBlank(message = "软件版本号不能为空")
    @Length(max = 50, message = "软件版本号长度非法")
    @ExcelProperty(value = {"softwareVersion", "软件版本号"}, index = 5)
    private String softwareVersion;

    @Length(max = 50, message = "供应商硬件版本号长度非法")
    @ExcelProperty(value = {"supplierHardwareVersion", "供应商硬件版本号"}, index = 6)
    private String supplierHardwareVersion;

    @Length(max = 50, message = "供应商软件版本号长度非法")
    @ExcelProperty(value = {"supplierSoftwareVersion", "供应商软件版本号"}, index = 7)
    private String supplierSoftwareVersion;

    @NotBlank(message = "boot版本号不能为空")
    @Length(max = 30, message = "boot版本号长度非法")
    @ExcelProperty(value = {"bootVersion", "boot版本号"}, index = 8)
    private String bootVersion;

    @ExcelProperty(value = {"productTime", "生产时间"}, index = 9, converter = StringToDateConvert.class)
    private LocalDateTime productTime;

    @Length(max = 50, message = "批次号长度非法")
    @ExcelProperty(value = {"batchNo", "批次号"}, index = 10)
    private String batchNo;

    @Length(max = 20, message = "iccid长度非法")
    @ExcelProperty(value = {"iccid", "iccid"}, index = 11)
    private String iccid;

    /**
     * 反转义，由于可能存在excel名称空间限制,所以需要做一下转换
     */
    public void backEscapeName() {
        deviceType = NameRule.backEscapeName(deviceType);
        deviceModel = NameRule.backEscapeName(deviceModel);
    }
}
