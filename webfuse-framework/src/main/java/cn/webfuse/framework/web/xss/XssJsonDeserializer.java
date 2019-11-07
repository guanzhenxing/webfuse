package cn.webfuse.framework.web.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

/**
 * 基于xss的JsonSerializer
 * <p>
 * 参考：https://dzone.com/articles/anti-cross-site-scripting-xss-for-spring-boot-apps
 */
public class XssJsonDeserializer extends JsonDeserializer<String> implements ContextualDeserializer {


    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = parser.getValueAsString();
        if (StringUtils.isEmpty(value)) {
            return value;
        } else {
            return StringEscapeUtils.escapeHtml4(value.trim());
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        return this;
    }
}