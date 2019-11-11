package cn.webfuse.data.datasource.dynamic;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(DynamicDataSourceRegister.class)
public @interface EnableDynamicDataSource {
}
