package cn.webfuse.data.datasource.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源上下文
 *
 * @author Jesen
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
    public static List<String> dataSourceNames = new ArrayList<>();

    public static void setDataSourceName(String name) {
        CONTEXT_HOLDER.set(name);
    }

    public static String getDataSourceName() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSourceName() {
        CONTEXT_HOLDER.remove();
    }

    public static boolean containsDataSource(String dataSourceName) {
        return dataSourceNames.contains(dataSourceName);
    }

}
