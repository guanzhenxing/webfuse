package cn.webfuse.data.datasource.dynamic;

import java.lang.annotation.*;

/**
 * 在方法上使用，用于指定使用哪个数据源
 * <p>
 * 用法：
 * <pre>
 *   {@literal @}TargetDataSource(value = "primaryDataSource")
 * </pre>
 * <p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}

