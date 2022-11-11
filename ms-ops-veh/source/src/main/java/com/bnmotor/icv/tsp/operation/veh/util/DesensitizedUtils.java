package com.bnmotor.icv.tsp.operation.veh.util;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: DesensitizedUtils
 * @Description: 脱敏工具类
 * @author: huangyun1
 * @date: 2020/8/6
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class DesensitizedUtils {
    /**
     * 填充字符
     */
    private static final String padChar = "*";
    /**
     * 空格字符
     */
    private static final String spaceChar = " ";

    /**
     * 【手机号码】前三位，后四位，其他隐藏，比如135****6810
     *
     * @param num
     * @return
     */
    public static String desensitizedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num) - 3, padChar));
    }

    /**
     * 姓名 脱敏规则: 只显示第一个汉字,比如李某某置换为李**, 李某置换为李*
     * @param fullName
     * @return
     */
    public static String desensitizedName(String fullName) {
        if (!Strings.isNullOrEmpty(fullName)) {
            String regex="^[\u4E00-\u9FA5]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher match = pattern.matcher(fullName);
            boolean zhCNFlag = match.matches();
            if(zhCNFlag) {
                String name = StringUtils.left(fullName, 1);
                return StringUtils.rightPad(name, fullName.length() > 2 ? (fullName.length() - 1) : 2, padChar)
                        .concat(fullName.substring(fullName.length() > 2 ?  (fullName.length() - 1) : fullName.length()));
            }
        }
        return fullName;
    }

    /**
     * 车牌号脱敏 粤B D9***F，粤B F9***F，粤B 9***F
     * @param licensePlate
     * @return
     */
    public static String desensitizedLicensePlate(String licensePlate) {
        if (!Strings.isNullOrEmpty(licensePlate)) {
            //判断是否包含空格
            if(licensePlate.contains(spaceChar)) {
                return StringUtils.left(licensePlate, 5).concat(StringUtils.leftPad(StringUtils.right(licensePlate, 1), 4, padChar));
            } else {
                return StringUtils.left(licensePlate, 4).concat(StringUtils.leftPad(StringUtils.right(licensePlate, 1), 4, padChar));
            }
        }
        return licensePlate;
    }
}
