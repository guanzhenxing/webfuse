package cn.webfuse.framework.exception.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Restful错误解析器
 *
 * @author Jesen
 */
public interface RestfulErrorResolver {

    /**
     * 解析错误
     *
     * @param request  request请求
     * @param response response返回
     * @param handler  处理器
     * @param ex       待处理的异常
     * @return 处理后的RestfulError信息
     */
    RestfulError resolveError(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);

}
