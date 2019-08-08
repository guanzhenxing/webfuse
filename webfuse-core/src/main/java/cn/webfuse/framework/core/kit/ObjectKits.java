package cn.webfuse.framework.core.kit;

import cn.webfuse.framework.core.kit.reflect.ClassKits;
import com.google.common.base.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Object工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.base.ObjectUtil) and made some changes.
 */
public class ObjectKits {

    private static final String NULL = "null";

    /**
     * JDK7 引入的Null安全的equals
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equal(a, b);
    }

    /**
     * 多个对象的HashCode串联, 组成新的HashCode
     */
    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }


    /**
     * 将对象转为指定的类型
     * <br/>
     * 支持日期，数字，boolean类型转换
     *
     * @param value 需要转换的值
     * @param type  目标类型
     * @return 转换后的值
     */
    public static final <T> T cast(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        Object newVal = null;

        if (ClassKits.instanceOf(value.getClass(), type)) {
            newVal = value;
        } else if (type == Byte.class || type == byte.class) {
            newVal = NumberKits.parseNumber(value, Byte.class);
        } else if (type == Short.class || type == short.class) {
            newVal = NumberKits.parseNumber(value, Short.class);
        } else if (type == Integer.class || type == int.class) {
            newVal = NumberKits.parseNumber(value, Integer.class);
        } else if (type == Long.class || type == long.class) {
            newVal = NumberKits.parseNumber(value, Long.class);
        } else if (type == Float.class || type == float.class) {
            newVal = NumberKits.parseNumber(value, Float.class);
        } else if (type == Double.class || type == double.class || type == Float.class || type == float.class) {
            newVal = NumberKits.parseNumber(value, Double.class);
        } else if (BigInteger.class == type) {
            return (T) NumberKits.parseNumber(value, BigInteger.class);
        } else if (BigDecimal.class == type || Number.class == type) {
            return (T) NumberKits.parseNumber(value, BigDecimal.class);
        } else if (type == Boolean.class || type == boolean.class) {
            newVal = Boolean.valueOf(String.valueOf(value));
        } else if (type == String.class) {
            newVal = String.valueOf(value);
        }
        return (T) newVal;
    }

    /**
     * 对象的toString(), 处理了对象为数组的情况，JDK的默认toString()只打数组的地址如 "[Ljava.lang.Integer;@490d6c15.
     */
    public static String toPrettyString(Object value) {
        if (value == null) {
            return NULL;
        }

        Class<?> type = value.getClass();

        if (type.isArray()) {
            Class componentType = type.getComponentType();

            if (componentType.isPrimitive()) {
                return primitiveArrayToString(value, componentType);
            } else {
                return objectArrayToString(value);
            }
        } else if (value instanceof Iterable) {
            // 因为Collection的处理也是默认调用元素的toString(),
            // 为了处理元素是数组的情况，同样需要重载
            return collectionToString(value);
        }

        return value.toString();
    }

    private static String primitiveArrayToString(Object value, Class componentType) {
        StringBuilder sb = new StringBuilder();

        if (componentType == int.class) {
            sb.append(Arrays.toString((int[]) value));
        } else if (componentType == long.class) {
            sb.append(Arrays.toString((long[]) value));
        } else if (componentType == double.class) {
            sb.append(Arrays.toString((double[]) value));
        } else if (componentType == float.class) {
            sb.append(Arrays.toString((float[]) value));
        } else if (componentType == boolean.class) {
            sb.append(Arrays.toString((boolean[]) value));
        } else if (componentType == short.class) {
            sb.append(Arrays.toString((short[]) value));
        } else if (componentType == byte.class) {
            sb.append(Arrays.toString((byte[]) value));
        } else if (componentType == char.class) {
            sb.append(Arrays.toString((char[]) value));
        } else {
            throw new IllegalArgumentException("unsupport array type");
        }
        return sb.toString();
    }

    private static String objectArrayToString(Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        Object[] array = (Object[]) value;
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(toPrettyString(array[i]));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String collectionToString(Object value) {
        Iterable iterable = (Iterable) value;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        int i = 0;
        for (Object o : iterable) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(toPrettyString(o));
            i++;
        }
        sb.append('}');
        return sb.toString();
    }

}
