package cn.webfuse.framework.exception.handle.impl;

import cn.webfuse.core.kit.mapper.BeanMapper;
import cn.webfuse.framework.exception.handle.RestfulError;
import cn.webfuse.framework.exception.handle.RestfulErrorConverter;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 默认的RestfulError转换器，默认转换为Map。也就是在这里输出异常的格式。<br/>
 *
 * @author Jesen
 */
public class DefaultRestfulErrorConverter implements RestfulErrorConverter<DefaultRestfulErrorVO> {


    @Override
    public DefaultRestfulErrorVO convert(RestfulError restfulError) {
        DefaultRestfulErrorVO vo = new DefaultRestfulErrorVO();
        vo.setCode(restfulError.getCode());
        vo.setMessage(restfulError.getMessage());
        Map<String, Object> detail = BeanMapper.convertBeanToMap(restfulError.getDetail(), false, true);
        vo.setDetail(detail);
        return vo;
    }


}

