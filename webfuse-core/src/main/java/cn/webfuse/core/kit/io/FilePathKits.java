package cn.webfuse.core.kit.io;

import cn.webfuse.core.kit.text.StringKits;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * copy from vjtools
 * <p>
 * 关于文件路径的工具集. 这个类只适合处理纯字符串的路径，如果是File对象或者Path对象的路径处理，建议直接使用Path类的方法。
 *
 * @see {@link java.nio.file.Path}
 */
public class FilePathKits {

    /**
     * 在Windows环境里，兼容Windows上的路径分割符，将 '/' 转回 '\'
     */
    public static String normalizePath(String path) {
        if (File.separatorChar == '\\'
                && StringUtils.indexOf(path, '/') != -1) {
            return StringUtils.replaceChars(path, '/', '\\');
        }
        return path;

    }

    /**
     * 将路径整理，如 "a/../b"，整理成 "b"
     */
    public static String simplifyPath(String path) {
        return Files.simplifyPath(path);
    }

    /**
     * 以拼接路径名
     */
    public static String concat(String baseName, String... appendName) {
        if (appendName.length == 0) {
            return baseName;
        }

        StringBuilder concatName = new StringBuilder();
        if (StringKits.endWith(baseName, File.separatorChar)) {
            concatName.append(baseName).append(appendName[0]);
        } else {
            concatName.append(baseName).append(File.separatorChar).append(appendName[0]);
        }

        if (appendName.length > 1) {
            for (int i = 1; i < appendName.length; i++) {
                concatName.append(File.separatorChar).append(appendName[i]);
            }
        }

        return concatName.toString();
    }

    /**
     * 获得上层目录的路径
     */
    public static String getParentPath(String path) {
        String parentPath = path;

        if (File.separator.equals(parentPath)) {
            return parentPath;
        }

        parentPath = StringKits.removeEnd(parentPath, File.separatorChar);

        int idx = parentPath.lastIndexOf(File.separatorChar);
        if (idx >= 0) {
            parentPath = parentPath.substring(0, idx + 1);
        } else {
            parentPath = File.separator;
        }

        return parentPath;
    }

    /**
     * 获得参数clazz所在的Jar文件的绝对路径
     */
    public static String getJarPath(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

}
