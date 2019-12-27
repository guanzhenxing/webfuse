package cn.webfuse.framework.web.filter;

import cn.webfuse.framework.kit.HttpServletKits;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在Header中添加X-Request-Id的值
 *
 * @author Jesen
 */
//@WebFilter(filterName = "requestIdSettingFilter", value = "/*")
@Slf4j
public class RequestIdSettingFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) {
        log.info("======>>> RequestIdSettingFilter init.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        RequestIdSettingRequestWrapper requestWrapper = new RequestIdSettingRequestWrapper((HttpServletRequest) servletRequest);

        String xRequestId = ((HttpServletRequest) servletRequest).getHeader("X-Request-Id");
        //如果还没有X-Request-Id，则进行设置
        if (StringUtils.isEmpty(xRequestId)) {
            requestWrapper.addHeader("X-Request-Id", HttpServletKits.buildRequestId());
        }

        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private static class RequestIdSettingRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String> customHeaders;

        public RequestIdSettingRequestWrapper(HttpServletRequest request) {
            super(request);
            customHeaders = new ConcurrentHashMap<>();
        }

        public void addHeader(String name, String value) {
            customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String value = this.customHeaders.get(name);
            if (value != null) {
                return value;
            }
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> set = new HashSet<>(customHeaders.keySet());
            Enumeration<String> enumeration = ((HttpServletRequest) getRequest()).getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                set.add(name);
            }
            return Collections.enumeration(set);
        }
    }
}



