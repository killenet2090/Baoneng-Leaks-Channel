package com.bnmotor.icv.tsp.vehstatus.common.util;

import com.bnmotor.icv.adam.core.exception.AdamException;
import com.bnmotor.icv.adam.core.exception.RespCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ClassName: DateUtils
 * @Description: 日期工具类
 * @author: shuqi1
 * @date: 2020/4/27
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class DateUtils
{
    /**
     * 时区
     */
    public static final String DATE_TIMEZONE = "GMT+8";
    /**
     * 年月
     */
    public final static String MONTH_FORMAT = "yyyy-MM";
    /**
     * 日期
     */
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 日期时间
     */
    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 带时区的时间格式
     */
    public final static String DATETIME_FORMAT_ZONE = "yyyy-MM-dd HH:mm:ssZ";
    /**
     * 时间
     */
    public final static String TIME_FORMAT = "HH:mm:ss";

    /**
     * 例：20200427105651
     */
    public static final String TIME_FORMAT_WITHOUT_UNDERLINE_AND_CHINESE = "yyyyMMddHHmmss";
    /**
     * 例：20200427
     */
    public static final String DATAE_FORMAT_WITHOUT_UNDERLINE_AND_CHINESE = "yyyyMMdd";
    /**
     *例：2020年04月27日11时17分29秒
     */
    public static final String TIME_FORMAT_WITHOUT_UNDERLINE = "yyyy年MM月dd日HH时mm分ss秒";
    /**
     * 例：2020年04月27日
     */
    public static final String DATE_FORMAT_WITHOUT_UNDERLINE = "yyyy年MM月dd日";
    /**
     * 例：2020-04-27 11:18:22
     */
    public static final String TIME_FORMAT_WITHOUT_CHINESE = "yyyy-MM-dd HH:mm:ss";
    /**
     * 例：2020-04-27
     */
    public static final String DATE_FORMAT_WITHOUT_CHINESE = "yyyy-MM-dd";

    private DateUtils()
    {
        throw new IllegalAccessError("工具类不能实例化");
    }

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 根据格式类型，返回当前时间
     * @param format String
     * @return 当前时间
     */
    public static String getNowTimeWithFormat(String format)
    {
        ZonedDateTime zonedDateTime = Instant.now().atZone(ZoneId.systemDefault());
        return getFormatter(format).format(zonedDateTime);

    }

    /**
     * 根据格式类型，返回当前日期
     * @param format String
     * @return 当前日期
     */
    public static String getDateWithFormat(String format)
    {
        Objects.requireNonNull(format,"format must not be null");
        return getFormatter(format).format(LocalDate.now());
    }

    /**
     * LocalDate转换成指定格式字符串
     * @param localDate LocalDate
     * @param format  String
     * @return 指定格式字符串日期
     */
    public static String localDateToString(LocalDate localDate , String format)
    {
        return  getFormatter(format).format(localDate);
    }

    /**
     * localDateTime转换成指定格式字符串
     * @param localDateTime LocalDateTime
     * @param format String
     * @return 指定格式字符串日期
     */
    public static String localDateTimeToString(LocalDateTime localDateTime , String format)
    {
        Objects.requireNonNull(localDateTime, "localDateTime must not be null");
        return getFormatter(format).format(localDateTime);
    }

    /**
     * Date类型转换成指定格式字符串
     * @param date Date日期
     * @param format 格式
     * @return 指定格式字符串日期
     */
    public static String dateToString(Date date , String format)
    {
        Objects.requireNonNull(date,"date must not be null");
        return getFormatter(format).format(dateToLocalDateTime(date));
    }

    /**
     * Date时间转换成LocalDateTime类型
     * @param date Date类型日期
     * @return LocalDateTime
     */
    public static LocalDateTime  dateToLocalDateTime(Date date)
    {
        Objects.requireNonNull(date,"date must not be null");
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param format 格式
     * @return Date
     */
    public static Date parse(String dateStr, String format)
    {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(format));
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param format 格式
     * @return Date
     */
    public static LocalDateTime parseToLocalDateTime(String dateStr, String format)
    {
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取指定格式formatter
     * @param format 格式
     * @return DateTimeFormatter
     */

    private static DateTimeFormatter getFormatter(String format)
    {
        return  DateTimeFormatter.ofPattern(format);
    }


    /**
     * 返回指定日期的前几天
     * @param days int 天数
     * @return date 之前的日期
     */
    public static Date previous(int days)
    {
        return new Date(System.currentTimeMillis() - days * 3_600_000L * 24L);
    }

    /**
     * 日期类型转换为字符串类型
     * @param d date
     * @return  String
     */
    public static String formatDateTime(Date d)
    {
        return new SimpleDateFormat(DATETIME_FORMAT).format(d);
    }

    /**
     * 根据指定格式和日期，返回字符串格式时间
     * @param d Date类型日期
     * @param fmt 格式
     * @return 指定格式字符串
     */
    public static String formatDateTime(Date d, String fmt)
    {
        return new SimpleDateFormat(fmt).format(d);
    }

    /**
     * 时间戳转换为字符串时间
     * @param d 时间戳
     * @return 字符串类型时间
     */
    public static String formatDateTime(long d)
    {
        return new SimpleDateFormat(DATETIME_FORMAT).format(d);
    }


    /**
     * 时间戳转换为字符串日期
     * @param d  时间戳
     * @return  字符串类型日期
     */
    public static String formatDate(long d)
    {
        return new SimpleDateFormat(DATE_FORMAT).format(d);
    }

    /**
     * string 类型转换为Date类型
     * @param d  字符串类型日期
     * @return 转换后的日期
     */
    public static Date parseDate(String d)
    {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(d);
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Parse date and time like "yyyy-MM-dd hh:mm".
     */
    public static Date parseDateTime(String dt)
    {
        try
        {
            return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 转换日期字符串得到指定格式的日期类型
     * @param formatString 需要转换的格式字符串
     * @param targetDate   需要转换的时间
     * @return 转换后的日期
     * @throws ParseException
     */
    public static Date convertString2Date(String formatString, String targetDate) throws ParseException
    {
        if (StringUtils.isBlank(targetDate))
        {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date result = null;
        try
        {
            result = format.parse(targetDate);
        } catch (ParseException pe)
        {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return result;
    }

    /**
     * 转换字符串得到默认格式的日期类型
     *
     * @param strDate
     * @return Date 类型日期
     * @throws ParseException
     */
    public static Date convertString2Date(String strDate) throws ParseException
    {
        Date result = null;
        try
        {
            result = convertString2Date(DATE_FORMAT, strDate);
        } catch (ParseException pe)
        {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return result;
    }

    /**
     * 转换日期得到指定格式的日期字符串
     *
     * @param formatString 需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
     * @param targetDate   目标日期
     * @return  转换后的日期
     */
    public static String convertDate2String(String formatString, Date targetDate)
    {
        SimpleDateFormat format = null;
        String result = null;
        if (targetDate != null)
        {
            format = new SimpleDateFormat(formatString);
            result = format.format(targetDate);
        }
        else

        {
            return null;
        }
        return result;
    }

    /**
     * 转换日期,得到默认日期格式字符串
     *
     * @param targetDate
     * @return
     */
    public static String convertDate2String(Date targetDate)
    {
        return convertDate2String(DATE_FORMAT, targetDate);
    }

    /**
     * 比较日期大小
     * @param src
     * @param src
     * @return int; 1:DATE1>DATE2;
     */
    public static int compareDate(Date src, Date src1)
    {

        String date1 = convertDate2String(DATETIME_FORMAT, src);
        String date2 = convertDate2String(DATETIME_FORMAT, src1);
        DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime())
            {
                return 1;
            }
            else if (dt1.getTime() < dt2.getTime())
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)

        {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 日期比较
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateBefore(String date1, String date2)
    {
        try
        {
            DateFormat df = DateFormat.getDateTimeInstance();
            return df.parse(date1).before(df.parse(date2));
        } catch (ParseException e)
        {
            return false;
        }
    }

    /**
     * 日期比较
     * 判断当前时间是否在时间date2之前 <br/>
     * 时间格式 2005-4-21 16:16:34 <br/>
     * @param date2
     * @return
     */
    public static boolean isDateBefore(String date2)
    {
        if (date2 == null)
        {
            return false;
        }
        try
        {
            Date date1 = new Date();
            DateFormat df = DateFormat.getDateTimeInstance();
            return date1.before(df.parse(date2));
        } catch (ParseException e)
        {
            return false;
        }
    }

    /**
     * 比较当前时间与时间date2的天相等 时间格式 2008-11-25 16:30:10 如:当前时间是2008-11-25
     * 16:30:10与传入时间2008-11-25 15:31:20 相比较,返回true即相等
     * @param date2
     * @return boolean; true:相等
     */
    public static boolean equalDate(String date2)
    {
        try
        {
            String date1 = convertDate2String(DATETIME_FORMAT, new Date());
            Date d1 = convertString2Date(DATE_FORMAT, date1);
            Date d2 = convertString2Date(DATE_FORMAT, date2);
            if(d1 != null) {
                return d1.equals(d2);
            } else {
                throw new AdamException(RespCode.SERVER_DATA_ERROR);
            }
        }
        catch (ParseException e)

        {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 比较时间date1与时间date2的天相等 时间格式 2008-11-25 16:30:10
     * @param date1
     * @param date2
     * @return boolean; true:相等
     * @author zhangjl
     */
    public static boolean equalDate(String date1, String date2)
    {
        if (StringUtils.isEmpty(date1) || StringUtils.isEmpty(date2))
        {
            return false;
        }
        try
        {
            Date d1 = convertString2Date(DATE_FORMAT, date1);
            Date d2 = convertString2Date(DATE_FORMAT, date2);
            if(d1 != null) {
                return d1.equals(d2);
            } else {
                throw new AdamException(RespCode.SERVER_DATA_ERROR);
            }
        }
        catch (ParseException e)
        {
            return false;
        }
    }

    /**
     * 比较时间date1是否在时间date2之前 时间格式 2008-11-25 16:30:10
     * @param date1
     * @param date2
     * @return boolean; true:在date2之前
     * @author 胡建国
     */
    public static boolean beforeDate(String date1, String date2)
    {
        if (StringUtils.isEmpty(date1) || StringUtils.isEmpty(date2))
        {
            return false;
        }
        try
        {
            Date d1 = convertString2Date(DATE_FORMAT, date1);
            Date d2 = convertString2Date(DATE_FORMAT, date2);
            if(d1 != null) {
                return d1.before(d2);
            } else {
                throw new AdamException(RespCode.SERVER_DATA_ERROR);
            }

        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 获取上个月开始时间
     *
     * @param currentDate 当前时间
     * @return 上个月的第一天
     */
    public static Date getBoferBeginDate(Calendar currentDate)
    {
        Calendar result = Calendar.getInstance();
        result.set(currentDate.get(Calendar.YEAR), (currentDate.get(Calendar.MONTH)) - 1,
                result.getActualMinimum(Calendar.DATE), 0, 0, 0);
        return result.getTime();
    }

    /**
     * 获取指定月份的第一天
     * @param currentDate
     * @return
     */
    public static Date getBeginDate(Calendar currentDate)
    {
        Calendar result = Calendar.getInstance();
        result.set(currentDate.get(Calendar.YEAR), (currentDate.get(Calendar.MONTH)),
                result.getActualMinimum(Calendar.DATE));
        return result.getTime();
    }

    /**
     * 获取上个月结束时间
     * @param currentDate 当前时间
     * @return 上个月最后一天
     */
    public static Date getBoferEndDate(Calendar currentDate)
    {
        Calendar result = currentDate;
        result.set(Calendar.DATE, 1);
        result.add(Calendar.DATE, -1);
        return result.getTime();
    }

    /**
     * 获取两个时间的时间间隔
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    public static int getDaysBetween(Calendar beginDate, Calendar endDate)
    {
        if (beginDate.after(endDate))
        {
            Calendar swap = beginDate;
            beginDate = endDate;
            endDate = swap;
        }
        int days = endDate.get(Calendar.DAY_OF_YEAR) - beginDate.get(Calendar.DAY_OF_YEAR) + 1;
        int year = endDate.get(Calendar.YEAR);
        if (beginDate.get(Calendar.YEAR) != year)
        {
            beginDate = (Calendar) beginDate.clone();
            do
            {
                days += beginDate.getActualMaximum(Calendar.DAY_OF_YEAR);
                beginDate.add(Calendar.YEAR, 1);
            } while (beginDate.get(Calendar.YEAR) != year);
        }
        return days;
    }

    /**
     * 获得日期的下一个星期一的日期
     *
     * @param date 任意时间
     * @return
     */
    public static Calendar getNextMonday(Calendar date)
    {
        Calendar result = null;
        do
        {
            result = (Calendar) date.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }

    public static boolean isDateEnable(Date beginDate, Date endDate, Date currentDate) {
        // 开始日期
        long beginDateLong = beginDate.getTime();
        // 结束日期
        long endDateLong = endDate.getTime();
        // 当前日期
        long currentDateLong = currentDate.getTime();
        if (currentDateLong >= beginDateLong && currentDateLong <= endDateLong)
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 获取当前月份的第一天
     *
     * @param currentDate 当前时间
     * @return
     */
    public static Date getMinDate(Calendar currentDate)
    {
        Calendar result = Calendar.getInstance();
        result.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.getActualMinimum(Calendar.DATE));
        return result.getTime();
    }

    public static Calendar getDate(int year, int month, int date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar;
    }

    public static Calendar getDate(int year, int month)
    {
        return getDate(year, month, 0);
    }

    public static Date getCountMinDate(int year, int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, calendar.getActualMinimum(Calendar.DATE));
        return calendar.getTime();
    }

    public static Date getCountMaxDate(int year, int month)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 0);
        return calendar2.getTime();
    }

    /**
     * 获取当前月份的第一天
     *
     * @return
     */
    public static Date getMinDate()
    {
        Calendar currentDate = Calendar.getInstance();
        return getMinDate(currentDate);
    }

    /**
     * 获取当前月分的最大天数
     *
     * @param currentDate 当前时间
     * @return
     */
    public static Date getMaxDate(Calendar currentDate)
    {
        Calendar result = Calendar.getInstance();
        result.set(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                currentDate.getActualMaximum(Calendar.DATE));
        return result.getTime();
    }

    /**
     * 获取当前月分的最大天数
     *
     * @return
     */
    public static Date getMaxDate()
    {
        Calendar currentDate = Calendar.getInstance();
        return getMaxDate(currentDate);
    }

    /**
     * 获取今天最大的时间
     *
     * @return
     */
    public static String getMaxDateTimeForToDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        return convertDate2String(DATETIME_FORMAT, calendar.getTime());
    }

    /**
     * 获取日期最大的时间
     *
     * @return
     */
    public static Date getMaxDateTimeForToDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        return calendar.getTime();
    }

    /**
     * 获取今天最小时间
     *
     * @return
     */
    public static String getMinDateTimeForToDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        return convertDate2String(DATETIME_FORMAT, calendar.getTime());
    }

    /**
     * 获取 date 最小时间
     *
     * @return
     */
    public static Date getMinDateTimeForToDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        return calendar.getTime();
    }

    /**
     * 获取发生日期的结束时间 根据用户设置的日期天数来判定这这个日期是什么(例如 (getHappenMinDate = 2008-10-1) 的话 那么
     * (getHappenMaxDate = 2008-11-1) 号)
     *
     * @return
     */
    public static Date getHappenMaxDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return calendar.getTime();
    }

    /**
     * 加减天数
     * @param num
     * @param dateStr
     * @return
     */
    public static String addDay(int num, String dateStr)
    {
        Date date = parseDate(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 把日期往后增加 num 天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, num);
        // 这个时间就是日期往后推一天的结果
        return formatDate(calendar.getTime().getTime());
    }

    /**
     * 加减天数
     *
     * @param num
     * @param date
     * @return
     */
    public static Date addDay(int num, Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 把日期往后增加 num 天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, num);
        // 这个时间就是日期往后推一天的结果
        return calendar.getTime();
    }

    /**
     * 加减小时
     *
     * @param num  正数：时间往后推num小时；负数：往前推num小时。
     * @param date
     * @return
     */
    public static Date addHour(int num, Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR, num);
        return calendar.getTime();
    }

    /**
     * 格式化日期
     */
    public static String dateFormat(Date date)
    {
        if (date == null)
        {
            return null;
        }
        return DateFormat.getDateInstance().format(date);
    }

    /**
     *  格式化日期
     * @param formatString
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date formatDate(String formatString, Date date) throws ParseException
    {
        if (date == null)
        {
            date = new Date();
        }
        if (StringUtils.isBlank(formatString))
        {
            formatString = DateUtils.DATE_FORMAT;
        }
        date = DateUtils.convertString2Date(formatString, DateUtils.convertDate2String(formatString, date));

        return date;
    }

    /**
     * 格式化日期 yyyy-MM-dd
     *
     * @throws ParseException 例： DateUtils.formatDate(new Date()) "yyyy-MM-dd
     *                        00:00:00"
     */
    public static Date formatDate(Date date) throws ParseException
    {
        date = formatDate(DateUtils.DATE_FORMAT, date);
        return date;
    }

    /**
     * @throws ParseException 根据日期获得 星期一的日期
     */
    public static Date getMonDay(Date date) throws ParseException
    {

        Calendar cal = Calendar.getInstance();
        if (date == null)
        {
            date = new Date();
        }
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }


        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date = formatDate(cal.getTime());
        return date;
    }

    /**
     * 根据日期获得 星期日 的日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getSunDay(Date date) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        if (date == null)
        {
            date = new Date();
        }
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
        {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        date = formatDate(cal.getTime());
        return date;
    }

    /**
     * 获得 下个月的第一天
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getNextDay(Date date) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DATE, 1);
        return formatDate(cal.getTime());
    }


    /**
     * 获取时间间隔
     * @param endDate
     * @param startDate
     * @return
     */

    public static String getDatePoor(Date endDate, Date startDate)
    {
        long nd = 1_000L * 24 * 60 * 60;
        long nh = 1_000L * 60 * 60;
        long nm = 1_000L * 60;
        long ns = 1_000L;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒
        long sec = diff % nd % nh % nm / ns;

        StringBuffer bf = new StringBuffer();

        if (day != 0)
        {
            bf.append(day).append("天");
        }
        if (hour != 0)
        {
            bf.append(hour).append("小时");
        }
        if (min != 0)
        {
            bf.append(min).append("分钟");
        }
        if (sec != 0)
        {
            bf.append(sec).append("秒");
        }
        return bf.toString();
    }

    public static String getDateSec(Date endDate, Date startDate)
    {
        long nd = 1_000L * 24 * 60 * 60;
        long nh = 1_000L * 60 * 60;
        long nm = 1_000L * 60;
        long ns = 1_000L;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少秒
        long sec = diff % nd % nh % nm / ns;
        StringBuffer bf = new StringBuffer();
        if (sec != 0)
        {
            bf.append(sec);
        }
        return bf.toString();
    }

    /**
     * 月份加减
     * @param num
     * @param date
     * @return
     */
    public static Date addMonth(int num, Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 把日期往后增加 num 月.整数往后推,负数往前移动
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    /**
     * 获得一个范围日期里面的每一个月份
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static List<String> getDateBetweenTwo(Date startDate, Date endDate) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        List<String> dateList = new ArrayList<String>();
        // 定义日期实例
        Calendar dd = Calendar.getInstance();
        // 设置日期起始时间
        dd.setTime(startDate);
        // 判断是否到结束日期
        while (dd.getTime().before(endDate))
        {
            String str = sdf.format(dd.getTime());
            dateList.add(str);
            // 进行当前日期月份加1
            dd.add(Calendar.MONTH, 1);
        }
        return dateList;
    }

    public static String getDateTime(long microsecond)
    {
        return detailFormat(new Date(microsecond));
    }

    /**
     * 返回日期的详细格式：yyyy-MM-dd HH:mm:ss
     * @param date 传入一个日期
     * @return
     */
    public static String detailFormat(Date date)
    {
        if (date == null)
        {
            return "";
        } else
        {
            return new SimpleDateFormat(DATETIME_FORMAT).format(date);
        }
    }

    /**
     * 当时间相差过长时设置简短的年份显示
     *
     * @param microsecond
     * @return
     */
    public static String getshortDateTime(long microsecond)
    {
        return new SimpleDateFormat("yy-MM-d HH:mm").format(new Date(microsecond));
    }

    /**
     * 转换为毫秒
     * @param localDateTime
     * @return
     */
    public static long getMillFromLocalDateTime(LocalDateTime localDateTime) {
        //获取毫秒数
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 毫秒转LocalDateTime
     * @param timeMillis
     * @return
     */
    public static LocalDateTime getLocalDateTimeFromMill(long timeMillis) {
        //获取毫秒数
        return new Date(timeMillis).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }
}
