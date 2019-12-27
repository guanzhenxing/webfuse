# webfuse-ext-launch

## starter

this feature will develop

## shutdown

## how to test shutdown

- Create a controller
```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class LongProcessController {
    @RequestMapping("/long-process")
    public String pause() throws InterruptedException {
        Thread.sleep(10000);
        return "Process finished";
    }
}
```
- Start the server. Locate the Process ID.
- Request `curl -i localhost:8080/long-process`
- Do kill process-id
- Get the info below
```
HTTP/1.1 200
Content-Type: text/plain;charset=UTF-8
Content-Length: 14
Date: Thu, 28 Jun 2018 18:39:56 GMT
Process finished
```

## other graceful shutdown 

- [spring-boot-graceful-shutdown](https://github.com/timpeeters/spring-boot-graceful-shutdown)
- [springboot-graceful-shutdown](https://github.com/SchweizerischeBundesbahnen/springboot-graceful-shutdown)
- [spring-boot-graceful-shutdown](https://github.com/corentin59/spring-boot-graceful-shutdown)