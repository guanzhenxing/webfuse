package cn.webfuse.data.datasource.dynamic;

import cn.webfuse.core.kit.ExceptionKits;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源注册<br/>
 * 启动动态数据源请在启动类中（如SpringBootSampleApplication）
 * 添加 @Import(DynamicDataSourceRegister.class)
 * <p>
 * <br/>
 * 默认数据源：
 * <pre>
 *    spring.datasource.url=""
 *    spring.datasource.username=""
 *    spring.datasource.password=""
 *    spring.datasource.driver-class-name=""
 * </pre>
 * <p>
 * 新增数据源配置：
 * <pre>
 *    spring.custom-datasource.names=ds1,ds2
 *    spring.custom-datasource.ds1.driver-class-name=""
 *    spring.custom-datasource.ds1.url=""
 *    spring.custom-datasource.ds1.username=""
 *    spring.custom.datasource.ds1.password=""
 *
 *    spring.custom-datasource.ds2.driver-class-name=""
 *    spring.custom-datasource.ds2.url=""
 *    spring.custom-datasource.ds2.username=""
 *    spring.custom-datasource.ds2.password=""
 * </pre>
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    /**
     * 如配置文件中未指定数据源类型，使用该默认值
     */
    private static final Object DATASOURCE_TYPE_DEFAULT = "com.zaxxer.hikari.HikariDataSource";

    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new HashMap<>();

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
        initCustomDataSources(env);
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<Object, Object> targetDataSources = new HashMap<>();

        // 将主数据源添加到targetDataSources中
        if (defaultDataSource != null) {
            targetDataSources.put("default", defaultDataSource);
            DynamicDataSourceContextHolder.dataSourceNames.add("default");
        }

        // 添加更多数据源到targetDataSources中
        if (!MapUtils.isEmpty(customDataSources)) {
            targetDataSources.putAll(customDataSources);
            for (String key : customDataSources.keySet()) {
                DynamicDataSourceContextHolder.dataSourceNames.add(key);
            }
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);

        logger.info("Dynamic DataSource Registry");
    }


    /**
     * 初始化主数据源
     */
    private void initDefaultDataSource(Environment env) {
        //如果不使用默认的，那么就直接设置为null；
        boolean useDefault = BooleanUtils.toBoolean(env.getProperty("spring.datasource.use-default"));
        if (useDefault == false) {
            defaultDataSource = null;
            return;
        }

        // 读取主数据源
        Map<String, Object> dsMap = new HashMap<>();
        dsMap.put("type", env.getProperty("spring.datasource.type"));
        dsMap.put("driver-class-name", env.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("url", env.getProperty("spring.datasource.url"));
        dsMap.put("username", env.getProperty("spring.datasource.username"));
        dsMap.put("password", env.getProperty("spring.datasource.password"));

        DataSource dataSource = buildDataSource(dsMap);
        //为DataSource绑定更多数据
        Bindable<DataSource> bindAble = Bindable.ofInstance(dataSource);
        defaultDataSource = Binder.get(env).bind("spring.datasource", bindAble).get();
    }


    /**
     * 初始化更多数据源
     */
    private void initCustomDataSources(Environment env) {
        String customPrefix = "spring.custom-datasource";
        // 读取配置文件获取更多数据源，也可以通过defaultDataSource读取数据库获取更多数据源
        String dsPrefixes = env.getProperty(customPrefix + ".names");
        if (dsPrefixes == null) {
            return;
        }
        // 多个数据源
        for (String dsName : dsPrefixes.split(",")) {

            // 读取主数据源
            Map<String, Object> dsMap = new HashMap<>();
            dsMap.put("type", env.getProperty(customPrefix + "." + dsName + ".type"));
            dsMap.put("driver-class-name", env.getProperty(customPrefix + "." + dsName + ".driver-class-name"));
            dsMap.put("url", env.getProperty(customPrefix + "." + dsName + ".url"));
            dsMap.put("username", env.getProperty(customPrefix + "." + dsName + ".username"));
            dsMap.put("password", env.getProperty(customPrefix + "." + dsName + ".password"));

            DataSource dataSource = buildDataSource(dsMap);
            //为DataSource绑定更多数据
            Bindable<DataSource> bindAble = Bindable.ofInstance(dataSource);
            DataSource customDataSource = Binder.get(env).bind("spring.custom-datasource." + dsName, bindAble).get();
            customDataSources.put(dsName, customDataSource);
        }
    }

    /**
     * 创建DataSource
     */
    @SuppressWarnings("unchecked")
    private DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null) {
                // 默认DataSource
                type = DATASOURCE_TYPE_DEFAULT;
            }

            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);

            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            throw ExceptionKits.unchecked(e);
        }
    }

}