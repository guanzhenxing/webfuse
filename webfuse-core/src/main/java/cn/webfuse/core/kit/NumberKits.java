package cn.webfuse.core.kit;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

/**
 * 数字的工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.number.NumberUtil) and made some changes.
 */
public class NumberKits {

    private static final double DEFAULT_DOUBLE_EPSILON = 0.00001d;

    /**
     * 因为double的精度问题, 允许两个double在0.00001内的误差为相等。
     */
    public static boolean equalsWithin(double d1, double d2) {
        return Math.abs(d1 - d2) < DEFAULT_DOUBLE_EPSILON;
    }

    /**
     * 因为double的精度问题, 允许两个double在epsilon内的误差为相等
     */
    public static boolean equalsWithin(double d1, double d2, double epsilon) {
        return Math.abs(d1 - d2) < epsilon;
    }


    /**
     * 判断字符串是否合法数字
     */
    public static boolean isNumber(String str) {
        return NumberUtils.isCreatable(str);
    }

    /**
     * 判断字符串是否16进制
     */
    public static boolean isHexNumber(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }

        int index = value.startsWith("-") ? 1 : 0;
        return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index);
    }

    /**
     * 将给定的数字转换为给定目标类的实例
     *
     * @param number      将要转换的数字
     * @param targetClass 目标类
     * @return 转换后的数字
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see java.lang.Byte
     * @see java.lang.Short
     * @see java.lang.Integer
     * @see java.lang.Long
     * @see java.math.BigInteger
     * @see java.lang.Float
     * @see java.lang.Double
     * @see java.math.BigDecimal
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass)
            throws IllegalArgumentException {

        Validate.notNull(number, "Number must not be null");
        Validate.notNull(targetClass, "Target class must not be null");

        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (Byte.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Byte.valueOf(number.byteValue());
        } else if (Short.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Short.valueOf(number.shortValue());
        } else if (Integer.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Integer.valueOf(number.intValue());
        } else if (Long.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            return (T) Long.valueOf(value);
        } else if (BigInteger.class == targetClass) {
            if (number instanceof BigDecimal) {
                // do not lose precision - use BigDecimal's own conversion
                return (T) ((BigDecimal) number).toBigInteger();
            } else {
                // original value is not a Big* number - use standard long conversion
                return (T) BigInteger.valueOf(number.longValue());
            }
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(number.floatValue());
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(number.doubleValue());
        } else if (BigDecimal.class == targetClass) {
            // always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
            // (see BigDecimal javadoc for details)
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                    number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Parse the given {@code text} into a {@link Number} instance of the given
     * target class, using the corresponding {@code decode} / {@code valueOf} method.
     * <p>Trims the input {@code String} before attempting to parse the number.
     * <p>Supports numbers in hex format (with leading "0x", "0X", or "#") as well.
     *
     * @param text        the text to convert
     * @param targetClass the target class to parse into
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see Byte#decode
     * @see Short#decode
     * @see Integer#decode
     * @see Long#decode
     * @see #decodeBigInteger(String)
     * @see Float#valueOf
     * @see Double#valueOf
     * @see java.math.BigDecimal#BigDecimal(String)
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        Validate.notNull(text, "Text must not be null");
        Validate.notNull(targetClass, "Target class must not be null");
        String trimmed = StringKits.trimAllWhitespace(text);

        if (Byte.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
        } else if (Short.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
        } else if (Integer.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
        } else if (Long.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
        } else if (BigInteger.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        } else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) new BigDecimal(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Parse the given {@code val} into a {@link Number} instance of the given
     * target class, the default value is returned if an error occurs.
     * <p>Trims the input {@code Object} before attempting to parse the number.
     * <p>Supports numbers in hex format (with leading "0x", "0X", or "#") as well.
     *
     * @param val          an object to convert
     * @param targetClass  the target class to parse into
     * @param defaultValue an default value
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see Byte#decode
     * @see Short#decode
     * @see Integer#decode
     * @see Long#decode
     * @see #decodeBigInteger(String)
     * @see Float#valueOf
     * @see Double#valueOf
     * @see java.math.BigDecimal#BigDecimal(String)
     */
    public static <T extends Number> T parseNumber(Object val, Class<T> targetClass, T defaultValue) {
        if (Objects.isNull(val)) {
            return defaultValue;
        }
        String str = Objects.toString(val);
        try {
            return parseNumber(str, targetClass);
        } catch (IllegalArgumentException ex) {
            return defaultValue;
        }
    }

    /**
     * Parse the given {@code val} into a {@link Number} instance of the given
     * target class, the default value is returned if an error occurs.
     * <p>Trims the input {@code Object} before attempting to parse the number.
     * <p>Supports numbers in hex format (with leading "0x", "0X", or "#") as well.
     *
     * @param val         an object to convert
     * @param targetClass the target class to parse into
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see Byte#decode
     * @see Short#decode
     * @see Integer#decode
     * @see Long#decode
     * @see #decodeBigInteger(String)
     * @see Float#valueOf
     * @see Double#valueOf
     * @see java.math.BigDecimal#BigDecimal(String)
     */
    public static <T extends Number> T parseNumber(Object val, Class<T> targetClass) {
        if (Objects.isNull(val)) {
            throw ExceptionKits.unchecked(new IllegalArgumentException());
        }
        String str = Objects.toString(val);
        try {
            return parseNumber(str, targetClass);
        } catch (IllegalArgumentException ex) {
            throw ExceptionKits.unchecked(new IllegalArgumentException());
        }
    }

    /////// toString ///////
    // 定义了原子类型与对象类型的参数，保证不会用错函数会导致额外AutoBoxing转换//

    public static String toString(int i) {
        return Integer.toString(i);
    }

    public static String toString(Integer i) {
        return i.toString();
    }

    public static String toString(float f) {
        return Float.toString(f);
    }

    public static String toString(Float f) {
        return Float.toString(f);
    }

    public static String toString(long l) {
        return Long.toString(l);
    }

    public static String toString(Long l) {
        return l.toString();
    }

    public static String toString(double d) {
        return Double.toString(d);
    }

    public static String toString(Double d) {
        return d.toString();
    }

    /**
     * 输出格式化为小数后两位的double字符串
     */
    public static String to2DigitString(double d) {
        return String.format(Locale.ROOT, "%.2f", d);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(Number number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = 0;
        }
        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }
        BigDecimal num = convertNumberToTargetClass(number, BigDecimal.class);

        return num.setScale(scale, roundingMode);
    }

    /**
     * 保留固定位数小数<br>
     * 例如保留四位小数：123.456789 =》 123.4567
     *
     * @param number       数字值
     * @param scale        保留小数位数，如果传入小于0，则默认0
     * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
     * @return 新值
     */
    public static BigDecimal round(String number, int scale, RoundingMode roundingMode) {
        return round(NumberUtils.createNumber(number), scale, roundingMode);
    }

    /////////// 杂项 ///////

    /**
     * 安全的将小于Integer.MAX的long转为int，否则抛出IllegalArgumentException异常
     */
    public static int toInt32(long x) {
        if ((int) x == x) {
            return (int) x;
        }
        throw new IllegalArgumentException("Int " + x + " out of range");
    }

    ///////////// bytes[] 与原始类型数字转换 ///////

    public static byte[] toBytes(int value) {
        return Ints.toByteArray(value);
    }

    public static byte[] toBytes(long value) {
        return Longs.toByteArray(value);
    }

    /**
     * copy from ElasticSearch Numbers
     */
    public static byte[] toBytes(double val) {
        return toBytes(Double.doubleToRawLongBits(val));
    }

    public static int toInt(byte[] bytes) {
        return Ints.fromByteArray(bytes);
    }

    public static long toLong(byte[] bytes) {
        return Longs.fromByteArray(bytes);
    }

    /**
     * copy from ElasticSearch Numbers
     */
    public static double toDouble(byte[] bytes) {
        return Double.longBitsToDouble(toLong(bytes));
    }

    /**
     * Check for a {@code BigInteger}/{@code BigDecimal} long overflow
     * before returning the given number as a long value.
     *
     * @param number      the number to convert
     * @param targetClass the target class to convert to
     * @return the long value, if convertible without overflow
     * @throws IllegalArgumentException if there is an overflow
     * @see #raiseOverflowException
     */
    private static long checkedLongValue(Number number, Class<? extends Number> targetClass) {
        BigInteger bigInt = null;
        if (number instanceof BigInteger) {
            bigInt = (BigInteger) number;
        } else if (number instanceof BigDecimal) {
            bigInt = ((BigDecimal) number).toBigInteger();
        }
        // Effectively analogous to JDK 8's BigInteger.longValueExact()
        if (bigInt != null &&
                (bigInt.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0 || bigInt.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)) {
            raiseOverflowException(number, targetClass);
        }
        return number.longValue();
    }

    /**
     * Raise an <em>overflow</em> exception for the given number and target class.
     *
     * @param number      the number we tried to convert
     * @param targetClass the target class we tried to convert to
     * @throws IllegalArgumentException if there is an overflow
     */
    private static void raiseOverflowException(Number number, Class<?> targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    /**
     * Decode a {@link java.math.BigInteger} from the supplied {@link String} value.
     * <p>Supports decimal, hex, and octal notation.
     *
     * @see BigInteger#BigInteger(String, int)
     */
    private static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;

        // Handle minus sign, if present.
        if (value.startsWith("-")) {
            negative = true;
            index++;
        }

        // Handle radix specifier, if present.
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith("#", index)) {
            index++;
            radix = 16;
        } else if (value.startsWith("0", index) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return (negative ? result.negate() : result);
    }
}
