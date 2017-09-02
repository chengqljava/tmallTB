package com.cheng.qian.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

import com.google.common.base.Strings;

public class DateUtil {

    private static final String yyMMdd               = "yy-MM-dd";
    private static final String yyyyMMdd             = "yyyy-MM-dd";
    private static final String yyyyMMddHHmm         = "yyyy-MM-dd HH:mm";
    private static final String yyyyMMddHHmmss       = "yyyy-MM-dd HH:mm:ss";

    static SimpleDateFormat     formatyyMMdd         = new SimpleDateFormat(yyMMdd);
    static SimpleDateFormat     formatyyyyMMdd       = new SimpleDateFormat(yyyyMMdd);
    static SimpleDateFormat     formatyyyyMMddHHmm   = new SimpleDateFormat(yyyyMMddHHmm);
    static SimpleDateFormat     formatyyyyMMddHHmmss = new SimpleDateFormat(yyyyMMddHHmmss);

    static DateTime             dateTime             = new DateTime();

    public static String format_yyMMdd(Date date) {
        if (date == null) {
            return null;
        }
        return formatyyMMdd.format(date);
    }

    public static Date parse_yyMMdd(String date) {
        if (Strings.isNullOrEmpty(date)) {
            return null;
        }
        try {
            return formatyyMMdd.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format_yyyyMMdd(Date date) {
        if (date == null) {
            return null;
        }
        return formatyyyyMMdd.format(date);
    }

    public static Date parse_yyyyMMdd(String date) {
        if (Strings.isNullOrEmpty(date)) {
            return null;
        }
        try {
            return formatyyyyMMdd.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String format_yyyyMMddHHmmss(Date date) {
        if (date == null) {
            return null;
        }
        return formatyyyyMMddHHmmss.format(date);
    }

    public static Date parse_yyyyMMddHHmmss(String date) {
        if (Strings.isNullOrEmpty(date)) {
            return null;
        }
        try {
            return formatyyyyMMddHHmmss.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String get_yyMMdd() {
        return dateTime.toString(yyMMdd);
    }

    public static String get_yyyyMMdd() {
        return dateTime.toString(yyyyMMdd);
    }

    public static String get_yyyyMMddHHmm() {
        return dateTime.toString(yyyyMMddHHmm);
    }

    public static String get_yyyyMMddHHmmss() {
        return dateTime.toString(yyyyMMddHHmmss);
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    public static String getYearMonth() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        return sf.format(date);
    }

    public static String formatDate(String date, String format) {

        DateTime time = new DateTime(date);

        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(new Date(time.getMillis()));
    }

    public static String timestampStrToString(String time) {

        try {
            Date date = new Date();
            // 注意format的格式要与日期String的格式相匹配
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = sdf.parse(time);
            return formatDate(date, "yyyy-MM-dd HH:mm");
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    public static String getMonthDay() {
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        Date date = new Date();
        return sf.format(date);
    }

    public static Date getLastDayEnd() {

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return ca.getTime();
    }

    // 获取今日的日期
    public static String getToday() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(new Date());
    }

    // 获取当前时间
    public static String getCurrentTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date());
    }

    // 获取昨天的日期
    public static String getYesterday() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setDate(date.getDate() - 1);
        return sf.format(date);
    }

    // 获取明天的日期
    public static String getTomorrow() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setDate(date.getDate() + 1);
        return sf.format(date);
    }

    public static int getWeek() {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek;
    }

    public static Date getTodayforDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return DateUtil.stringToDate(sf.format(new Date()));
    }

    public static Date getStartOfDate(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse(new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getEndOfDate(Date date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse(new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date stringToDate(String dateStr, String pattern) throws ParseException {
        if (dateStr == null || pattern == null)
            return null;
        return new SimpleDateFormat(pattern).parse(dateStr);
    }

    public static Date stringToDate(String dateStr) {
        if (null == dateStr) {

            return null;
        }
        DateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            if (dateStr.length() < 11) {

                dateStr += " 00:00:00";
            }
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String timestampToString(Timestamp time) {

        if (null == time) {

            return "";
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 定义格式，不显示毫秒
        return df.format(time);
    }

    public static String timestampToString(Timestamp time, String format) {

        if (null == time) {

            return "";
        }

        SimpleDateFormat df = new SimpleDateFormat(format);// 定义格式，不显示毫秒
        return df.format(time);
    }

    public static Date timestampToDate(Timestamp time) {
        if (null == time) {

            return null;
        }
        return new Date(time.getTime());
    }

    public static int miao(Timestamp time1, Timestamp time2) {

        long a = time1.getTime();
        long b = time2.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    public static String dateDiff(long startTime, long endTime) {
        // 按照传入的格式生成一个simpledateformate对象
        long nd = 1000 * 60 * 60 * 24;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        try {
            // 获得两个时间的毫秒时间差异
            diff = endTime - startTime;
            long day = diff / nd;// 计算差多少天
            long year = day / 365; // 计算差多少年
            long month = day / 30; // 计算差多少月
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒

            if (year > 0) {

                return year + "年前";
            }
            if (month > 0) {

                return month + "月前";
            } else if (day > 0) {

                return day + "天前";
            } else if (hour > 0) {

                return hour + "小时前";
            } else if (min > 0) {

                return min + "分钟前";
            } else if (sec > 0) {

                return sec + "秒前";
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String[] getCurrentMonthFistAndLastDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());

        // 获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());

        return new String[] { first, last };
    }

    /**
     * 获得一周开始的时间
     * @param date
     * @return
     */
    public static Date getStartOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (week == -1) {
            week = 6;
        }
        calendar.add(Calendar.DAY_OF_MONTH, -week);
        return calendar.getTime();
    }

    /**
     * 获得一周结束的日期
     * @param date
     * @return
     */
    public static Date getEndOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        Date startOfWeek = getStartOfWeek(date);
        calendar.setTime(startOfWeek);
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        return calendar.getTime();
    }

    /**
     * 获取本月的第一天
     * @return
     */
    public static Date getStartOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        //设置为1号，当前日期为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * 获取本月的最后一天
     * @return
     */
    public static Date getEndOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }
}
