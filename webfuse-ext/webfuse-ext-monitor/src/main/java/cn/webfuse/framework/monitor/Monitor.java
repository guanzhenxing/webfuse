package cn.webfuse.framework.monitor;

import java.lang.annotation.*;

/**
 * 日志,在类或者方法上注解此类,将对方法进行访问日志记录
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Monitor {
    /**
     * @return 对类或方法的简单说明
     * @see MonitorInfo#getAction()
     */
    String value();

    /**
     * @return 对类或方法的详细描述
     * @see MonitorInfo#getDescribe()
     */
    String[] describe() default "";

    /**
     * @return 是否取消日志记录, 如果不想记录某些方法或者类, 设置为true即可
     */
    boolean ignore() default false;
}
