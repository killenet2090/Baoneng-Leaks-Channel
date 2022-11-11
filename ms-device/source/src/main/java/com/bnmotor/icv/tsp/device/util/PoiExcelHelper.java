package com.bnmotor.icv.tsp.device.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import com.bnmotor.icv.tsp.device.common.excel.CascadeRelation;
import com.bnmotor.icv.tsp.device.common.excel.SheetData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: PoiExcelHelper
 * @Description: 基于poi实现excel导出功能
 * @author: zhangwei2
 * @date: 2020/11/5
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class PoiExcelHelper {
    public static void exportExcelTemplate(OutputStream outputStream, SheetData... sheets) throws Exception {
        XSSFWorkbook book = new XSSFWorkbook();
        try {
            for (SheetData sheet : sheets) {
                XSSFSheet sheetPro = writeHeader(book, sheet);
                XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetPro);

                // 如果没有数据级联限制，则直接输出
                if (!sheet.isCascade()) {
                    continue;
                }

                // 写入隐藏数据,用于作为名字管理器获取的内容
                List<CascadeRelation> cascadeRelations = sheet.getRelations();
                for (CascadeRelation cascade : cascadeRelations) {
                    writeHideData(book, cascade);
                    List<String> roots = cascade.getRoots();
                    DataValidationConstraint dataConstraint = dvHelper.createExplicitListConstraint(roots.toArray(new String[0]));
                    CellRangeAddressList dataRangeAddressList = new CellRangeAddressList(1, cascade.getTotalRow(), cascade.getFirstColumn(), cascade.getFirstColumn());
                    DataValidation dataValidation = dvHelper.createValidation(dataConstraint, dataRangeAddressList);
                    dataValidation.createErrorBox("error", "请使用下拉方式选择合适的值");
                    dataValidation.setShowErrorBox(true);
                    dataValidation.setSuppressDropDownArrow(true);
                    sheetPro.addValidationData(dataValidation);

                    for (int i = 2; i < cascade.getTotalRow(); i++) {
                        for (int j = 0; j < cascade.getTotalLevel() - 1; j++) {
                            char offset = (char) (cascade.getOffset() + j);
                            int offsetColumn = cascade.getOffsetColumn() + j;
                            setDataValidation(String.valueOf(offset), sheetPro, i, offsetColumn);
                        }
                    }
                }
            }

            book.write(outputStream);
        } catch (Exception e) {
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        } finally {
            book.close();
        }
    }

    /**
     * 写excel header
     */
    private static XSSFSheet writeHeader(XSSFWorkbook book, SheetData sheet) {
        XSSFSheet sheetPro = book.createSheet(sheet.getSheetName());
        Row row0 = sheetPro.createRow(0);
        int headerNum = 0;
        for (String head : sheet.getHeaders()) {
            sheetPro.setColumnWidth(headerNum, head.length() * 900);
            Cell cell = row0.createCell(headerNum);
            cell.setCellValue(head);
            cell.setCellStyle(getCellStyle(book));
            headerNum++;
        }

        return sheetPro;
    }

    /**
     * 写入隐藏数据,用户设置名字管理器读取数据范围
     */
    private static void writeHideData(XSSFWorkbook book, CascadeRelation cascade) {
        List<String> roots = cascade.getRoots();
        String hideSheetName = cascade.getHideSheetName() + 1;
        Sheet hideSheet = book.createSheet(hideSheetName);
        book.setSheetHidden(book.getSheetIndex(hideSheet), true);
        int rowId = 0;
        Row rootRow = hideSheet.createRow(rowId++);
        rootRow.createCell(0).setCellValue("test");
        for (int i = 0; i < roots.size(); i++) {
            Cell rootCell = rootRow.createCell(i + 1);
            rootCell.setCellValue(roots.get(i));
        }

        List<String> allLevel = cascade.getNames();
        Map<String, List<String>> relation = cascade.getDataRelation();
        for (String key : allLevel) {
            List<String> son = relation.get(key);
            if (son == null) {
                continue;
            }
            Row row = hideSheet.createRow(rowId++);
            row.createCell(0).setCellValue(key);
            for (int j = 0; j < son.size(); j++) {
                Cell cell = row.createCell(j + 1);
                cell.setCellValue(son.get(j));
            }

            String range = getRange(1, rowId, son.size());
            Name name = book.createName();
            name.setNameName(key);
            String formula = hideSheetName + "!" + range;
            name.setRefersToFormula(formula);
        }
    }

    /**
     * 设置单元格样式
     *
     * @param wb 工作薄
     * @return 样式
     */
    private static XSSFCellStyle getCellStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setBorderTop(BorderStyle.THIN); //上边框
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中 、居左Left、居右RIGHT
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中、居上TOP、居下button
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 14);//设置字号
        font.setBold(true);//设置是否加粗
        style.setFont(font);
        return style;
    }

    /**
     * 设置有效性
     *
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param rowNum 行数
     * @param colNum 列数
     */
    public static void setDataValidation(String offset, XSSFSheet sheet, int rowNum, int colNum) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidation data_validation_list = getDataValidationByFormula("INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, dvHelper);
        sheet.addValidationData(data_validation_list);
    }

    /**
     * 加载下拉列表内容
     */
    private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex, XSSFDataValidationHelper dvHelper) {
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(formulaString);
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation data_validation_list = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        data_validation_list.setSuppressDropDownArrow(true);
        data_validation_list.setShowErrorBox(true);
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        return data_validation_list;
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix;
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

}
