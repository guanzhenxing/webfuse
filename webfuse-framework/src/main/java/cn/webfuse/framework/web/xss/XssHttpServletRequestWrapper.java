package cn.webfuse.framework.web.xss;

import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * XssHttpServletRequestWrapper
 *
 * @author Jesen
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
     */
    HttpServletRequest originalRequest;


    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
        this.originalRequest = servletRequest;
    }

    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        if (queryString == null) {
            return null;
        }
        return cleanXSS(queryString);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = cleanXSS(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        return StringEscapeUtils.escapeHtml4(value.trim());
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOriginalRequest() {
        return originalRequest;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOriginalRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOriginalRequest();
        }
        return request;
    }


}
