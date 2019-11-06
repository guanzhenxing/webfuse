package cn.webfuse.framework.web.xss;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 防止XSS攻击的过滤器
 *
 * @author Jesen
 */
@Slf4j
public class XssFilter implements Filter {

    private FilterConfig filterConfig = null;
    private List<String> urlExclusion = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        log.info("======>>> XssFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String servletPath = httpServletRequest.getServletPath();

        if (getUrlExclusion() != null && getUrlExclusion().contains(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    public List<String> getUrlExclusion() {
        return urlExclusion;
    }

    public void setUrlExclusion(List<String> urlExclusion) {
        this.urlExclusion = urlExclusion;
    }
}
