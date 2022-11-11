package com.bnmotor.icv.tsp.ota.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bnmotor.icv.tsp.ota.model.req.v2.ExcelDataVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ExcelReader.java
 * @Description: Excel上传车辆并关联任务 读取Excel文件内容的工具类
 * @author E.YanLonG
 * @since 2020-12-29 9:42:03
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
public class ExcelReader {

	private static final String XLS = "xls";
	private static final String XLSX = "xlsx";

	public static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
		Workbook workbook = null;
		if (fileType.equalsIgnoreCase(XLS)) {
			workbook = new HSSFWorkbook(inputStream);
		} else if (fileType.equalsIgnoreCase(XLSX)) {
			workbook = new XSSFWorkbook(inputStream);
		}
		return workbook;
	}

//	public static void main(String[] args) throws IOException {
//		byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\eyanlong2\\Desktop\\vehicle.xlsx"));
//		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//		String vin = "LLNC6ADB5JA047661";
//		boolean flag = isVehicleVin(vin);
//		System.err.println(flag);
//	}

	public static List<ExcelDataVo> resolve(InputStream inputstream, String filetype) throws IOException {
		Workbook workbook = getWorkbook(inputstream, filetype);
		return parseExcel(workbook);
	}

	public static List<ExcelDataVo> readExcel(String fileName) {

		Workbook workbook = null;
		FileInputStream inputStream = null;

		try {
			// 获取Excel后缀名
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			// 获取Excel文件
			File excelFile = new File(fileName);

			// 获取Excel工作簿
			inputStream = new FileInputStream(excelFile);
			workbook = getWorkbook(inputStream, fileType);

			// 读取excel中的数据
			List<ExcelDataVo> resultDataList = parseExcel(workbook);

			return resultDataList;
		} catch (Exception e) {
			log.warn("解析Excel失败，文件名：" + fileName + " 错误信息：" + e.getMessage());
			return null;
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(inputStream);
		}
	}

	/**
	 * 解析Excel数据
	 * 
	 * @param workbook Excel工作簿对象
	 * @return 解析结果
	 */
	private static List<ExcelDataVo> parseExcel(Workbook workbook) {
		List<ExcelDataVo> resultDataList = new ArrayList<>();
		// 解析sheet
		for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
			Sheet sheet = workbook.getSheetAt(sheetNum);
			int i = 0;
			for (Row row : sheet) {
				if (null == row || ++i == 1) { // 过滤首行
					continue;
				}
				ExcelDataVo resultData = convertRowToData(row);
				if (null == resultData) {
					log.warn("第 " + row.getRowNum() + "行数据不合法，已忽略！");
					continue;
				}
				resultDataList.add(resultData);
			}
		}

		return resultDataList;
	}

	/**
	 * 将单元格内容转换为字符串
	 * 
	 * @param cell
	 * @return
	 */
	private static String convertCellValueToString(Cell cell) {
		if (cell == null) {
			return null;
		}
		String returnValue = null;
		switch (cell.getCellType()) {
		case NUMERIC: // 数字
			Double doubleValue = cell.getNumericCellValue();

			// 格式化科学计数法，取一位整数
			DecimalFormat df = new DecimalFormat("0");
			returnValue = df.format(doubleValue);
			break;
		case STRING: // 字符串
			returnValue = cell.getStringCellValue();
			break;
		case BOOLEAN: // 布尔
			Boolean booleanValue = cell.getBooleanCellValue();
			returnValue = booleanValue.toString();
			break;
		case BLANK: // 空值
			break;
		case FORMULA: // 公式
			returnValue = cell.getCellFormula();
			break;
		case ERROR: // 故障
			break;
		default:
			break;
		}
		return returnValue;
	}

	/**
	 * 提取每一行中需要的数据，构造成为一个结果数据对象
	 *
	 * 当该行中有单元格的数据为空或不合法时，忽略该行的数据
	 *
	 * @param row 行数据
	 * @return 解析后的行数据对象，行数据错误时返回null
	 */
	private static ExcelDataVo convertRowToData(Row row) {
		ExcelDataVo resultData = new ExcelDataVo();

		Cell cell = null;
		int cellNum = 0;
		// 获取vin
		cell = row.getCell(cellNum++);
		String vin = convertCellValueToString(cell);
		resultData.setVin(vin);
		// 获取objectId
		cell = row.getCell(cellNum++);
		String objectId = convertCellValueToString(cell);

		resultData.setObjectId(NumberUtils.toLong(objectId));
		return resultData;
	}
}
