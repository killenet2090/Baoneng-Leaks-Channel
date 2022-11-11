package com.bnmotor.icv.tsp.device.model.request.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.bnmotor.icv.tsp.device.common.excel.NameRule;
import com.bnmotor.icv.tsp.device.common.excel.StringToDateConvert;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @ClassName: VehicleExcelDto
 * @Description: 车辆excel信息
 * @author: zhangwei2
 * @date: 2020/7/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
@Valid
public class ExcelVehicleDto {
    @NotBlank(message = "车架号不能为空")
    @Length(max = 30, message = "车架号长度非法")
    @ExcelProperty(value = {"vin", "车机号"}, index = 0)
    private String vin;

    @Length(max = 30, message = "发动机号长度非法")
    @ExcelProperty(value = {"engineNo", "发动机号"}, index = 1)
    private String engineNo;

    @NotBlank(message = "型号不能为空")
    @Length(max = 50, message = "型号长度非法")
    @ExcelProperty(value = {"vehModelName", "型号"}, index = 2)
    private String vehModelName;

    @NotBlank(message = "年款不能为空")
    @Length(max = 50, message = "年款长度非法")
    @ExcelProperty(value = {"yearStyleName", "年款"}, index = 3)
    private String yearStyleName;

    @NotBlank(message = "配置不能为空")
    @Length(max = 50, message = "配置长度非法")
    @ExcelProperty(value = {"configName", "配置"}, index = 4)
    private String configName;

    @Length(max = 20, message = "颜色长度非法")
    @ExcelProperty(value = {"colorName", "颜色"}, index = 5)
    private String colorName;

    @Length(max = 30, message = "批次号长度非法")
    @ExcelProperty(value = {"batchNo", "批次号"}, index = 6)
    private String batchNo;

    @NotBlank(message = "生产商不能为空")
    @Length(max = 50, message = "生产商长度非法")
    @ExcelProperty(value = {"manufacturer", "生产商"}, index = 7)
    private String manufacturer;

    @NotNull(message = "生产日期不能为空")
    @ExcelProperty(value = {"productTime", "生产日期"}, index = 8, converter = StringToDateConvert.class)
    private LocalDateTime productTime;

    @NotNull(message = "下线日期不能为空")
    @ExcelProperty(value = {"downlineDate", "下线日期"}, index = 9, converter = StringToDateConvert.class)
    private LocalDateTime downlineDate;

    @ExcelProperty(value = {"certificateDate", "合格证日期"}, index = 10, converter = StringToDateConvert.class)
    private LocalDateTime certificateDate;

    @ExcelProperty(value = {"outFactoryTime", "出厂日期"}, index = 11, converter = StringToDateConvert.class)
    private LocalDateTime outFactoryTime;

    /**
     * 反转义，由于可能存在excel名称空间限制,所以需要做一下转换
     */
    public void backEscapeName() {
        vehModelName = NameRule.backEscapeName(vehModelName);
        yearStyleName = NameRule.backEscapeName(yearStyleName);
        configName = NameRule.backEscapeName(configName);
        colorName = NameRule.backEscapeName(colorName);
    }
}
