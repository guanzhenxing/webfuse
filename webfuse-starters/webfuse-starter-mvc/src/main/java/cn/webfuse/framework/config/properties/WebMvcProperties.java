package cn.webfuse.framework.config.properties;

import cn.webfuse.core.constant.BaseErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * webfuse框架mvc的基本配置
 */
@ConfigurationProperties(prefix = "webfuse.mvc")
@Data
public class WebMvcProperties {

    /**
     * 在url上蛇形参数转换为驼峰
     */
    private boolean urlQueriesSnakeToCamel = false;

    /**
     * API版本控制
     */
    private ApiVersion apiVersion = new ApiVersion();

    /**
     * Restful异常处理
     */
    private RestfulExceptionHandle restfulExceptionHandle = new RestfulExceptionHandle();

    /**
     * cors
     */
    private Cors cors = new Cors();


    /**
     * xss
     */
    private Xss xss = new Xss();

    @Data
    @NoArgsConstructor
    public static class ApiVersion {
        /**
         * 是否使用API版本控制
         */
        private boolean enabled = true;
        /**
         * API版本控制前缀正则表达式
         */
        private String versionPrefix;
        /**
         * 最小的版本号
         */
        private int minimumVersion = 1;

        /**
         * 解析包版本
         */
        private boolean parsePackageVersion = true;

        /**
         * 版本号的标志
         */
        private String VersionFlag = "{version}";


    }

    @Data
    @NoArgsConstructor
    public static class RestfulExceptionHandle {

        /**
         * 是否使用Restful异常处理
         */
        private boolean enabled = true;

        /**
         * 默认的错误文档
         */
        private String defaultDocument;

        /**
         * 是否显示开发者错误信息
         */
        private boolean showDeveloperMessage = false;
        /**
         * 是否显示异常
         */
        private boolean showThrowable = false;

        /**
         * 是否显示hostId
         */
        private boolean showHostId = false;

        /**
         * 异常处理匹配
         */
        private List<Mapping> mappings = new ArrayList<>();

        @Data
        @NoArgsConstructor
        public static class Mapping {
            private String clazz;
            private int status = BaseErrorCode.SYSTEM_ERROR.getHttpStatus();
            private String code = BaseErrorCode.SYSTEM_ERROR.getCode();
            private String message = "";
            private String developerMessage = "";
            private String document = "";
        }

    }


    /**
     * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS
     */
    @Data
    @NoArgsConstructor
    public static class Cors {
        /**
         * 是否使用CORS
         */
        private boolean enabled = true;

        /**
         * 跨域信息
         */
        private List<RegistrationConfig> registrationConfig = new ArrayList<>();

        /**
         * 描述 : 跨域信息
         */
        @Data
        public static class RegistrationConfig {
            /**
             * 描述 : 扫描地址
             */
            private String mapping = "/**";

            /**
             * 描述 : 允许cookie
             */
            private Boolean allowCredentials = true;

            /**
             * 描述 : 允许的域
             */
            private String allowedOrigins = "*";

            /**
             * 描述 : 允许的方法
             */
            private String allowedMethods = "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE";

            /**
             * 描述 : 允许的头信息
             */
            private String allowedHeaders = "*";

            /**
             * 描述 : 请求的结果能够被缓存多久
             */
            private Long maxAge = 3600L;

            /**
             * 描述 : 让服务器把允许浏览器访问的头放入白名单
             */
            private String exposedHeaders = "";

        }
    }

    @Data
    @NoArgsConstructor
    public static class Xss {
        /**
         * 是否使用XSS
         */
        private boolean enabled = true;

        /**
         * 排除的url
         */
        private List<String> urlExclusion;

        /**
         * array of URL patterns to which this Filter applies
         */
        private List<String> urlPatterns = Arrays.asList("/*");

        /**
         * filter的名称
         */
        private String filterName = "xssFilter";
    }

}
