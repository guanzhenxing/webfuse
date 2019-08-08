package cn.webfuse.framework.core.kit;

import cn.webfuse.framework.core.kit.reflect.ClassLoaderKits;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 针对Jar包内的文件的工具类
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.io.ResourceUtil) and made some changes.
 */
public class ResourceKits {

    // 打开单个文件////

    /**
     * 读取规则见本类注释.
     */
    public static URL asUrl(String resourceName) {
        return Resources.getResource(resourceName);
    }

    /**
     * 读取规则见本类注释.
     */
    public static URL asUrl(Class<?> contextClass, String resourceName) {
        return Resources.getResource(contextClass, resourceName);
    }

    /**
     * 读取规则见本类注释.
     */
    public static InputStream asStream(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static InputStream asStream(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.getResource(contextClass, resourceName).openStream();
    }

    ////// 读取单个文件内容／／／／／

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), StandardCharsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName), StandardCharsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), StandardCharsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static List<String> toLines(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName), StandardCharsets.UTF_8);
    }

    ///////////// 打开所有同名文件///////

    public static List<URL> getResourcesQuietly(String resourceName) {
        return getResourcesQuietly(resourceName, ClassLoaderKits.getDefaultClassLoader());
    }

    public static List<URL> getResourcesQuietly(String resourceName, ClassLoader contextClassLoader) {
        try {
            Enumeration<URL> urls = contextClassLoader.getResources(resourceName);
            List<URL> list = new ArrayList<>(10);
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
            return list;
        } catch (IOException e) {// NOSONAR
            return Collections.emptyList();
        }
    }
}
