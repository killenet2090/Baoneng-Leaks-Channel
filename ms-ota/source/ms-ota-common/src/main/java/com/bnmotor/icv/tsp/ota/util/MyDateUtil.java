package com.bnmotor.icv.tsp.ota.util;

import com.bnmotor.icv.tsp.ota.common.enums.OTARespCodeEnum;
import org.assertj.core.util.Lists;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @ClassName: MyDateUtil
 * @Description: 日期工具类
 * @author: xuxiaochang1
 * @date: 2020/7/17 11:23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyDateUtil {
    private MyDateUtil() {
    }

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_WEEK_FORMAT = "yyyy-ww";

    private static final String DATE_MONTH_FORMAT = "yyyy-MM";

    private static final String DAY_TIME_FORMAT = "HH:00:00-HH:59:59";

    private static final Integer HOURS_OF_DAY = 24;

    private static final ThreadLocal<DateFormat> THREADDATETIMELOCAL = new ThreadLocal<>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_TIME_FORMAT);
        }
    };

    private static final ThreadLocal<DateFormat> THREADDATELOCAL = new ThreadLocal<>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT);
        }
    };

    private static final ThreadLocal<DateFormat> THREADWEEKDATELOCAL = new ThreadLocal<>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_WEEK_FORMAT);
        }
    };
    private static final ThreadLocal<DateFormat> THREADMONTHDATELOCAL = new ThreadLocal<>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATE_MONTH_FORMAT);
        }
    };

    private static final ThreadLocal<DateFormat> THREADDDAYTIMELOCAL = new ThreadLocal<>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DAY_TIME_FORMAT);
        }
    };

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 是否在给定的日期范围内
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean inRange(Date start, Date end) {
        long current = System.currentTimeMillis();
        return current >= start.getTime() && current <= end.getTime();
    }

    /**
     * 计算消耗时间
     *
     * @param start
     * @param end
     * @return
     */
    public static Integer spendTime(Date start, Date end) {
        if (start.before(end)) {
            return (int) (end.getTime() - start.getTime()) / 1000;
        } else {
            return (int) (start.getTime() - end.getTime()) / 1000;
        }
    }

    /**
     * 计算消耗时间
     * @param startTimeStamp
     * @param endTimeStamp
     * @return
     */
    public static Integer spendTime(Long startTimeStamp, Long endTimeStamp) {
        if(Objects.isNull(startTimeStamp) || Objects.isNull(endTimeStamp)){
            return 0;
        }
        return Math.abs((int) (startTimeStamp - endTimeStamp) / 1000);
    }

    /**
     * LocalDateTime 2 Date
     */
    public static Date localDateTimeToUDate(final LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return date;
    }

    /**
     * uDate 2 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime uDateToLocalDateTime(final Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    /**
     * 时间戳转LocalDateTime
     * @param timestamp
     * @return
     */
    public static LocalDateTime epochSecondsToLocalDateTime(Long timestamp) {
        if(Objects.nonNull(timestamp)) {
            return LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
        }
        return null;
    }

    public static List<String> computeTimeRangeDateExStrs(Date date) {
        ArrayList<String> dates = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        for (int i = 0; i < HOURS_OF_DAY; i++) {
            date = cal.getTime();
            cal.add(Calendar.HOUR, 1);
            String s1 = THREADDDAYTIMELOCAL.get().format(date);
            dates.add(s1);
        }
        return dates;
    }

    public void unload() {
        THREADDATETIMELOCAL.remove(); // Compliant
    }

    public static DateFormat getDateTimeFormat() {
        DateFormat dateFormat = THREADDATETIMELOCAL.get();
        if (dateFormat != null) {
            return dateFormat;
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
    }

    public static DateFormat getDateFormat() {
        DateFormat dateFormat = THREADDATELOCAL.get();
        if (dateFormat != null) {
            return dateFormat;
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
    }

    public static DateFormat getWeekDateFormat() {
        DateFormat dateFormat = THREADWEEKDATELOCAL.get();
        if (dateFormat != null) {
            return dateFormat;
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
    }


    private static DateFormat getMonthDateFormat() {
        DateFormat dateFormat = THREADMONTHDATELOCAL.get();
        if (dateFormat != null) {
            return dateFormat;
        }
        throw ExceptionUtil.buildAdamException(OTARespCodeEnum.DATA_NOT_NULL);
    }

    /**
     * 用安全线程格式化日期时间
     *
     * @param date
     * @return
     */
    public static String formateDateTime(Date date) {
        return getDateTimeFormat().format(date);
    }

    /**
     * 用安全线程格式化日期时间
     *
     * @param date
     * @return
     */
    public static String formateDate(Date date) {
        return getDateFormat().format(date);
    }

    /**
     * 用安全线程格式化日期周
     *
     * @param date
     * @return
     */
    public static String formateWeekDate(Date date) {
        return getWeekDateFormat().format(date);
    }

    /**
     * 用安全线程格式化日期月
     *
     * @param date
     * @return
     */
    private static String formateMonthDate(Date date) {
        return getMonthDateFormat().format(date);
    }


    /**
     * 用安全线程格式化日期时间
     *
     * @param strDate
     * @return
     */
    public static Date parseDateTime(String strDate) throws ParseException {
        return getDateTimeFormat().parse(strDate);
    }

    /**
     * 用安全线程格式化日期
     *
     * @param strDate
     * @return
     */
    public static Date parseDate(String strDate) throws ParseException {
        return getDateFormat().parse(strDate);
    }


    /**
     * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
     */
    public static long dayDiff(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 86400000;
    }

    /**
     * 计算得到日期区间的所有日期（包含开始日期）
     * 例如：from=2017-06-25 to=2017-06-28 那么他们之间的日期就是 2017-06-25 2017-06-26 2017-06-27
     *
     * @param from
     * @param to
     * @return
     */
    public static List<Date> computeDayRangeDateEx(Date from, Date to) {
        List<Date> dateRange = Lists.newArrayList();
        dateRange.add(from);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(from);
        startCal.add(Calendar.DAY_OF_YEAR, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(to);
        while (startCal.before(endCal)) {
            dateRange.add(startCal.getTime());
            startCal.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dateRange;
    }

    /**
     * 计算得到日期区间的所有日期（包含开始日期）
     * 例如：from=2017-06-25 to=2017-06-28 那么他们之间的日期就是 2017-06-25 2017-06-26 2017-06-27
     *
     * @param from
     * @param to
     * @return
     */
    public static List<String> computeDayRangeDateExStrs(Date from, Date to) {
        List<String> strDates = Lists.newArrayList();
        List<Date> dates = computeDayRangeDateEx(from, to);
        dates.forEach(item -> {
            strDates.add(formateDate(item));
        });
        return strDates;
    }

    /**
     * 计算得到日期区间的所有日期（周）
     * 例如：from=2017-06-25 to=2017-06-28 那么他们之间的日期就是 2017-06-25 2017-06-26 2017-06-27
     *
     * @param from
     * @param to
     * @return List<Date>
     */
    public static List<Date> computeWeekRangeDateEx(Date from, Date to) throws ParseException {
        List<Date> dateRange = Lists.newArrayList();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(from);
        int dayOfWeek = (startCal.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : startCal.get(Calendar.DAY_OF_WEEK) - 1;
        startCal.add(Calendar.DAY_OF_YEAR, 7 - dayOfWeek);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(to);
        dayOfWeek = (endCal.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : endCal.get(Calendar.DAY_OF_WEEK) - 1;
        endCal.add(Calendar.DAY_OF_YEAR, 7 - dayOfWeek);
        while (startCal.before(endCal)) {
            dateRange.add(startCal.getTime());
            startCal.add(Calendar.DAY_OF_YEAR, 7);
        }
        return dateRange;
    }

    /**
     * 计算得到日期区间的所有日期（周）
     * 例如：from=2017-06-25 to=2017-06-28 那么他们之间的日期就是 2017-06-25 2017-06-26 2017-06-27
     *
     * @param from
     * @param to
     * @return List<String>
     */
    public static List<String> computeWeekRangeDateExStrs(Date from, Date to) throws ParseException {
        List<String> strDates = Lists.newArrayList();
        List<Date> dates = computeWeekRangeDateEx(from, to);
        dates.forEach(item -> {
            Calendar cal = Calendar.getInstance();
            cal.setTime(item);
            int weekOfYear= cal.get(Calendar.WEEK_OF_YEAR) - 1;
            strDates.add(cal.get(Calendar.YEAR) + "-" + String.format("%02d", weekOfYear));
        });
        return strDates;
    }

    /**
     * 计算得到日期区间的所有日期（月）
     * 例如：from=2017-06-25 to=2017-06-28 那么他们之间的日期就是 2017-06-25 2017-06-26 2017-06-27
     *
     * @param from
     * @param to
     * @return List<String>
     */
    private static List<Date> computeMonthRangeDateEx(Date from, Date to) {
        List<Date> dateRange = Lists.newArrayList();
        dateRange.add(from);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(from);
        startCal.add(Calendar.MONTH, 1);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(to);
        while (startCal.before(endCal)) {
            dateRange.add(startCal.getTime());
            startCal.add(Calendar.MONTH, 1);
        }
        return dateRange;
    }

    /**
     * 计算得到日期区间的所有日期（月）
     * 例如：from=2017-06-25 to=2017-06-28 那么他们之间的日期就是 2017-06-25 2017-06-26 2017-06-27
     *
     * @param from
     * @param to
     * @return List<String>
     */
    public static List<String> computeMonthRangeDateExStrs(Date from, Date to) throws ParseException {
        List<String> strDates = Lists.newArrayList();
        List<Date> dates = computeMonthRangeDateEx(from, to);
        dates.forEach(item -> {
            strDates.add(formateMonthDate(item));
        });
        return strDates;
    }

    /**
     * 获取以当前时间定义的版本号
     * @return
     * @throws ParseException
     */
    public static String genVersionWithTimeStamp(){
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonth().getValue();
        return new StringBuilder("V").append(now.getYear()).append(month < 10 ? "0" + month : month).append(now.getDayOfMonth()).append(now.getHour()).append(now.getMinute()).append(now.getSecond()).toString();
    }



    /**
     * 获取毫秒数
     * @param now
     * @return
     */
    public static Long toEpochMilli(LocalDateTime now){
        return Objects.nonNull(now) ? now.toInstant(ZoneOffset.of("+8")).toEpochMilli() : 0L;
    }

    /**
     * 获取LocalDateTime字符串
     * @param now
     * @return
     */
    public static String localDateTimeStr(LocalDateTime now){
        return dtf.format(now);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(MyDateUtil.genVersionWithTimeStamp());


        Date date = MyDateUtil.parseDateTime("2021-01-21 13:39:39");
        System.out.println(date);
    }
}
