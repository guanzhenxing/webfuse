package cn.webfuse.core.kit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 关于Properties的工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.base.PropertiesUtil) and made some changes.
 */
public class PropertiesKits {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesKits.class);

    /////////////////// 读取Properties ////////////////////

    public static Boolean getBoolean(Properties p, String name, Boolean defaultValue) {
        return BooleanKits.toBooleanDefaultIfNull(p.getProperty(name), defaultValue);
    }

    public static Integer getInt(Properties p, String name, Integer defaultValue) {
        return NumberKits.parseNumber(p.getProperty(name), Integer.class, defaultValue);
    }

    public static Long getLong(Properties p, String name, Long defaultValue) {
        return NumberKits.parseNumber(p.getProperty(name), Long.class, defaultValue);
    }

    public static Double getDouble(Properties p, String name, Double defaultValue) {
        return NumberKits.parseNumber(p.getProperty(name), Double.class, defaultValue);
    }

    public static Float getFloat(Properties p, String name, Float defaultValue) {
        return NumberKits.parseNumber(p.getProperty(name), Float.class, defaultValue);
    }

    public static String getString(Properties p, String name, String defaultValue) {
        return p.getProperty(name, defaultValue);
    }

    /////////// 加载Properties////////

    /**
     * 从文件路径加载properties. 默认使用utf-8编码解析文件
     * <p>
     * 路径支持从外部文件或resources文件加载, "file://"或无前缀代表外部文件, "classpath:"代表resources
     */
    public static Properties loadFromFile(String generalPath) {
        Properties p = new Properties();
        try (Reader reader = new InputStreamReader(URLResourceKits.asStream(generalPath), StandardCharsets.UTF_8)) {
            p.load(reader);
        } catch (IOException e) {
            LOGGER.warn("Load property from " + generalPath + " failed", e);
        }
        return p;
    }

    /**
     * 从字符串内容加载Properties
     */
    public static Properties loadFromString(String content) {
        Properties p = new Properties();
        try (Reader reader = new StringReader(content)) {
            p.load(reader);
        } catch (IOException ignored) { // NOSONAR
        }
        return p;
    }

}
