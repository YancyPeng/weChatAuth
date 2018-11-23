package com.cn.controller.weChat.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * describe: 字符串和日期转换类
 * 新方数据库中的字符串一般为"yyyyMMddHHmmss"格式
 * 以下注释中按照日期的长度来区分日期
 * 注意：本工具类不会去处理任何异常，都会直接将异常抛出给调用者
 *
 * @author : 王校兵
 * @version : v1.0
 * @time : 2017/8/25  20:53
 */
public final class DateUtils {

    private static final String DATE14FORMAT = "yyyyMMddHHmmss";
    private static final String DATE19FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE8FORMAT = "yyyyMMdd";
    private static final String TIME6FORMAT = "HHmmss";
    private static final String PARSEDATESTR = "EEE MMM dd HH:mm:ss z yyyy";

    /**
     * 禁止实例化
     */
    private DateUtils() {
    }

    /**
     * 将传入的十四位的字符串转换为date类型
     *
     * @param date 日期字符串，格式为："yyyyMMddHHmmss"
     * @return 转换结果，格式为date类型
     * @throws ParseException 日期转换异常
     */
    public static Date getDateFromString14(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE14FORMAT);
        return format.parse(date);
    }

    /**
     * 将传入日期和时间字符串转化为date类型
     *
     * @param date 日期字符串，格式为："yyyyMMdd"
     * @param time 时间字符串，格式为："HHmmss"
     * @return 转换结果
     * @throws ParseException 日期转换异常
     */
    public static Date getDateFromStringDateAndTime(String date, String time) throws ParseException {
        if (date == null || time == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE14FORMAT);
        return format.parse(date + time);
    }

    /**
     * 将传入日期字符串转化为date类型
     *
     * @param date 日期字符串，格式为："yyyyMMdd"
     * @return 转换结果
     * @throws ParseException 日期转换异常
     */
    public static Date getDateFromStringDate(String date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE8FORMAT);
        return format.parse(date);
    }

    /**
     * 将传入的字符串转化为date类型
     *
     * @param date 日期字符串
     * @return 转换结果
     * @throws ParseException 日期转换异常
     */
    public static Date getDateFromString(String date, String dateFormat) throws ParseException {
        return getDateFromString(date, dateFormat, null);
    }

    /**
     * 将传入的字符串转化为指定时区的date类型
     *
     * @param date 日期字符串
     * @return 转换结果
     * @throws ParseException 日期转换异常
     */
    public static Date getDateFromString(String date, String dateFormat, String timeZone) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        if (StringUtils.hasLength(timeZone)) {
            format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

            // modify Time Zone.
            format.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return format.parse(date);
    }

    /**
     * 将传入的字符串转化为时间date类型
     *
     * @param time 时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     * @return 转换结果
     * @throws ParseException 日期转换异常
     */
    public static Date getTime19FromDate(String time) throws ParseException {
        if (time == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE19FORMAT);
        return format.parse(time);
    }

    /**
     * 将传入的日期类型转换为14位字符串
     *
     * @param date 日期
     * @return 转换为14位之后的日期，格式为"yyyyMMddHHmmss"
     */
    public static String getStringFromDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE14FORMAT);
        return dateFormat.format(date);
    }

    /**
     * 将传入的日期类型转换为19位字符串
     *
     * @param date 日期
     * @return 转换为19位之后的日期，格式为"yyyy-MM-dd HH:mm:ss"
     */
    public static String getDate19FromDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE19FORMAT);
        return dateFormat.format(date);
    }

    /**
     * 将传入的日期类型转换为8位字符串
     *
     * @param date 日期
     * @return 转换为8位之后的日期，格式为"yyyyMMdd"
     */
    public static String getDate8FromDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE8FORMAT);
        return dateFormat.format(date);
    }

    /**
     * 将传入的日期类型转换为6位字符串
     *
     * @param date 日期
     * @return 转换为6位之后的时间，格式为"HHmmss"
     */
    public static String getTime6FromDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME6FORMAT);
        return timeFormat.format(date);
    }

    /**
     * 将传入的日期类型转换为指定格式的字符串
     *
     * @param date       日期
     * @param dateFormat 日期的格式
     * @return 转换之后的日期
     */
    public static String getStringFromDate(Date date, String dateFormat) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /**
     * 将传入的字符串类型转换为指定格式的日期
     *
     * @param string    日期字符串
     * @param formatStr 日期的格式
     * @return 转换之后的日期
     * @throws ParseException 日期转换异常
     */
    public static Date stringToDate(String string, String formatStr) throws ParseException {
        if (string == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.parse(string);
    }

    /**
     * 获取当前时间的String类型的字符串
     *
     * @return 当前时间的字符串表示形式, 格式为:"yyyyMMddHHmmss"
     */
    public static String getCurrentTimeString() {
        return getCalculateTimeStr("yyyyMMddHHmmss", 0, 0);
    }

    /**
     * 根据传入的日期格式，获取当前时间的String类型的字符串
     *
     * @param format 日期格式
     * @return 当前时间的字符串表示形式, 格式为入参传入的格式
     */
    public static String getCurrentTimeString(String format) {
        return getCalculateTimeStr(format, 0, 0);
    }

    /**
     * 根据传入的日期格式，获取当前时间的String类型的字符串
     *
     * @param format 日期格式
     * @param field  日历字段
     * @param amount 要添加到该字段的日期或时间的量
     * @return 根据当前时间计算出的字符串表示形式, 格式为入参传入的格式
     */
    public static String getCalculateTimeStr(String format, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        if (amount != 0) {
            cal.add(field, amount);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }

    /*
     * 给定一个日期，返回指定天数后的日期
     * @param date 日期
     * @param days 天数
     * @return 指定天数后的日期
     */
    public static Date getDateAfterDays(Date date, int days) {
        if (date == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 给定一个日期，返回指定天数后的日期字符串
     *
     * @param date 日期
     * @param days 天数
     * @return 指定天数后的日期字符串
     */
    public static String getStringAfterDays(Date date, int days) {
        return getStringFromDate(getDateAfterDays(date, days));
    }


    /*
    * 给定一个日期，返回指定分钟后的日期
    *  @Author LiHongDa
    * @param date 日期
    * @param Minutes 分钟数
    * @return 指定分钟后的日期
    */
    public static Date getDateAfterMinutes(Date date, int minutes) {
        if (date == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    /**
     * 给定一个日期，返回指定分钟后的日期字符串
     *
     * @param date    日期
     * @param minutes 分钟
     * @return 指定分钟后的日期字符串
     * @Author LiHongDa
     */
    public static String getStringAfterMinutes(Date date, int minutes) {
        return getStringFromDate(getDateAfterMinutes(date, minutes));
    }
    /*
   * 给定一个日期，返回指定月份后的日期
   *  @Author LiHongDa
   * @param date 日期
   * @param months 月份数
   * @return 指定月份后的日期
   */
    public static Date getDateAfterMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 给定一个日期，返回指定月份后的日期字符串
     *
     * @param date    日期
     * @param months  月份
     * @return 指定月份后的日期字符串
     * @Author LiHongDa
     */
    public static String getStringAfterMonths(Date date, int months) {
        return getStringFromDate(getDateAfterMonths(date, months));
    }

    /**
     * 因为数据库时间字段由原varchar(14)改为datetime类型，所以在这个方法中做一个转换，保证返回给前端还是原来的不变
     * 传入   yyyy-MM-dd hh:mm:ss 类型字符串
     * 转换为 yyyyMMddhhmmss      类型字符串
     * @param dateStr 2018-02-24 17:10:22类型的字符串
     * @return
     */
    public static String getConvertDateStr(String dateStr){
        if(!StringUtils.hasText(dateStr)){
            return null;
        }
        String result = null;
        //2018-02-24 17:10:22.0
        SimpleDateFormat dateFormat19 = new SimpleDateFormat(DATE19FORMAT);
        SimpleDateFormat dateFormat14 = new SimpleDateFormat(DATE14FORMAT);
        try {
            Date date = dateFormat19.parse(dateStr);
            result = dateFormat14.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将字符串类型的Date转换为Date
     * @param dateStr
     * @return
     */
    public static Date getStrConvertDate(String dateStr){
        if(!StringUtils.hasText(dateStr)){
            return null;
        }
        // "Fri Mar 09 09:26:41 CST 2018"
        Date date = null;
        try {
            date = new SimpleDateFormat(PARSEDATESTR, Locale.ENGLISH).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}