package cn.webfuse.framework.exception.handle.impl;

import cn.webfuse.framework.exception.handle.RestfulError;
import cn.webfuse.framework.exception.handle.RestfulErrorConverter;

/**
 * 默认的RestfulError转换器，默认转换为Map。也就是在这里输出异常的格式。<br/>
 * 默认显示：<br/>
 * code: 错误的代码<br/>
 * message: 错误的信息<br/>
 * server_time: 发生错误的服务器时间<br/>
 * developer_message：开发者信息<br/>
 * request_id: 请求的ID(如果有)<br/>
 * host_id: 服务器实例ID(如果有)<br/>
 * document: 错误解决的文档(如果有)<br/>
 *
 * @author Jesen
 */
public class DefaultRestfulErrorConverter implements RestfulErrorConverter<DefaultRestfulErrorVO> {


    @Override
    public DefaultRestfulErrorVO convert(RestfulError restfulError) {
        DefaultRestfulErrorVO vo = new DefaultRestfulErrorVO();
        vo.setCode(restfulError.getCode());
        vo.setServerTime(restfulError.getServerTime());
        vo.setMessage(restfulError.getMessage());
        vo.setDeveloperMessage(restfulError.getDeveloperMessage());
        vo.setHostId(restfulError.getHostId());
        vo.setRequestId(restfulError.getRequestId());
        vo.setDocument(restfulError.getDocument());
        return vo;
    }


}

