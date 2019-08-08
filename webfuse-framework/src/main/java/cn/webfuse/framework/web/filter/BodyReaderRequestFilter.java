package cn.webfuse.framework.web.filter;

import cn.webfuse.framework.kit.HttpServletKits;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 读取RequestBody的filter
 *
 * @author Jesen
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "bodyReaderRequestFilter")
public class BodyReaderRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("======>>> BodyReaderRequestFilter init.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        //这里获得ParameterMap，如果没有这个，会出现重复读流的问题
        httpServletRequest.getParameterMap();

        // 这里将原始request传入，读出流并存储
        ServletRequest requestWrapper = new BodyReaderRequestWrapper(httpServletRequest);
        // 这里将原始request替换为包装后的request，此后所有进入controller的request均为包装后的
        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private class BodyReaderRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        public BodyReaderRequestWrapper(HttpServletRequest request) {
            super(request);
            String bodyString = HttpServletKits.getBodyString(request);
            body = bodyString.getBytes(Charset.forName("UTF-8"));
        }


        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() {

            final ByteArrayInputStream bais = new ByteArrayInputStream(body);

            return new ServletInputStream() {

                @Override
                public int read() {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }
    }
}
