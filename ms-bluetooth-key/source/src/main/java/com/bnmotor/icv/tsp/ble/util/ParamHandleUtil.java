package com.bnmotor.icv.tsp.ble.util;

import com.bnmotor.icv.tsp.ble.common.constant.Constant;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 参数处理工具
 */
public final class ParamHandleUtil {

    private static final Pattern EMAIL_FIRST_SEGMENT_PATTERN = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._-]*");
    private static final Pattern EMAIL_SECOND_SEGMENT_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}(/([1-9]|1\\d|2\\d|3[0-2]))?$");
    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|" +
                    "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|" +
                    "(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                    "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|" +
                    "((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|" +
                    "(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                    "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|" +
                    "2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|" +
                    "((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|" +
                    "((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?"
    );

    private static final Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");

    private static final Pattern AUTH_CODE = Pattern.compile("^\\d{6}$");

    /**
     * 检查字符串内容是否为数字
     */
    public static boolean checkNumber(String str, int count) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        if (count > 0 && str.length() != count) {
            return false;
        }
        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 检查邮箱是否正确
     */
    public static boolean checkEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        String[] array = email.split("@");
        if (array.length != 2 || StringUtils.isEmpty(array[0]) || StringUtils.isEmpty(array[1])) {
            return false;
        }
        boolean preMatch = EMAIL_FIRST_SEGMENT_PATTERN.matcher(array[0]).matches();
        if (!preMatch) {
            return false;
        }
        String[] subArray = array[1].split("\\.");
        if (subArray.length < 2) {
            return false;
        }
        for (String subStr : subArray) {
            if (StringUtils.isEmpty(subStr)) {
                return false;
            }
            if (!EMAIL_SECOND_SEGMENT_PATTERN.matcher(subStr).matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查手机号是否正确
     */
    public static boolean checkPhone(String mobileNumber) {
        if (StringUtils.isEmpty(mobileNumber)) {
            return false;
        }
        Matcher matcher = Constant.PHONE_PATTERN.matcher(mobileNumber);
        return matcher.matches();
    }

    public static Boolean isPhoneRegexp(String mobileNumber)
    {
        String regexp = "";
        String mobilePhoneRegexp = "(?:(\\(\\+?86\\))((1[0-9]{1}[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|" +
                "(?:86-?((1[0-9]{1}[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|" +
                "(?:((1[0-9]{1}[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})";
        String landlinePhoneRegexp = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
                "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
        regexp += "(?:" + mobilePhoneRegexp + "|" + landlinePhoneRegexp +")";

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(mobileNumber);

        return matcher.matches();
    }
    public static boolean securityVerify(String pwd) {
        return pwd.length() == 32 && pwd.matches("[a-f0-9]+");
    }

    /**
     * 检查ip是否正确
     */
    public static boolean checkIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        Matcher matcher = IPV4_PATTERN.matcher(ip);
        if (matcher.matches() && checkoutIpNetmask(ip)) {
            return true;
        }
        Matcher matcher_ipv6 = IPV6_PATTERN.matcher(ip);
        return matcher_ipv6.matches();
    }

    private static boolean checkoutIpNetmask(String ip) {
        int index = ip.lastIndexOf('/');
        if (index == -1) {
            return true;
        }

        String realIp = ip.substring(0, index);

        int netNum = Integer.parseInt(ip.substring(index + 1));
        String[] ips = realIp.split("\\.");
        int temp = 0;
        int i;
        for (i = 3; i >= 0; i--) {
            temp = Integer.parseInt(ips[i]);
            if (temp != 0) {
                break;
            }
        }

        int j;
        for (j = 7; j >= 0; j--) {
            int current = 1 << j;
            if (temp - current == 0) {
                break;
            }
            if (temp - current > 0) {
                temp -= current;
            }
        }

        int current = i * 8 + 8 - j;

        return netNum >= current && netNum <= 32;
    }

    /**
     * 检查是否为IPv4
     */
    public static boolean checkIPv4(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        Matcher matcher = IPV4_PATTERN.matcher(ip);
        return matcher.matches();
    }

    /**
     * 验证码是否正确
     */
    public static boolean checkAuthCode(String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        Matcher matcher = AUTH_CODE.matcher(authCode);
        return matcher.matches();
    }

    /**
     * 检查传入内容是数字、字母集
     */
    public static boolean charAndNumberVerify(String data, int length) {
        return data.length() == length && data.matches("[a-z0-9A-Z]+");
    }
}
