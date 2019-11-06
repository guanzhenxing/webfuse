package cn.webfuse.core.kit.date;

import cn.webfuse.core.kit.ExceptionKits;
import cn.webfuse.core.kit.text.RegexKits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 日期格式化工具
 * <p>
 * https://www.ibm.com/support/knowledgecenter/zh/SSMKHH_9.0.0/com.ibm.etools.mft.doc/ak05616_.htm
 */
public class DateFormatKits {

    private final static List<DatePatternFormatter> supportFormatter = Arrays.asList(
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
        return pattern == null ? null : parseDate(pattern, dateString);
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


    @AllArgsConstructor
    @Getter
    private static class DatePatternFormatter {
        private Pattern pattern;
        private String formatter;
    }


}
