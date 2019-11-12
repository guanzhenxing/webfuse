package cn.webfuse.framework.web.version;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * api版本控制的HandlerMapping
 *
 * @author Jesen
 */
@Slf4j
public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    /**
     * 版本控制的前缀
     */
    private String versionPrefix;

    /**
     * 最小版本号
     */
    private int minimumVersion;

    /**
     * 自动解析包名，获取版本号
     */
    private boolean parsePackageVersion;

    /**
     * 自动解析包名，获取版本号
     */
    private String versionFlag = "{version}";
    /**
     * 版本的正则
     */
    private final static Pattern DEFAULT_VERSION_PREFIX_PATTERN = Pattern.compile(".*v(\\d+).*");


    @Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        return createCondition(handlerType);
    }

    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        return createCondition(method.getClass());
    }

    private RequestCondition<ApiVersionCondition> createCondition(Class<?> clazz) {

        RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
        if (classRequestMapping == null) {
            return null;
        }
        StringBuilder mappingUrlBuilder = new StringBuilder();
        if (classRequestMapping.value().length > 0) {
            mappingUrlBuilder.append(classRequestMapping.value()[0]);
        }
        String mappingUrl = mappingUrlBuilder.toString();
        if (!mappingUrl.contains(versionFlag)) {
            return null;
        }

        ApiVersion apiVersion = AnnotationUtils.findAnnotation(clazz, ApiVersion.class);
        if (apiVersion == null) {
            return null;
        }
        ApiVersionCondition apiVersionCondition = new ApiVersionCondition(
                new ApiVersionState.ApiVersionStateBuilder()
                        .apiVersion(apiVersion)
                        .packageVersion(parseVersionByPackage(clazz))
                        .minimumVersion(minimumVersion)
                        .build());
        if (versionPrefix != null) {
            apiVersionCondition.setVersionPrefixPattern(Pattern.compile(versionPrefix));
        }
        return apiVersionCondition;
    }

    /**
     * 通过包名解析出版本号
     *
     * @param clazz 类
     * @return 版本号/null
     */
    private Integer parseVersionByPackage(Class<?> clazz) {
        //如果关闭了自动解析包名，直接返回null
        if (!this.parsePackageVersion) {
            return null;
        }
        Matcher m = DEFAULT_VERSION_PREFIX_PATTERN.matcher(clazz.getPackage().getName());
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return null;
    }


    public void setVersionPrefix(String versionPrefix) {
        this.versionPrefix = versionPrefix;
    }

    public void setMinimumVersion(int minimumVersion) {
        this.minimumVersion = minimumVersion;
    }

    public void setParsePackageVersion(boolean parsePackageVersion) {
        this.parsePackageVersion = parsePackageVersion;
    }

    public void setVersionFlag(String versionFlag) {
        this.versionFlag = versionFlag;
    }
}
