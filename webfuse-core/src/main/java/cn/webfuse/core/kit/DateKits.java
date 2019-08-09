package cn.webfuse.core.kit;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.time.DateUtil) and made some changes.
 */
public class DateKits {

    public static final long MILLIS_PER_SECOND = 1000; // Number of milliseconds in a standard second.
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND; // Number of milliseconds in a standard minute.
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE; // Number of milliseconds in a standard hour.
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR; // Number of milliseconds in a standard day.

    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //////// 日期比较 ///////////

    /**
     * 判断日期是否在范围内，包含相等的日期
     */
    public static boolean isBetween(final Date date, final Date start, final Date end) {
        if (date == null || start == null || end == null || start.after(end)) {
            throw new IllegalArgumentException("some date parameters is null or dateBein after dateEnd");
        }
        return !date.before(start) && !date.after(end);
    }

    ///// 获取日期的位置//////

    /**
     * 获得日期是一周的第几天. 已改为中国习惯，1 是Monday，而不是Sundays.
     */
    public static int getDayOfWeek(final Date date) {
        int result = getWithMondayFirst(date, Calendar.DAY_OF_WEEK);
        return result == 1 ? 7 : result - 1;
    }

    /**
     * 获得日期是一年的第几天，返回值从1开始
     */
    public static int getDayOfYear(final Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得日期是一月的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那个月里都算. 已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfMonth(final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得日期是一年的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那一年里都算.已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfYear(final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_YEAR);
    }

    private static int get(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    private static int getWithMondayFirst(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(field);
    }

    ///// 获得往前往后的日期//////

    /**
     * 获得一年的开始
     */
    public static Date beginOfYear(final Date date) {
        return DateUtils.truncate(date, Calendar.YEAR);
    }

    /**
     * 获得一年的结束
     */
    public static Date endOfYear(final Date date) {
        return new Date(nextYear(date).getTime() - 1);
    }

    /**
     * 获得下一年
     */
    public static Date nextYear(final Date date) {
        return DateUtils.ceiling(date, Calendar.YEAR);
    }

    /**
     * 获得上一年
     */
    public static Date previousYear(final Date date) {
        return new Date(beginOfDate(date).getTime() - 1);
    }

    /**
     * 获得月的开始
     */
    public static Date beginOfMonth(final Date date) {
        return DateUtils.truncate(date, Calendar.MONTH);
    }

    /**
     * 获得月的结束
     */
    public static Date endOfMonth(final Date date) {
        return new Date(nextMonth(date).getTime() - 1);
    }

    /**
     * 获得下个月
     */
    public static Date nextMonth(final Date date) {
        return DateUtils.ceiling(date, Calendar.MONTH);
    }

    /**
     * 获得上个月
     */
    public static Date previousMonth(final Date date) {
        return new Date(beginOfMonth(date).getTime() - 1);
    }

    /**
     * 获得一周的开始
     */
    public static Date beginOfWeek(final Date date) {
        return DateUtils.truncate(DateUtils.addDays(date, 1 - DateKits.getDayOfWeek(date)), Calendar.DATE);
    }

    /**
     * 获得一周的结束
     */
    public static Date endOfWeek(final Date date) {
        return new Date(nextWeek(date).getTime() - 1);
    }

    /**
     * 获得下一周
     */
    public static Date nextWeek(final Date date) {
        return DateUtils.truncate(DateUtils.addDays(date, 8 - DateKits.getDayOfWeek(date)), Calendar.DATE);
    }

    /**
     * 获得上一周
     */
    public static Date previousWeek(final Date date) {
        return new Date(beginOfWeek(date).getTime() - 1);
    }

    /**
     * 获得一天的开始
     */
    public static Date beginOfDate(final Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    /**
     * 获得一天的结束
     */
    public static Date endOfDate(final Date date) {
        return new Date(nextDate(date).getTime() - 1);
    }

    /**
     * 下一天
     */
    public static Date nextDate(final Date date) {
        return DateUtils.ceiling(date, Calendar.DATE);
    }

    /**
     * 上一天
     */
    public static Date previousDate(final Date date) {
        return new Date(beginOfDate(date).getTime() - 1);
    }

    /**
     * 一小时的开始
     */
    public static Date beginOfHour(final Date date) {
        return DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 一小时的结束
     */
    public static Date endOfHour(final Date date) {
        return new Date(nextHour(date).getTime() - 1);
    }

    /**
     * 下一个小时
     */
    public static Date nextHour(final Date date) {
        return DateUtils.ceiling(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 上一个小时
     */
    public static Date previousHour(final Date date) {
        return new Date(beginOfHour(date).getTime() - 1);
    }

    /**
     * 一分钟的开始
     */
    public static Date beginOfMinute(final Date date) {
        return DateUtils.truncate(date, Calendar.MINUTE);
    }

    /**
     * 一分钟的结束
     */
    public static Date endOfMinute(final Date date) {
        return new Date(nextMinute(date).getTime() - 1);
    }

    /**
     * 下一分钟
     */
    public static Date nextMinute(final Date date) {
        return DateUtils.ceiling(date, Calendar.MINUTE);
    }

    /**
     * 上一分钟
     */
    public static Date previousMinute(final Date date) {
        return new Date(beginOfMinute(date).getTime() - 1);
    }


    /**
     * 是否闰年.
     */
    public static boolean isLeapYear(final Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }

    /**
     * 是否闰年，copy from Jodd Core的TimeUtil
     * <p>
     * 参数是公元计数, 如2016
     */
    public static boolean isLeapYear(int y) {
        boolean result = false;

        if (((y % 4) == 0) && // must be divisible by 4...
                ((y < 1582) || // and either before reform year...
                        ((y % 100) != 0) || // or not a century...
                        ((y % 400) == 0))) { // or a multiple of 400...
            result = true; // for leap year.
        }
        return result;
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(final Date date) {
        int year = get(date, Calendar.YEAR);
        int month = get(date, Calendar.MONTH);
        return getMonthLength(year, month);
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(int year, int month) {

        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return MONTH_LENGTH[month];
    }
}
