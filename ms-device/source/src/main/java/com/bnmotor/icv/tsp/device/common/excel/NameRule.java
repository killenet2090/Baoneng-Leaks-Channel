package com.bnmotor.icv.tsp.device.common.excel;

import lombok.Data;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.CellReference;
import org.springframework.util.StringUtils;

/**
 * @ClassName: NameDataItem
 * @Description: 名字字典数据
 * @author: zhangwei2
 * @date: 2020/12/1
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Data
public class NameRule {
    /**
     * 名字
     */
    private String name;

    /**
     * 反转义名称
     *
     * @param name 转义后的字符串
     * @return 转义前的字符串
     */
    public static String backEscapeName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        int index = name.lastIndexOf("__");
        return index != -1 ? name.substring(index + 2) : name;
    }


    /**
     * 转义名字，转换为符合名称空间的命名规则
     *
     * @param name 名称
     * @return 转转义后的字符串
     */
    public static String escapeName(String name) {
        try {
            validateName(name);
            return name;
        } catch (Exception e) {
            return "__" + name;
        }
    }

    private static void validateName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Invalid name: '" + name + "': cannot exceed 255 characters in length");
        }
        if (name.equalsIgnoreCase("R") || name.equalsIgnoreCase("C")) {
            throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be special shorthand R or C");
        }

        char c = name.charAt(0);
        String allowedSymbols = "_\\";
        boolean characterIsValid = (Character.isLetter(c) || allowedSymbols.indexOf(c) != -1);
        if (!characterIsValid) {
            throw new IllegalArgumentException("Invalid name: '" + name + "': first character must be underscore or a letter");
        }

        allowedSymbols = "_.\\";
        for (final char ch : name.toCharArray()) {
            characterIsValid = (Character.isLetterOrDigit(ch) || allowedSymbols.indexOf(ch) != -1);
            if (!characterIsValid) {
                throw new IllegalArgumentException("Invalid name: '" + name + "': name must be letter, digit, period, or underscore");
            }
        }

        if (name.matches("[A-Za-z]+\\d+")) {
            String col = name.replaceAll("\\d", "");
            String row = name.replaceAll("[A-Za-z]", "");
            if (CellReference.cellReferenceIsWithinRange(col, row, SpreadsheetVersion.EXCEL97)) {
                throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be $A$1-style cell reference");
            }
        }

        if (name.matches("[Rr]\\d+[Cc]\\d+")) {
            throw new IllegalArgumentException("Invalid name: '" + name + "': cannot be R1C1-style cell reference");
        }
    }
}
