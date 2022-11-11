package com.bnmotor.icv.tsp.device.common.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @ClassName: StringToDateConvert
 * @Description: 字符串转date
 * @author: zhangwei2
 * @date: 2020/9/25
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class StringToDateConvert implements Converter {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        String cellDate = cellData.getNumberValue().toString();
        if (cellDate == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar(1900, Calendar.JANUARY, 0);
        Date date = DateUtils.addDays(calendar.getTime(), Integer.parseInt(cellDate));
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public CellData convertToExcelData(Object o, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        return null;
    }
}
