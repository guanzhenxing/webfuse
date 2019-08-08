# webfuse-service-starter

## 功能

- [swagger2支持](https://github.com/SpringForAll/spring-boot-starter-swagger)

## 运维

### 如何优雅下线微服务

在想下线应用的application.yml 中添加配置，从而暴露/service-registry 端点：

```yml
management:
  endpoints:
    web:
      exposure:
        include: service-registry

```

发送：`curl -X "POST" "http://localhost:8000/actuator/service-registry?status=DOWN" \
       -H "Content-Type: application/vnd.spring-boot.actuator.v2+json;charset=UTF-8"
`



TODO LIST

- 版本控制配置

spring-boot-autoconfigure：

- [了解 Spring Boot AutoConfiguration](https://www.cnblogs.com/oopsguy/p/7484398.html)
- [SpringBoot使用AutoConfiguration自定义Starter](https://www.jianshu.com/p/188065e1137b) *

-----
- @ConditionalOnBean：当SpringIoc容器内存在指定Bean的条件
- @ConditionalOnClass：当SpringIoc容器内存在指定Class的条件
- @ConditionalOnExpression：基于SpEL表达式作为判断条件
- @ConditionalOnJava：基于JVM版本作为判断条件
- @ConditionalOnJndi：在JNDI存在时查找指定的位置
- @ConditionalOnMissingBean：当SpringIoc容器内不存在指定Bean的条件
- @ConditionalOnMissingClass：当SpringIoc容器内不存在指定Class的条件
- @ConditionalOnNotWebApplication：当前项目不是Web项目的条件
- @ConditionalOnProperty：指定的属性是否有指定的值
- @ConditionalOnResource：类路径是否有指定的值
- @ConditionalOnSingleCandidate：当指定Bean在SpringIoc容器内只有一个，或者虽然有多个但是指定首选的Bean
- @ConditionalOnWebApplication：当前项目是Web项目的条件


- mica项目的launcher





