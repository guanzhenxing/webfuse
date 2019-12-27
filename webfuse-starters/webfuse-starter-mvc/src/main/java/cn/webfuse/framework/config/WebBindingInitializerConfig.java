package cn.webfuse.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

import java.beans.PropertyEditorSupport;

/**
 * 配置属性自定义转换
 *
 * @author Jesen
 */
@Configuration
@Slf4j
public class WebBindingInitializerConfig {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ConfigurableWebBindingInitializer configurableWebBindingInitializer(FormattingConversionService mvcConversionService, Validator mvcValidator) {

        log.info("======>>> ConfigurableWebBindingInitializer instancing.");

        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(mvcConversionService);
        initializer.setValidator(mvcValidator);

        //we can add our custom converters and formatters
        //conversionService.addConverter(...);
        //conversionService.addFormatter(...);

        //we can set our custom validator
        //initializer.setValidator(....);

        //装配自定义属性编辑器
//        initializer.setPropertyEditorRegistrar(propertyEditorRegistry -> {
//            stringEscapeHtml4(propertyEditorRegistry);
//        });

        return initializer;
    }

    private void stringEscapeHtml4(PropertyEditorRegistry propertyEditorRegistry) {
        //PropertyEditors并不是线程安全的，对于每一个请求，我们都需要new一个PropertyEditor对象
        propertyEditorRegistry.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }
        });
    }


}
