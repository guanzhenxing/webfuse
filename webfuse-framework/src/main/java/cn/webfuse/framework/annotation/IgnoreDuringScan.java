package cn.webfuse.framework.annotation;

/**
 * 在Spring自动扫描的时候忽略。
 * <p>
 * 使用：
 * <pre>
 * {@literal @}SpringBootApplication
 * {@literal @}ComponentScan(excludeFilters = @ComponentScan.Filter(IgnoreDuringScan.class))
 * public class Application {
 *    public static void main(String[] args) {
 *         SpringApplication.run(Application.class, args);
 *    }
 * }
 * </pre>
 *
 * @author Jesen
 */
public @interface IgnoreDuringScan {
}

