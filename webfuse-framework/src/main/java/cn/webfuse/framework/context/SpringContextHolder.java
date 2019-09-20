package cn.webfuse.framework.context;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring帮助类，实现了BeanFactoryPostProcessor接口
 *
 * @author Jesen
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }


    /**
     * 获得spring应用上下文
     *
     * @return
     */
    public static AutowireCapableBeanFactory getBeanFactory() {
        return applicationContext.getAutowireCapableBeanFactory();
    }

    /**
     * 获得对象
     *
     * @param name 对象的名字
     * @return 以name为名字注册的实例对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获得对象
     *
     * @param requiredType 对象的类型
     * @return 类型为clz的对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获得对象
     *
     * @param name         对象的名称
     * @param requiredType 对象的类型
     * @return
     */
    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 是否包含该名字的bean对象
     *
     * @param name
     * @return
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 是否单例
     *
     * @param name
     * @return
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获得类型
     *
     * @param name
     * @return Class 注册对象的类型
     */
    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

    /**
     * 获得ApplicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取profile值
     *
     * @return
     */
    public static String getActiveProfile() {
        String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
        if (ArrayUtils.isNotEmpty(profiles)) {
            return profiles[0];
        }
        return null;
    }

}
