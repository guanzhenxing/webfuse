package cn.webfuse.framework.core.kit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 日期格式化工具
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.time.DateFormatUtil) and made some changes.
 * <p>
 * https://www.ibm.com/support/knowledgecenter/zh/SSMKHH_9.0.0/com.ibm.etools.mft.doc/ak05616_.htm
 */
public class DateFormatKits {

    private final static List<DatePatternFormatter> supportFormatter = ArrayKits.asList(
            new DatePatternFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}"), "yyyyMMdd"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}"), "yyyy-MM-dd"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2}"), "yyyy/MM/dd"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日"), "yyyy年MM月dd日"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日"), "yyyy年M月d日"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{1,2}日"), "yyyy年MM月d日"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}年[0-9]{1,2}月[0-9]{2}日"), "yyyy年M月dd日"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}"), "yyyy-M-d"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{1,2}"), "yyyy-MM-d"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{2}"), "yyyy-M-dd"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}"), "yyyy/M/d"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{1,2}"), "yyyy/MM/d"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{1,2}/[0-9]{2}"), "yyyy/M/dd"),
            new DatePatternFormatter(Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}"), "MM/dd/yyyy"),
            new DatePatternFormatter(Pattern.compile("[0-9]{1,2}/[0-9]{2}/[0-9]{4}"), "M/dd/yyyy"),
            new DatePatternFormatter(Pattern.compile("[0-9]{2}/[0-9]{1,2}/[0-9]{4}"), "MM/d/yyyy"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-d HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"), "yyyy-M-d H:m:s"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-d HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-M-dd HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{1,2}-[0-9]{2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"), "yyyy-M-dd H:m:s"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"), "yyyy-MM-d H:m:s"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/dd HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/d HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"), "yyyy/M/d H:m:s"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/MM/d HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{1,2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy/M/dd HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{1,2}/[0-9]{2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"), "yyyy/M/dd H:m:s"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}/[0-9]{2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}"), "yyyy/MM/d H:m:s"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日[0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日HH时mm分ss秒"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}年[0-9]{2}月[0-9]{2}日 [0-9]{2}时[0-9]{2}分[0-9]{2}秒"), "yyyy年MM月dd日 HH时mm分ss秒"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyyMMdd HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMddHHmmss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2} [0-9]{2}[0-9]{2}[0-9]{2}"), "yyyyMMdd HHmmss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{2}:[0-9]{2}:[0-9]{2}"), "HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}"), "yyyy-MM-dd HH:mm:ss.SSS"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd'T'HH:mm:ss"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\+|-)[0-9]{4}"), "yyyy-MM-dd'T'HH:mm:ssZ"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}(\\+|-)[0-9]{4}"), "yyyy-MM-dd HH:mm:ssZ"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}(\\+|-)[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd'T'HH:mm:ssZZ"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}(\\+|-)[0-9]{4}"), "yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
            new DatePatternFormatter(Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3}(\\+|-)[0-9]{2}:[0-9]{2}"), "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")

//            "EEE MMM dd HH:mm:ss zzz yyyy"
//            MMM d, yyyy h:m:s aa

    );


    /**
     * 分析日期字符串, 仅用于pattern不固定的情况.
     */
    public static Date parseDate(String pattern, String dateString) {
        try {
            return FastDateFormat.getInstance(pattern).parse(dateString);
        } catch (ParseException e) {
            throw ExceptionKits.unchecked(e);
        }
    }

    /**
     * 字符串类似的时间转Date
     *
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        String pattern = getFormatter(dateString);
        System.out.println(pattern);
        return pattern == null ? null : parseDate(pattern, dateString);
    }

    private static String getFormatter(String dateString) {

        DatePatternFormatter datePatternFormatter = supportFormatter.stream()
                .filter(dpf -> RegexKits.matches(dpf.getPattern().pattern(), dateString))
                .findFirst()
                .orElse(null);

        if (datePatternFormatter == null) {
            return null;
        }

        return datePatternFormatter.getFormatter();
    }

    /**
     * 格式化日期, 仅用于pattern不固定的情况.
     */
    public static String formatDate(String pattern, Date date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 格式化日期, 仅用于不固定pattern不固定的情况.
     */
    public static String formatDate(String pattern, long date) {
        return FastDateFormat.getInstance(pattern).format(date);
    }


    /////// 格式化间隔时间/////////

    /**
     * 按HH:mm:ss.SSS格式，格式化时间间隔.
     * <p>
     * endDate必须大于startDate，间隔可大于1天，
     *
     * @see DurationFormatUtils
     */
    public static String formatDuration(Date startDate, Date endDate) {
        return DurationFormatUtils.formatDurationHMS(endDate.getTime() - startDate.getTime());
    }

    /**
     * 按HH:mm:ss.SSS格式，格式化时间间隔
     * <p>
     * 单位为毫秒，必须大于0，可大于1天
     *
     * @see DurationFormatUtils
     */
    public static String formatDuration(long durationMillis) {
        return DurationFormatUtils.formatDurationHMS(durationMillis);
    }

    /**
     * 按HH:mm:ss格式，格式化时间间隔
     * <p>
     * endDate必须大于startDate，间隔可大于1天
     *
     * @see DurationFormatUtils
     */
    public static String formatDurationOnSecond(Date startDate, Date endDate) {
        return DurationFormatUtils.formatDuration(endDate.getTime() - startDate.getTime(), "HH:mm:ss");
    }

    /**
     * 按HH:mm:ss格式，格式化时间间隔
     * <p>
     * 单位为毫秒，必须大于0，可大于1天
     *
     * @see DurationFormatUtils
     */
    public static String formatDurationOnSecond(long durationMillis) {
        return DurationFormatUtils.formatDuration(durationMillis, "HH:mm:ss");
    }

    //////// 打印用于页面显示的用户友好，与当前时间比的时间差

    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     * <p>
     * copy from AndroidUtilCode
     */
    public static String formatFriendlyTimeSpanByNow(Date date) {
        return formatFriendlyTimeSpanByNow(date.getTime());
    }

    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     * <p>
     * copy from AndroidUtilCode
     */
    public static String formatFriendlyTimeSpanByNow(long timeStampMillis) {
        long now = System.currentTimeMillis();
        long span = now - timeStampMillis;
        if (span < 0) {
            // 'c' 日期和时间，被格式化为 "%ta %tb %td %tT %tZ %tY"，例如 "Sun Jul 20 16:17:00 EDT 1969"。
            return String.format("%tc", timeStampMillis);
        }
        if (span < DateKits.MILLIS_PER_SECOND) {
            return "刚刚";
        } else if (span < DateKits.MILLIS_PER_MINUTE) {
            return String.format("%d秒前", span / DateKits.MILLIS_PER_SECOND);
        } else if (span < DateKits.MILLIS_PER_HOUR) {
            return String.format("%d分钟前", span / DateKits.MILLIS_PER_MINUTE);
        }
        // 获取当天00:00
        long wee = DateKits.beginOfDate(new Date(now)).getTime();
        if (timeStampMillis >= wee) {
            // 'R' 24 小时制的时间，被格式化为 "%tH:%tM"
            return String.format("今天%tR", timeStampMillis);
        } else if (timeStampMillis >= wee - DateKits.MILLIS_PER_DAY) {
            return String.format("昨天%tR", timeStampMillis);
        } else {
            // 'F' ISO 8601 格式的完整日期，被格式化为 "%tY-%tm-%td"。
            return String.format("%tF", timeStampMillis);
        }
    }


    @AllArgsConstructor
    @Getter
    private static class DatePatternFormatter {
        private Pattern pattern;
        private String formatter;
    }
}
