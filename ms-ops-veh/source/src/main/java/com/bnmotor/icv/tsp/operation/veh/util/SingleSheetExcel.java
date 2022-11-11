package com.bnmotor.icv.tsp.operation.veh.util;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName: SingleSheetExcel
 * @Description: excel工具类,仅支持格式xlsx
 * @author: wuhao1
 * @data: 2020-07-20
 * @Copyrigght: 2020 www.baoneng.com Inc. All rights reserved.注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class SingleSheetExcel
{

    private static final int HEAD_ROW = 0;

    @Getter
    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public SingleSheetExcel(String sheetName) {
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet(sheetName);
    }

    /**
     * 添加表头
     * @param colNames
     */
    public void addHeadRow(String[] colNames)
    {
        addRow(colNames, HEAD_ROW);
    }

    /**
     * 添加非表头
     * @param valueCells
     * @param rowNum
     */
    public void addValueRow(String[] valueCells, int rowNum)
    {
        addRow(valueCells, rowNum);
    }

    private void addRow(String[] valueCells, int rowNum) {
        XSSFRow row = sheet.createRow(rowNum);
        for (int i = 0; i < valueCells.length; i++)
        {
            XSSFCell cell = row.createCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(valueCells[i]);
        }
    }

    public void writeToOutstream(OutputStream outputStream) throws IOException
    {
        workbook.write(outputStream);
    }
}
