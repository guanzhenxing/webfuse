package cn.webfuse.framework.web.version;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义一个条件筛选器，让SpringMVC在原有逻辑的基本上添加一个版本号匹配的规则
 * <p>
 * 参考：https://www.hifreud.com/2018/01/30/01-API-versioning/
 *
 * @author Jesen
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * 路径中版本的前缀， 这里用 vxx的形式
     */
    private final static Pattern DEFAULT_VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d+)/");

    private int apiVersion;

    private Pattern versionPrefixPattern;


    public ApiVersionCondition(int apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition condition) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(condition.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = getVersionPrefixPattern().matcher(request.getRequestURI());
        if (m.find()) {
            Integer version = Integer.valueOf(m.group(1));
            // 如果请求的版本号大于配置版本号， 则满足
            if (version >= this.apiVersion) {
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition condition, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return condition.getApiVersion() - this.apiVersion;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public Pattern getVersionPrefixPattern() {
        return versionPrefixPattern != null ? versionPrefixPattern : DEFAULT_VERSION_PREFIX_PATTERN;
    }

    public void setVersionPrefixPattern(Pattern versionPrefixPattern) {
        this.versionPrefixPattern = versionPrefixPattern;
    }

}
