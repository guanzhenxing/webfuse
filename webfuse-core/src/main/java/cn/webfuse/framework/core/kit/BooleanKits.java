package cn.webfuse.framework.core.kit;

import org.apache.commons.lang3.BooleanUtils;

/**
 * Boolean工具类
 */
public class BooleanKits {

    /**
     * 支持true/false,on/off, y/n, yes/no的转换, str为空或无法分析时返回defaultValue
     *
     * @param str         true/false,on/off, y/n, yes/no
     * @param valueIfNull 默认值
     * @return {@code true} or {@code false}
     */
    public static Boolean toBooleanDefaultIfNull(final String str, final boolean valueIfNull) {
        return BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(str), valueIfNull);
    }

}
