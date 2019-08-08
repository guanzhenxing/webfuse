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
 * 自定义ConfigurableWebBindingInitializer
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
        initializer.setPropertyEditorRegistrar(propertyEditorRegistry -> {
            //PropertyEditors并不是线程安全的，对于每一个请求，我们都需要new一个PropertyEditor对象

            stringEscapeHtml4(propertyEditorRegistry);

        });

        return initializer;
    }

    private void stringEscapeHtml4(PropertyEditorRegistry propertyEditorRegistry) {
        propertyEditorRegistry.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }
        });
    }


}

/**
 * * https://www.logicbig.com/tutorials/spring-framework/spring-boot/custom-web-binding-initializer.html
 * * <p>
 * * https://blog.csdn.net/Michean/article/details/90901450
 * * <p>
 * * https://segmentfault.com/a/1190000016941868
 */
