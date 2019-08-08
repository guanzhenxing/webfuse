package cn.webfuse.framework.core.kit;

/**
 * 枚举工具集
 */
public class EnumKits {

    /**
     * String转换为Enum
     */
    public static <T extends Enum<T>> T fromString(Class<T> enumClass, String value) {
        return Enum.valueOf(enumClass, value);
    }

    /**
     * Enum转换为String
     */
    public static String toString(Enum e) {
        return e.name();
    }
}
