package cn.webfuse.framework.exception.handle;


import org.springframework.core.convert.converter.Converter;

/**
 * RestfulError转换器，将RestfulError对象转换成其他对象
 *
 * @author Jesen
 */
public interface RestfulErrorConverter<T> extends Converter<RestfulError, T> {

    /**
     * 将RestfulError转出其他
     *
     * @param source
     * @return
     */
    @Override
    T convert(RestfulError source);
}
