package com.bnmotor.icv.tsp.device.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: ExcelHelper
 * @Description: excel处理类
 * @author: zhangwei2
 * @date: 2020/7/29
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@Data
public class ExcelProcessor {
    /**
     * 流的方式处理excel数据
     */
    public static <T> void read(MultipartFile file, AbstractSaxReadListener<T> listener, Integer sheetNo, String sheetName) {
        Assert.isTrue(file != null, "上传文件不能为空");
        Assert.isTrue(listener != null, "监听器不能为空");
        Assert.isTrue(sheetNo != null, "sheetNo不能为空");
        Assert.isTrue(sheetName != null, "sheetName不能为空");

        InputStream is = checkFile(file);
        ReadSheet readSheet = new ReadSheet(sheetNo, sheetName);
        Type type = ((ParameterizedType) listener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        readSheet.setClazz((Class) type);
        ExcelReader excelReader = EasyExcelFactory.read(is, listener).build();
        excelReader.read(readSheet);
    }

    /**
     * excel导出
     */
    public static <T> void write(HttpServletResponse response, List<T> datas, String fileName, Integer sheetNo, String sheetName) {
        if (CollectionUtils.isEmpty(datas)) {
            return;
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String temp = URLEncoder.encode(fileName + sdf.format(new Date()), "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + temp + ".xlsx");
            EasyExcel.write(response.getOutputStream(), datas.get(0).getClass())
                    .autoCloseStream(Boolean.FALSE)
                    .sheet(sheetNo, sheetName)
                    .doWrite(datas);
        } catch (Exception e) {
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 读取多sheet数据
     */
    public static void readSheets(MultipartFile file, ReadSheet... readSheets) {
        try {
            ExcelReader excelReader = EasyExcel.read(file.getInputStream()).build();
            excelReader.read(readSheets);
            excelReader.finish();
        } catch (ExcelAnalysisException e) {
            String message = e.getMessage();
            if (!StringUtils.isEmpty(message) && message.contains("LocalDateTime")) {
                throw new AdamException(RespCode.USER_INVALID_INPUT, "非法的日期格式");
            } else {
                throw new AdamException(RespCode.USER_INVALID_INPUT, e.getMessage());
            }
        } catch (Exception e) {
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 获取readSheet
     */
    public static <T> ReadSheet readSheet(String sheetName, Class type, AbstractSaxReadListener<T> listener) {
        listener.setSheetName(sheetName);
        return EasyExcel.readSheet(sheetName).head(type).registerReadListener(listener).build();
    }

    /**
     * 校验excel文件
     *
     * @param uploadFile 上传文件
     * @return 流
     */
    private static InputStream checkFile(MultipartFile uploadFile) {
        try {
            String filename = uploadFile.getOriginalFilename();
            if (filename != null && (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx"))) {
                return new BufferedInputStream(uploadFile.getInputStream());
            } else {
                throw new AdamException(RespCode.USER_INVALID_INPUT);
            }
        } catch (Exception e) {
            log.error("import excel exception:{}", e);
            throw new AdamException(RespCode.UNKNOWN_ERROR);
        }
    }
}
