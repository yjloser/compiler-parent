package com.semitransfer.compiler.core.config.internal.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * <p>
 * 日期相关
 * </p>
 *
 * @author Mr.Yang
 * @since 2019/4/20
 */
public class DateUtils {

    private static final String YYYY_MMM_DD = "yyyy-MM-dd";

    private DateUtils() {
    }

    /**
     * 检查日期是否有效
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 返回true或false
     * @author Mr.Yang
     * @date 2018/12/3 0003
     */
    public static boolean getDateIsTrue(String year, String month, String day) {
        if (Integer.parseInt(month) > 12 || Integer.parseInt(day) > 31) {
            return false;
        }
        try {
            String data = year + month + day;
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
            simpledateformat.setLenient(false);
            simpledateformat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断是否为同一天
     *
     * @param timeMillis1 时间毫秒值1
     * @param timeMillis2 时间毫秒值2
     * @return 是否为同一天
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isSameDay(int timeMillis1, int timeMillis2) {
        return isSameDay(new Date(timeMillis1), new Date(timeMillis2));
    }

    /**
     * 判断是否为同一天
     *
     * @param date1 日期对象1
     * @param date2 日期对象2
     * @return 是否为同一天
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MMM_DD);
        String firstDtStr = sdf.format(date1);
        String secentDtStr = sdf.format(date2);
        LocalDate localDate1 = LocalDate.parse(firstDtStr);
        LocalDate localDate2 = LocalDate.parse(secentDtStr);
        return localDate2.until(localDate1, ChronoUnit.DAYS) == 0;
    }

    /**
     * 获取当前年份
     *
     * @return 当前年份
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static int getCurYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return 当前月份, 1 - 12
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static int getCurMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日份
     *
     * @return 当前日份, 1 - 31
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static int getCurDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 当前时间下, 获取格式化的日期字符串, 英式, 如: 2000-01-01
     *
     * @return 时间字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getCurDateStr() {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * 将date时间转换为字符串 默认 yyyy-MM-dd HH:ss:mm
     *
     * @param date 时间
     * @return 时间字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String parse(Date date) {
        return parse(date, "yyyy-MM-dd HH:ss:mm");
    }

    /**
     * 将date时间转换为字符串
     *
     * @param date    时间
     * @param pattern 格式
     * @return 时间字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String parse(Date date, String pattern) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析时间字符串, 字符串需要符合格式: [yyyy-MM-dd HH:mm:ss]
     *
     * @param date 时间字符串
     * @return 时间, 解析失败返回 -1
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static long parse(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 解析时间字符串
     *
     * @param time    时间字符串
     * @param pattern 格式
     * @return 时间
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static long parse(String time, String pattern) {
        try {
            Date parse = new SimpleDateFormat(pattern, Locale.getDefault()).parse(time);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 当前时间下, 获取格式化的日期字符串, 英式, 如: 2019-02-14T08:41:08.079
     *
     * @return 时间字符串
     * @author Mr.Yang
     * @date 2018/12/1
     */
    public static String getCurDateTimeStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
