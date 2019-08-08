# webfuse-commons-framework

## 主要特性

### API版本控制

1. 配置webfuse.mvc.api-version.enabled=true
2. 配置webfuse.mvc.prefix=API版本的前缀，默认为：^v([0-9]*)+(.[0-9]{1,3})?$

- [API版本控制](https://www.hifreud.com/2018/01/30/01-API-versioning/)
- [Spring Boot API 版本权限控制](https://blog.csdn.net/u010782227/article/details/74905404)

### Restful异常处理

1. 配置webfuse.mvc.restful-exception-handle.enabled=true
2. 配置mapping关系，如：

```
webfuse.mvc.restful-exception-handle.mappings[0].clazz=cn.webfuse.framework.exception.BadParameterException
webfuse.mvc.restful-exception-handle.mappings[0].status=500
webfuse.mvc.restful-exception-handle.mappings[0].code=INTERNAL SERVER ERROR
```

### 记录访问日志

1. 配置webfuse.logging.access.enabled=true
2. 在需要日志的类或者方法上使用@AccessLogger
3. 默认使用的是Logger去写日志，如果想要其他方式的写日志，可以继承LoggerWriter


## 主要配置

```
######
# i18n
######
webfuse.i18n.base-folder=i18n   # 指定的国际化文件目录
webfuse.i18n.base-name=messages # 指定MessageSource指定的国际化文件

######
# web mvc
######
webfuse.mvc.argument.snake-to-camel=false # 是否转换蛇形为驼峰型参数

# API版本控制
webfuse.mvc.api-version.enabled=true # 是否使用API版本控制
webfuse.mvc.api-version.prefix=^v([0-9]*)+(.[0-9]{1,3})?$ # API版本控制的前缀

# Restful异常处理
webfuse.mvc.restful-exception-handle.enabled=true   # 是否使用Restful异常处理
webfuse.mvc.restful-exception-handle.mappings[index].clazz= # 待处理的异常类。如：cn.webfuse.framework.exception.BadParameterException
webfuse.mvc.restful-exception-handle.mappings[index].status= # 状态码，最好为HttpStatus状态码。如：404，500
webfuse.mvc.restful-exception-handle.mappings[index].code= # 业务代码
webfuse.mvc.restful-exception-handle.mappings[index].message= #返回出去的异常消息
webfuse.mvc.restful-exception-handle.mappings[index].developer-message= # 开发者消息

######
# 日志信息
######
webfuse.logging.access.enabled=false    # 是否记录访问日志
```