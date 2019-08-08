package cn.webfuse.framework.web.method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的请求参数数据绑定（url上的）
 *
 * @author Jesen
 */
public class UrlQueriesSnakeToCamelServletRequestDataBinder extends ExtendedServletRequestDataBinder {

    public UrlQueriesSnakeToCamelServletRequestDataBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override
    protected void addBindValues(MutablePropertyValues mpv, ServletRequest request) {
        super.addBindValues(mpv, request);

        handleJsonProperty(mpv);
        handleSnakeToCamel(mpv);
    }

    /**
     * 处理蛇形命名法转驼峰命名法
     *
     * @param mpv
     */
    private void handleSnakeToCamel(MutablePropertyValues mpv) {
        List<PropertyValue> covertValues = new ArrayList<>();
        for (PropertyValue propertyValue : mpv.getPropertyValueList()) {
            if (propertyValue.getName().contains("_")) {

                String camelName = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)
                        .convert(propertyValue.getName());
                if (!mpv.contains(camelName)) {
                    covertValues.add(new PropertyValue(camelName, propertyValue.getValue()));
                }
            }
        }
        mpv.getPropertyValueList().addAll(covertValues);
    }

    //处理JsonProperty注释的对象
    private void handleJsonProperty(MutablePropertyValues mpv) {
        Class<?> targetClass = getTarget().getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
            if (jsonPropertyAnnotation != null && mpv.contains(jsonPropertyAnnotation.value())) {
                if (!mpv.contains(field.getName())) {
                    mpv.add(field.getName(), mpv.getPropertyValue(jsonPropertyAnnotation.value()).getValue());
                }
            }
        }
    }

}

