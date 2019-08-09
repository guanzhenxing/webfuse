package cn.webfuse.core.kit.mapper;

import cn.webfuse.core.kit.ArrayKits;
import cn.webfuse.core.kit.reflect.ReflectionKits;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现深度的BeanOfClasssA<->BeanOfClassB复制
 * <p>
 * copy from vipshop VJTools(com.vip.vjtools.vjkit.mapper.BeanMapper) and made some changes.
 */
public class BeanMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * 简单的复制出新类型对象.
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 简单的复制出新对象ArrayList
     */
    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        List<D> destinationList = new ArrayList<D>();
        for (S source : sourceList) {
            if (source != null) {
                destinationList.add(mapper.map(source, destinationClass));
            }
        }
        return destinationList;
    }

    /**
     * 简单复制出新对象数组
     */
    public static <S, D> D[] mapArray(final S[] sourceArray, final Class<D> destinationClass) {
        D[] destinationArray = ArrayKits.newArray(destinationClass, sourceArray.length);
        int i = 0;
        for (S source : sourceArray) {
            if (source != null) {
                destinationArray[i] = mapper.map(sourceArray[i], destinationClass);
                i++;
            }
        }
        return destinationArray;
    }

    /**
     * Bean对象转换为Map
     */
    public static <T> Map<String, T> convertBeanToMap(Object source) {
        return convertBeanToMap(source, false);
    }

    /**
     * 将Bean对象转换为Map，并提供是否忽略父属性的参数
     */
    public static <T> Map<String, T> convertBeanToMap(Object source, boolean ignoreParent) {
        return convertBeanToMap(source, ignoreParent, false);
    }

    /**
     * 将Bean对象转换为Map，并提供 是否忽略父属性的参数 和 是否忽略值为null的参数
     */
    public static <T> Map<String, T> convertBeanToMap(Object source, boolean ignoreParent, boolean ignoreNull) {
        return convertBeanToMap(source, ignoreParent, ignoreNull, new String[0]);
    }

    /**
     * 将Bean对象转换为Map，并提供 是否忽略父属性的参数 和 是否忽略值为null的参数 还有 忽略哪些字段的参数
     */
    public static <T> Map<String, T> convertBeanToMap(Object source, boolean ignoreParent, boolean ignoreEmptyValue, String... ignoreProperties) {
        Map<String, T> map = new HashMap<>();

        List<Field> fieldList = FieldUtils.getAllFieldsList(source.getClass());
        fieldList.stream().forEach(field -> {
            //是否需要父类的字段
            if (ignoreParent) {
                //如果不是本类，下一个
                if (field.getDeclaringClass() != source.getClass()) {
                    return;
                }
            }
            //设置为可以访问
            field.setAccessible(true);
            T value = ReflectionKits.getFieldValue(source, field);

            //如果过滤掉空以及字段值为空
            if (ignoreEmptyValue && value == null) {
                return;
            }

            boolean flag = true;
            String key = field.getName();

            for (String ignoreProperty : ignoreProperties) {
                if (key.equals(ignoreProperty)) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                map.put(key, value);
            }
        });

        return map;
    }

    /**
     * 将Map对象转换成Bean对象
     *
     * @param fromValue   待转换的Map对象
     * @param toValueType 待转换成的类型
     * @param <T>
     * @return 转换后的对象
     */
    public static <T> T convertMapToBean(Map<String, Object> fromValue, Class<T> toValueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(fromValue, toValueType);
    }

}
