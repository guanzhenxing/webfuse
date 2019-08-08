package cn.webfuse.framework.web.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * 接口版本标识注解
 *
 * @author Jesen
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {
    /**
     * header 路径中的版本
     *
     * @return 版本号
     */
    int value();
}
