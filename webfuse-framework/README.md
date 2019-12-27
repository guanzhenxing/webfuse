# webfuse-framework

基于 SpringBoot 的开发框架

## 特性

### API版本控制

基于 RequestCondition 和 RequestMappingHandlerMapping 的 API 版本控制。

主要参考：

- [API版本控制](https://www.hifreud.com/2018/01/30/01-API-versioning/)
- [Spring Boot API 版本权限控制](https://blog.csdn.net/u010782227/article/details/74905404)

详细的使用情况见：[webfuse-starters/webfuse-starter-mvc](../webfuse-starters/webfuse-starter-mvc)

### RESTful异常处理

为 RESTful API 提供一个方便的异常处理程序（解析程序），可以处理自定义异常，支持国际化，为异常处理提供一个统一的解决方案。

主要类说明：

- AbstractWebfuseException ： 抽象的业务异常
- RestfulErrorResolver ： 接口类。是一个Restful错误解析器，提供解析错误的方法
- RestfulErrorConverter ： RestfulError转换器，将RestfulError对象转换成其他对象。
- DefaultRestfulExceptionHandler ： 默认的Restful异常处理分析器
- DefaultRestfulErrorConverter ： 默认的RestfulError转换器，默认转换为Map。也就是在这里输出异常的格式
- DefaultRestfulErrorResolver ： 默认的RestfulError解析器

详细的使用情况见：[webfuse-starters/webfuse-starter-mvc](../webfuse-starters/webfuse-starter-mvc)

### Filters

提供了常用的 Filter：

- 设置 RequestId 的Filter （ RequestIdSettingFilter ）
- 读取 RequestBody 的Filter ( BodyReaderRequestFilter )

### Web 安全

- 防 XSS 攻击

### 其他特性

- URL 上的参数 `小写下划线参数` 转 `驼峰参数` 。
- Spring 自动扫描的时候忽略注解（IgnoreDuringScan）
- Spring 上下文工具类（SpringContextHolder）
- 全局上下文的线程保存类（GlobalThreadLocalHolder）
